package com.becksm64.pitch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {

    private Game game;
    private SpriteBatch batch;
    private Vector3 touchPos;

    private Deck deck;
    private CardCollection mainPile;
    private List<Player> players;
    private Texture spadeImage;
    private Texture clubImage;
    private Texture heartImage;
    private Texture diamondImage;
    private Texture trumpImage;//Will change depending on current trump
    private Texture arrowImage;

    //Test game logic
    private String trump;
    private String currentSuit;
    private int numPlays;
    private int playerTurn;
    private int playedBestCard;
    private boolean calculatedScore;
    private int winner;
    private int bidsTaken;
    private boolean allBidsTaken;
    private int highestBid;
    private int currentBidder;
    private int dealer;

    //Scoring
    private int highPoint;
    private int jackPoint;
    private int lowPoint;
    private int gamePoint;

    private OrthographicCamera cam;

    //Table to display score at the end of round
    private ScoreTable scoreTable;
    private BidTable bidTable;
    private Hud hud;

    private float timeSeconds = 0f;
    private float period = 1f;

    public GameScreen(Game game) {

        this.game = game;
        batch = new SpriteBatch();
        hud = new Hud(batch);
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
        arrowImage = new Texture("arrow.png");//Arrow image to point to player whose turn it is

        dealHands();//Deal hands

        //Game logic
        trump = null;//Starts as null
        currentSuit = null;
        numPlays = 0;//Number of cards played so far in one play
        playerTurn = 0;//Index for collection of hands
        calculatedScore = false;
        bidsTaken = 0;
        allBidsTaken = false;
        highestBid = -1;
        currentBidder = 0;//Player makes initial bid
        dealer = 0;
        jackPoint = -1;//Make sure point is not counted unless jack is out

        cam = new OrthographicCamera();
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //Table for score at end of round
        scoreTable = new ScoreTable();

        //Table for taking bid at the beginning of the round
        bidTable = new BidTable();

        //Set input processors for stages
        InputMultiplexer multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(scoreTable.getStage());
        multiplexer.addProcessor(bidTable.getStage());
    }

    /*
     * Return true if all hands are empty, signifying end of round
     */
    private boolean isRoundOver() {
        return players.get(0).getPlayerHand().isEmpty()
                && players.get(1).getPlayerHand().isEmpty()
                && players.get(2).getPlayerHand().isEmpty()
                && players.get(3).getPlayerHand().isEmpty();
    }

    /*
     * Return true if someone reaches the score of 11
     */
    private boolean isGameOver() {
        for(int i = 0; i < players.size(); i++) {
            if(players.get(i).getScore() >= 11) {
                winner = i + 1;
                return true;
            }
        } return false;
    }

    /*
     * Return true if all players have all their cards
     */
    private boolean isStartOfRound() {
        return players.get(0).getPlayerHand().isFull()
                && players.get(1).getPlayerHand().isFull()
                && players.get(2).getPlayerHand().isFull()
                && players.get(3).getPlayerHand().isFull();
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

        //Reset and dispose of game objects
        trump = null;
        currentSuit = null;
        calculatedScore = false;
        highestBid = -1;
        bidsTaken = 0;
        allBidsTaken = false;

        if(dealer == 3)
            dealer = 0;
        else
            dealer++;
        System.out.println("Dealer: " + dealer);
        currentBidder = dealer;

        mainPile.dispose();//I think assets need to be disposed before recreating object
        deck.dispose();
        arrowImage = new Texture("arrow.png");
        for(Player player : players) {
            player.getWonCards().dispose();
            player.resetWonCards();
        }
        jackPoint = -1;//Jack may not always be out so make sure point doesn't get set by accident

        //Create new game objects and setup game
        scoreTable.resetPointLabels();
        mainPile = new CardCollection();
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

    private void displayScoreTable() {

        //Set the appropriate player scores in the score table
        scoreTable.setPlayer1Score(players.get(0).getScore());
        scoreTable.setPlayer2Score(players.get(1).getScore());
        scoreTable.setPlayer3Score(players.get(2).getScore());
        scoreTable.setPlayer4Score(players.get(3).getScore());

        scoreTable.setPlayerHigh(highPoint);
        scoreTable.setPlayerJack(jackPoint);
        scoreTable.setPlayerLow(lowPoint);
        scoreTable.setPlayerGame(gamePoint);

        calculatedScore = true;
        arrowImage = null;
        trump = null;
        trumpImage = null;
        scoreTable.getStage().draw();
    }

    /*
     * Returns the index of the player with the highest trump card
     */
    private int findHighestTrumpPlayer() {

        int hadHighestTrump = -1;
        Card highestTrump = null;
        for(int i = 0; i < players.size(); i++) {

            Player currentPlayer = players.get(i);
            Card playersHighestTrump = currentPlayer.getWonCards().getHighestTrump(trump);//This could be null if had no trump, check for it

            if(playersHighestTrump != null) {

                if(highestTrump == null || playersHighestTrump.getValue() > highestTrump.getValue()) {
                    highestTrump = playersHighestTrump;
                    hadHighestTrump = i;
                }
            }
        } return hadHighestTrump;
    }

    /*
     * Returns the index of the player with the highest game point score
     */
    private int findHighestGamePointPlayer() {

        int hadHighestGameScore = -1;
        int highestGameScore = 0;
        for(int i = 0; i < players.size(); i++) {

            Player currentPlayer = players.get(i);
            int playersGameScore = currentPlayer.getWonCards().getGamePointScore();//Player's game point score

            if(playersGameScore > 0) {

                if(highestGameScore == 0 || playersGameScore > highestGameScore) {
                    highestGameScore = playersGameScore;
                    hadHighestGameScore = i;
                }
            }
        } return hadHighestGameScore;
    }

    /*
     * Returns the index of the player with the lowest trump card
     */
    private int findLowestTrumpPlayer() {

        int hadLowestTrump = -1;
        Card lowestTrump = null;
        for(int i = 0; i < players.size(); i++) {

            Player currentPlayer = players.get(i);
            Card playersLowestTrump = currentPlayer.getWonCards().getLowestTrump(trump);//This could be null if had no trump, check for it

            if(playersLowestTrump != null) {

                if(lowestTrump == null || playersLowestTrump.getValue() < lowestTrump.getValue()) {
                    lowestTrump = playersLowestTrump;
                    hadLowestTrump = i;
                }
            }
        } return hadLowestTrump;
    }

    /*
     * Calculates each player's score and awards their points
     */
    private void calculatePlayerScores() {

        //Get player index who had lowest trump
        int hadLowestTrump = findLowestTrumpPlayer();
        int hadHighestTrump = findHighestTrumpPlayer();
        int hadGamePoint = findHighestGamePointPlayer();

        //Loop through player list to calculate score for each individual player
        for(int i = 0; i < players.size(); i++) {

            Player currentPlayer = players.get(i);
            int pointsToAward = 0;
            int playerBid = currentPlayer.getBid();

            //See if player was lowest trump holder
            if(hadLowestTrump == i) {
                pointsToAward += 1;
                lowPoint = i;
            }

            //See if player was highest trump holder
            if(hadHighestTrump == i) {
                pointsToAward += 1;
                highPoint = i;
            }

            //See if this player had the jack
            if(currentPlayer.getWonCards().hasTrumpJack(trump)) {
                pointsToAward += 1;
                jackPoint = i;
            }

            //See if this player got game point
            if(hadGamePoint == i) {
                pointsToAward += 1;
                gamePoint = i;
            }

            //Check to make sure player met their bid before awarding points
            if(pointsToAward >= playerBid)
                currentPlayer.addToScore(pointsToAward);
            else
                currentPlayer.addToScore(playerBid * -1);//Subtract bid from player score
        }
    }

    private void resetPlayerBids(int exclude) {

        for(int i = 0; i < players.size(); i++) {
            if(i != exclude)
                players.get(i).setBid(0);
        }
    }

    private void takePlayerBid() {

        //Take player bid
        if(currentBidder == 0) {
            bidTable.getStage().draw();
        }

        //Take computer bids
        if(currentBidder != 0 && bidsTaken < 4){

            int currentBid = players.get(currentBidder).makeBid(highestBid);
            if(currentBid > highestBid) {

                highestBid = currentBid;
                playerTurn = currentBidder;//Highest bidder starts round
                hud.setPlayerBid("P" + (currentBidder + 1) + ": " + highestBid);
            }

            resetPlayerBids(playerTurn);//Reset bids to 0 except for player whose turn it is (Highest bidder)
            if(currentBidder == 3)
                currentBidder = 0;
            else
                currentBidder++;
            bidsTaken++;//Increment the number of bids taken
        }

        //Stop taking bids when everyone has bid
        if(bidsTaken == 4)
            allBidsTaken = true;
    }

    @Override
    public void show() {

        //Hand next round button click
        scoreTable.getNextRoundBtn().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                resetRound();
            }
        });

        bidTable.getBid0Btn().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                players.get(0).setBid(0);
                bidsTaken++;
                currentBidder++;
            }
        });

        bidTable.getBid2Btn().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(2 > highestBid) {
                    players.get(0).setBid(2);
                    highestBid = 2;
                    hud.setPlayerBid("P" + (currentBidder + 1) + ": " + highestBid);
                    playerTurn = 0;
                    resetPlayerBids(playerTurn);
                    bidsTaken++;
                    currentBidder++;
                }
            }
        });

        bidTable.getBid3Btn().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(3 > highestBid) {
                    players.get(0).setBid(3);
                    highestBid = 3;
                    hud.setPlayerBid("P" + (currentBidder + 1) + ": " + highestBid);
                    playerTurn = 0;
                    resetPlayerBids(playerTurn);
                    bidsTaken++;
                    currentBidder++;
                }
            }
        });

        bidTable.getBid4Btn().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(4 > highestBid) {
                    players.get(0).setBid(4);
                    highestBid = 4;
                    hud.setPlayerBid("P" + (currentBidder + 1) + ": " + highestBid);
                    playerTurn = 0;
                    resetPlayerBids(playerTurn);
                    bidsTaken++;
                    currentBidder++;
                }
            }
        });
    }

    @Override
    public void render(float delta) {

        //Clear the screen with some color
        Gdx.gl.glClearColor(0, 0.75f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();//Update camera

        //Draw the heads up display
        batch.setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();

        if(!allBidsTaken) {
            takePlayerBid();
        }

        if(!isRoundOver()) {
            //Update cards player hands
            for(Player player : players) {
                player.getPlayerHand().update();
            }

            batch.setProjectionMatrix(cam.combined);
            batch.begin();//Start drawing

            //Draw arrow to point to player whose turn it is
            if(arrowImage != null && allBidsTaken) {
                if (playerTurn == 0)
                    batch.draw(new TextureRegion(arrowImage), (Gdx.graphics.getWidth() / 2.0f) - (Card.WIDTH / 2.0f), (Gdx.graphics.getHeight() / 2.0f) - Card.HEIGHT, Card.WIDTH / 2.0f, Card.WIDTH / 2.0f, Card.WIDTH, Card.WIDTH, 1, 1, 0);
                else if (playerTurn == 1)
                    batch.draw(new TextureRegion(arrowImage), (Gdx.graphics.getWidth() / 2.0f) - Card.HEIGHT, (Gdx.graphics.getHeight() / 2.0f) - (Card.WIDTH / 2.0f), Card.WIDTH / 2.0f, Card.WIDTH / 2.0f, Card.WIDTH, Card.WIDTH, 1, 1, 270);
                else if (playerTurn == 2)
                    batch.draw(new TextureRegion(arrowImage), (Gdx.graphics.getWidth() / 2.0f) - (Card.WIDTH / 2.0f), (Gdx.graphics.getHeight() / 2.0f) + Card.WIDTH, Card.WIDTH / 2.0f, Card.WIDTH / 2.0f, Card.WIDTH, Card.WIDTH, 1, 1, 180);
                else
                    batch.draw(new TextureRegion(arrowImage), (Gdx.graphics.getWidth() / 2.0f) + (Card.HEIGHT / 2.0f), (Gdx.graphics.getHeight() / 2.0f) - (Card.WIDTH / 2.0f), Card.WIDTH / 2.0f, Card.WIDTH / 2.0f, Card.WIDTH, Card.WIDTH, 1, 1, 90);
            }

            //Draw all hands
            for(int i = 0; i < players.size(); i++) {

                Hand currentHand = players.get(i).getPlayerHand();
                for(int j = 0; j < currentHand.size(); j++) {

                    Card currentCard = currentHand.getCard(j);//Get the current card
                    boolean hasCurrentSuit = false;
                    if(currentHand.hasCurrentSuitCard(currentSuit))
                        hasCurrentSuit = true;

                    if(i == 0) {
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
                        currentCard.setPositionX((Gdx.graphics.getWidth() / 6.0f) * j);
                        batch.draw(currentCard.getCardImage(), currentCard.getPosition().x, currentCard.getPosition().y, currentCard.getCardWidth(), currentCard.getCardHeight());
                    } else if(i == 1) {

                        currentCard.setPosition(0 - (currentCard.getCardWidth() / 4.0f), (Gdx.graphics.getHeight() / 3.30f) + (Gdx.graphics.getHeight() / 24.0f * j));
                        currentCard.setRotation(270);
                        batch.draw(new TextureRegion(Card.backCardImage), currentCard.getPosition().x, currentCard.getPosition().y, currentCard.getCardWidth() / 2.0f, currentCard.getCardHeight() / 2.0f, currentCard.getCardWidth() /1.5f, currentCard.getCardHeight() / 1.5f, 1, 1, 270);
                    } else if(i == 2) {

                        currentCard.setPosition((Gdx.graphics.getWidth() / 4.0f) + (Gdx.graphics.getWidth() / 16.0f) * j, Gdx.graphics.getHeight() - (currentCard.getCardHeight() / 1.5f));
                        currentCard.setRotation(180);
                        batch.draw(new TextureRegion(Card.backCardImage), currentCard.getPosition().x, currentCard.getPosition().y, currentCard.getCardWidth() / 2.0f, currentCard.getCardHeight() / 2.0f, currentCard.getCardWidth() / 1.5f, currentCard.getCardHeight() / 1.5f, 1, 1, 180);
                    } else {

                        currentCard.setPosition(Gdx.graphics.getWidth() - (currentCard.getCardWidth() / 1.25f), (Gdx.graphics.getHeight() / 3.0f) + (Gdx.graphics.getHeight() / 24.0f * j));
                        currentCard.setRotation(90);
                        batch.draw(new TextureRegion(Card.backCardImage), currentCard.getPosition().x, currentCard.getPosition().y, currentCard.getCardWidth() / 2.0f, currentCard.getCardHeight() / 2.0f, currentCard.getCardWidth() / 1.5f, currentCard.getCardHeight() / 1.5f, 1, 1, 90);
                    }
                }

                //Get rid of transparency
                Color c = batch.getColor();
                batch.setColor(c.r, c.g, c.b, 1);
            }

            //Draw main pile into which cards are played
            for(int i = 0; i < mainPile.size(); i++) {
                Card currentCard = mainPile.getCard(i);
                currentCard.setPosition((Gdx.graphics.getWidth() / 2.0f) - (currentCard.getCardWidth() / 2.0f), (Gdx.graphics.getHeight() / 2.0f) - (currentCard.getCardHeight() / 2.0f));
                //batch.draw(currentCard.getCardImage(), currentCard.getPosition().x + (i * 20), currentCard.getPosition().y + (i * 20), currentCard.getCardWidth(), currentCard.getCardHeight());
                batch.draw(new TextureRegion(currentCard.getCardImage()), currentCard.getPosition().x, currentCard.getPosition().y, currentCard.getCardWidth() / 2.0f, currentCard.getCardHeight() / 2.0f, currentCard.getCardWidth() / 1.5f, currentCard.getCardHeight() / 1.5f, 1, 1, currentCard.getRotation());
            }

            //Draw trump image below main pile
            setTrumpImage();
            if(trump != null)
                batch.draw(trumpImage, (Gdx.graphics.getWidth() / 2.0f) - (Card.WIDTH / 2.0f), (Gdx.graphics.getHeight() / 2.0f) +
                        (Card.HEIGHT + Card.HEIGHT / 2.0f), Card.WIDTH, Card.WIDTH);
            batch.end();

            //Only start turns after bids have been taken
            if(allBidsTaken) {

                for (int i = 0; i < players.size(); i++) {

                    //Check if player card has been touched
                    Hand currentHand = players.get(i).getPlayerHand();

                    //If hand has playable card, only select few cards may be playable\
                    boolean hasCurrentSuit = false;
                    if (currentHand.hasCurrentSuitCard(currentSuit))
                        hasCurrentSuit = true;

                    //If it is player 1's turn
                    if (i == 0 && playerTurn == i) {
                        if (Gdx.input.justTouched()) {

                            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                            cam.unproject(touchPos);

                            for (int j = 0; j < currentHand.size(); j++) {

                                Card currentCard = currentHand.getCard(j);

                                //Only play card if it was touched, is playable, and it is that player's turn
                                if (currentCard.getBounds().contains(touchPos.x, touchPos.y) && currentCard.isPlayable(trump, currentSuit, numPlays, hasCurrentSuit)) {

                                    if (isStartOfRound())
                                        trump = currentCard.getSuit();

                                    Card cardToPlay = currentHand.playCard(j);
                                    if (numPlays == 0)
                                        currentSuit = cardToPlay.getSuit();//Test setting currentSuit

                                    //Check if the card that was just played is the best card
                                    if (mainPile.isBestCard(cardToPlay, trump, currentSuit))
                                        playedBestCard = playerTurn;

                                    mainPile.addToPile(cardToPlay);

                                    if (numPlays != 3)
                                        playerTurn += 1;//Make it the next player's turn
                                    numPlays += 1;//Increment the number of cards played so far this play
                                }
                            }
                        }
                    } else if (i != 0 && playerTurn == i) {

                        timeSeconds += Gdx.graphics.getRawDeltaTime();//Wait specified amount of time until opponent takes their turn
                        if (timeSeconds > period) {

                            timeSeconds -= period;

                            Player player = players.get(i);
                            Card cardPlayed = player.takeTurn(trump, currentSuit, numPlays, hasCurrentSuit);

                            if (trump == null)
                                trump = cardPlayed.getSuit();

                            if (numPlays == 0)
                                currentSuit = cardPlayed.getSuit();//Test setting currentSuit

                            //Check if the card that was just played is the best card
                            if (mainPile.isBestCard(cardPlayed, trump, currentSuit))
                                playedBestCard = playerTurn;

                            mainPile.addToPile(cardPlayed);

                            if (numPlays != 3)
                                playerTurn += 1;//Make it the next player's turn
                            numPlays += 1;//Increment the number of cards played so far this play
                        }
                    }
                }
            }
        }

        //Check if all player's have gone
        if(playerTurn > 3)
            playerTurn = 0;//Go back to first player

        //Check if play is over, after all four players have gone
        if(numPlays > 3)
            resetPlay();

        //When round is over, start a new round
        if(isRoundOver()) {
            if(!calculatedScore) {

                calculatePlayerScores();
                //Load Game over screen if game is over
                if(isGameOver()) {
                    this.dispose();
                    game.setScreen(new GameOverScreen(this.game, winner));
                }
            }
            displayScoreTable();
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
        scoreTable.dispose();
        bidTable.dispose();
        spadeImage.dispose();
        heartImage.dispose();
        diamondImage.dispose();
        clubImage.dispose();
        if(trumpImage != null)
            trumpImage.dispose();
        if(arrowImage != null)
            arrowImage.dispose();
    }
}
