package com.becksm64.pitch;

import java.util.List;
import java.util.ArrayList;

public class Hand {

    private List<Card> hand;//Hand which will hold six cards

    /* Create a hand that contains six cards from the deck */
    public Hand(Card card1, Card card2, Card card3, Card card4, Card card5, Card card6) {

        hand = new ArrayList<Card>();

        //Add cards to hand
        hand.add(card1);
        hand.add(card2);
        hand.add(card3);
        hand.add(card4);
        hand.add(card5);
        hand.add(card6);
    }

    /* Constructor with no arguments to make an empty hand and then add cards one by one after (not sure which would be better) */
    public Hand() {
        hand = new ArrayList<Card>();
    }

    /*
     * Update all the cards in hand
     */
    public void update() {
        for(int i = 0; i < this.size(); i++) {
            this.getCard(i).update();
        }
    }

    /* Add a card to the hand */
    public void addToHand(Card card) {
        this.hand.add(card);
    }

    /* Get the number of cards currently in hand */
    public int size() {
        return this.hand.size();
    }

    /*
     * Returns true if hand is empty
     */
    public boolean isEmpty() {
        if(this.hand.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    /*
     * Returns true if hand has 6 cards
     */
    public boolean isFull() {
        if(this.hand.size() == 6) {
            return true;
        } else{
            return false;
        }
    }

    /*
     * Returns true if there is a playable card in hand
     */
    public boolean hasPlayableCard(String trump, String currentSuit, int numPlays) {

        for(int i = 0; i < this.hand.size(); i++) {
            if(this.getCard(i).isPlayable(trump, currentSuit, numPlays)) {
                return true;
            }
        } return false;
    }

    /* Plays the specified card, according to index, from hand. Also removes that card from hand */
    public Card playCard(int index) {

        Card cardToPlay = this.hand.get(index);
        this.hand.remove(index);
        return cardToPlay;
    }

    /* Get the specified card given provided index. Does not remove card from hand */
    public Card getCard(int index) {
        return this.hand.get(index);
    }

    /* Check if hand contains a trump card */
    public boolean hasSuit(String suitToFind) {

        for(int i = 0; i < this.hand.size(); i++) {

            Card currentCard = this.hand.get(i);
            if(currentCard.getSuit() == suitToFind) {
                return true;
            }
        }
        return false;//If trump card wasn't found, return false
    }

    /*
     * Dispose all the card images in hand
     */
    public void dispose() {
        for(int i = 0; i < this.size(); i++) {
            this.getCard(i).dispose();
        }
    }
}
