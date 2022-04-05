package models.smashGG;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.Map;


public class PlayerDataInTournament {

    public String name;
    public int playerID;
    @JsonProperty("id")
    public int playerIDInTournament;
    public int placing;
    public String isDisqualified;

    public PlayerDataInTournament() {
    }

    public PlayerDataInTournament(String name, int playerID, int playerIDInTournament, int placing, String isDisqualified) {
        this.name = name;
        this.playerID = playerID;
        this.playerIDInTournament = playerIDInTournament;
        this.placing = placing;
        this.isDisqualified = isDisqualified;
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

    public int getPlayerIDInTournament() {
        return playerIDInTournament;
    }

    public void setPlayerIDInTournament(int playerIDInTournament) {
        this.playerIDInTournament = playerIDInTournament;
    }

    public int getPlacing() {
        return placing;
    }

    public void setPlacing(int placing) {
        this.placing = placing;
    }

    public String getIsDisqualified() {
        return isDisqualified;
    }

    public void setIsDisqualified(String isDisqualified) {
        this.isDisqualified = isDisqualified;
    }

    @JsonProperty("standing")
    public void setPlacing(Map<String, String> standing) {
        this.placing = Integer.parseInt(standing.get("placement"));
    }

    @JsonProperty("participants")
    public void setParticipantsInfo(ArrayNode participants) {
        String id = participants.get(0).get("user") == null || participants.get(0).get("user").toString().equals("null") ? "0" : participants.get(0).get("user").get("id").toString() ;
        this.name = participants.get(0).get("gamerTag").asText();
        this.playerID = Integer.parseInt(id);
    }

    @Override
    public String toString() {
        return "PlayerDataInTournament{" +
                "name='" + name + '\'' +
                ", playerID=" + playerID +
                ", playerIDInTournament=" + playerIDInTournament +
                ", placing=" + placing +
                ", isDisqualified='" + isDisqualified + '\'' +
                '}';
    }
}
