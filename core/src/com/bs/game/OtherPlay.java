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
public class OtherPlay implements Screen {

    //game state vars
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

    //construct screen
    public OtherPlay(BSGame game) {
        this.game = game;
        this.ID = game.prev_player;
        this.suitPlayed = Card.convertToStringRank(game.targetRank);
        this.numberPlayed = game.lastPlay.size();
    }

    @Override
    public void show() {

        //set game vars
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        batch = game.batch;
        count = game.font;

        backButton = new Texture("back.png");
        goButton = new Texture("bs.png");

    }

    @Override
    public void render(float delta) {
        //set screen based on game state
        if (game.wrongBSCall || game.rightBSCall) {
            game.setScreen(new CalledBSScreen(game));
        }

        //track elapsed time
        elapsed += delta;
        if (elapsed > 7.0) {
            game.setScreen(new PlayScreen(game));
        }

        //set color and clear screen
        Gdx.gl.glClearColor(0.05f, 0.3f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //draw to screen
        batch.begin();
        count.draw(batch, game.player_names.get(game.prev_player) + " played " + numberPlayed + " " + suitPlayed, 400, 1000);
        count.draw(batch, "You have " + (int) (7 - elapsed) + " seconds to call BS!", 250, 600);
        batch.end();
        batch.begin();
        batch.draw(goButton, width / 2 - goButton.getWidth() / 2, 625);
        batch.end();

        //track user input touch
        if(Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            System.out.println(Gdx.input.getX() + " " + Gdx.input.getY());

            clickInBack(touchPos);
            clickInGo(touchPos);
        }
    }


    //check if click in go button and sends data
    private void clickInGo(Vector3 touchPos) {
        if (touchPos.x > width / 2 - goButton.getWidth() / 2 && touchPos.x < width / 2 + goButton.getWidth() / 2
                && (height - touchPos.y) > 625 && (height - touchPos.y) < 625 + goButton.getHeight()) {
            game.bridge.sendDataToController(Constants.M_CALL_BS, null);
        }
    }

    //check if click in back and sets screen
    private void clickInBack(Vector3 touchPos) {
        if (touchPos.x > 10 && touchPos.x < backButton.getWidth()
                && (height - touchPos.y) > (height - backButton.getHeight()) && (height - touchPos.y) < height) {
            game.setScreen(new MainMenu(game));
            dispose();
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
