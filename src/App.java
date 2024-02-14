import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.Player;
import models.smashGG.PlayerDataInTournament;
import models.smashGG.SetOfTournament;
import models.smashGG.Tournament;
import okhttp3.*;
import utils.ConnectionCredentials;
import utils.Constants;
import utils.GraphQLQuery;
import utils.Queries;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.*;
import java.sql.Connection;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

public class App {
    public static List<Tournament> tournaments = new ArrayList<>();
    public static List<Player> players = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        generatePRFromDatabaseUsingPeriod(Date.valueOf("2023-01-01"), Date.valueOf("2024-02-01"));
        //generatePRFromTournamentSlugs(Constants.SEASON_2022);
    }

    public static void generatePRFromTournamentSlugs(List<String> slugs) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        ObjectMapper mapper = new ObjectMapper();
        for (String eventSlug : slugs) {
            // Fetch the Tournament
            String query = mapper.writeValueAsString(new GraphQLQuery(Queries.eventBySlug, Queries.eventBySlugVariablesMapper(eventSlug)));

            RequestBody body = RequestBody.create(query, mediaType);
            Request request = new Request.Builder()
                    .url(Constants.API_URL)
                    .post(body)
                    .addHeader("authorization", "Bearer " + ConnectionCredentials.API_KEY)
                    .build();

            Response response = client.newCall(request).execute();
            String json = response.body().string();
            JsonNode jsonNodeTournament = mapper.readTree(json).get("data").get("event");

            // Fetch Sets of a Tournament
            query = mapper.writeValueAsString(new GraphQLQuery(Queries.eventSetsBySlug, Queries.eventSetsBySlugVariablesMapper(eventSlug)));

            body = RequestBody.create(query, mediaType);
            request = new Request.Builder()
                    .url(Constants.API_URL)
                    .post(body)
                    .addHeader("authorization", "Bearer " + ConnectionCredentials.API_KEY)
                    .build();

            response = client.newCall(request).execute();

            json = response.body().string();

            ArrayNode jsonNodeSets = (ArrayNode) mapper.readTree(json).get("data").get("event").get("sets").get("nodes");

            try {

                Tournament tournamentToBeAdded = mapper.readValue(jsonNodeTournament.toString(), Tournament.class);
                tournamentToBeAdded.setSets(mapper.readValue(jsonNodeSets.toString(), new TypeReference<List<SetOfTournament>>() {
                    @Override
                    public Type getType() {
                        return super.getType();
                    }
                }));

                Player.setOrUpdatePlayersList(players, tournamentToBeAdded);
                tournaments.add(tournamentToBeAdded);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        Player.calculateConsistencyScoresForAll(players, tournaments);
        Player.calculateWinLossScore(players, tournaments);
        Player.calculateAndDisplayFinalResults(players);

    }

    // Format date like this : YYYY-mm-dd
    public static void generatePRFromDatabaseUsingPeriod(Date start, Date end) {


        try (Connection connection = DriverManager.getConnection(ConnectionCredentials.JDBC_URL, ConnectionCredentials.DB_USER, ConnectionCredentials.DB_PASSWORD)) {
            String tournamentQuery = "SELECT tournament_name, id, entrants, type FROM public.tournaments where start_date>=? and start_date<=? and type <> 'ladder' order by id asc ;";

            PreparedStatement preparedStatement = connection.prepareStatement(tournamentQuery);
            preparedStatement.setDate(1, start);
            preparedStatement.setDate(2, end);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Tournament tournament = new Tournament();
                tournament.setTournamentID(resultSet.getInt("id"));
                tournament.setEntrants(resultSet.getInt("entrants"));
                tournament.setName(resultSet.getString("tournament_name"));
                tournament.setType(resultSet.getString("type"));
                tournaments.add(tournament);
            }


            for (Tournament tournament : tournaments) {


                //retrieve all sets from said tournament
                String setQuery = "Select S.id as set_id, winner, p1_id, p2_id, T.id as tournament_id FROM public.sets S LEFT join tournaments T on T.tournament_slug = S.tournament_name and T.event_slug = S.tournament_event where T.id = ? and S.p1_score<>-1 and S.p2_score<>-1 order by S.id asc";

                preparedStatement = connection.prepareStatement(setQuery);
                preparedStatement.setInt(1, tournament.getTournamentID());
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    SetOfTournament set = new SetOfTournament();
                    set.setId(resultSet.getInt("set_id"));
                    set.setWinnerId(resultSet.getInt("winner"));
                    set.setPlayer1(resultSet.getInt("p1_id"));
                    set.setPlayer2(resultSet.getInt("p2_id"));

                    tournament.getSets().add(set);
                }

                //retrieve all players from said tournament
                String playerQuery = "SELECT tournament_id, P.player_id as player_id, J.ign as ign, \"placing\", \"DQ\", J.main_region as main_region FROM public.placings P LEFT OUTER JOIN players J ON J.player_id = P.player_id where tournament_id=?;";

                preparedStatement = connection.prepareStatement(playerQuery);
                preparedStatement.setInt(1, tournament.getTournamentID());
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    PlayerDataInTournament playerDataInTournament = new PlayerDataInTournament();
                    playerDataInTournament.setPlayerID(resultSet.getInt("player_id"));
                    playerDataInTournament.setName(resultSet.getString("ign"));
                    playerDataInTournament.setPlacing(calculatePlacing(resultSet.getInt(4), tournament.getEntrants()));
                    playerDataInTournament.setIsDisqualified(resultSet.getInt(5) == 1 ? "true" : "false");
                    playerDataInTournament.setRegion(resultSet.getInt("main_region"));
                    tournament.getPlayersData().add(playerDataInTournament);
                }
                Player.setOrUpdatePlayersList(players, tournament);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        Constants.MAJOR_COUNT = (int) tournaments.stream().filter(t -> t.getType().equals("major")).count();
        Constants.NATIONAL_COUNT = (int) tournaments.stream().filter(t -> t.getType().equals("national")).count();
        Player.calculateConsistencyScoresForAll(players, tournaments);
        Player.calculateWinLossScore(players, tournaments);
        Player.calculateAndDisplayFinalResults(players);
    }

    private static int calculateNbOfPlacings(int entrants) {

        if (entrants <= 5)
            return entrants;
        double entrantsLog = Math.log(entrants) / Math.log(2);

        int floor = (int) Math.floor(entrantsLog), ceil = (int) Math.ceil(entrantsLog);


        if (floor == ceil)
            return 2 * floor;
        else if (Math.abs(entrants - Math.pow(2, floor)) <= Math.abs(entrants - Math.pow(2, ceil)))
            return 2 * ceil - 1;
        else
            return 2 * ceil;


    }

    private static int calculatePlacing(int placing, int entrants) {

        int dividend = calculateNbOfPlacings(entrants) + 1 - calculateNbOfPlacings(placing);
        float divisor = calculateNbOfPlacings(entrants) * 1f;
        return Math.round((dividend / divisor) * 100);
    }


}
