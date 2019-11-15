package com.becksm64.pitch;

import java.util.Collections;
import java.util.Stack;

public class Deck
{
    //Stack to represent deck
    private Stack<Card> deckOfCards;

    /*
     * Create a standard deck of cards containing 52 cards, no jokers
     */
    public Deck()
    {
        deckOfCards = new Stack<Card>();

        String suit;//Suit will change every 13 cards

        //Hearts
        suit = "hearts";
        for(int i = 2; i < 15; i++)
        {
            deckOfCards.push(new Card(suit, i));//Add the new card to the deck
        }

        //Diamonds
        suit = "diamonds";
        for(int i = 2; i < 15; i++)
        {
            deckOfCards.push(new Card(suit, i));//Add the new card to the deck
        }

        //Clubs
        suit = "clubs";
        for(int i = 2; i < 15; i++)
        {
            deckOfCards.push(new Card(suit, i));//Add the new card to the deck
        }

        //Spades
        suit = "spades";
        for(int i = 2; i < 15; i++)
        {
            deckOfCards.push(new Card(suit, i));//Add the new card to the deck
        }
    }

    /*
     * Return the card at the top of the deck
     */
    public Card getTopCard()
    {
        return this.deckOfCards.pop();
    }

    /*
     * Get specific card from deck based on index
     */
    public Card viewCard(int index)
    {
        return this.deckOfCards.get(index);
    }

    /*
    * Returns the number of cards in the deck
    */
    public int size()
    {
        return deckOfCards.size();
    }

    /*
     * Shuffles the deck
     */
    public void shuffle()
    {
        Collections.shuffle(this.deckOfCards);
    }

    /*
     * Dispose all card images in deck
     */
    public void dispose()
    {
        for(int i = 0; i < this.size(); i++)
        {
            this.viewCard(i).dispose();
        }
    }
}
