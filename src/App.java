import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.Player;
import models.smashGG.SetOfTournament;
import models.smashGG.Tournament;
import okhttp3.*;
import utils.Constants;
import utils.GraphQLQuery;
import utils.Queries;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class App {
    public static List<Tournament> tournamentsImportedFromSmashGG = new ArrayList<>();
    public static List<Player> players = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        ObjectMapper mapper = new ObjectMapper();
        for (String eventSlug : Constants.POST_COVID_SLUGS) {
            // Fetch the Tournament
            String query = mapper.writeValueAsString(new GraphQLQuery(Queries.eventBySlug, Queries.eventBySlugVariablesMapper(eventSlug)));

            RequestBody body = RequestBody.create(query, mediaType);
            Request request = new Request.Builder()
                    .url(Constants.API_URL)
                    .post(body)
                    .addHeader("authorization", "Bearer " + Constants.API_KEY)
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
                    .addHeader("authorization", "Bearer " + Constants.API_KEY)
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
                tournamentsImportedFromSmashGG.add(tournamentToBeAdded);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        Player.calculateConsistencyScoresForAll(players);
        Player.calculateWinLossScore(players);
        Player.calculateAndDisplayFinalResults(players);


    }


}
