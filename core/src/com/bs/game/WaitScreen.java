package com.bs.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by rcornew on 5/25/16.
 */
public class WaitScreen implements Screen {

    //screen state vars
    BSGame game;
    BitmapFont bitFont;
    private Texture backButton;
    int width;
    int height;

    //init screen
    WaitScreen(BSGame g){
        game = g;
    }


    @Override
    //shows screen to user
    public void show() {
        //get necessary data about game state
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        backButton = new Texture("back.png");
        bitFont = game.font;
    }

    @Override
    public void render(float delta) {
        //sets new screen for player
        if (game.isGameStarted) {
            if (game.ID == game.curr_player) {
                game.setScreen(new PlayScreen(game));
            }
            else {
                game.setScreen(new OtherWaitScreen(game));
            }
        }

        //sets clear color and clears screen
        Gdx.gl.glClearColor(0.05f, 0.3f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //draws screen info
        game.batch.begin();
        bitFont.draw(game.batch, "Waiting for Host to Start", 400, 1300);
        game.batch.draw(backButton, 10, height - backButton.getHeight());
        game.batch.end();

        //checks touch input
        if(Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

            clickInBack(touchPos);

        }

    }

    //checks if clicked in back button and sets screen if does
    private void clickInBack(Vector3 touchPos) {
        if (touchPos.x > 10 && touchPos.x < backButton.getWidth()
                && (height - touchPos.y) > (height - backButton.getHeight()) && (height - touchPos.y) < height) {
            game.setScreen(new JoinScreen(game));
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
