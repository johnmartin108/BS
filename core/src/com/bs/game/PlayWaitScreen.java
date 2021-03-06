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

    //constructors for screen
    public PlayWaitScreen(BSGame game) {
        this.game = game;
        this.suitPlayed = Card.convertToStringRank(game.targetRank);
        this.numberPlayed = game.lastPlay.size();
    }

    public PlayWaitScreen(BSGame game, int numberPlayed, String suitPlayed) {
        this.game = game;
        this.numberPlayed = numberPlayed;
        this.suitPlayed = suitPlayed;
    }

    @Override
    //show to user
    public void show() {

        //set game vars
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        batch = new SpriteBatch();
        count = game.font;

        backButton = new Texture("back.png");

    }

    @Override
    public void render(float delta) {
        //set screen based on state
        if (game.wrongBSCall || game.rightBSCall) {
            game.setScreen(new CalledBSScreen(game));
        }

        //update elapsed time
        elapsed += delta;

        if (elapsed > 7.0) {
            game.setScreen(new OtherWaitScreen(game));
        }

        //set color and clear
        Gdx.gl.glClearColor(0.05f, 0.3f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //draw for user
        batch.begin();
        if (game.prev_player == game.ID) {
            count.draw(batch, "You played " + numberPlayed + " " + suitPlayed + "\nWaiting for " + game.player_names.get(game.curr_player) + "..." +
                    "\nThey have " + (int) (7 - elapsed) + " seconds to call BS!", 250, 1100);
        }
        else {
            count.draw(batch, game.player_names.get(game.prev_player) + " played " + numberPlayed + " " + suitPlayed +
                    "\nWaiting for " + game.player_names.get(game.curr_player) + "..." +
                    "\nThey have " + (int) (7 - elapsed) + " seconds to call BS!", 250, 1100);
        }
        batch.end();

        //track touch input
        if(Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            System.out.println(Gdx.input.getX() + " " + Gdx.input.getY());

            clickInBack(touchPos);

        }
    }

    //check if click in back and move screen
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
