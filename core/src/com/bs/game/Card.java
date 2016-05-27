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

        String path = "large/card_a_" + this.suit + this.rank + "_large.png";
        this.img = new Texture(path);
    }

    //rank, suit
    public Card(ArrayList<String> vals) {
        this.suit = vals.get(1);
        this.rank = vals.get(0);
    }

    public String toString() {
        return this.suit + " " + this.rank;
    }

    //for comparing cards
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

//    public Bitmap toImageAsset() {
//        //TODO: update with correct image assets
//        return null;
//    }
    public Texture getTexture() {
        return this.img;
    }

    public ArrayList<String> toCompressable() {
        ArrayList<String> result = new ArrayList<String>();
        result.add(this.rank);
        result.add(this.suit);
    }

}
