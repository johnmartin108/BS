package com.bs.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by nick on 5/24/16.
 */
public class MainMenu implements Screen {

    final BSGame game;
    private Texture playButton;
    int width;
    int height;

    public MainMenu(BSGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        playButton = new Texture("play.png");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.3f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.font.draw(game.batch, "BS*", 1000, 1000);
        game.font.getData().setScale(10);
        game.batch.draw(playButton, width/2 - playButton.getWidth()/2, 100);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            if (inPlayButton(touchPos)) {
                game.setScreen(new PlayScreen(game));
                dispose();
            }
        }

    }

    private boolean inPlayButton(Vector3 touchPos) {
        System.out.println("" + touchPos.y);
        if (touchPos.x > (width/2 - playButton.getWidth()/2) && touchPos.x < (width/2 + playButton.getWidth()/2)
                && touchPos.y > (height - (100 + playButton.getHeight())) && touchPos.y < (height - 100)) {
            return true;
        }
        return false;
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
