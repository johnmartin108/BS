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

    public OtherPlay(BSGame game) {
        this.game = game;
        this.ID = game.prev_player;
        this.suitPlayed = game.targetRank + "";
        this.numberPlayed = game.lastPlay.size();
    }

    public OtherPlay(BSGame game, int ID, int numberPlayed, String suitPlayed) {
        this.game = game;
        this.ID = ID;
        this.numberPlayed = numberPlayed;
        this.suitPlayed = suitPlayed;
    }

    @Override
    public void show() {

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        batch = game.batch;
        count = game.font;

        backButton = new Texture("back.png");
        goButton = new Texture("bs.png");

    }

    @Override
    public void render(float delta) {

        elapsed += delta;

        Gdx.gl.glClearColor(0.05f, 0.3f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (elapsed > 7.0) {
            game.setScreen(new CalledBSScreen(game));
            dispose();
        }

        batch.begin();
        count.getData().setScale(10);
        count.draw(batch, "Player " + ID + " played " + numberPlayed + " " + suitPlayed, 500, 1000);
        count.draw(batch, "You have " + (int) (7 - elapsed) + " seconds to call BS!", 250, 600);
        batch.end();
        batch.begin();
        batch.draw(goButton, width / 2 - goButton.getWidth() / 2, 625);
        batch.end();

        if(Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            System.out.println(Gdx.input.getX() + " " + Gdx.input.getY());

            clickInBack(touchPos);
            clickInGo(touchPos);
        }
    }


    private void clickInGo(Vector3 touchPos) {
        if (touchPos.x > width / 2 - goButton.getWidth() / 2 && touchPos.x < width / 2 + goButton.getWidth() / 2
                && (height - touchPos.y) > 625 && (height - touchPos.y) < 625 + goButton.getHeight()) {
            game.setScreen(new CalledBSScreen(game, 1, 2));
            dispose();
        }
    }

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
