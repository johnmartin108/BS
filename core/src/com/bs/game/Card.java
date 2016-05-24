package com.bs.game;

import com.badlogic.gdx.graphics.Texture;

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

        String path = "small/card_a_" + this.suit + this.rank + ".png";
        this.img = new Texture(path);
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

}
