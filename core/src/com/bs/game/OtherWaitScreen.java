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
public class OtherWaitScreen implements Screen {

    //game screen vars
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
    private ArrayList<Card> lastPlay;

    //construct screen
    public OtherWaitScreen(BSGame game) {
        this.game = game;
        this.suitPlayed = game.targetRank + "";
        this.numberPlayed = game.lastPlay.size();
        this.lastPlay = game.lastPlay;
    }

    //other constructor
    public OtherWaitScreen(BSGame game, int numberPlayed, String suitPlayed) {
        this.game = game;
    }

    @Override
    public void show() {

        //game var info
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        batch = new SpriteBatch();
        count = game.font;

        backButton = new Texture("back.png");

    }

    @Override
    public void render(float delta) {
        //set screen based on game state for user
        if (!game.lastPlay.equals(this.lastPlay)) {
            Gdx.app.log("BSGame", game.curr_player + " " + game.prev_player + " " + game.ID);
            if (game.curr_player == game.ID) {
                if (game.hands.get(game.prev_player).isEmpty()) {
                    game.setScreen(new MandatoryBSScreen(game));
                }
                else {
                    game.setScreen(new OtherPlay(game));
                }
            }
            else {
                if (game.hands.get(game.prev_player).isEmpty()) {
                    game.setScreen(new MandatoryBSWaitScreen(game));
                }
                else {
                    game.setScreen(new PlayWaitScreen(game));
                }
            }
        }

        //clear screen and set color
        Gdx.gl.glClearColor(0.05f, 0.3f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //draw text
        batch.begin();
        count.draw(batch, "Waiting for " + game.player_names.get(game.curr_player) + " to play...", 200, 1100);
        batch.end();

        //track user input
        if(Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            System.out.println(Gdx.input.getX() + " " + Gdx.input.getY());

            clickInBack(touchPos);
        }
    }

    //check if in back button and set screen
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

}
