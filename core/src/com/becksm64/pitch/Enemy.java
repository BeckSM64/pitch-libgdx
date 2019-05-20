package com.becksm64.pitch;

public class Enemy extends Player {

    public Enemy() {
        super();
    }

    @Override
    public int getScore() {
        return super.getScore();
    }

    @Override
    public void addToScore(int pointsToAdd) {
        super.addToScore(pointsToAdd);
    }

    @Override
    public Hand getPlayerHand() {
        return super.getPlayerHand();
    }

    @Override
    public void discardHand() {
        super.discardHand();
    }

    @Override
    public void addToHand(Card card) {
        super.addToHand(card);
    }

    @Override
    public Card playCard(int index) {
        return super.playCard(index);
    }

    @Override
    public void addToWonCards(Card card) {
        super.addToWonCards(card);
    }

    @Override
    public boolean isHandEmpty() {
        return super.isHandEmpty();
    }

    @Override
    public void addToCardsWon(Card card) {
        super.addToCardsWon(card);
    }

    @Override
    public CardCollection getWonCards() {
        return super.getWonCards();
    }

    @Override
    public void resetWonCards() {
        super.resetWonCards();
    }

    public void takeTurn(String trump, String currentSuit, int numPlays, boolean hasCurrentSuit) {

    }
}
