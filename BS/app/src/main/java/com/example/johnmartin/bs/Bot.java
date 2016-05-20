package com.example.johnmartin.bs;

import java.util.ArrayList;

/**
 * Created by johnmartin on 5/20/16.
 */
public class Bot extends Player {
    private ArrayList<Card> hand;
    private ArrayList<Card> turnPlays; //cards this bot has played this turn

    public Bot() {
        hand = new ArrayList<Card>();
    }

    public void takeTurn() {
        //get info from server - want number of cards in each player's hand, what was apparently just played,
        //what we're supposed to play

        //decide whether to call BS:
        //guaranteed:
        //was the last card from a hand just played?
        //do we know for a fact that the last player lied? (check turnPlays)?
        //(on highest difficulty) - just check what cards were played.

        //uncertain:
        //how close is the player to winning? (more aggressive as we get closer)
        //how improbable is their play? (can compute probabilities directly - we'll see if that throws off bot behavior)
        //what else have people said this turn? (may not matter because we don't know who lied)

        //the above should have double values that we add, and check if they're over a certain threshold
        //consider having random thresholds for different bots so they aren't all the same
        //and/or add a random value at the end.


        //play cards:
        




    }

    //reorder the hand in terms of play priority
    //priority is determined by distance from the target card
    //the game tends to move fast, so a good bot will get rid of cards that won't be around for a while
    //once the end is near
    public void prioritize() {

    }
}
