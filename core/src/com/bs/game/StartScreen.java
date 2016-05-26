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
        bitFont = new BitmapFont();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.3f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
<<<<<<< Updated upstream
        bitFont.getData().setScale(10);
        bitFont.draw(game.batch, "Host Status: "+game.hostStatus, 725, 900);
        bitFont.draw(game.batch, "List of Players in game", 725, 1300);
        bitFont.draw(game.batch, "Press Start when ready", 725, 1100);
        for (int i = 0; i < game.peerlist.size(); i ++){
=======
        bitFont.getData().setScale(7);
        bitFont.draw(game.batch, "List of Players in game", 660, 1300);
        bitFont.draw(game.batch, "Press Start when ready", 640, 1200);
        /*for (int i = 0; i < game.peerlist.size(); i ++){
>>>>>>> Stashed changes
            final String btnName = (String)game.peerlist.get(i);
            TextButton newBtn = new TextButton(btnName, textButtonStyle);
            newBtn.setPosition(400, 500+100*i);
            newBtn.addListener(new ClickListener(){
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                }
            });
        }*/
        game.batch.draw(startButton, width / 2 - startButton.getWidth() / 2, 50);
        game.batch.end();

        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

            // Start the game
            if (inStartButton(touchPos)) {
                //game.bridge.sendDataToController(Constants.M_START_GAME, true);
                game.setScreen(new StartScreen(game));
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
