package com.becksm64.pitch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {

    Game game;
    SpriteBatch batch;
    Vector3 touchPos;

    Deck deck;
    CardCollection mainPile;
    List<Hand> hands;

    //Test game logic
    String trump;
    String currentSuit;
    int numPlays;
    int playerTurn;

    OrthographicCamera cam;

    public GameScreen(Game game) {

        this.game = game;
        batch = new SpriteBatch();
        touchPos = new Vector3(0,0,0);

        //Create deck for testing
        deck = new Deck();
        deck.shuffle();

        mainPile = new CardCollection();//Create the pile which cards are played to

        //Add new hands to collection of hands
        hands = new ArrayList<Hand>();
        for(int i = 0; i < 4; i ++) {
            hands.add(new Hand());
        }

        //Deal hands
        dealHands();

        //Game logic
        trump = null;//Starts as null
        currentSuit = null;
        numPlays = 0;//Number of cards played so far in one play
        playerTurn = 0;//Index for collection of hands

        cam = new OrthographicCamera();
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    /*
     * Return true if all hands are empty, signifying end of round
     */
    private boolean isRoundOver() {
        if(hands.get(0).isEmpty() && hands.get(1).isEmpty() && hands.get(2).isEmpty() && hands.get(3).isEmpty()) {
            return true;
        } return false;
    }

    /*
     * Return true if all players have all their cards
     */
    private boolean isStartOfRound() {
        if(hands.get(0).isFull() && hands.get(1).isFull() && hands.get(2).isFull() && hands.get(3).isFull()) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * Deals a hand to all the players from the deck
     */
    private void dealHands() {
        for(int i = 0; i < 6; i++) {
            for(Hand hand : hands) {
                hand.addToHand(deck.getTopCard());
            }
        }
    }

    /*
     * Resets deck, player hands, trump, and main card pile for a new round
     */
    private void resetRound() {
        trump = null;
        mainPile.dispose();//I think assets need to be disposed before recreating object
        mainPile = new CardCollection();
        deck.dispose();
        deck = new Deck();
        deck.shuffle();
        dealHands();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        //Clear the screen with some color
        Gdx.gl.glClearColor(0, 0.75f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();//Update camera

        //Update cards in test hands
        for(Hand hand : hands) {
            hand.update();
        }

        batch.begin();
        //Draw all hands
        for(int i = 0; i < hands.size(); i++) {

            Hand currentHand = hands.get(i);
            for(int j = 0; j < currentHand.size(); j++) {

                Card currentCard = currentHand.getCard(j);
                if(i == 0) {
                    currentCard.setPositionX((Gdx.graphics.getWidth() / 6) * j);
                    batch.draw(currentCard.getCardImage(), currentCard.getPosition().x, currentCard.getPosition().y, currentCard.getCardWidth(), currentCard.getCardHeight());
                } else if(i == 1) {
                    currentCard.setPosition(0 - (currentCard.getCardWidth() / 2), (currentCard.getCardWidth() * 2) + (Gdx.graphics.getHeight() / 10 * j));
                    batch.draw(new TextureRegion(currentCard.getCardImage()), currentCard.getPosition().x, currentCard.getPosition().y, currentCard.getCardWidth() / 2, currentCard.getCardHeight() / 2, currentCard.getCardWidth(), currentCard.getCardHeight(), 1, 1, 270);
                } else if(i == 2) {
                    currentCard.setPosition((Gdx.graphics.getWidth() / 6) * j, Gdx.graphics.getHeight() - (currentCard.getCardHeight() / 2));
                    batch.draw(new TextureRegion(currentCard.getCardImage()), currentCard.getPosition().x, currentCard.getPosition().y, currentCard.getCardWidth() / 2, currentCard.getCardHeight() / 2, currentCard.getCardWidth(), currentCard.getCardHeight(), 1, 1, 180);
                } else {
                    currentCard.setPosition(Gdx.graphics.getWidth() - (currentCard.getCardWidth() / 2), (currentCard.getCardWidth() * 2) + (Gdx.graphics.getHeight() / 10 * j));
                    batch.draw(new TextureRegion(currentCard.getCardImage()), currentCard.getPosition().x, currentCard.getPosition().y, currentCard.getCardWidth() / 2, currentCard.getCardHeight() / 2, currentCard.getCardWidth(), currentCard.getCardHeight(), 1, 1, 270);
                }
            }
        }

        //Draw main pile into which cards are played
        for(int i = 0; i < mainPile.size(); i++) {
            Card currentCard = mainPile.getCard(i);
            currentCard.setPosition((Gdx.graphics.getWidth() / 2) - (currentCard.getCardWidth() / 2), (Gdx.graphics.getHeight() / 2) - (currentCard.getCardHeight() / 2));
            batch.draw(currentCard.getCardImage(), currentCard.getPosition().x + (i * 20), currentCard.getPosition().y + (i * 20), currentCard.getCardWidth(), currentCard.getCardHeight());
        }
        batch.end();

        //Test playing a card, only if all hands are not empty
        if(Gdx.input.justTouched() && !isRoundOver()) {

            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            cam.unproject(touchPos);

            //Loop through each player
            for(int i = 0; i < hands.size(); i++) {

                //Check if player card has been touched
                Hand currentHand = hands.get(i);

                //If hand has playable card, only select few cards may be playable
                if(currentHand.hasPlayableCard(trump, currentSuit, numPlays)) {
                    for (int j = 0; j < currentHand.size(); j++) {

                        Card currentCard = currentHand.getCard(j);

                        //Only play card if it was touched, is playable, and it is that player's turn
                        if (currentCard.getBounds().contains(touchPos.x, touchPos.y) && currentCard.isPlayable(trump, currentSuit, numPlays) && playerTurn == i) {

                            if (isStartOfRound()) {
                                trump = currentCard.getSuit();
                            }
                            System.out.println(trump);
                            Card cardToPlay = currentHand.playCard(j);
                            if (numPlays == 0) {
                                currentSuit = cardToPlay.getSuit();//Test setting currentSuit
                            }
                            mainPile.addToPile(cardToPlay);
                            numPlays += 1;//Increment the number of cards played so far this play
                            playerTurn += 1;//Make it the next player's turn
                        }
                    }
                } else {
                    for (int j = 0; j < currentHand.size(); j++) {

                        Card currentCard = currentHand.getCard(j);

                        //Only play card if it was touched, is playable, and it is that player's turn
                        if (currentCard.getBounds().contains(touchPos.x, touchPos.y) && playerTurn == i) {

                            if (isStartOfRound()) {
                                trump = currentCard.getSuit();
                            }
                            System.out.println(trump);
                            Card cardToPlay = currentHand.playCard(j);
                            if (numPlays == 0) {
                                currentSuit = cardToPlay.getSuit();//Test setting currentSuit
                            }
                            mainPile.addToPile(cardToPlay);
                            numPlays += 1;//Increment the number of cards played so far this play
                            playerTurn += 1;//Make it the next player's turn
                        }
                    }
                }
            }

            //Check if all player's have gone
            if(playerTurn > 3) {
                playerTurn = 0;//Go back to first player
            }

            //Check if play is over, after all four players have gone
            if(numPlays > 3) {
                currentSuit = null;//Reset current suit
                numPlays = 0;
            }
        }

        //When round is over, start a new round
        if(isRoundOver()) {
            resetRound();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

        batch.dispose();
        deck.dispose();
        for(int i = 0; i < hands.size(); i++) {
            hands.get(i).dispose();
        }
        mainPile.dispose();
    }
}
