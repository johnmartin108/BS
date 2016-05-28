package com.bs.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by rcornew on 5/25/16.
 */
public class WaitScreen implements Screen {

    BSGame game;
    BitmapFont bitFont;
    private Texture backButton;
    int width;
    int height;

    WaitScreen(BSGame g){
        game = g;
    }


    @Override
    public void show() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        backButton = new Texture("back.png");
        bitFont = game.font;
    }

    @Override
    public void render(float delta) {
        if (game.isGameStarted) {
            if (game.ID == game.curr_player) {
                game.setScreen(new PlayScreen(game));
            }
            else {
                game.setScreen(new PlayWaitScreen(game));
            }
        }

        Gdx.gl.glClearColor(0.05f, 0.3f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        bitFont.getData().setScale(10);
        bitFont.draw(game.batch, "Waiting for Host to Start", 500, 1300);
        game.batch.draw(backButton, 10, height - backButton.getHeight());
        game.batch.end();

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
