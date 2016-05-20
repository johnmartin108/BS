package com.example.johnmartin.bs;

/**
 * Created by johnmartin on 5/19/16.
 */
public class Card {

    private String suit;
    private String rank;

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public String toString() {
        return this.suit + " " + this.rank;
    }

    //for comparing cards
    public int valueOf() {
        switch (this.rank) {
            case "A":
                return 1;
            case "J":
                return 11;
            case "Q":
                return 12;
            case "K":
                return 13;
            default:
                return Integer.parseInt(this.rank);
        }
    }

    //helper functions for building deck
    public static String toRank(int r) {
        if (2 <= r && r <= 10) {
            return ""+r;
        }
        else {
            switch(r) {
                case 1:
                    return "A";
                case 11:
                    return "J";
                case 12:
                    return "Q";
                case 13:
                    return "K";
                default:
                    return "A";
            }
        }

    }

    public static String toSuit(int s) {
        switch(s) {
            case 0:
                return "Hearts";
            case 1:
                return "Diamonds";
            case 2:
                return "Clubs";
            case 3:
                return "Spades";
            default:
                return "Hearts";
        }
    }

}
