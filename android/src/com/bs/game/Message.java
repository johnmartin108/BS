package com.bs.game;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * this is a inter device message payload serializer
 * Created by ziyuanliu on 5/25/16.
 */

@JsonObject
public class Message {


    @JsonField
    public int eventType;

    @JsonField
    public String message;

    @JsonField
    public int currentPlayerTurn;

    @JsonField
    public int numPlayers;

    @JsonField
    public Map<String, ArrayList<Card>> cardsInHands;

    @JsonField
    public int targetRank;

    @JsonField
    public String winningName;

    @JsonField
    public int CallerID;

    @JsonField
    public int PlayerID;

    @JsonField
    public ArrayList<Card> cardPile;

    @JsonField
    public ArrayList<Card> prevPlay;

    @JsonField
    public ArrayList<Card> addCards;

    @JsonField
    public ArrayList<Card> playedCards;
}
