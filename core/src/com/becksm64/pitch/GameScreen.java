package com.becksm64.pitch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
    List<Player> players;
    Texture spadeImage;
    Texture clubImage;
    Texture heartImage;
    Texture diamondImage;
    Texture trumpImage;//Will change depending on current trump

    //Test game logic
    String trump;
    String currentSuit;
    int numPlays;
    int playerTurn;
    Card bestCardPlayed;
    int playedBestCard;

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
        players = new ArrayList<Player>();
        for(int i = 0; i < 4; i ++) {
            players.add(new Player());
        }

        //Create suit textures
        spadeImage = new Texture("suits/spades.png");
        clubImage = new Texture("suits/clubs.png");
        heartImage = new Texture("suits/hearts.png");
        diamondImage = new Texture("suits/diamonds.png");

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
        if(players.get(0).getPlayerHand().isEmpty() && players.get(1).getPlayerHand().isEmpty() && players.get(2).getPlayerHand().isEmpty() && players.get(3).getPlayerHand().isEmpty()) {
            return true;
        } return false;
    }

    /*
     * Return true if all players have all their cards
     */
    private boolean isStartOfRound() {
        if(players.get(0).getPlayerHand().isFull() && players.get(1).getPlayerHand().isFull() && players.get(2).getPlayerHand().isFull() && players.get(3).getPlayerHand().isFull()) {
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
            for(Player player : players) {
                player.addToHand(deck.getTopCard());
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

    /*
     * Resets the table after each player has played a card
     */
    private void resetPlay() {

        //Remove cards from the main pile and add them to the pile of cards that the player won
        for(int i = 0; i < players.size(); i++) {
            players.get(playedBestCard).addToCardsWon(mainPile.removeCard());
        }
        currentSuit = null;//Reset current suit
        numPlays = 0;
        playerTurn = playedBestCard;
        bestCardPlayed = null;
    }

    /*
     * Sets the trump texture to the appropriate suit texture to be drawn on screen
     */
    private void setTrumpImage() {
        if(trump != null) {
            if (trump.equals("spades"))
                trumpImage = spadeImage;
            else if (trump.equals("clubs"))
                trumpImage = clubImage;
            else if (trump.equals("hearts"))
                trumpImage = heartImage;
            else
                trumpImage = diamondImage;
        }
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

        //Update cards player hands
        for(Player player : players) {
            player.getPlayerHand().update();
        }

        batch.begin();
        //Draw all hands
        for(int i = 0; i < players.size(); i++) {

            Hand currentHand = players.get(i).getPlayerHand();
            for(int j = 0; j < currentHand.size(); j++) {

                Card currentCard = currentHand.getCard(j);//Get the current card
                boolean hasCurrentSuit = false;
                if(currentHand.hasCurrentSuitCard(currentSuit)) {
                    hasCurrentSuit = true;
                }

                //Draw a card with transparency if it isn't a playable card
                if(currentHand.hasCurrentSuitCard(currentSuit)) {
                    if (currentCard.isPlayable(trump, currentSuit, numPlays, hasCurrentSuit)) {
                        Color c = batch.getColor();
                        batch.setColor(c.r, c.g, c.b, 1);
                    } else {
                        Color c = batch.getColor();
                        batch.setColor(c.r, c.g, c.b, 0.1f);
                    }
                } else {
                    Color c = batch.getColor();
                    batch.setColor(c.r, c.g, c.b, 1);
                }

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

        //Get rid of transparency
        Color c = batch.getColor();
        batch.setColor(c.r, c.g, c.b, 1);

        //Draw main pile into which cards are played
        for(int i = 0; i < mainPile.size(); i++) {
            Card currentCard = mainPile.getCard(i);
            currentCard.setPosition((Gdx.graphics.getWidth() / 2) - (currentCard.getCardWidth() / 2), (Gdx.graphics.getHeight() / 2) - (currentCard.getCardHeight() / 2));
            batch.draw(currentCard.getCardImage(), currentCard.getPosition().x + (i * 20), currentCard.getPosition().y + (i * 20), currentCard.getCardWidth(), currentCard.getCardHeight());
        }

        //Draw trump image below main pile
        setTrumpImage();
        if(trump != null)
            batch.draw(trumpImage, (Gdx.graphics.getWidth() / 2) - (Card.WIDTH / 2), (Gdx.graphics.getHeight() / 2) - (Card.HEIGHT + Card.HEIGHT / 2), Card.WIDTH, Card.WIDTH);
        batch.end();

        //Test playing a card, only if all hands are not empty
        if(Gdx.input.justTouched() && !isRoundOver()) {

            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            cam.unproject(touchPos);

            //Loop through each player
            for(int i = 0; i < players.size(); i++) {

                //Check if player card has been touched
                Hand currentHand = players.get(i).getPlayerHand();

                //If hand has playable card, only select few cards may be playable\
                boolean hasCurrentSuit = false;
                if(currentHand.hasCurrentSuitCard(currentSuit)) {
                    hasCurrentSuit = true;
                }
                for (int j = 0; j < currentHand.size(); j++) {

                    Card currentCard = currentHand.getCard(j);

                    //Only play card if it was touched, is playable, and it is that player's turn
                    if (currentCard.getBounds().contains(touchPos.x, touchPos.y) && currentCard.isPlayable(trump, currentSuit, numPlays, hasCurrentSuit) && playerTurn == i) {

                        if (isStartOfRound()) {
                            trump = currentCard.getSuit();
                        }
                        Card cardToPlay = currentHand.playCard(j);
                        if (numPlays == 0) {
                            currentSuit = cardToPlay.getSuit();//Test setting currentSuit
                        }
                        //Check if the card that was just played is the best card
                        if(mainPile.isBestCard(cardToPlay, trump, currentSuit)) {
                            bestCardPlayed = cardToPlay;
                            playedBestCard = playerTurn;
                        }
                        System.out.println("Played best card: " + playedBestCard);
                        System.out.println("Turn " + numPlays);
                        mainPile.addToPile(cardToPlay);
                        if(numPlays != 3)
                            playerTurn += 1;//Make it the next player's turn
                        numPlays += 1;//Increment the number of cards played so far this play
                    }
                }
            }

            //Check if all player's have gone
            if(playerTurn > 3) {
                playerTurn = 0;//Go back to first player
            }

            //Check if play is over, after all four players have gone
            if(numPlays > 3) {
                resetPlay();
            }
        }

        //When round is over, start a new round
        if(isRoundOver()) {
            resetRound();
        }
    }

    @Override
    public void resize(int width, int height) {
        cam.setToOrtho(false, width, height);
        batch.setProjectionMatrix(cam.combined);
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
        for(int i = 0; i < players.size(); i++) {
            players.get(i).getPlayerHand().dispose();
        }
        mainPile.dispose();
    }
}
