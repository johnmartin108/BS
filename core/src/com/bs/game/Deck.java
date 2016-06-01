package com.bs.game;

import com.bs.game.Card;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by johnmartin on 5/19/16.
 * Deck class to handle creation and ordering of cards.
 */
public class Deck {
    private ArrayList<Card> cards;

    //four suits, 13 ranks
    public Deck() {
        this.cards = new ArrayList<Card>();
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j < 14; j++) {
                this.cards.add(new Card(Card.toSuit(i), Card.toRank(j)));
            }
        }
        shuffle();

    }

    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    //remove the "top" card of the deck
    public Card nextCard() {
        if (cards.isEmpty()) {
            return null;
        }
        else {
            return cards.remove(0);
        }
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
