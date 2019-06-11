package com.becksm64.pitch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Card {

    static final int WIDTH = Gdx.graphics.getWidth() / 6;
    static final int HEIGHT = Gdx.graphics.getHeight() / 6;
    static final Texture backCardImage = new Texture("cards/back.png");

    private String suit;
    private int value;
    private String name;
    private Texture cardImage;
    private int cardWidth;
    private int cardHeight;
    private Rectangle bounds;
    private Vector3 position;
    private int rotation;
    private Vector3 endPosition;

    /*
     * Constructor that takes the suit and value of the card to be created
     */
    public Card(String suit, int value) {

        this.suit = suit;
        this.value = value;

        if(value > 10) {
            if(value == 11) {
                this.name = "jack_of_" + suit;
            } else if(value == 12) {
                this.name = "queen_of_" + suit;
            } else if(value == 13) {
                this.name = "king_of_" + suit;
            } else {
                this.name = "ace_of_" + suit;
            }
        } else {
            this.name = value + "_of_" + suit;
        }

        cardImage = new Texture("cards/" + this.name + ".png");

        cardWidth = Gdx.graphics.getWidth() / 6;
        cardHeight = Gdx.graphics.getHeight() / 6;

        position = new Vector3(0, 0, 0);
        endPosition = new Vector3((Gdx.graphics.getWidth() / 2.0f) - (this.getCardWidth() / 2.0f), (Gdx.graphics.getHeight() / 2.0f) - (this.getCardHeight() / 2.0f), 0);
        bounds = new Rectangle(position.x, position.y, cardWidth, cardHeight);
    }

    public void update() {
        setBounds(position.x, position.y);//Update card bounds to match current position
    }

    public String getName()  {
        return this.name;
    }

    public String getSuit() {
        return this.suit;
    }

    public int getValue() {
        return this.value;
    }

    public int getCardWidth() {
        return this.cardWidth;
    }

    public int getCardHeight() {
        return this.cardHeight;
    }

    public Rectangle getBounds() {
        return this.bounds;
    }

    private void setBounds(float x, float y) {
        this.bounds.setPosition(x, y);
    }

    public Vector3 getPosition() {
        return this.position;
    }

    public Vector3 getEndPosition() {
        return endPosition;
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y, 0);
    }

    public void setPositionX(float x) {
        this.position.x = x;
    }

    public void setPositionY(float y) {
        this.position.y = y;
    }

    public Texture getCardImage() {
        return this.cardImage;
    }

    public boolean isPlayable(String trump, String currentSuit, int numPlays, boolean handHasCurrentSuit) {

        if(!handHasCurrentSuit) {
            return true;
        }

        //check if beginning of round
        if(currentSuit == null && trump == null) {
            return true;
        }

        //If beginning of play, then any card is playable
        if(numPlays == 0) {
            return true;
        }

        //If card suit is trump, then can be played no matter what
        if(this.suit.equals(trump)) {
            return true;
        }

        //Check if suit is equal to current suit
        if(this.suit.equals(currentSuit)) {
            return true;
        } else {
            return false;
        }
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public void dispose() {
        this.cardImage.dispose();
    }
}
