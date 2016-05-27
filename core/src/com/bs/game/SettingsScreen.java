package com.bs.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

/**
 * Created by nick on 5/25/16.
 */
public class SettingsScreen implements Screen {

    final BSGame game;
    int width;
    int height;
    private Preferences prefs;
    private String name;
    private BitmapFont nameString;
    private Texture backButton;


    public SettingsScreen(BSGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        prefs = Gdx.app.getPreferences("My Preferences");
        name = prefs.getString("name");
        nameString = game.font;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        backButton = new Texture("back.png");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.3f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(backButton, 10, height - backButton.getHeight());
        nameString.getData().setScale(10);
        nameString.draw(game.batch, name, 200, 200);
        game.batch.end();

        if(Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

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
