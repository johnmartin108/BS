package com.bs.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by nick on 5/30/16.
 */
public class WinScreen implements Screen {

    //game state variables
    BSGame game;
    BitmapFont bitFont;
    private Texture backButton;
    int width;
    int height;
    int PlayerID;

    //constructs screen
    WinScreen(BSGame g){
        game = g;
    }


    @Override
    //shows to users
    public void show() {
        //get necessary info
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        backButton = new Texture("back.png");
        bitFont = game.font;
    }

    @Override
    public void render(float delta) {

        //sets color and clears screen
        Gdx.gl.glClearColor(0.05f, 0.3f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //draws text
        game.batch.begin();
        bitFont.draw(game.batch, "Congrats! You won BS.", 500, 1300);
        game.batch.draw(backButton, 10, height - backButton.getHeight());
        game.batch.end();

        //checks touch input
        if(Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

            clickInBack(touchPos);
        }

    }


    //checks click in back button and sets screen
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