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
    private int region;
    private float consistencyScoreLocal;
    private float consistencyScoreRegional;
    private float consistencyScoreNational;
    private float consistencyScoreMajor;
    private float consistencyScoreGlobal;
    private float consistencyScoreGlobalWithStackedFactor;
    private float winLossImpactScoreRegionalsAndLocals;
    private float winLossImpactScoreNationalsAndMajors;
    private float winLossImpactScore;
    private float finalRankingScore;
    private List<Tournament> tournaments;
    private HashMap<Integer, Integer> placingsByTournament;
    private HashMap<Integer, List<SetOfTournament>> setsByTournament;

    private int majorCount = 0, nationalCount = 0, regionalCount = 0, localCount = 0;

    public Player() {
        this.tournaments = new ArrayList<>();
        this.placingsByTournament = new HashMap<>();
        this.setsByTournament = new HashMap<>();
    }

    public Player(String name, int playerID, int region, Tournament tournament) {
        this.name = name;
        this.playerID = playerID;
        this.region = region;
        this.consistencyScoreLocal = 0;
        this.consistencyScoreRegional = 0;
        this.consistencyScoreNational = 0;
        this.consistencyScoreMajor = 0;
        this.consistencyScoreGlobal = 0;
        this.consistencyScoreGlobalWithStackedFactor = 0;
        this.winLossImpactScoreRegionalsAndLocals = 0;
        this.winLossImpactScoreNationalsAndMajors = 0;
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

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public float getConsistencyScoreLocal() {
        return consistencyScoreLocal;
    }

    public void setConsistencyScoreLocal(float consistencyScoreLocal) {
        this.consistencyScoreLocal = consistencyScoreLocal;
    }

    public float getConsistencyScoreRegional() {
        return consistencyScoreRegional;
    }

    public void setConsistencyScoreRegional(float consistencyScoreRegional) {
        this.consistencyScoreRegional = consistencyScoreRegional;
    }

    public float getConsistencyScoreNational() {
        return consistencyScoreNational;
    }

    public void setConsistencyScoreNational(float consistencyScoreNational) {
        this.consistencyScoreNational = consistencyScoreNational;
    }

    public float getConsistencyScoreMajor() {
        return consistencyScoreMajor;
    }

    public void setConsistencyScoreMajor(float consistencyScoreMajor) {
        this.consistencyScoreMajor = consistencyScoreMajor;
    }

    public float getWinLossImpactScoreRegionalsAndLocals() {
        return winLossImpactScoreRegionalsAndLocals;
    }

    public void setWinLossImpactScoreRegionalsAndLocals(float winLossImpactScoreRegionalsAndLocals) {
        this.winLossImpactScoreRegionalsAndLocals = winLossImpactScoreRegionalsAndLocals;
    }

    public float getWinLossImpactScoreNationalsAndMajors() {
        return winLossImpactScoreNationalsAndMajors;
    }

    public void setWinLossImpactScoreNationalsAndMajors(float winLossImpactScoreNationalsAndMajors) {
        this.winLossImpactScoreNationalsAndMajors = winLossImpactScoreNationalsAndMajors;
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

    public float getConsistencyScoreGlobalWithStackedFactor() {
        return consistencyScoreGlobalWithStackedFactor;
    }

    public void setConsistencyScoreGlobalWithStackedFactor(float consistencyScoreGlobalWithStackedFactor) {
        this.consistencyScoreGlobalWithStackedFactor = consistencyScoreGlobalWithStackedFactor;
    }

    public HashMap<Integer, List<SetOfTournament>> getSetsByTournament() {
        return setsByTournament;
    }

    public void setSetsByTournament(HashMap<Integer, List<SetOfTournament>> setsByTournament) {
        this.setsByTournament = setsByTournament;
    }

    public int getMajorCount() {
        return majorCount;
    }

    public void setMajorCount(int majorCount) {
        this.majorCount = majorCount;
    }

    public int getNationalCount() {
        return nationalCount;
    }

    public void setNationalCount(int nationalCount) {
        this.nationalCount = nationalCount;
    }

    public int getRegionalCount() {
        return regionalCount;
    }

    public void setRegionalCount(int regionalCount) {
        this.regionalCount = regionalCount;
    }

    public int getLocalCount() {
        return localCount;
    }

    public void setLocalCount(int localCount) {
        this.localCount = localCount;
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
                    players.stream().filter(p -> p.getPlayerID() == playerData.getPlayerID()).findFirst().get().getPlacingsByTournament().putIfAbsent(tournament.getTournamentID(), playerData.getPlacing());
                }
                // if the player doesn't already exist, we add them to the list
                else {
                    Player player = new Player(playerData.getName(), playerData.getPlayerID(), playerData.getRegion(), tournament);
                    player.setSetsByTournament(new HashMap<>() {{
                        put(tournament.getTournamentID(), tournament.getSets().stream().filter(set -> set.getPlayer1() == playerData.getPlayerID() || set.getPlayer2() == playerData.getPlayerID()).collect(Collectors.toList()));
                    }});
                    player.setPlacingsByTournament(new HashMap<>() {{
                        put(tournament.getTournamentID(), playerData.getPlacing());
                    }});
                    players.add(player);
                }
            }
        }
    }

    public static void calculateConsistencyScoresForAll(List<Player> players, List<Tournament> tournaments) {
        players.forEach(Player::calculateConsistencyScoresAndRankListForOne);
        Map<Integer, Float> tournamentTiering = generateTournamentTiering(players, tournaments);
        for (Player player : players) {
            if (player.getRegion() > 13 && player.getRegion() != 35 && player.getRegion() != 36)
                player.updateConsistencyScoresForOneForeigner(tournamentTiering, players);
            else
                player.updateConsistencyScoresForOneFrench(tournamentTiering);
        }
    }

    private void updateConsistencyScoresForOneForeigner(Map<Integer, Float> tournamentTiering, List<Player> players) {
        float local = 0, regional = 0, national = 0, major = 0;
        for (Tournament tournament : getTournaments()) {
            float stackedIndicator = tournamentTiering.get(tournament.getTournamentID());
            switch (tournament.getType()) {
                case "local": {
                    local += stackedIndicator;
                    break;
                }
                case "regional": {
                    regional += stackedIndicator;
                    break;
                }
                case "national": {
                    national += stackedIndicator;
                    break;
                }
                case "major": {
                    major += stackedIndicator;
                    break;
                }
            }
        }

        if (getLocalCount() > 0)
            local = local / getLocalCount();

        if (getRegionalCount() > 0)
            regional = regional / getRegionalCount();

        if (getNationalCount() > 0)
            national = national / getNationalCount();

        if (getMajorCount() > 0)
            major = major / getMajorCount();

        if (getRegion() > 13 && getRegion() != 35 && getRegion() != 36) {
            if (getNationalCount() > 0 && getMajorCount() > 0) {
                setConsistencyScoreLocal(getConsistencyScoreLocal() + local);
                setConsistencyScoreRegional(getConsistencyScoreRegional() + regional * 1.5f);
                setConsistencyScoreNational(getConsistencyScoreNational() + national * 2.25f);
                setConsistencyScoreMajor(getConsistencyScoreMajor() + major * 3f);
                setConsistencyScoreGlobalWithStackedFactor(getConsistencyScoreGlobal() + national * 2.25f + major * 3f);
            } else if (getMajorCount() > 0) {
                setConsistencyScoreLocal(getConsistencyScoreLocal() + local);
                setConsistencyScoreRegional(getConsistencyScoreRegional() + regional * 1.5f);
                setConsistencyScoreMajor(getConsistencyScoreMajor() + major * 5.25f);
                setConsistencyScoreGlobalWithStackedFactor(getConsistencyScoreGlobal() + major * 5.25f);
            } else {
                setConsistencyScoreLocal(getConsistencyScoreLocal() + local);
                setConsistencyScoreRegional(getConsistencyScoreRegional() + regional * 1.5f);
                setConsistencyScoreNational(getConsistencyScoreNational() + national * 4f);
                setConsistencyScoreGlobalWithStackedFactor(getConsistencyScoreGlobal() + national * 4f);
            }
        }
    }

    private void updateConsistencyScoresForOneFrench(Map<Integer, Float> tournamentTiering) {
        float local = 0, regional = 0, national = 0, major = 0;
        for (Tournament tournament : getTournaments()) {
            float stackedIndicator = tournamentTiering.get(tournament.getTournamentID());
            switch (tournament.getType()) {
                case "local": {
                    local += stackedIndicator;
                    break;
                }
                case "regional": {
                    regional += stackedIndicator;
                    break;
                }
                case "national": {
                    national += stackedIndicator;
                    break;
                }
                case "major": {
                    major += stackedIndicator;
                    break;
                }
            }
        }

        if (getLocalCount() > 0)
            local = local / getLocalCount();

        if (getRegionalCount() > 0)
            regional = regional / getRegionalCount();

        if (getNationalCount() > 0)
            national = national * getNationalCount() / Constants.NATIONAL_COUNT;

        if (getMajorCount() > 0)
            major = major * getMajorCount() / Constants.MAJOR_COUNT;

        if (getLocalCount() != 0 && getRegionalCount() != 0 && getNationalCount() != 0 && getMajorCount() != 0) {
            setConsistencyScoreLocal(getConsistencyScoreLocal() + local * Constants.LOCALS_ENHANCED_FACTOR);
            setConsistencyScoreRegional(getConsistencyScoreRegional() + regional * Constants.REGIONALS_ENHANCED_FACTOR);
            setConsistencyScoreNational(getConsistencyScoreNational() + national * Constants.REGIONALS_ENHANCED_FACTOR);
            setConsistencyScoreMajor(getConsistencyScoreMajor() + major * Constants.REGIONALS_ENHANCED_FACTOR);
            setConsistencyScoreGlobalWithStackedFactor(getConsistencyScoreGlobal() + local * Constants.LOCALS_ENHANCED_FACTOR + regional * Constants.REGIONALS_ENHANCED_FACTOR + national * Constants.NATIONALS_ENHANCED_FACTOR + major * Constants.MAJORS_ENHANCED_FACTOR);
        } else {
            setConsistencyScoreLocal(getConsistencyScoreLocal() + local * Constants.LOCALS_FACTOR);
            setConsistencyScoreRegional(getConsistencyScoreRegional() + regional * Constants.REGIONALS_FACTOR);
            setConsistencyScoreNational(getConsistencyScoreNational() + national * Constants.REGIONALS_FACTOR);
            setConsistencyScoreMajor(getConsistencyScoreMajor() + major * Constants.REGIONALS_FACTOR);
            setConsistencyScoreGlobalWithStackedFactor(getConsistencyScoreGlobal() + local * Constants.LOCALS_FACTOR + regional * Constants.REGIONALS_FACTOR + national * Constants.NATIONALS_FACTOR + major * Constants.MAJORS_FACTOR);
        }

    }

    private static Map<Integer, Float> generateTournamentTiering(List<Player> players, List<Tournament> tournaments) {
        Map<Integer, Float> tournamentTiering = new HashMap<>();
        Object[] localAttendancePlayerCountPair = new Object[]{0f, 0};
        Object[] regionalAttendancePlayerCountPair = new Object[]{0f, 0};
        Object[] nationalAttendancePlayerCountPair = new Object[]{0f, 0};
        Object[] majorAttendancePlayerCountPair = new Object[]{0f, 0};

        for (Tournament tournament : tournaments) {
            float stackedIndicatorBasedOnPerformance = 0f, stackedIndicatorBasedOnAttendance = 0f;

            List<Player> playersInThisTournament = players.stream().filter(player -> tournament.getPlayersData().stream().anyMatch(playerDataInTournament -> playerDataInTournament.getPlayerID() == player.getPlayerID() && (playerDataInTournament.getIsDisqualified() == null || !playerDataInTournament.getIsDisqualified().equals("true")))).collect(Collectors.toList());
            for (Player player : playersInThisTournament) {
                switch (tournament.getType()) {
                    case "local": {
                        stackedIndicatorBasedOnPerformance += player.getConsistencyScoreLocal() - player.getPlacingsByTournament().get(tournament.getTournamentID());
                        stackedIndicatorBasedOnAttendance += player.getConsistencyScoreGlobal();

                        break;
                    }
                    case "regional": {
                        stackedIndicatorBasedOnPerformance += player.getConsistencyScoreRegional() - player.getPlacingsByTournament().get(tournament.getTournamentID());
                        stackedIndicatorBasedOnAttendance += player.getConsistencyScoreGlobal();

                        break;
                    }
                    case "national": {
                        stackedIndicatorBasedOnPerformance += player.getConsistencyScoreNational() - player.getPlacingsByTournament().get(tournament.getTournamentID());
                        stackedIndicatorBasedOnAttendance += player.getConsistencyScoreGlobal();

                        break;
                    }
                    case "major": {
                        stackedIndicatorBasedOnPerformance += player.getConsistencyScoreMajor() - player.getPlacingsByTournament().get(tournament.getTournamentID());
                        stackedIndicatorBasedOnAttendance += player.getConsistencyScoreGlobal();

                        break;
                    }
                }

            }
            // We don't penalize tournaments that are less stacked. However stacked tournaments get bonus points.


            tournamentTiering.putIfAbsent(tournament.getTournamentID(), (stackedIndicatorBasedOnPerformance + stackedIndicatorBasedOnAttendance) / playersInThisTournament.size());
            switch (tournament.getType()) {
                case "local": {
                    localAttendancePlayerCountPair[0] = (float) localAttendancePlayerCountPair[0] + stackedIndicatorBasedOnAttendance;
                    localAttendancePlayerCountPair[1] = (int) localAttendancePlayerCountPair[1] + playersInThisTournament.size();
                    break;
                }
                case "regional": {
                    regionalAttendancePlayerCountPair[0] = (float) regionalAttendancePlayerCountPair[0] + stackedIndicatorBasedOnAttendance;
                    regionalAttendancePlayerCountPair[1] = (int) regionalAttendancePlayerCountPair[1] + playersInThisTournament.size();
                    break;
                }
                case "national": {
                    nationalAttendancePlayerCountPair[0] = (float) nationalAttendancePlayerCountPair[0] + stackedIndicatorBasedOnAttendance;
                    nationalAttendancePlayerCountPair[1] = (int) nationalAttendancePlayerCountPair[1] + playersInThisTournament.size();
                    break;
                }
                case "major": {
                    majorAttendancePlayerCountPair[0] = (float) majorAttendancePlayerCountPair[0] + stackedIndicatorBasedOnAttendance;
                    majorAttendancePlayerCountPair[1] = (int) majorAttendancePlayerCountPair[1] + playersInThisTournament.size();
                    break;
                }
            }

        }


        for (Tournament tournament : tournaments) {

            switch (tournament.getType()) {
                case "local": {
                    tournamentTiering.put(tournament.getTournamentID(), Math.max(0f, 0.25f * (tournamentTiering.get(tournament.getTournamentID()) - (float) localAttendancePlayerCountPair[0] / (int) localAttendancePlayerCountPair[1])));
                    break;
                }
                case "regional": {
                    tournamentTiering.put(tournament.getTournamentID(), Math.max(0f, 0.5f * (tournamentTiering.get(tournament.getTournamentID()) - (float) regionalAttendancePlayerCountPair[0] / (int) regionalAttendancePlayerCountPair[1])));
                    break;
                }
                case "national": {
                    tournamentTiering.put(tournament.getTournamentID(), Math.max(0f, 0.75f * (tournamentTiering.get(tournament.getTournamentID()) - (float) nationalAttendancePlayerCountPair[0] / (int) nationalAttendancePlayerCountPair[1])));
                    break;
                }
                case "major": {
                    tournamentTiering.put(tournament.getTournamentID(), Math.max(0f, tournamentTiering.get(tournament.getTournamentID()) - (float) majorAttendancePlayerCountPair[0] / (int) majorAttendancePlayerCountPair[1]));
                    break;
                }
            }

        }
        return tournamentTiering;
    }

    // Highest Score achievable is 525
    private static void calculateConsistencyScoresAndRankListForOne(Player player) {

        // Adds all the scores based on tournament type
        for (Tournament tournament : player.getTournaments()) {
            int placing = tournament.getPlayersData().stream().filter(p -> p.getPlayerID() == player.getPlayerID()).findFirst().get().getPlacing();


            switch (tournament.getType()) {
                case "local": {
                    player.setLocalCount(player.getLocalCount() + 1);
                    player.setConsistencyScoreLocal(player.getConsistencyScoreLocal() + placing);
                    break;
                }
                case "regional": {
                    player.setRegionalCount(player.getRegionalCount() + 1);
                    player.setConsistencyScoreRegional(player.getConsistencyScoreRegional() + placing);
                    break;
                }
                case "national": {
                    player.setNationalCount(player.getNationalCount() + 1);
                    player.setConsistencyScoreNational(player.getConsistencyScoreNational() + placing);
                    break;
                }
                case "major": {
                    player.setMajorCount(player.getMajorCount() + 1);
                    player.setConsistencyScoreMajor(player.getConsistencyScoreMajor() + placing);
                    break;
                }
            }


        }
        if (player.getLocalCount() > 0)
            player.setConsistencyScoreLocal(player.getConsistencyScoreLocal() / player.getLocalCount());

        if (player.getRegionalCount() > 0)
            player.setConsistencyScoreRegional(player.getConsistencyScoreRegional() / player.getRegionalCount());

        if (player.getNationalCount() > 0)
            player.setConsistencyScoreNational(player.getConsistencyScoreNational() / player.getNationalCount());

        if (player.getMajorCount() > 0)
            player.setConsistencyScoreMajor(player.getConsistencyScoreMajor() / player.getMajorCount());

        float playerConsistencyAll = 0f, playerConsistencyRegionalPlus = 0f;
        if (player.getRegion() > 13 && player.getRegion() != 35 && player.getRegion() != 36) {
            if (player.getNationalCount() > 0 && player.getMajorCount() > 0) {
                player.setConsistencyScoreGlobal(player.getConsistencyScoreNational() * 2.25f + player.getConsistencyScoreMajor() * 3f);
            } else if (player.getMajorCount() > 0) {
                player.setConsistencyScoreGlobal(player.getConsistencyScoreMajor() * 5.25f);
            } else {
                player.setConsistencyScoreGlobal(player.getConsistencyScoreNational() * 4f);
            }
        } else {
            playerConsistencyRegionalPlus = player.getConsistencyScoreRegional() * Constants.REGIONALS_ENHANCED_FACTOR + player.getConsistencyScoreNational() * Constants.NATIONALS_ENHANCED_FACTOR + player.getConsistencyScoreMajor() * Constants.MAJORS_ENHANCED_FACTOR;

            playerConsistencyAll = player.getConsistencyScoreLocal() * Constants.LOCALS_FACTOR + player.getConsistencyScoreRegional() * Constants.REGIONALS_FACTOR + player.getConsistencyScoreNational() * Constants.NATIONALS_FACTOR + player.getConsistencyScoreMajor() * Constants.MAJORS_FACTOR;

            player.setConsistencyScoreGlobal(Math.max(playerConsistencyAll, playerConsistencyRegionalPlus) * 0.6f + Math.min(playerConsistencyAll, playerConsistencyRegionalPlus) * 0.4f);
        }

    }

    public static void calculateWinLossScore(List<Player> players, List<Tournament> tournaments) {
        players.forEach(p -> p.calculateWinLossScoreForOne(players, tournaments));
    }

    private void calculateWinLossScoreForOne(List<Player> players, List<Tournament> tournaments) {
        HashMap<Integer, Float> majorConsistencyByPlayer = getMajorConsistencyMap(players);
        HashMap<Integer, Float> nationalConsistencyByPlayer = getNationalConsistencyMap(players);
        HashMap<Integer, Float> regionalConsistencyByPlayer = getRegionalConsistencyMap(players);
        HashMap<Integer, Float> localConsistencyByPlayer = getLocalConsistencyMap(players);
        HashMap<Integer, Float> globalConsistencyByPlayer = getGlobalConsistencyMap(players);
        HashMap<Integer, String> tournamentTypeMap = getTournamentTypeMap(tournaments);

        for (Integer tournamentKey : this.getSetsByTournament().keySet()) {
            // only the two best wins and two worst losses are accounted for
            float[] maxValue = {0f, 0f}, minValue = {0f, 0f};
            float calculatedValue, upsetFactorOnTournamentType, upsetFactorOnTournamentPerformance, upsetFactorOnGlobalConsistencyPerformance;
            // Map of the placings of all participants of the tournament
            HashMap<Integer, Integer> playersPlacements = getPlayersPlacementsForCurrentTournament(players, tournamentKey);
            List<SetOfTournament> setsOfTournament = this.getSetsByTournament().get(tournamentKey);
            if (tournamentTypeMap.get(tournamentKey).equals("major")) {
                for (SetOfTournament set : setsOfTournament) {

                    int opponentID = set.getPlayer1() == this.getPlayerID() ? set.getPlayer2() : set.getPlayer1();
                    // We dont want to use DQs
                    if (!playersPlacements.containsKey(opponentID))
                        continue;
                    // current player won the set
                    if (set.getWinnerId() == this.getPlayerID()) {

                        // max value = 125
                        upsetFactorOnTournamentType = majorConsistencyByPlayer.get(opponentID) * 1.5f;
                        // max value = 525
                        upsetFactorOnGlobalConsistencyPerformance = globalConsistencyByPlayer.get(opponentID);
                        // max value = 125
                        upsetFactorOnTournamentPerformance = playersPlacements.get(opponentID) * 1.5f;

                        calculatedValue = (upsetFactorOnTournamentType + upsetFactorOnGlobalConsistencyPerformance + upsetFactorOnTournamentPerformance) / Constants.WIN_LOSS_IMPACT_SCORE;
                        updateValues(maxValue, calculatedValue, 0);
                    } // current player lost the set
                    else {
                        // max value = 100, min value = 7
                        if (regionalConsistencyByPlayer.get(set.getWinnerId()) < this.getConsistencyScoreMajor())
                            upsetFactorOnTournamentType = this.getConsistencyScoreRegional() - regionalConsistencyByPlayer.get(set.getWinnerId());
                        else
                            upsetFactorOnTournamentType = 0;
                        // max value = 525, min value = 12.25
                        if (globalConsistencyByPlayer.get(set.getWinnerId()) < this.getConsistencyScoreGlobalWithStackedFactor())
                            upsetFactorOnGlobalConsistencyPerformance = this.getConsistencyScoreGlobalWithStackedFactor() - globalConsistencyByPlayer.get(set.getWinnerId());
                        else
                            upsetFactorOnGlobalConsistencyPerformance = 0;
                        // max value = 100, min value = 7
                        if (playersPlacements.get(set.getWinnerId()) < playersPlacements.get(this.getPlayerID()))
                            upsetFactorOnTournamentPerformance = playersPlacements.get(this.getPlayerID()) - playersPlacements.get(set.getWinnerId());
                        else
                            upsetFactorOnTournamentPerformance = 0;
                        // max value = roughly 250
                        calculatedValue = -(upsetFactorOnTournamentType + upsetFactorOnGlobalConsistencyPerformance + upsetFactorOnTournamentPerformance) / Constants.WIN_LOSS_IMPACT_SCORE;
                        updateValues(minValue, calculatedValue, 1);
                    }

                }
            } else if (tournamentTypeMap.get(tournamentKey).equals("national")) {
                for (SetOfTournament set : setsOfTournament) {

                    int opponentID = set.getPlayer1() == this.getPlayerID() ? set.getPlayer2() : set.getPlayer1();
                    // We dont want to use DQs
                    if (!playersPlacements.containsKey(opponentID))
                        continue;
                    // current player won the set
                    if (set.getWinnerId() == this.getPlayerID()) {

                        // max value = 100
                        upsetFactorOnTournamentType = nationalConsistencyByPlayer.get(opponentID) * 0.75f;
                        // max value = 525
                        upsetFactorOnGlobalConsistencyPerformance = globalConsistencyByPlayer.get(opponentID);
                        // max value = 100
                        upsetFactorOnTournamentPerformance = playersPlacements.get(opponentID) * 0.75f;
                        // max value = roughly 250; first place is guaranteed to get full points

                        calculatedValue = (upsetFactorOnTournamentType + upsetFactorOnGlobalConsistencyPerformance + upsetFactorOnTournamentPerformance) / Constants.WIN_LOSS_IMPACT_SCORE;
                        updateValues(maxValue, calculatedValue, 0);
                    } // current player lost the set
                    else {
                        // max value = 100, min value = 7
                        if (regionalConsistencyByPlayer.get(set.getWinnerId()) < this.getConsistencyScoreNational())
                            upsetFactorOnTournamentType = this.getConsistencyScoreNational() - nationalConsistencyByPlayer.get(set.getWinnerId());
                        else
                            upsetFactorOnTournamentType = 0;
                        // max value = 525, min value = 12.25
                        if (globalConsistencyByPlayer.get(set.getWinnerId()) < this.getConsistencyScoreGlobalWithStackedFactor())
                            upsetFactorOnGlobalConsistencyPerformance = this.getConsistencyScoreGlobalWithStackedFactor() - globalConsistencyByPlayer.get(set.getWinnerId());
                        else
                            upsetFactorOnGlobalConsistencyPerformance = 0;
                        // max value = 100, min value = 7
                        if (playersPlacements.get(set.getWinnerId()) < playersPlacements.get(this.getPlayerID()))
                            upsetFactorOnTournamentPerformance = playersPlacements.get(this.getPlayerID()) - playersPlacements.get(set.getWinnerId());
                        else
                            upsetFactorOnTournamentPerformance = 0;
                        // max value = roughly 250
                        calculatedValue = -(upsetFactorOnTournamentType + upsetFactorOnGlobalConsistencyPerformance + upsetFactorOnTournamentPerformance) / Constants.WIN_LOSS_IMPACT_SCORE;
                        updateValues(minValue, calculatedValue, 1);
                    }

                }
            } else if (tournamentTypeMap.get(tournamentKey).equals("regional")) {
                for (SetOfTournament set : setsOfTournament) {

                    int opponentID = set.getPlayer1() == this.getPlayerID() ? set.getPlayer2() : set.getPlayer1();
                    // We dont want to use DQs
                    if (!playersPlacements.containsKey(opponentID))
                        continue;
                    // current player won the set
                    if (set.getWinnerId() == this.getPlayerID()) {
                        // max value = 100
                        upsetFactorOnTournamentType = regionalConsistencyByPlayer.get(opponentID) * 1.10f;
                        // max value = 525
                        upsetFactorOnGlobalConsistencyPerformance = globalConsistencyByPlayer.get(opponentID);
                        // max value = 100
                        upsetFactorOnTournamentPerformance = playersPlacements.get(opponentID) * 1.10f;
                        // max value = roughly 250; first place is guaranteed to get full points

                        calculatedValue = (upsetFactorOnTournamentType + upsetFactorOnGlobalConsistencyPerformance + upsetFactorOnTournamentPerformance) / Constants.WIN_LOSS_IMPACT_SCORE;
                        updateValues(maxValue, calculatedValue, 0);
                    } // current player lost the set
                    else {
                        // max value = 100, min value = 7
                        if (regionalConsistencyByPlayer.get(set.getWinnerId()) < this.getConsistencyScoreRegional())
                            upsetFactorOnTournamentType = this.getConsistencyScoreRegional() - regionalConsistencyByPlayer.get(set.getWinnerId());
                        else
                            upsetFactorOnTournamentType = 0;
                        // max value = 525, min value = 12.25
                        if (globalConsistencyByPlayer.get(set.getWinnerId()) < this.getConsistencyScoreGlobalWithStackedFactor())
                            upsetFactorOnGlobalConsistencyPerformance = this.getConsistencyScoreGlobalWithStackedFactor() - globalConsistencyByPlayer.get(set.getWinnerId());
                        else
                            upsetFactorOnGlobalConsistencyPerformance = 0;
                        // max value = 100, min value = 7
                        if (playersPlacements.get(set.getWinnerId()) < playersPlacements.get(this.getPlayerID()))
                            upsetFactorOnTournamentPerformance = playersPlacements.get(this.getPlayerID()) - playersPlacements.get(set.getWinnerId());
                        else
                            upsetFactorOnTournamentPerformance = 0;
                        // max value = roughly 250
                        calculatedValue = -(upsetFactorOnTournamentType + upsetFactorOnGlobalConsistencyPerformance + upsetFactorOnTournamentPerformance) / Constants.WIN_LOSS_IMPACT_SCORE;
                        updateValues(minValue, calculatedValue, 1);
                    }

                }
            } else if (tournamentTypeMap.get(tournamentKey).equals("local")) {
                for (SetOfTournament set : setsOfTournament) {

                    int opponentID = set.getPlayer1() == this.getPlayerID() ? set.getPlayer2() : set.getPlayer1();
                    // We dont want to use DQs
                    if (!playersPlacements.containsKey(opponentID))
                        continue;
                    // current player won the set
                    if (set.getWinnerId() == this.getPlayerID()) {

                        // max value = 100
                        upsetFactorOnTournamentType = localConsistencyByPlayer.get(opponentID);
                        // max value = 525
                        upsetFactorOnGlobalConsistencyPerformance = globalConsistencyByPlayer.get(opponentID);
                        // max value = 100
                        upsetFactorOnTournamentPerformance = playersPlacements.get(opponentID);
                        // max value = roughly 250; first place is guaranteed to get full points

                        calculatedValue = (upsetFactorOnTournamentType + upsetFactorOnGlobalConsistencyPerformance + upsetFactorOnTournamentPerformance) / Constants.WIN_LOSS_IMPACT_SCORE;
                        updateValues(maxValue, calculatedValue, 0);
                    } // current player lost the set
                    else {
                        // max value = 100, min value = 9
                        if (localConsistencyByPlayer.get(set.getWinnerId()) < this.getConsistencyScoreLocal())
                            upsetFactorOnTournamentType = this.getConsistencyScoreLocal() - localConsistencyByPlayer.get(set.getWinnerId());
                        else
                            upsetFactorOnTournamentType = 0;
                        // max value = 525, min value = 12.25
                        if (globalConsistencyByPlayer.get(set.getWinnerId()) < this.getConsistencyScoreGlobalWithStackedFactor())
                            upsetFactorOnGlobalConsistencyPerformance = this.getConsistencyScoreGlobalWithStackedFactor() - globalConsistencyByPlayer.get(set.getWinnerId());
                        else
                            upsetFactorOnGlobalConsistencyPerformance = 0;
                        // max value = 100, min value = 9
                        if (playersPlacements.get(set.getWinnerId()) < playersPlacements.get(this.getPlayerID()))
                            upsetFactorOnTournamentPerformance = playersPlacements.get(this.getPlayerID()) - playersPlacements.get(set.getWinnerId());
                        else
                            upsetFactorOnTournamentPerformance = 0;
                        // max value = roughly 250
                        calculatedValue = -(upsetFactorOnTournamentType + upsetFactorOnGlobalConsistencyPerformance + upsetFactorOnTournamentPerformance) / Constants.WIN_LOSS_IMPACT_SCORE;
                        updateValues(minValue, calculatedValue, 1);
                    }

                }
            }
            if (tournamentTypeMap.get(tournamentKey).equals("local") || tournamentTypeMap.get(tournamentKey).equals("regional"))
                this.setWinLossImpactScoreRegionalsAndLocals(this.getWinLossImpactScoreRegionalsAndLocals() + (maxValue[0] + maxValue[1] + minValue[0] + minValue[1]) / 2f);
            else
                this.setWinLossImpactScoreNationalsAndMajors(this.getWinLossImpactScoreNationalsAndMajors() + (maxValue[0] + maxValue[1] + minValue[0] + minValue[1]) / 2f);


        }
        if (getLocalCount() + getRegionalCount() > 0 && getNationalCount() + getMajorCount() > 0) {
            this.setWinLossImpactScoreRegionalsAndLocals(this.getWinLossImpactScoreRegionalsAndLocals() / (getLocalCount() + getRegionalCount()));
            this.setWinLossImpactScoreNationalsAndMajors(this.getWinLossImpactScoreNationalsAndMajors() / (getNationalCount() + getMajorCount()));
            this.setWinLossImpactScore(this.getWinLossImpactScoreRegionalsAndLocals() * 0.75f + this.getWinLossImpactScoreNationalsAndMajors() * 1.75f);
        } else if (getNationalCount() + getMajorCount() > 0) {
            this.setWinLossImpactScoreNationalsAndMajors(this.getWinLossImpactScoreNationalsAndMajors() / (getNationalCount() + getMajorCount()));
            this.setWinLossImpactScore(this.getWinLossImpactScoreNationalsAndMajors() * 2.5f);
        } else {
            this.setWinLossImpactScoreRegionalsAndLocals(this.getWinLossImpactScoreRegionalsAndLocals() / (getLocalCount() + getRegionalCount()));
            this.setWinLossImpactScore(this.getWinLossImpactScoreRegionalsAndLocals() * 1.75f);
        }
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
        players.forEach(p -> p.setFinalRankingScore(p.getConsistencyScoreGlobalWithStackedFactor() * Constants.CONSISTENCY_COEFFICIENT + p.getWinLossImpactScore() * Constants.WIN_LOSS_IMPACT_COEFFICIENT));
        players.sort((o1, o2) -> Float.compare(o1.getFinalRankingScore(), o2.getFinalRankingScore()));
        Collections.reverse(players);
        List<Player> top100 = players.stream().filter(p -> ((p.getRegion() < 14 || p.getRegion() == 35 || p.getRegion() == 36) && p.getTournaments().size() >= 4 && p.getRegionalCount() + p.getNationalCount() + p.getMajorCount() >= 1) || (p.getRegion() > 13 && p.getRegion() != 35 && p.getRegion() != 36 && p.getTournaments().size() >= 6 && p.getRegionalCount() + p.getNationalCount() + p.getMajorCount() >= 1)).limit(200).collect(Collectors.toList());


        int i = 1;
        int j = 1;
        System.out.println("placing;name;local;regional;national;major;global;globalwithstackedfactor;winlossimpactscorelocalsandregionals;winlossimpactscorenationalsandmajors;winlossimpactscoreglobal;finalscore");
        for (Player player : top100) {
            // System.out.println(i++ + " : " + player.getName() + " : " + player.getFinalRankingScore());
            if (player.getRegion() > 13 && player.getRegion() != 35 && player.getRegion() != 36)
                System.out.println(i - 0.5f + ";" + player.toCSV());
            else
                System.out.println(i++ + ";" + player.toCSV());
            j++;
        }

    }

    private HashMap<Integer, Integer> getPlayersPlacementsForCurrentTournament(List<Player> players, Integer tournamentKey) {
        return (HashMap<Integer, Integer>) players.stream().filter(p -> p.getPlacingsByTournament().containsKey(tournamentKey)).collect(Collectors.toMap(Player::getPlayerID, e -> e.getPlacingsByTournament().get(tournamentKey)));
    }

    private HashMap<Integer, Float> getGlobalConsistencyMap(List<Player> players) {
        return (HashMap<Integer, Float>) players.stream().collect(Collectors.toMap(Player::getPlayerID, Player::getConsistencyScoreGlobalWithStackedFactor));
    }

    private HashMap<Integer, Float> getLocalConsistencyMap(List<Player> players) {
        return (HashMap<Integer, Float>) players.stream().collect(Collectors.toMap(Player::getPlayerID, Player::getConsistencyScoreLocal));

    }

    private HashMap<Integer, Float> getRegionalConsistencyMap(List<Player> players) {
        return (HashMap<Integer, Float>) players.stream().collect(Collectors.toMap(Player::getPlayerID, Player::getConsistencyScoreRegional));

    }

    private HashMap<Integer, Float> getNationalConsistencyMap(List<Player> players) {
        return (HashMap<Integer, Float>) players.stream().collect(Collectors.toMap(Player::getPlayerID, Player::getConsistencyScoreNational));

    }

    private HashMap<Integer, Float> getMajorConsistencyMap(List<Player> players) {
        return (HashMap<Integer, Float>) players.stream().collect(Collectors.toMap(Player::getPlayerID, Player::getConsistencyScoreMajor));

    }

    private HashMap<Integer, String> getTournamentTypeMap(List<Tournament> tournaments) {
        return (HashMap<Integer, String>) tournaments.stream().collect(Collectors.toMap(Tournament::getTournamentID, Tournament::getType));
    }


    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", Local=" + consistencyScoreLocal +
                ", Regional=" + consistencyScoreRegional +
                ", National=" + consistencyScoreNational +
                ", Major=" + consistencyScoreMajor +
                ", Global=" + consistencyScoreGlobal +
                ", GlobalWithStackedFactor=" + consistencyScoreGlobalWithStackedFactor +
                ", winLossImpactScoreRegionalsAndLocals=" + winLossImpactScoreRegionalsAndLocals +
                ", winLossImpactScoreNationalsAndMajors=" + winLossImpactScoreNationalsAndMajors +
                ", winLossImpactScore=" + winLossImpactScore +
                ", finalRankingScore=" + finalRankingScore +
                "}\n";
    }

    public String toCSV() {
        return name + ";" + consistencyScoreLocal + ";" + consistencyScoreRegional + ";" + consistencyScoreNational + ";" + consistencyScoreMajor + ";" + consistencyScoreGlobal + ";" + consistencyScoreGlobalWithStackedFactor + ";" + winLossImpactScoreRegionalsAndLocals + ";" + winLossImpactScoreNationalsAndMajors + ";" + winLossImpactScore + ";" + finalRankingScore;

    }
}
