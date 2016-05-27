package com.bs.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by nick on 5/24/16.
 */
public class PlayWaitScreen implements Screen {

    SpriteBatch batch;
    private int numberPlayed;
    private String suitPlayed;
    private BitmapFont count;
    final BSGame game;
    private Texture backButton;
    private Texture goButton;
    private int width;
    private int height;
    private int ID;
    private float elapsed = 0;

    public PlayWaitScreen(BSGame game) {
        this.game = game;
    }

    public PlayWaitScreen(BSGame game, int numberPlayed, String suitPlayed) {
        this.game = game;
        this.numberPlayed = numberPlayed;
        this.suitPlayed = suitPlayed;
    }

    @Override
    public void show() {

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        batch = new SpriteBatch();
        count = game.font;

        backButton = new Texture("back.png");

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.3f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        elapsed += delta;

        if (elapsed > 7.0) {
            game.setScreen(new OtherWaitScreen(game));
        }

        batch.begin();
        count.getData().setScale(10);
        count.draw(batch, "You played " + numberPlayed + " " + suitPlayed + "\nWaiting for other players..." +
                "\nThey have " + (int) (7 - elapsed) + " seconds to call BS!", 250, 1100);
        batch.end();

        if(Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            System.out.println(Gdx.input.getX() + " " + Gdx.input.getY());

            clickInBack(touchPos);

        }
    }

    private void clickInBack(Vector3 touchPos) {
        if (touchPos.x > 10 && touchPos.x < backButton.getWidth()
                && (height - touchPos.y) > (height - backButton.getHeight()) && (height - touchPos.y) < height) {
            game.setScreen(new MainMenu(game));
            dispose();
        }
    }

    //TODO: need UI people to fill this in. uncomment and make screens show up accordingly.
//    public void onReceivedData(int name, Object obj) {
//        String str = (String) obj;
//        Message m = LoganSquare.parse(str, Message.class);
//        switch (name) {
//            case Constants.M_PLAYER_ID:
//                ID = m.PlayerID;
//                break;
//            case Constants.M_GAME_OVER:
//                //TODO: protocol for finishing game
//                break;
//            case Constants.M_PLAYER_BS_CORRECT:
//                //TODO: protocol for handling game events like incorrect BS calls
//                hands = m.cardsInHands;
//                inputCards = hands.get(ID);
//                break;
//            case Constants.M_PLAYER_BS_CORRECT:
//                hands = m.cardsInHands;
//                inputCards = hands.get(ID);
//                break;
//            case Constants.M_PLAYER_TURN:
//                hands = m.cardsInHands;
//                inputCards = hands.get(ID);
//                if (ID = m.PlayerID) {
//                    //do a turn
//                }
//                else {
//                    //display waiting screen
//                }
//                break;
//            case Constants.M_PLAYER_TURN_START:
//                //same as above, but there should be no option to call BS
//                break;
//
//
//
//
//        }
//    }

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

    }

    public class CardInfo {
        private float x;
        private float y;
        private boolean chosen;

        public CardInfo() {
            x = 0;
            y = 0;
            chosen = false;
        }

        public CardInfo(float x, float y) {
            this.x = x;
            this.y = y;
            chosen = false;
        }

        public void setXY(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return this.x;
        }

        public float getY() {
            return this.y;
        }

        public void setChosen() {
            this.chosen = !this.chosen;
        }
    }

}
