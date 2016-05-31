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
public class CalledBSScreen implements Screen {

    SpriteBatch batch;
    private int ID1;
    private int ID2;
    private BitmapFont count;
    final BSGame game;
    private Texture backButton;
    private int width;
    private int height;
    private int ID;
    private float elapsed = 0;

    public CalledBSScreen(BSGame game) {
        this.game = game;
        this.ID1 = game.curr_player;
        this.ID2 = game.prev_player;
    }


    @Override
    public void show() {

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        batch = new SpriteBatch();
        count = game.font;

        backButton = game.backButton;

    }

    @Override
    public void render(float delta) {
        elapsed += delta;

        if (elapsed > 5.0) {
            game.rightBSCall = false;
            game.wrongBSCall = false;
            if (game.gameFinished) {
                if (game.gameWinner == game.ID) {
                    game.setScreen(new WinScreen(game));
                }
                else {
                    game.setScreen(new LossScreen(game));
                }
            }
            else {
                if (game.curr_player == game.ID) {
                    game.setScreen(new PlayScreen(game));
                } else {
                    game.setScreen(new OtherWaitScreen(game));
                }
            }
        }

        Gdx.gl.glClearColor(0.05f, 0.3f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        if (game.rightBSCall) {
            count.draw(batch, game.player_names.get(ID1) + " called BS on\n" + game.player_names.get(ID2)
                    + "\nThey were right!\n" + game.player_names.get(ID2) + " collected the pile.", 250, 1000);
        }
        else if (game.wrongBSCall) {
            count.draw(batch, game.player_names.get(ID1) + " called BS on " + game.player_names.get(ID2)
                    + "\nThey were wrong!\n" + game.player_names.get(ID1) + " collected the pile.", 250, 1000);
        }
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
