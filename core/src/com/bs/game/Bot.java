//package com.bs.game;
//import com.bs.game.Player;
//import com.bs.game.Card;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Map;
//
///**
// * Created by johnmartin on 5/20/16.
// */
//
////Difficulty modifiers:
//    //lying strategically
//    //keeping track of known cards
//
//
//public class Bot extends Player {
//    private ArrayList<Card> hand;
//    private ArrayList<Card> turnPlays; //cards this bot has played this turn - collapse into below
//    private Map<Integer, ArrayList<Card>> knowns; //needs a category for cards that are already in the pile
//
//    private int difficulty;
//    private double threshold;
//
//    public Bot(int difficulty) {
//        hand = new ArrayList<Card>();
//        this.difficulty = difficulty;
//
//        this.threshold = .7 + (.6 * Math.random());
//    }
//
//    public void takeTurn() {
//        //get info from server - want number of cards in each player's hand, what was apparently just played,
//        //what we're supposed to play
//
//
//
//        //decide whether to call BS:
//        double call = 0.0;
//        //guaranteed:
//        //was the last card from a hand just played?
//        //do we know for a fact that the last player lied? (check turnPlays)?
//        //(on highest difficulty) - just check what cards were played.
//
//        //uncertain:
//        //how close is the player to winning? (more aggressive as we get closer)
//        //how improbable is their play? (can compute probabilities directly - we'll see if that throws off bot behavior)
//        //what else have people said this turn? (may not matter because we don't know who lied) - this
//            //HAS NOT been implemented yet, and may never be
//        //TODO: consider weighting the number of turns taken so far - dumb heuristic but maybe right
//
//
//        int cards_left = cards_in_hand.get(last_player);
//
//        if (cards_left == 0 || cards_played.size() > 4) {
//            call = Double.POSITIVE_INFINITY;
//        }
//        else {
//            if (difficulty == 3) {
//                int truths = 0;
//                for (Card card:cards_played) {
//                    if (card.valueOf() == targetRank) {
//                        truths++;
//                    }
//                }
//                if (truths != num_played) {
//                    call = Double.POSITIVE_INFINITY;
//                }
//                else {
//                    call = Double.NEGATIVE_INFINITY;
//                }
//            }
//            else if (difficulty == 2) {
//                int known = 0;
//                for (int i = 0; i < num_players+1; i++) {
//                    if (last_player == i) {
//                        int known_true = 0;
//                        for (Card card: knowns.get(i)) {
//                            if (card.valueOf() == targetRank) {
//                                known_true++;
//                            }
//                        }
//                        if (known_true == num_played) {
//                            call = Double.NEGATIVE_INFINITY;
//                            break;
//                        }
//                    }
//                    for (Card card:knowns.get(i)) {
//                        if (card.valueOf() == targetRank) {
//                            known++;
//                        }
//                    }
//                }
//
//                if (known > 4-num_played) {
//                    call = Double.POSITIVE_INFINITY;
//                }
//
//                call += Math.pow(1.3, (-1)*cards_left); //fine tune? arbitrary decreasing function
//                call += 5.0/num_played;
//
//            }
//
//            else if (difficulty == 1) {
//                int known = 0;
//                for (Card card: turnPlays) {
//                    if (card.valueOf() == targetRank) {
//                        known++;
//                    }
//                }
//
//                if (known > 4-num_played) {
//                    call = Double.POSITIVE_INFINITY;
//                }
//
//                call += Math.pow(1.3, (-1)*cards_left); //fine tune? arbitrary decreasing function
//                call += 5.0/num_played;
//            }
//
//            else if (difficulty == 0) {
//                call = Math.random();
//            }
//        }
//
//        //introduce slight variance - call BS if above a certain threshold
//        call += Math.random()/10;
//
//        if (call > threshold) {
//            callBS();
//        }
//
//
//        //TODO: keep track of next player's habits
//        ArrayList<Card> play = new ArrayList<Card>();
//        for (Card card: hand) {
//            if (card.valueOf() == targetRank) {
//                play.add(card);
//                hand.remove(card);
//            }
//        }
//
//        if (play.isEmpty()) {
//            if (difficulty > 1) {
//                prioritize(targetRank);
//            }
//            play.add(hand.remove(0));
//        }
//        else {
//
//            //maybe lie if we've only played one card
//            if (play.size() == 1) {
//                if (hand.size() > 10) {
//                    double v = Math.random();
//                    if (v > .8) {
//                        if (difficulty > 1) {
//                            prioritize(targetRank);
//                        }
//                        play.add(hand.remove(0));
//                        play.add(hand.remove(0));
//                    }
//                    else if (v > .3) {
//                        if (difficulty > 1) {
//                            prioritize(targetRank);
//                        }
//                        play.add(hand.remove(0));
//                    }
//                }
//                else if (hand.size() > 5) {
//                    double v = Math.random();
//                    if (v > .55) {
//                        if (difficulty > 1) {
//                            prioritize(targetRank);
//                        }
//                        play.add(hand.remove(0));
//                    }
//                }
//            }
//        }
//
//        playCards(play);
//
//
//
//
//        //play cards:
//        //do we have a legal play?
//        //Yes:
//            //should we lie anyway?
//                //how often does the next guy call us out?
//                //would a lie be feasible? (i.e. are we already playing three cards?)
//        //No:
//            //how do we lie? this should probably be mostly random and somewhat
//            //proportional to the number of cards in your hand.
//
//
//
//
//
//    }
//
//    //reorder the hand in terms of play priority
//    //priority is determined by distance from the target card
//    //the game tends to move fast, so a good bot will get rid of cards that won't be around for a while
//    //once the end is near
//    public void prioritize(int targetRank) {
//
//        //TODO: modify based on remaining cards and number of players
//        final int tr = targetRank;
//        Collections.sort(hand, new Comparator<Card>() {
//            public int compare(Card c1, Card c2) {
//                if (c1.valueOf() < tr && c2.valueOf() > tr) {
//                    return -1;
//                }
//                else if (c1.valueOf() > tr && c2.valueOf() < tr) {
//                    return 1;
//                }
//                else if (c1.valueOf() > tr && c2.valueOf() > tr) {
//                    if (c1.valueOf() > c2.valueOf()) {
//                        return 1;
//                    }
//                    else if (c1.valueOf() == c2.valueOf()) {
//                        return 0;
//                    }
//                    else {
//                        return -1;
//                    }
//                }
//                else {
//                    if (c1.valueOf() > c2.valueOf()) {
//                        return -1;
//                    }
//                    else if (c1.valueOf() == c2.valueOf()) {
//                        return 0;
//                    }
//                    else {
//                        return 1;
//                    }
//                }
//
//            }
//        });
//
//    }
//
//    public void callBS() {
//        //send relevant message to server
//
//    }
//
//    public void playCards(ArrayList<Card> cards) {
//        //send relevant message to server
//    }
//}
