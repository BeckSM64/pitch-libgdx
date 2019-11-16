package com.becksm64.pitch;

import java.util.List;
import java.util.ArrayList;

public class Hand
{
    private List<Card> hand;//Hand which will hold six cards

    /*
     * Constructor with no arguments to make an empty hand and then
     * add cards one by one after (not sure which would be better)
    */
    public Hand()
    {
        hand = new ArrayList<Card>();
    }

    /*
     * Update all the cards in hand
     */
    public void update()
    {
        for(int i = 0; i < this.size(); i++)
        {
            this.getCard(i).update();
        }
    }

    /*
     * Add a card to the hand
     * Only does so if hand is not already full
     * If it is full, the card is disposed of
     */
    public void addToHand(Card card)
    {
        if(!isFull())
        {
            this.hand.add(card);
        }
        else
        {
            card.dispose();
        }
    }

    /* Get the number of cards currently in hand */
    public int size()
    {
        return this.hand.size();
    }

    /*
     * Returns true if hand is empty
     */
    public boolean isEmpty()
    {
        return this.hand.size() == 0;
    }

    /*
     * Returns true if hand has 6 cards
     */
    public boolean isFull()
    {
        return this.hand.size() == 6;
    }

    /*
     * Returns true if there is a playable card in hand
     */
    public boolean hasCurrentSuitCard(String currentSuit)
    {
        for(int i = 0; i < this.hand.size(); i++)
        {
            if(this.getCard(i).getSuit().equals(currentSuit))
            {
                return true;
            }
        }
        return false;
    }

    /*
     * Plays the specified card, according to index,
     * from hand. Also removes that card from hand
     */
    public Card playCard(int index)
    {
        Card cardToPlay = this.hand.get(index);
        this.hand.remove(index);
        return cardToPlay;
    }

    /*
     * Get the specified card given provided index
     * Does not remove card from hand
     */
    public Card getCard(int index)
    {
        return this.hand.get(index);
    }

    /*
     * Dispose all the card images in hand
     */
    public void dispose()
    {
        for(int i = 0; i < this.size(); i++)
        {
            this.getCard(i).dispose();
        }
    }
}
