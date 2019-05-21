package com.becksm64.pitch;

public class Player {

    private Hand hand;
    private CardCollection cardsWonInRound;//A collection of cards that the player wins in a round
    private int score;

    public Player() {

        hand = new Hand();
        cardsWonInRound = new CardCollection();
        score = 0;
    }

    /* Get the player's score */
    public int getScore() {
        return this.score;
    }

    /* Add to score */
    public void addToScore(int pointsToAdd) {
        this.score += pointsToAdd;
    }

    /* Get the player's current hand */
    public Hand getPlayerHand() {
        return this.hand;
    }

    /* Empty the player's hand */
    public void discardHand() {
        hand = new Hand();
    }

    /* Deal a card to player hand */
    public void addToHand(Card card) {
        this.hand.addToHand(card);
    }

    /* Takes a card from the player's hand a returns it. Also removes it from hand */
    public Card playCard(int index) {
        return this.hand.playCard(index);
    }

    /* Add a card to the player's collection */
    public void addToWonCards(Card card) {
        this.cardsWonInRound.addToPile(card);
    }

    /* Check if the player's hand is empty */
    public boolean isHandEmpty() {

        if(this.hand.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /* Add cards to player's collection of won cards */
    public void addToCardsWon(Card card) {
        this.cardsWonInRound.addToPile(card);
    }

    /* Get the cards won in round */
    public CardCollection getWonCards() {
        return this.cardsWonInRound;
    }

    /* Get rid of cards won in round */
    public void resetWonCards() {
        this.cardsWonInRound = new CardCollection();
    }

    public Card takeTurn(String trump, String currentSuit, int numPlays, boolean hasCurrentSuit) {

        for(int i = 0; i < this.hand.size(); i++) {
            Card currentCard = this.hand.getCard(i);
            if(currentCard.isPlayable(trump, currentSuit, numPlays, hasCurrentSuit)) {
                Card cardToPlay = this.playCard(i);
                return cardToPlay;
            }
        } return null;//Should never reach this point
    }
}
