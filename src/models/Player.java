package models;

import models.smashGG.PlayerDataInTournament;
import models.smashGG.SetOfTournament;
import models.smashGG.Tournament;
import utils.Constants;

import java.util.*;
import java.util.stream.Collectors;

public class Player {

    private String name;
    private int playerID;
    private float consistencyScoreWeekly;
    private float consistencyScoreMonthly;
    private float consistencyScoreGlobal;
    private float winLossImpactScore;
    private float finalRankingScore;
    private List<Tournament> tournaments;
    private HashMap<Integer, Integer> placingsByTournament;
    private HashMap<Integer, List<SetOfTournament>> setsByTournament;

    public Player() {
        this.tournaments = new ArrayList<>();
        this.placingsByTournament = new HashMap<>();
        this.setsByTournament = new HashMap<>();
    }

    public Player(String name, int playerID, Tournament tournament) {
        this.name = name;
        this.playerID = playerID;
        this.consistencyScoreWeekly = 0;
        this.consistencyScoreMonthly = 0;
        this.consistencyScoreGlobal = 0;
        this.winLossImpactScore = 0;
        this.finalRankingScore = 0;
        this.tournaments = new ArrayList<>();
        this.placingsByTournament = new HashMap<>();
        this.setsByTournament = new HashMap<>();
        this.tournaments.add(tournament);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public float getConsistencyScoreWeekly() {
        return consistencyScoreWeekly;
    }

    public void setConsistencyScoreWeekly(float consistencyScoreWeekly) {
        this.consistencyScoreWeekly = consistencyScoreWeekly;
    }

    public float getConsistencyScoreMonthly() {
        return consistencyScoreMonthly;
    }

    public void setConsistencyScoreMonthly(float consistencyScoreMonthly) {
        this.consistencyScoreMonthly = consistencyScoreMonthly;
    }

    public float getWinLossImpactScore() {
        return winLossImpactScore;
    }

    public void setWinLossImpactScore(float winLossImpactScore) {
        this.winLossImpactScore = winLossImpactScore;
    }

    public float getFinalRankingScore() {
        return finalRankingScore;
    }

    public void setFinalRankingScore(float finalRankingScore) {
        this.finalRankingScore = finalRankingScore;
    }

    public List<Tournament> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<Tournament> tournaments) {
        this.tournaments = tournaments;
    }

    public HashMap<Integer, Integer> getPlacingsByTournament() {
        return placingsByTournament;
    }

    public void setPlacingsByTournament(HashMap<Integer, Integer> placingsByTournament) {
        this.placingsByTournament = placingsByTournament;
    }

    public float getConsistencyScoreGlobal() {
        return consistencyScoreGlobal;
    }

    public void setConsistencyScoreGlobal(float consistencyScoreGlobal) {
        this.consistencyScoreGlobal = consistencyScoreGlobal;
    }

    public HashMap<Integer, List<SetOfTournament>> getSetsByTournament() {
        return setsByTournament;
    }

    public void setSetsByTournament(HashMap<Integer, List<SetOfTournament>> setsByTournament) {
        this.setsByTournament = setsByTournament;
    }

    public static void setOrUpdatePlayersList(List<Player> players, Tournament tournament) {
        for (PlayerDataInTournament playerData : tournament.getPlayersData()) {
            // We do not import DQ'd players
            if (playerData.getIsDisqualified() == null || !playerData.getIsDisqualified().equals("true")) {
                // if the player already exist, we update their data
                if (players.stream().anyMatch(p -> p.getPlayerID() == playerData.getPlayerID())) {
                    players.stream().filter(p -> p.getPlayerID() == playerData.getPlayerID()).findFirst().get().getTournaments().add(tournament);
                    players.stream().filter(p -> p.getPlayerID() == playerData.getPlayerID()).findFirst().get().getSetsByTournament().putIfAbsent(tournament.getTournamentID(),
                            tournament.getSets().stream().filter(set -> set.getPlayer1() == playerData.getPlayerID() || set.getPlayer2() == playerData.getPlayerID()).collect(Collectors.toList())
                    );
                    players.stream().filter(p -> p.getPlayerID() == playerData.getPlayerID()).findFirst().get().getPlacingsByTournament().putIfAbsent(tournament.getTournamentID(),
                            tournament.getEntrants() > 48 ? Constants.MONTHLY_CONSISTENCY_POINTS.get(playerData.getPlacing()) : Constants.WEEKLY_CONSISTENCY_POINTS.get(playerData.getPlacing())
                    );
                }
                // if the player doesn't already exist, we add them to the list
                else {
                    Player player = new Player(playerData.getName(), playerData.getPlayerID(), tournament);
                    player.setSetsByTournament(new HashMap<>() {{
                        put(tournament.getTournamentID(), tournament.getSets().stream().filter(set -> set.getPlayer1() == playerData.getPlayerID() || set.getPlayer2() == playerData.getPlayerID()).collect(Collectors.toList()));
                    }});
                    player.setPlacingsByTournament(new HashMap<>() {{
                        put(tournament.getTournamentID(),
                                tournament.getEntrants() > 48 ? Constants.MONTHLY_CONSISTENCY_POINTS.get(playerData.getPlacing()) : Constants.WEEKLY_CONSISTENCY_POINTS.get(playerData.getPlacing()));
                    }});
                    players.add(player);
                }
            }
        }
    }

    public static void calculateConsistencyScoresForAll(List<Player> players) {
        players.forEach(Player::calculateConsistencyScoresAndRankListForOne);
    }

    // Highest Score achievable is 175
    private static void calculateConsistencyScoresAndRankListForOne(Player player) {
        int monthlyCount = 0, weeklyCount = 0;
        // Adds all the scores based on tournament type
        for (Tournament tournament : player.getTournaments()) {
            int placing = tournament.getPlayersData().stream().filter(p -> p.getPlayerID() == player.getPlayerID()).findFirst().get().getPlacing();
            // Case of a monthly
            if (tournament.getEntrants() > 48) {
                monthlyCount++;
                player.setConsistencyScoreMonthly(player.getConsistencyScoreMonthly() + Constants.MONTHLY_CONSISTENCY_POINTS.get(placing));
            }
            // Case of a weekly
            else {
                weeklyCount++;
                player.setConsistencyScoreWeekly(player.getConsistencyScoreWeekly() + Constants.WEEKLY_CONSISTENCY_POINTS.get(placing));
            }
        }
        // if the player has participated in at least one monthly
        if (monthlyCount > 0) {
            // Sets the average consistency score for monthlies
            player.setConsistencyScoreMonthly(player.getConsistencyScoreMonthly() / monthlyCount);
            // Sets the global consistency score for monthly that will change later based on weekly attendance
            player.setConsistencyScoreGlobal(player.getConsistencyScoreMonthly());
        } else
            // The player hasn't done any monthly so we notify the weekly part with a negative integer
            player.setConsistencyScoreMonthly(-1);
        // if the player has participated in at least one weekly
        if (weeklyCount > 0) {
            // Sets the average consistency score for weeklies
            player.setConsistencyScoreWeekly(player.getConsistencyScoreWeekly() / weeklyCount);
            // if the player has participated in at least one monthly
            if (player.getConsistencyScoreMonthly() != -1)
                // Adds the weekly consistency score to the global but monthly consitency has more weight compared to it
                player.setConsistencyScoreGlobal(player.getConsistencyScoreGlobal() + 0.75f * player.getConsistencyScoreWeekly());
            else
                // Current player has only participated in weeklies so we multiply their score by a ratio to not penalize them from the others
                player.setConsistencyScoreGlobal(player.getConsistencyScoreWeekly() * Constants.ALL_WEEKLIES_CONSISTENCY_MULTIPLICATOR);
        } else {
            // Current player has only participated in monthlies so we multiply their score by a ratio to not penalize them from the others. Monthly ratio > Weekly ratio.
            player.setConsistencyScoreWeekly(-1);
            player.setConsistencyScoreGlobal(player.getConsistencyScoreGlobal() * Constants.ALL_MONTHLIES_CONSISTENCY_MULTIPLICATOR);
        }


    }

    public static void calculateWinLossScore(List<Player> players) {
        players.forEach(p -> p.calculateWinLossScoreForOne(players));
    }

    private void calculateWinLossScoreForOne(List<Player> players) {
        HashMap<Integer, Float> monthlyConsistencyByPlayer = getMonthlyConsistencyMap(players);
        HashMap<Integer, Float> weeklyConsistencyByPlayer = getWeeklyConsistencyMap(players);
        HashMap<Integer, Float> globalConsistencyByPlayer = getGlobalConsistencyMap(players);

        for (Integer tournamentKey : this.getSetsByTournament().keySet()) {
            // only the two best wins and two worst losses are accounted for
            float[] maxValue = {0f, 0f}, minValue = {0f, 0f};
            float calculatedValue, upsetFactorOnTournamentType, upsetFactorOnTournamentPerformance, upsetFactorOnGlobalConsistencyPerformance;
            // Map of the placings of all participants of the tournament
            HashMap<Integer, Integer> playersPlacements = getPlayersPlacementsForCurrentTournament(players, tournamentKey);
            List<SetOfTournament> setsOfTournament = this.getSetsByTournament().get(tournamentKey);
            // More than 95 sets means that the tournament is not a 48 player weekly
            if (setsOfTournament.size() > 95) {
                for (SetOfTournament set : setsOfTournament) {
                    // current player won the set
                    if (set.getWinnerId() == this.getPlayerID()) {

                        int opponentID = set.getPlayer1() == this.getPlayerID() ? set.getPlayer2() : set.getPlayer1();
                        // We dont want to use DQs
                        if (!playersPlacements.containsKey(opponentID))
                            continue;
                        // max value = 100
                        upsetFactorOnTournamentType = monthlyConsistencyByPlayer.get(opponentID);
                        // max value = 175
                        upsetFactorOnGlobalConsistencyPerformance = globalConsistencyByPlayer.get(opponentID);
                        // max value = 100
                        upsetFactorOnTournamentPerformance = playersPlacements.get(opponentID);
                        // max value = roughly 190; first place is guaranteed to get full points
                        if (playersPlacements.get(this.getPlayerID()) == 100)
                            calculatedValue = 380f / Constants.WIN_LOSS_IMPACT_SCORE;
                        else
                            calculatedValue = (upsetFactorOnTournamentType + upsetFactorOnGlobalConsistencyPerformance + upsetFactorOnTournamentPerformance) / Constants.WIN_LOSS_IMPACT_SCORE;
                        updateValues(maxValue, calculatedValue, 0);
                    } // current player lost the set
                    else {
                        // max value = 100, min value = 7
                        if (monthlyConsistencyByPlayer.get(set.getWinnerId()) < this.getConsistencyScoreMonthly())
                            upsetFactorOnTournamentType = this.getConsistencyScoreMonthly() - monthlyConsistencyByPlayer.get(set.getWinnerId());
                        else
                            upsetFactorOnTournamentType = 0;
                        // max value = 175, min value = 12.25
                        if (globalConsistencyByPlayer.get(set.getWinnerId()) < this.getConsistencyScoreGlobal())
                            upsetFactorOnGlobalConsistencyPerformance = this.getConsistencyScoreGlobal() - globalConsistencyByPlayer.get(set.getWinnerId());
                        else
                            upsetFactorOnGlobalConsistencyPerformance = 0;
                        // max value = 100, min value = 7
                        if (playersPlacements.get(set.getWinnerId()) < playersPlacements.get(this.getPlayerID()))
                            upsetFactorOnTournamentPerformance = playersPlacements.get(this.getPlayerID()) - playersPlacements.get(set.getWinnerId());
                        else
                            upsetFactorOnTournamentPerformance = 0;
                        // max value = roughly 175
                        calculatedValue = -(upsetFactorOnTournamentType + upsetFactorOnGlobalConsistencyPerformance + upsetFactorOnTournamentPerformance) / Constants.WIN_LOSS_IMPACT_SCORE;
                        updateValues(minValue, calculatedValue, 1);
                    }

                }
            } // Less than or equal to 95 sets means that the tournament is a 48 player weekly
            else {
                for (SetOfTournament set : setsOfTournament) {
                    // current player won the set
                    if (set.getWinnerId() == this.getPlayerID()) {
                        int opponentID = set.getPlayer1() == this.getPlayerID() ? set.getPlayer2() : set.getPlayer1();
                        // We dont want to use DQs
                        if (!playersPlacements.containsKey(opponentID))
                            continue;
                        // max value = 100
                        upsetFactorOnTournamentType = weeklyConsistencyByPlayer.get(opponentID);
                        // max value = 175
                        upsetFactorOnGlobalConsistencyPerformance = globalConsistencyByPlayer.get(opponentID);
                        // max value = 100
                        upsetFactorOnTournamentPerformance = playersPlacements.get(opponentID);
                        // max value = roughly 190; first place is guaranteed to get full points
                        if (playersPlacements.get(this.getPlayerID()) == 100)
                            calculatedValue = 380f / Constants.WIN_LOSS_IMPACT_SCORE;
                        else
                            calculatedValue = (upsetFactorOnTournamentType + upsetFactorOnGlobalConsistencyPerformance + upsetFactorOnTournamentPerformance) / Constants.WIN_LOSS_IMPACT_SCORE;
                        updateValues(maxValue, calculatedValue, 0);
                    } // current player lost the set
                    else {
                        // max value = 100, min value = 9
                        if (weeklyConsistencyByPlayer.get(set.getWinnerId()) < this.getConsistencyScoreWeekly())
                            upsetFactorOnTournamentType = this.getConsistencyScoreWeekly() - weeklyConsistencyByPlayer.get(set.getWinnerId());
                        else
                            upsetFactorOnTournamentType = 0;
                        // max value = 175, min value = 12.25
                        if (globalConsistencyByPlayer.get(set.getWinnerId()) < this.getConsistencyScoreGlobal())
                            upsetFactorOnGlobalConsistencyPerformance = this.getConsistencyScoreGlobal() - globalConsistencyByPlayer.get(set.getWinnerId());
                        else
                            upsetFactorOnGlobalConsistencyPerformance = 0;
                        // max value = 100, min value = 9
                        if (playersPlacements.get(set.getWinnerId()) < playersPlacements.get(this.getPlayerID()))
                            upsetFactorOnTournamentPerformance = playersPlacements.get(this.getPlayerID()) - playersPlacements.get(set.getWinnerId());
                        else
                            upsetFactorOnTournamentPerformance = 0;
                        // max value = roughly 175
                        calculatedValue = -(upsetFactorOnTournamentType + upsetFactorOnGlobalConsistencyPerformance + upsetFactorOnTournamentPerformance) / Constants.WIN_LOSS_IMPACT_SCORE;
                        updateValues(minValue, calculatedValue, 1);
                    }

                }
            }
            this.setWinLossImpactScore(this.getWinLossImpactScore() + (maxValue[0] + maxValue[1] + minValue[0] + minValue[1]) / 2f);

        }

        this.setWinLossImpactScore(this.getWinLossImpactScore() / this.getTournaments().size());
    }

    private void updateValues(float[] valueArray, float calculatedValue, int flag) {
        // max Array to be updated
        if (flag == 0) {
            // biggest value of the array is smaller than the new upset provided
            if (Float.compare(valueArray[0], calculatedValue) < 0) {
                valueArray[1] = valueArray[0];
                valueArray[0] = calculatedValue;
            } // second biggest value of the array is smaller than the new upset provided
            else if (Float.compare(valueArray[1], calculatedValue) < 0)
                valueArray[1] = calculatedValue;
        } // min Array to be updated
        else {
            // smallest value of the array is bigger than the new upset provided
            if (Float.compare(valueArray[0], calculatedValue) > 0) {
                valueArray[1] = valueArray[0];
                valueArray[0] = calculatedValue;
            } // second smallest value of the array is bigger than the new upset provided
            else if (Float.compare(valueArray[1], calculatedValue) > 0)
                valueArray[1] = calculatedValue;

        }
    }

    public static void calculateAndDisplayFinalResults(List<Player> players) {
        players.forEach(p -> p.setFinalRankingScore(p.getConsistencyScoreGlobal() * Constants.CONSISTENCY_COEFFICIENT + p.getWinLossImpactScore() * Constants.WIN_LOSS_IMPACT_COEFFICIENT));
        players.sort((o1, o2) -> Float.compare(o1.getFinalRankingScore(), o2.getFinalRankingScore()));
        Collections.reverse(players);
        System.out.println(players.stream().filter(p -> p.getTournaments().size() >= Constants.TOURNAMENT_NUMBER_THRESHOLD).collect(Collectors.toList()));
        //System.out.println(players);
    }

    private HashMap<Integer, Integer> getPlayersPlacementsForCurrentTournament(List<Player> players, Integer tournamentKey) {
        return (HashMap<Integer, Integer>) players.stream().filter(p -> p.getPlacingsByTournament().containsKey(tournamentKey)).collect(Collectors.toMap(Player::getPlayerID, e -> e.getPlacingsByTournament().get(tournamentKey)));
    }

    private HashMap<Integer, Float> getGlobalConsistencyMap(List<Player> players) {
        return (HashMap<Integer, Float>) players.stream().collect(Collectors.toMap(Player::getPlayerID, Player::getConsistencyScoreGlobal));
    }

    private HashMap<Integer, Float> getWeeklyConsistencyMap(List<Player> players) {
        return (HashMap<Integer, Float>) players.stream().collect(Collectors.toMap(Player::getPlayerID, Player::getConsistencyScoreWeekly));

    }

    private HashMap<Integer, Float> getMonthlyConsistencyMap(List<Player> players) {
        return (HashMap<Integer, Float>) players.stream().collect(Collectors.toMap(Player::getPlayerID, Player::getConsistencyScoreMonthly));

    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", playerID=" + playerID +
                ", consistencyScoreWeekly=" + consistencyScoreWeekly +
                ", consistencyScoreMonthly=" + consistencyScoreMonthly +
                ", consistencyScoreGlobal=" + consistencyScoreGlobal +
                ", winLossImpactScore=" + winLossImpactScore +
                ", finalRankingScore=" + finalRankingScore +
                "}\n";
    }
}
