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
public class HostScreen implements Screen {


    BSGame game;
    private Texture easyBtn;
    private Texture mediumBtn;
    private Texture hardBtn;
    private Texture backButton;
    private Texture impBtn;
    int width;
    int height;

    private BitmapFont bitFont;

    HostScreen(BSGame g){
        game = g;
    }

    @Override
    public void show() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        easyBtn = new Texture("easy.png");
        mediumBtn = new Texture("medium.png");
        hardBtn = new Texture("hard.png");
        impBtn = new Texture("impossible.png");
        backButton = new Texture("back.png");
        bitFont = game.font;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.3f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        bitFont.draw(game.batch, "Select Your Difficulty", 550, 1400);
        game.batch.draw(easyBtn, width/2 - easyBtn.getWidth()/2, 950);
        game.batch.draw(mediumBtn, width/2 - mediumBtn.getWidth()/2, 650);
        game.batch.draw(hardBtn, width/2 - hardBtn.getWidth()/2, 350);
        game.batch.draw(impBtn, width/2 - impBtn.getWidth()/2, 50);
        game.batch.draw(backButton, 10, height - backButton.getHeight());
        game.batch.end();

        if(Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

            System.out.println(String.valueOf(touchPos));
            clickInBack(touchPos);

            int diff = inEasyButton(touchPos) ? 0 : inMediumButton(touchPos) ? 1 : inHardButton(touchPos) ? 2 : inImpButton(touchPos)? 3 : -1;
            if(diff >= 0) {
                game.bridge.sendDataToController(Constants.M_SET_DIFF, diff);
                game.bridge.sendDataToController(Constants.M_BECOME_HOST, null);
                game.setScreen(new StartScreen(game));
                dispose();
            }

        }
    }

    private boolean inEasyButton(Vector3 touchPos) {
        if (touchPos.x > (width/2 - easyBtn.getWidth()/2) && touchPos.x < (width/2 + easyBtn.getWidth()/2)
                && touchPos.y > (height - (950 + easyBtn.getHeight())) && touchPos.y < (height - 950)) {
            return true;
        }
        return false;
    }

    private boolean inMediumButton(Vector3 touchPos) {
        if (touchPos.x > (width/2 - mediumBtn.getWidth()/2) && touchPos.x < (width/2 + mediumBtn.getWidth()/2)
                && touchPos.y > (height - (650 + mediumBtn.getHeight())) && touchPos.y < (height - 650)) {
            return true;
        }
        return false;
    }

    private boolean inHardButton(Vector3 touchPos) {
        if (touchPos.x > (width/2 - hardBtn.getWidth()/2) && touchPos.x < (width/2 + hardBtn.getWidth()/2)
                && touchPos.y > (height - (350 + hardBtn.getHeight())) && touchPos.y < (height - 350)) {
            return true;
        }
        return false;
    }

    private boolean inImpButton(Vector3 touchPos) {
        if (touchPos.x > (width/2 - impBtn.getWidth()/2) && touchPos.x < (width/2 + impBtn.getWidth()/2)
                && touchPos.y > (height - (50 + impBtn.getHeight())) && touchPos.y < (height - 50)) {
            return true;
        }
        return false;
    }

    private void clickInBack(Vector3 touchPos) {
        if (touchPos.x > 10 && touchPos.x < backButton.getWidth()
                && (height - touchPos.y) > (height - backButton.getHeight()) && (height - touchPos.y) < height) {
            game.setScreen(new JHScreen(game));
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
