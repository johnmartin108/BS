package com.bs.game;

import com.bs.game.Card;
import java.util.ArrayList;

/**
 * Created by johnmartin on 5/20/16.
 * Unused class. Only here to impose structure on Bot class.
 */
public class Player {
    private ArrayList<Card> hand;

    public Player() {
        hand = new ArrayList<Card>();
    }

    public void takeTurn() {
        //display dialog with options
        //if call BS
            //get result, refresh options (including update info from server)
        //else
            //process play normally, send info to server
    }

    //helper function for dealing/taking cards
    public void addToHand(ArrayList<Card> c) {
        hand.addAll(c);
    }
    public void addToHand(Card c) {hand.add(c);}
    public ArrayList<Card> getHand() {return hand;}

}
