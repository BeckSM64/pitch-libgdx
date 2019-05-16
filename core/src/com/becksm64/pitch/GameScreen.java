package com.becksm64.pitch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class GameScreen implements Screen {

    Game game;
    SpriteBatch batch;
    Vector3 touchPos;

    Deck deck;
    CardCollection mainPile;
    Hand hand;
    Hand hand2;
    Hand hand3;
    Hand hand4;

    //Test game logic
    String trump;
    String currentSuit;
    int numPlays;

    OrthographicCamera cam;

    public GameScreen(Game game) {

        this.game = game;
        batch = new SpriteBatch();
        touchPos = new Vector3(0,0,0);

        //Create deck for testing
        deck = new Deck();
        deck.shuffle();

        mainPile = new CardCollection();//Create the pile which cards are played to
        hand = new Hand();//Create test hand
        hand2 = new Hand();
        hand3 = new Hand();
        hand4 = new Hand();

        //Deal hands
        dealHands();

        //Game logic
        trump = null;//Starts as null
        currentSuit = null;
        numPlays = 0;//Number of cards played so far in one play
        //isStartOfRound = true;
        //isRoundOver = false;

        cam = new OrthographicCamera();
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    /*
     * Return true if all hands are empty, signifying end of round
     */
    private boolean isRoundOver() {
        if(hand.isEmpty() && hand2.isEmpty() && hand3.isEmpty() && hand4.isEmpty()) {
            return true;
        } return false;
    }

    /*
     * Deals a hand to all the players from the deck
     */
    private void dealHands() {
        for(int i = 0; i < 6; i++) {
            hand.addToHand(deck.getTopCard());
            hand2.addToHand(deck.getTopCard());
            hand3.addToHand(deck.getTopCard());
            hand4.addToHand(deck.getTopCard());
        }
    }

    /*
     * Resets deck, player hands, and main card pile for a new round
     */
    private void resetRound() {
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

        //Update camera
        cam.update();

        //Update cards in test hands
        hand.update();
        hand2.update();
        hand3.update();
        hand4.update();

        batch.begin();
        //Test drawing player hand
        for(int i = 0; i < hand.size(); i++) {
            Card currentCard = hand.getCard(i);
            currentCard.setPositionX((Gdx.graphics.getWidth() / 6) * i);
            batch.draw(currentCard.getCardImage(), currentCard.getPosition().x, currentCard.getPosition().y, currentCard.getCardWidth(), currentCard.getCardHeight());
        }

        //Test drawing computer hand
        for(int i = 0; i < hand2.size(); i++) {
            Card currentCard = hand2.getCard(i);
            currentCard.setPosition((Gdx.graphics.getWidth() / 6) * i, Gdx.graphics.getHeight() - (currentCard.getCardHeight() / 2));
            batch.draw(new TextureRegion(currentCard.getCardImage()), currentCard.getPosition().x, currentCard.getPosition().y, currentCard.getCardWidth() / 2, currentCard.getCardHeight() / 2, currentCard.getCardWidth(), currentCard.getCardHeight(), 1, 1, 180);
        }

        //Test drawing computer hand 2
        for(int i = 0; i < hand3.size(); i++) {
            Card currentCard = hand3.getCard(i);
            currentCard.setPosition(Gdx.graphics.getWidth() - (currentCard.getCardWidth() / 2), (currentCard.getCardWidth() * 2) + (Gdx.graphics.getHeight() / 10 * i));
            batch.draw(new TextureRegion(currentCard.getCardImage()), currentCard.getPosition().x, currentCard.getPosition().y, currentCard.getCardWidth() / 2, currentCard.getCardHeight() / 2, currentCard.getCardWidth(), currentCard.getCardHeight(), 1, 1, 270);
        }

        //Test drawing computer hand 3
        for(int i = 0; i < hand4.size(); i++) {
            Card currentCard = hand4.getCard(i);
            currentCard.setPosition(0 - (currentCard.getCardWidth() / 2), (currentCard.getCardWidth() * 2) + (Gdx.graphics.getHeight() / 10 * i));
            batch.draw(new TextureRegion(currentCard.getCardImage()), currentCard.getPosition().x, currentCard.getPosition().y, currentCard.getCardWidth() / 2, currentCard.getCardHeight() / 2, currentCard.getCardWidth(), currentCard.getCardHeight(), 1, 1, 270);
        }

        //Test drawing main pile into which cards are played
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

            //Check if player card has been touched
            for(int i = 0; i < hand.size(); i++) {

                Card currentCard = hand.getCard(i);
                if(currentCard.getBounds().contains(touchPos.x, touchPos.y) && currentCard.isPlayable(trump, currentSuit)) {

                    Card cardToPlay = hand.playCard(i);
                    if(numPlays == 0) {
                        currentSuit = cardToPlay.getSuit();//Test setting currentSuit
                    }
                    //trump = cardToPlay.getSuit();//Test setting currentSuit when card is played
                    mainPile.addToPile(cardToPlay);
                    numPlays += 1;//Increment the number of cards played so far this play
                }
            }

            //Check if test computer player card has been touched
            for(int i = 0; i < hand2.size(); i++) {

                Card currentCard = hand2.getCard(i);
                if(currentCard.getBounds().contains(touchPos.x, touchPos.y) && currentCard.isPlayable(trump, currentSuit)) {

                    Card cardToPlay = hand2.playCard(i);
                    if(numPlays == 0) {
                        currentSuit = cardToPlay.getSuit();//Test setting currentSuit
                    }
                    mainPile.addToPile(cardToPlay);
                    numPlays += 1;//Increment the number of cards played so far this play
                }
            }

            //Check if test computer player 2 card has been touched
            for(int i = 0; i < hand3.size(); i++) {

                Card currentCard = hand3.getCard(i);
                if(currentCard.getBounds().contains(touchPos.x, touchPos.y) && currentCard.isPlayable(trump, currentSuit)) {

                    Card cardToPlay = hand3.playCard(i);
                    if(numPlays == 0) {
                        currentSuit = cardToPlay.getSuit();//Test setting currentSuit
                    }
                    mainPile.addToPile(cardToPlay);
                    numPlays += 1;//Increment the number of cards played so far this play
                }
            }

            //Check if test computer player 2 card has been touched
            for(int i = 0; i < hand4.size(); i++) {

                Card currentCard = hand4.getCard(i);
                if(currentCard.getBounds().contains(touchPos.x, touchPos.y) && currentCard.isPlayable(trump, currentSuit)) {

                    Card cardToPlay = hand4.playCard(i);
                    if(numPlays == 0) {
                        currentSuit = cardToPlay.getSuit();//Test setting currentSuit
                    }
                    mainPile.addToPile(cardToPlay);
                    numPlays += 1;//Increment the number of cards played so far this play
                }
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
        hand.dispose();
        hand2.dispose();
        hand3.dispose();
        hand4.dispose();
        mainPile.dispose();
    }
}
