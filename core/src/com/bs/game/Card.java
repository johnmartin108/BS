package com.bs.game;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

/**
 * Created by johnmartin on 5/19/16.
 */
public class Card {

    private String suit;
    private String rank;
    private Texture img;

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
        String path = "cards/" + getPath(this.suit, this.rank) + ".png";
        this.img = new Texture(path);
    }

    //deserialization constructor - see below
    public Card(ArrayList<String> vals) {
        this.rank = vals.get(0);
        this.suit = vals.get(1);
        String path = "cards/" + getPath(this.suit, this.rank) + ".png";
        this.img = new Texture(path);
    }

    //get the image asset that should be associated with the card
    public void loadTexture() {
        String path = "cards/" + getPath(this.suit, this.rank) + ".png";
        this.img = new Texture(path);
    }

    //helper for debugging
    public String toString() {
        return this.suit + " " + this.rank;
    }

    //for checking card values
    public int valueOf() {
       String s = this.rank;
        if (s.equals("a")) {
            return 1;
        }
        else if (s.equals("j")) {
            return 11;
        }
        else if (s.equals("q")) {
            return 12;
        }
        else if (s.equals("k")) {
            return 13;
        }
        else {
            return Integer.parseInt(this.rank);
        }
    }

    public String suitOf() {
        return this.suit;
    }

    //helper functions for building deck
    public static String toRank(int r) {
        if (2 <= r && r <= 10) {
            return ""+r;
        }
        else {
            switch(r) {
                case 1:
                    return "a";
                case 11:
                    return "j";
                case 12:
                    return "q";
                case 13:
                    return "k";
                default:
                    return "a";
            }
        }

    }

    public static String toSuit(int s) {
        switch(s) {
            case 0:
                return "h";
            case 1:
                return "d";
            case 2:
                return "c";
            case 3:
                return "s";
            default:
                return "h";
        }
    }

    //get the path to the image asset corresponding to the card
    public static String getPath(String suit, String rank) {
        String fullSuit = "";
        String fullRank = "" + rank;
        if (suit.equals("h")) {
            fullSuit = "hearts";
        }
        else if(suit.equals("d")) {
            fullSuit = "diamonds";
        }
        else if(suit.equals("c")) {
            fullSuit = "clubs";
        }
        else if(suit.equals("s")) {
            fullSuit = "spades";
        }
        if (rank.equals("a")) {
            fullRank = "ace";
        }
        else if(rank.equals("j")) {
            fullRank = "jack";
        }
        else if(rank.equals("q")) {
            fullRank = "queen";
        }
        else if(rank.equals("k")) {
            fullRank = "king";
        }
        return fullRank + "_of_" + fullSuit;

    }


    public Texture getTexture() {
        return this.img;
    }
    
    //serialize a card so it can be sent in JSON form
    public ArrayList<String> toSerializable() {
        ArrayList<String> result = new ArrayList<String>();
        result.add(this.rank);
        result.add(this.suit);
        return result;
    }

    //helpers for serializing/deserializing lists of cards or the hands variable (i.e. list of list of cards)
    public static ArrayList<ArrayList<ArrayList<String>>> toHandsDump(ArrayList<ArrayList<Card>> e) {
        ArrayList<ArrayList<ArrayList<String>>> result = new ArrayList<ArrayList<ArrayList<String>>>();
        for (int i = 0; i < e.size(); i++) {
            result.add(new ArrayList<ArrayList<String>>());
            for (Card c: e.get(i)) {
                result.get(i).add(c.toSerializable());
            }
        }
        return result;
    }

    public static ArrayList<ArrayList<String>> toCardsDump(ArrayList<Card> e) {
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        for (Card c: e) {
            result.add(c.toSerializable());
        }
        return result;
    }

    public static ArrayList<ArrayList<Card>> fromHandsDump(ArrayList<ArrayList<ArrayList<String>>> e) {
        ArrayList<ArrayList<Card>> result = new ArrayList<ArrayList<Card>>();
        for (int i = 0; i < e.size(); i++) {
            result.add(new ArrayList<Card>());
            for (ArrayList<String> nums: e.get(i)) {
                result.get(i).add(new Card(nums));
            }
        }
        return result;
    }

    public static ArrayList<Card> fromCardsDump(ArrayList<ArrayList<String>> e) {
        ArrayList<Card> result = new ArrayList<Card>();
        for (ArrayList<String> nums: e) {
            result.add(new Card(nums));
        }
        return result;
    }

    @Override
    public int hashCode() {
        return (suit+rank).hashCode();
    }

    //UI helper - word form of card value
    public static String convertToStringRank(int rank) {
        switch(rank) {
            case 1:
                return "aces";
            case 2:
                return "twos";
            case 3:
                return "threes";
            case 4:
                return "fours";
            case 5:
                return "fives";
            case 6:
                return "sixes";
            case 7:
                return "sevens";
            case 8:
                return "eights";
            case 9:
                return "nines";
            case 10:
                return "tens";
            case 11:
                return "jacks";
            case 12:
                return "queens";
            case 13:
                return "kings";
        }
        return "";
    }
}
