package models.smashGG;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.HashMap;

public class SetOfTournament {
    private int id;
    private int winnerId;
    private int player1;
    private int player2;

    public SetOfTournament() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(int winnerId) {
        this.winnerId = winnerId;
    }

    public int getPlayer1() {
        return player1;
    }

    public void setPlayer1(int player1) {
        this.player1 = player1;
    }

    public int getPlayer2() {
        return player2;
    }

    public void setPlayer2(int player2) {
        this.player2 = player2;
    }

    @JsonProperty("slots")
    public void setPlayersInfo(ArrayNode players) {
        this.player1 = Integer.parseInt(players.get(0).get("entrant").get("id").toString());
        this.player2 = Integer.parseInt(players.get(1).get("entrant").get("id").toString());
    }

    @Override
    public String toString() {
        return "SetOfTournament{" +
                "id=" + id +
                ", winnerId=" + winnerId +
                ", player1=" + player1 +
                ", player2=" + player2 +
                '}';
    }

    public void updateIDs(HashMap<Integer, Integer> tournamentPlayerIDAndPlayerIDPairs) {
        if(this.getWinnerId() == this.getPlayer1())
            this.setWinnerId(tournamentPlayerIDAndPlayerIDPairs.get(this.getPlayer1()));
        else
            this.setWinnerId(tournamentPlayerIDAndPlayerIDPairs.get(this.getPlayer2()));
        this.setPlayer1(tournamentPlayerIDAndPlayerIDPairs.get(this.getPlayer1()));
        this.setPlayer2(tournamentPlayerIDAndPlayerIDPairs.get(this.getPlayer2()));
    }
}
