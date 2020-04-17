package com.becksm64.pitch.UI;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.becksm64.pitch.Card;
import com.becksm64.pitch.CardCollection;
import com.becksm64.pitch.Deck;
import com.becksm64.pitch.Hand;
import com.becksm64.pitch.Pitch;
import com.becksm64.pitch.Player;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen
{
    private Game game;
    private SpriteBatch batch;
    private Vector3 touchPos;
    private Viewport viewport;

    //Game objects and assets
    private Deck deck;
    private CardCollection mainPile;
    private List<Player> players;
    private Texture spadeImage;
    private Texture clubImage;
    private Texture heartImage;
    private Texture diamondImage;
    private Texture trumpImage;//Will change depending on current trump
    private Texture arrowImage;
    private Sound playCardSound;//Sound for playing a single card

    //Game logic
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
    private int cardSpeed;
    private boolean cardsMoving;
    private boolean displayScoreTable;

    //Scoring
    private int highPoint;
    private int jackPoint;
    private int lowPoint;
    private int gamePoint;

    private OrthographicCamera cam;//Camera for game world

    //Table to display score at the end of round
    private ScoreTable scoreTable;
    private BidTable bidTable;
    private Hud hud;

    //Floats for time delays
    private float timeSeconds = 0f;
    private float timeSeconds2 = 0f;
    private float period = 1f;

    public GameScreen(Game game)
    {
        this.game = game;
        batch = new SpriteBatch();
        touchPos = new Vector3(0,0,0);

        deck = new Deck();
        deck.shuffle();
        mainPile = new CardCollection();//Create the pile which cards are played to

        //Add new hands to collection of hands
        players = new ArrayList<>();
        for(int i = 0; i < 4; i ++)
        {
            players.add(new Player());
        }

        //Create suit textures
        spadeImage = new Texture("suits/spade_xxxhdpi.png");
        clubImage = new Texture("suits/club_xxxhdpi.png");
        heartImage = new Texture("suits/heart_xxxhdpi.png");
        diamondImage = new Texture("suits/diamond_xxxhdpi.png");
        arrowImage = new Texture("arrow.png");//Arrow image to point to player whose turn it is

        //Create game sounds
        playCardSound = Gdx.audio.newSound(Gdx.files.internal("audio/play_card.mp3"));

        dealHands();//Deal hands

        //Game logic
        trump = null;
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
        cardSpeed = (int) (20 * Gdx.graphics.getDensity());//Card speed calculated based on screen size
        cardsMoving = true;
        displayScoreTable = false;//Boolean to determine whether or not to display scores

        //Create camera and set view
        cam = new OrthographicCamera();
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cam);

        scoreTable = new ScoreTable(viewport);//Table for score at end of round
        bidTable = new BidTable(viewport);//Table for taking bid at the beginning of the round
        hud = new Hud(batch, viewport);

        //Set input processors for stages
        InputMultiplexer multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(scoreTable.getStage());
        multiplexer.addProcessor(bidTable.getStage());
    }

    /*
     * Return true if all hands are empty, signifying end of round
     */
    private boolean isRoundOver()
    {
        return players.get(0).getPlayerHand().isEmpty()
                && players.get(1).getPlayerHand().isEmpty()
                && players.get(2).getPlayerHand().isEmpty()
                && players.get(3).getPlayerHand().isEmpty();
    }

    /*
     * Return true if someone reaches the score of 11
     */
    private boolean isGameOver()
    {
        for(int i = 0; i < players.size(); i++)
        {
            if(players.get(i).getScore() >= 11)
            {
                winner = i + 1;
                return true;
            }
        } return false;
    }

    /*
     * Return true if all players have all their cards
     */
    private boolean isStartOfRound()
    {
        return players.get(0).getPlayerHand().isFull()
                && players.get(1).getPlayerHand().isFull()
                && players.get(2).getPlayerHand().isFull()
                && players.get(3).getPlayerHand().isFull();
    }

    /*
     * Deals a hand to all the players from the deck
     */
    private void dealHands()
    {
        for(int i = 0; i < 6; i++)
        {
            for(Player player : players)
            {
                player.addToHand(deck.getTopCard());
            }
        }
    }

    /*
     * Resets deck, player hands, trump, and main card pile for a new round
     */
    private void resetRound()
    {

        //Reset and dispose of game objects
        trump = null;
        currentSuit = null;
        calculatedScore = false;
        highestBid = -1;
        bidsTaken = 0;
        allBidsTaken = false;
        displayScoreTable = false;

        //Set the next dealer
        if(dealer == 3)
        {
            dealer = 0;
        }
        else
        {
            dealer++;
        }

        currentBidder = dealer;//Set the next bidder

        //Dispose of assets in deck and main pile
        mainPile.dispose();
        deck.dispose();
        arrowImage = new Texture("arrow.png");

        //Discard cards won during previous round
        for(Player player : players)
        {
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
    private void resetPlay()
    {
        //Remove cards from the main pile and add them to the pile of cards that the player won
        for(int i = 0; i < players.size(); i++)
        {
            if(mainPile.size() > 0)
            {
                players.get(playedBestCard).addToCardsWon(mainPile.removeCard());
            }
        }

        currentSuit = null;//Reset current suit
        numPlays = 0;
        playerTurn = playedBestCard;
        cardsMoving = true;
    }

    /*
     * Sets the trump texture to the appropriate suit texture to be drawn on screen
     */
    private void setTrumpImage()
    {

        if(trump != null)
        {
            switch(trump)
            {
                case("spades"):
                    trumpImage = spadeImage;
                    break;
                case("clubs"):
                    trumpImage = clubImage;
                    break;
                case("hearts"):
                    trumpImage = heartImage;
                    break;
                default:
                    trumpImage = diamondImage;
            }
        }
    }

    /*
     * Displays the table that shows the player scores including which points were won during the round
     */
    private void displayScoreTable()
    {
        //Set the appropriate player scores in the score table
        scoreTable.setPlayer1Score(players.get(0).getScore());
        scoreTable.setPlayer2Score(players.get(1).getScore());
        scoreTable.setPlayer3Score(players.get(2).getScore());
        scoreTable.setPlayer4Score(players.get(3).getScore());

        //Set the point labels in the score table
        scoreTable.setPlayerHigh(highPoint);
        scoreTable.setPlayerJack(jackPoint);
        scoreTable.setPlayerLow(lowPoint);
        scoreTable.setPlayerGame(gamePoint);

        //Set assets to null and draw score table
        calculatedScore = true;
        arrowImage = null;
        trump = null;
        trumpImage = null;
        scoreTable.getStage().draw();
    }

    /*
     * Returns the index of the player with the highest trump card
     */
    private int findHighestTrumpPlayer()
    {
        int hadHighestTrump = -1;
        Card highestTrump = null;

        for(int i = 0; i < players.size(); i++)
        {
            Player currentPlayer = players.get(i);

            //This could be null if had no trump, check for it
            Card playersHighestTrump = currentPlayer.getWonCards().getHighestTrump(trump);

            if(playersHighestTrump != null)
            {
                if(highestTrump == null || playersHighestTrump.getValue() > highestTrump.getValue())
                {
                    highestTrump = playersHighestTrump;
                    hadHighestTrump = i;
                }
            }
        }
        return hadHighestTrump;
    }

    /*
     * Returns the index of the player with the highest game point score
     */
    private int findHighestGamePointPlayer()
    {
        int hadHighestGameScore = -1;
        int highestGameScore = 0;

        for(int i = 0; i < players.size(); i++)
        {
            //Current player
            Player currentPlayer = players.get(i);

            //Player's game point score
            int playersGameScore = currentPlayer.getWonCards().getGamePointScore();

            if(playersGameScore > 0)
            {
                if(highestGameScore == 0 || playersGameScore > highestGameScore)
                {
                    highestGameScore = playersGameScore;
                    hadHighestGameScore = i;
                }
            }
        }
        return hadHighestGameScore;
    }

    /*
     * Returns the index of the player with the lowest trump card
     */
    private int findLowestTrumpPlayer()
    {
        int hadLowestTrump = -1;
        Card lowestTrump = null;

        for(int i = 0; i < players.size(); i++)
        {
            //Get the current player
            Player currentPlayer = players.get(i);

            //This could be null if had no trump, check for it
            Card playersLowestTrump = currentPlayer.getWonCards().getLowestTrump(trump);

            if(playersLowestTrump != null)
            {
                if(lowestTrump == null || playersLowestTrump.getValue() < lowestTrump.getValue())
                {
                    lowestTrump = playersLowestTrump;
                    hadLowestTrump = i;
                }
            }
        }
        return hadLowestTrump;
    }

    /*
     * Calculates each player's score and awards their points
     */
    private void calculatePlayerScores()
    {
        //Get player index who had lowest trump
        int hadLowestTrump = findLowestTrumpPlayer();
        int hadHighestTrump = findHighestTrumpPlayer();
        int hadGamePoint = findHighestGamePointPlayer();

        //Loop through player list to calculate score for each individual player
        for(int i = 0; i < players.size(); i++)
        {
            Player currentPlayer = players.get(i);
            int pointsToAward = 0;
            int playerBid = currentPlayer.getBid();

            //See if player was lowest trump holder
            if(hadLowestTrump == i)
            {
                pointsToAward += 1;
                lowPoint = i;
            }

            //See if player was highest trump holder
            if(hadHighestTrump == i)
            {
                pointsToAward += 1;
                highPoint = i;
            }

            //See if this player had the jack
            if(currentPlayer.getWonCards().hasTrumpJack(trump))
            {
                pointsToAward += 1;
                jackPoint = i;
            }

            //See if this player got game point
            if(hadGamePoint == i)
            {
                pointsToAward += 1;
                gamePoint = i;
            }

            //Check to make sure player met their bid before awarding points
            if(pointsToAward >= playerBid)
            {
                currentPlayer.addToScore(pointsToAward);
            }
            else
            {
                currentPlayer.addToScore(playerBid * -1);//Subtract bid from player score
            }
        }

        calculatedScore = true;
    }

    /*
     * Resets all player bids to 0 excluding the current highest bid
     */
    private void resetPlayerBids(int exclude)
    {
        for(int i = 0; i < players.size(); i++)
        {
            if(i != exclude)
            {
                players.get(i).setBid(0);
            }
        }
    }

    /*
     * Takes the computer player bids and displays a table with bid options for the player when it is their turn
     */
    private void takePlayerBid()
    {
        //Take player bid
        if(currentBidder == 0)
        {
            bidTable.getStage().draw();
        }

        //Take computer bids
        if(currentBidder != 0 && bidsTaken < 4)
        {
            int currentBid = players.get(currentBidder).makeBid(highestBid);

            if(currentBid > highestBid)
            {
                highestBid = currentBid;
                playerTurn = currentBidder;//Highest bidder starts round
                hud.setPlayerBid("P" + (currentBidder + 1) + ": " + highestBid);
            }

            //Reset bids to 0 except for player whose turn it is (Highest bidder)
            resetPlayerBids(playerTurn);

            if(currentBidder == 3)
            {
                currentBidder = 0;
            }
            else
            {
                currentBidder++;
            }

            //Increment the number of bids taken
            bidsTaken++;
        }

        //Stop taking bids when everyone has bid
        if(bidsTaken == 4)
        {
            allBidsTaken = true;
        }
    }

    @Override
    public void show()
    {
        //Hand next round button click
        scoreTable.getNextRoundBtn().addListener(new ChangeListener()
            {
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    resetRound();
                }
            }
        );

        bidTable.getBid0Btn().addListener(new ChangeListener()
            {
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    players.get(0).setBid(0);
                    bidsTaken++;
                    currentBidder++;
                }
            }
        );

        bidTable.getBid2Btn().addListener(new ChangeListener()
            {
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    if(2 > highestBid)
                    {
                        players.get(0).setBid(2);
                        highestBid = 2;
                        hud.setPlayerBid("P" + (currentBidder + 1) + ": " + highestBid);
                        playerTurn = 0;
                        resetPlayerBids(playerTurn);
                        bidsTaken++;
                        currentBidder++;
                    }
                }
            }
        );

        bidTable.getBid3Btn().addListener(new ChangeListener()
            {
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    if(3 > highestBid)
                    {
                        players.get(0).setBid(3);
                        highestBid = 3;
                        hud.setPlayerBid("P" + (currentBidder + 1) + ": " + highestBid);
                        playerTurn = 0;
                        resetPlayerBids(playerTurn);
                        bidsTaken++;
                        currentBidder++;
                    }
                }
            }
        );

        bidTable.getBid4Btn().addListener(new ChangeListener()
            {
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    if(4 > highestBid)
                    {
                        players.get(0).setBid(4);
                        highestBid = 4;
                        hud.setPlayerBid("P" + (currentBidder + 1) + ": " + highestBid);
                        playerTurn = 0;
                        resetPlayerBids(playerTurn);
                        bidsTaken++;
                        currentBidder++;
                    }
                }
            }
        );
    }

    /*
     * Changes card positions that are in the main pile
     * so they appear to be being played from player hands
     * Must be called from the render method
     */
    private void playCards(Card currentCard)
    {
        if(cardsMoving)
        {
            //Get offset for final card position depending on current rotation
            int offsetX = 0;
            int offsetY = 0;

            if(currentCard.getRotation() == 270)
            {
                offsetX = Card.WIDTH / 2 * -1;
            }
            else if(currentCard.getRotation() == 90)
            {
                offsetX = Card.WIDTH / 2;
            }
            else if(currentCard.getRotation() == 0)
            {
                offsetY = Card.HEIGHT / 3 * -1;
            }
            else
            {
                offsetY = Card.HEIGHT / 3;
            }

            //Move x position
            if (currentCard.getPosition().x > currentCard.getEndPosition().x + cardSpeed
                    && currentCard.isMoving())
            {
                currentCard.setPositionX(currentCard.getPosition().x - cardSpeed);
            }
            else if (currentCard.getPosition().x < currentCard.getEndPosition().x - cardSpeed
                    && currentCard.isMoving())
            {
                currentCard.setPositionX(currentCard.getPosition().x + cardSpeed);
            }
            else
            {
                currentCard.setMoving(false);//Stop card from moving
                currentCard.setPositionX(currentCard.getEndPosition().x + offsetX);
            }

            //Move y position
            if (currentCard.getPosition().y > currentCard.getEndPosition().y + cardSpeed
                    && currentCard.isMoving())
            {
                currentCard.setPositionY(currentCard.getPosition().y - cardSpeed);
            }
            else if (currentCard.getPosition().y < currentCard.getEndPosition().y - cardSpeed
                    && currentCard.isMoving())
            {
                currentCard.setPositionY(currentCard.getPosition().y + cardSpeed);
            }
            else
            {
                //Stop the card from moving once it hits its final position
                currentCard.setMoving(false);
                currentCard.setPositionY(currentCard.getEndPosition().y + offsetY);
            }
        }
    }

    /*
     * Slides cards off the screen by changing card positions
     * Slides cards towards player who won the trick
     * Must be called from the render method
     */
    private void removeMainPile()
    {
        //Slide cards off screen
        for(int i = 0; i < mainPile.size(); i++)
        {
            //Get the current card in the main card pile
            Card currentCard = mainPile.getCard(i);

            if(playedBestCard == 0)
            {
                currentCard.setPositionY(currentCard.getPosition().y - cardSpeed);
            }
            else if(playedBestCard == 1)
            {
                currentCard.setPositionX(currentCard.getPosition().x - cardSpeed);
            }
            else if(playedBestCard == 2)
            {
                currentCard.setPositionY(currentCard.getPosition().y + cardSpeed);
            }
            else
            {
                currentCard.setPositionX(currentCard.getPosition().x + cardSpeed);
            }
        }
    }

    /*
     * Returns true when all the players have taken their turn
     */
    private boolean allPlayersGone()
    {
        return numPlays > 3;
    }

    /*
     * Draw all the cards on screen
     * Must be called from the render method
     */
    private void drawCards()
    {
        batch.setProjectionMatrix(cam.combined);
        batch.begin();//Start drawing

        //Draw all hands
        for(int i = 0; i < players.size(); i++)
        {
            Hand currentHand = players.get(i).getPlayerHand();
            for(int j = 0; j < currentHand.size(); j++)
            {
                //Get the current card
                Card currentCard = currentHand.getCard(j);
                boolean hasCurrentSuit = false;

                if(currentHand.hasCurrentSuitCard(currentSuit))
                {
                    hasCurrentSuit = true;
                }

                if(i == 0)
                {
                    //Draw a card with transparency if it isn't a playable card
                    if(currentHand.hasCurrentSuitCard(currentSuit))
                    {
                        if (currentCard.isPlayable(trump, currentSuit, numPlays, hasCurrentSuit))
                        {
                            Color c = batch.getColor();
                            batch.setColor(c.r, c.g, c.b, 1);
                        }
                        else
                        {
                            Color c = batch.getColor();
                            batch.setColor(c.r, c.g, c.b, 0.1f);
                        }
                    }
                    else
                    {
                        Color c = batch.getColor();
                        batch.setColor(c.r, c.g, c.b, 1);
                    }

                    currentCard.setPositionX((viewport.getWorldWidth() / 6.0f) * j);
                    batch.draw(
                            currentCard.getCardImage(),
                            currentCard.getPosition().x,
                            currentCard.getPosition().y,
                            currentCard.getCardWidth(),
                            currentCard.getCardHeight()
                    );
                }
                else if(i == 1)
                {
                    currentCard.setPosition(
                            0 - (currentCard.getCardWidth() / 4.0f),//x
                            (viewport.getScreenHeight() / 3.30f)
                                    + (viewport.getScreenHeight() / 24.0f * j)//y
                    );
                    currentCard.setRotation(270);
                    batch.draw(
                            new TextureRegion(Card.backCardImage),
                            currentCard.getPosition().x, currentCard.getPosition().y,
                            currentCard.getCardWidth() / 2.0f,
                            currentCard.getCardHeight() / 2.0f,
                            currentCard.getCardWidth() / 1.5f,
                            currentCard.getCardHeight() / 1.5f,
                            1,
                            1,
                            270
                    );
                }
                else if(i == 2)
                {
                    currentCard.setPosition(
                            (viewport.getScreenWidth() / 4.0f)
                                    + (viewport.getScreenWidth() / 16.0f) * j,//x
                            viewport.getScreenHeight()
                                    - (currentCard.getCardHeight() / 1.5f)//y
                    );
                    currentCard.setRotation(180);
                    batch.draw(
                            new TextureRegion(Card.backCardImage),
                            currentCard.getPosition().x,
                            currentCard.getPosition().y,
                            currentCard.getCardWidth() / 2.0f,
                            currentCard.getCardHeight() / 2.0f,
                            currentCard.getCardWidth() / 1.5f,
                            currentCard.getCardHeight() / 1.5f,
                            1,
                            1,
                            180
                    );
                }
                else
                {
                    currentCard.setPosition(
                            viewport.getScreenWidth() - (currentCard.getCardWidth() / 1.25f),//x
                            (viewport.getScreenHeight() / 3.0f)
                                    + (viewport.getScreenHeight() / 24.0f * j)//y
                    );
                    currentCard.setRotation(90);
                    batch.draw(
                            new TextureRegion(Card.backCardImage),
                            currentCard.getPosition().x,
                            currentCard.getPosition().y,
                            currentCard.getCardWidth() / 2.0f,
                            currentCard.getCardHeight() / 2.0f,
                            currentCard.getCardWidth() / 1.5f,
                            currentCard.getCardHeight() / 1.5f,
                            1,
                            1,
                            90
                    );
                }
            }

            //Get rid of transparency
            Color c = batch.getColor();
            batch.setColor(c.r, c.g, c.b, 1);
        }

        //Draw main pile into which cards are played
        for(int i = 0; i < mainPile.size(); i++)
        {
            Card currentCard = mainPile.getCard(i);

            //Animation for playing cards (basically just changes
            //played cards position till they're in the middle)
            playCards(currentCard);

            //Draw the texture
            batch.draw(
                    new TextureRegion(currentCard.getCardImage()),
                    currentCard.getPosition().x,
                    currentCard.getPosition().y,
                    currentCard.getCardWidth() / 2.0f,
                    currentCard.getCardHeight() / 2.0f,
                    currentCard.getCardWidth(),
                    currentCard.getCardHeight(),
                    1,
                    1,
                    currentCard.getRotation()
            );
        }
        batch.end();
    }

    /*
     * Draws the suit that is currently trump
     */
    private void drawTrumpImage()
    {
        batch.setProjectionMatrix(cam.combined);
        batch.begin();//Start drawing

        //Draw trump image below main pile
        setTrumpImage();
        if (trump != null)
        {
            batch.draw(
                    trumpImage, (viewport.getScreenWidth() / 2.0f) - (Card.WIDTH / 2.0f),
                    (viewport.getScreenHeight() / 2.0f) + (Card.HEIGHT + Card.HEIGHT / 2.0f),
                    Card.WIDTH,
                    Card.WIDTH
            );
        }

        batch.end();//Stop drawing
    }

    /*
     * Draws the arrow that points to the player whose turn it is
     */
    private void drawArrowImage()
    {
        batch.setProjectionMatrix(cam.combined);
        batch.begin();//Start drawing

        //Draw arrow to point to player whose turn it is
        if(arrowImage != null && allBidsTaken)
        {
            if (playerTurn == 0)
            {
                batch.draw(
                        new TextureRegion(arrowImage),
                        (viewport.getScreenWidth() / 2.0f) - (Card.WIDTH / 2.0f),
                        (viewport.getScreenHeight() / 2.0f) - Card.HEIGHT,
                        Card.WIDTH / 2.0f, Card.WIDTH / 2.0f,
                        Card.WIDTH,
                        Card.WIDTH,
                        1,
                        1,
                        0
                );
            }
            else if (playerTurn == 1)
            {
                batch.draw(
                        new TextureRegion(arrowImage),
                        (viewport.getScreenWidth() / 2.0f) - Card.HEIGHT,
                        (viewport.getScreenHeight() / 2.0f) - (Card.WIDTH / 2.0f),
                        Card.WIDTH / 2.0f,
                        Card.WIDTH / 2.0f,
                        Card.WIDTH, Card.WIDTH,
                        1,
                        1,
                        270
                );
            }
            else if (playerTurn == 2)
            {
                batch.draw(
                        new TextureRegion(arrowImage),
                        (viewport.getScreenWidth() / 2.0f) - (Card.WIDTH / 2.0f),
                        (viewport.getScreenHeight() / 2.0f) + Card.WIDTH,
                        Card.WIDTH / 2.0f,
                        Card.WIDTH / 2.0f,
                        Card.WIDTH, Card.WIDTH,
                        1,
                        1,
                        180
                );
            }
            else
            {
                batch.draw(
                        new TextureRegion(arrowImage),
                        (viewport.getScreenWidth() / 2.0f) + (Card.HEIGHT / 2.0f),
                        (viewport.getScreenHeight() / 2.0f) - (Card.WIDTH / 2.0f),
                        Card.WIDTH / 2.0f,
                        Card.WIDTH / 2.0f,
                        Card.WIDTH, Card.WIDTH,
                        1,
                        1,
                        90
                );
            }
        }

        batch.end();
    }

    private void drawBackgroundImage()
    {
        batch.setProjectionMatrix(cam.combined);
        batch.begin();//Start drawing
        batch.draw(Pitch.background, 0, 0, viewport.getScreenWidth(), viewport.getScreenHeight());
        batch.end();//Stop drawing
    }

    @Override
    public void render(float delta)
    {
        //Clear the screen with some color
        Gdx.gl.glClearColor(0, 0.75f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();//Update camera
        drawBackgroundImage();

        if(!allBidsTaken)
        {
            takePlayerBid();
        }

        if(!isRoundOver())
        {
            //Draw the trump image
            drawTrumpImage();

            //Draw arrow image
            if(!allPlayersGone())
            {
                drawArrowImage();
            }

            //Draw the heads up display
            batch.setProjectionMatrix(hud.getStage().getCamera().combined);
            hud.getStage().draw();

            //Update cards player hands
            for(Player player : players)
            {
                player.getPlayerHand().update();
            }

            //Only start turns after bids have been taken and if all players haven't already gone
            if(allBidsTaken && numPlays <= 3)
            {
                for (int i = 0; i < players.size(); i++)
                {
                    //Check if player card has been touched
                    Hand currentHand = players.get(i).getPlayerHand();

                    //If hand has playable card, only select few cards may be playable\
                    boolean hasCurrentSuit = false;

                    if (currentHand.hasCurrentSuitCard(currentSuit))
                    {
                        hasCurrentSuit = true;
                    }

                    //If it is player 1's turn
                    if (i == 0 && playerTurn == i)
                    {
                        if (Gdx.input.justTouched())
                        {
                            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                            cam.unproject(touchPos);

                            for (int j = 0; j < currentHand.size(); j++)
                            {
                                Card currentCard = currentHand.getCard(j);

                                //Only play card if it was touched, is playable,
                                // and it is that player's turn
                                if (currentCard.getBounds().contains(touchPos.x, touchPos.y)
                                        && currentCard.isPlayable(trump, currentSuit, numPlays, hasCurrentSuit))
                                {
                                    //Play sound when card is played
                                    playCardSound.play();

                                    if (isStartOfRound())
                                    {
                                        trump = currentCard.getSuit();
                                    }

                                    Card cardToPlay = currentHand.playCard(j);
                                    if (numPlays == 0)
                                    {
                                        currentSuit = cardToPlay.getSuit();//Test setting currentSuit
                                    }

                                    //Check if the card that was just played is the best card
                                    if (mainPile.isBestCard(cardToPlay, trump, currentSuit))
                                    {
                                        playedBestCard = playerTurn;
                                    }

                                    mainPile.addToPile(cardToPlay);

                                    if (numPlays != 3)
                                    {
                                        playerTurn += 1;//Make it the next player's turn
                                    }

                                    //Increment the number of cards played so far this play
                                    numPlays += 1;
                                }
                            }
                        }
                    }
                    else if (i != 0 && playerTurn == i)
                    {
                        //Wait specified amount of time until opponent takes their turn
                        timeSeconds += Gdx.graphics.getRawDeltaTime();

                        if (timeSeconds > period)
                        {
                            timeSeconds -= period;

                            Player player = players.get(i);
                            Card cardPlayed = player.takeTurn(trump, currentSuit, numPlays, hasCurrentSuit);
                            playCardSound.play();

                            if (trump == null)
                            {
                                trump = cardPlayed.getSuit();
                            }

                            if (numPlays == 0)
                            {
                                //Test setting currentSuit
                                currentSuit = cardPlayed.getSuit();
                            }

                            //Check if the card that was just played is the best card
                            if (mainPile.isBestCard(cardPlayed, trump, currentSuit))
                            {
                                playedBestCard = playerTurn;
                            }

                            mainPile.addToPile(cardPlayed);

                            if (numPlays != 3)
                            {
                                //Make it the next player's turn
                                playerTurn += 1;
                            }

                            //Increment the number of cards played so far this play
                            numPlays += 1;
                        }
                    }
                }
            }

            //Check if all player's have gone
            if (playerTurn > 3)
            {
                //Go back to first player
                playerTurn = 0;
            }
        }

        //Check if play is over, after all four players have gone
        if(allPlayersGone())
        {
            //Adds a delay after all cards have been played so player can see what cards were played
            //Wait specified amount of time until opponent takes their turn
            timeSeconds2 += Gdx.graphics.getRawDeltaTime();

            if (timeSeconds2 > 2f)
            {
                cardsMoving = false;

                //Move cards in main pile towards player who one the trick
                removeMainPile();

                if(timeSeconds2 > 3f)
                {
                    //Reset time lapsed
                    timeSeconds2 -= 3f;

                    //Reset table
                    resetPlay();

                    if(isRoundOver())
                    {
                        displayScoreTable = true;
                    }
                }
            }
        }

        //When round is over, start a new round
        if(isRoundOver())
        {
            if(!calculatedScore)
            {
                //Calculate scores for round
                calculatePlayerScores();

                //Load Game over screen if game is over
                if(isGameOver())
                {
                    this.dispose();
                    game.setScreen(new GameOverScreen(this.game, winner));
                }
            }

            if(displayScoreTable)
            {
                displayScoreTable();
            }
        }

        //Called towards the end so the z-index
        //is greater than all other assets
        drawCards();
    }

    @Override
    public void resize(int width, int height)
    {
        //Update viewport with new screen size
        viewport.update(width, height, true);

        //Update stage viewports with new screen size
        bidTable.getStage().getViewport().update(width, height, true);
        scoreTable.getStage().getViewport().update(width, height, true);
        hud.getStage().getViewport().update(width, height, true);

        //Update position where cards will be played
        for(int i = 0; i < mainPile.size(); i++)
        {
            mainPile.getCard(i).setEndPosition();
        }

        //Update position of all cards in deck
        for(int j = 0; j < deck.size(); j++)
        {
            deck.getCard(j).setEndPosition();
        }

        //Update all cards in player hands
        for(Player player : players)
        {
            for(int k = 0; k < player.getPlayerHand().size(); k++)
            {
                player.getPlayerHand().getCard(k).setEndPosition();
            }
        }
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {
        batch.dispose();
        deck.dispose();
        mainPile.dispose();
        scoreTable.dispose();
        bidTable.dispose();
        hud.dispose();
        spadeImage.dispose();
        heartImage.dispose();
        diamondImage.dispose();
        clubImage.dispose();

        for(int i = 0; i < players.size(); i++)
        {
            players.get(i).getPlayerHand().dispose();
        }

        if(trumpImage != null)
        {
            trumpImage.dispose();
        }

        if(arrowImage != null)
        {
            arrowImage.dispose();
        }
    }
}
