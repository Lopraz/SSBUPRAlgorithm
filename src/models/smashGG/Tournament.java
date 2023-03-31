package models.smashGG;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.smashGG.PlayerDataInTournament;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties("tournament")
public class Tournament {

    @JsonProperty("name")
    public String name;
    @JsonProperty("id")
    public int tournamentID;
    @JsonProperty("numEntrants")
    public int entrants;
    public List<SetOfTournament> sets;

    public List<PlayerDataInTournament> playersData;

    public String type;

    public Tournament() {
        this.playersData = new ArrayList<>();
        this.sets = new ArrayList<>();
    }

    public Tournament(String name, int tournamentID, int entrants, List<SetOfTournament> sets, List<PlayerDataInTournament> playersData, String type) {
        this.name = name;
        this.tournamentID = tournamentID;
        this.entrants = entrants;
        this.sets = sets;
        this.playersData = playersData;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTournamentID() {
        return tournamentID;
    }

    public void setTournamentID(int tournamentID) {
        this.tournamentID = tournamentID;
    }

    public int getEntrants() {
        return entrants;
    }

    public void setEntrants(int entrants) {
        this.entrants = entrants;
    }

    public List<PlayerDataInTournament> getPlayersData() {
        return playersData;
    }

    public void setPlayersData(List<PlayerDataInTournament> playersData) {
        this.playersData = playersData;
    }

    public List<SetOfTournament> getSets() {
        return sets;
    }

    public void setSets(List<SetOfTournament> sets) {
        updatePlayersId(sets);
        this.sets = sets;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private void updatePlayersId(List<SetOfTournament> sets) {
        HashMap<Integer, Integer> tournamentPlayerIDAndPlayerIDPairs = getTournamentPlayerIDAndPlayerIDPairs();
        for (SetOfTournament set : sets) {
            set.updateIDs(tournamentPlayerIDAndPlayerIDPairs);
        }
    }

    private HashMap<Integer, Integer> getTournamentPlayerIDAndPlayerIDPairs() {
        return (HashMap<Integer, Integer>) this.getPlayersData().stream().collect(Collectors.toMap(PlayerDataInTournament::getPlayerIDInTournament, PlayerDataInTournament::getPlayerID));
    }

    @JsonProperty("entrants")
    public void setPlayersData(Map<String, JsonNode> playersData) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        this.playersData = mapper.readValue(playersData.get("nodes").toString(), new TypeReference<>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        });

    }

    @Override
    public String toString() {
        return "Tournament{" +
                "name='" + name + '\'' +
                ", tournamentID=" + tournamentID +
                ", entrants=" + entrants +
                ", playersData=" + playersData +
                ", type=" + type +
                '}';
    }
}
