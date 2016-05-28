package com.bs.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by rcornew on 5/25/16.
 */
public class StartScreen implements Screen {

    BSGame game;
    BitmapFont bitFont;
    private Texture backButton;
    private Texture startButton;
    TextButton.TextButtonStyle textButtonStyle;
    int width;
    int height;

    StartScreen(BSGame g){
        game = g;
    }

    @Override
    public void show() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        backButton = new Texture("back.png");
        startButton = new Texture("start.png");
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
            dispose();
        }
        
        Gdx.gl.glClearColor(0.05f, 0.3f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        bitFont.getData().setScale(7);
        bitFont.draw(game.batch, "List of Players in game", 660, 1300);
        bitFont.draw(game.batch, "Press Start when ready", 660, 1200);
        bitFont.draw(game.batch, "Host Status: "+game.hostStatus, 660, 1100);
        for (int i = 0; i < game.peerlist.size(); i ++){
            String devName = (String)game.peerlist.get(i);
            bitFont.draw(game.batch, devName, 720, 900-i*100);
        }

        game.batch.draw(startButton, width / 2 - startButton.getWidth() / 2, 50);
        game.batch.end();

        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

            // Start the game
            if (inStartButton(touchPos)) {
                game.bridge.sendDataToController(Constants.M_START_GAME, true);
                game.setScreen(new PlayScreen(game));
                dispose();
            }
        }
    }

    private boolean inStartButton(Vector3 touchPos) {
        if (touchPos.x > (width/2 - startButton.getWidth()/2) && touchPos.x < (width/2 + startButton.getWidth()/2)
                && touchPos.y > (height - (50 + startButton.getHeight())) && touchPos.y < (height - 50)) {
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
