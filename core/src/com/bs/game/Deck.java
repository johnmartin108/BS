package com.example.johnmartin.bs;

import com.bs.game.Card;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by johnmartin on 5/19/16.
 */
public class Deck {
    private ArrayList<Card> cards;

//    private final String[] suits = new String[]{"Spades", "Clubs", "Hearts", "Diamonds"};

    public Deck() {
        this.cards = new ArrayList<Card>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 14; j++) {
                this.cards.add(new Card(Card.toSuit(i), Card.toRank(j)));
            }
        }
        shuffle();

    }

    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    public Card nextCard() {
        if (cards.isEmpty()) {
            return null;
        }
        else {
            return cards.remove(0);
        }
    }
}
