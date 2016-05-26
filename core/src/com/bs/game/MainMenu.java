package com.bs.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

//import javafx.scene.control.Skin;

/**
 * Created by nick on 5/24/16.
 */
public class MainMenu implements Screen {

    final BSGame game;
    private Stage stage;
    private Texture mainImage;
    private Texture playButton;
    private Texture settingsButton;
//    private TextField name;
    int width;
    int height;
    private Preferences prefs;
    String name = "";

    public MainMenu(BSGame game) {
        this.game = game;
    }

    @Override
    public void show() {

        prefs = Gdx.app.getPreferences("My Preferences");

        stage = new Stage();
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        mainImage = new Texture("main.png");
        playButton = new Texture("play.png");
        settingsButton = new Texture("settings.png");
//        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
//        skin.addRegions(new TextureAtlas("back.png"));
//        name = new TextField("Enter your name", skin);
//        name.setPosition(200, 200);
//        name.setSize(200, 100);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.3f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(mainImage, width / 2 - mainImage.getWidth() / 2, (height - 100 - mainImage.getHeight()));
        game.batch.draw(playButton, width / 2 - playButton.getWidth() / 2, 300);
        game.batch.draw(settingsButton, width / 2 - settingsButton.getWidth() / 2, 75);
        game.batch.end();


        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            if (inPlayButton(touchPos)) {
                
                if (name.equals("")) {
                    NameListener nameListener = new NameListener();
                    Gdx.input.getTextInput(nameListener, "What is your name?", "", "");
                }
                else {
                    game.setScreen(new JHScreen(game));
                    dispose();
                }
            }
            else if (inSettingsButton(touchPos)) {
                game.setScreen(new SettingsScreen(game));
                dispose();
            }
        }

    }

    private boolean inSettingsButton(Vector3 touchPos) {
        if (touchPos.x > (width/2 - settingsButton.getWidth()/2) && touchPos.x < (width/2 + settingsButton.getWidth()/2)
                && touchPos.y > (height - (75 + settingsButton.getHeight())) && touchPos.y < (height - 75)) {
            return true;
        }
        return false;
    }

    private boolean inPlayButton(Vector3 touchPos) {
        if (touchPos.x > (width/2 - playButton.getWidth()/2) && touchPos.x < (width/2 + playButton.getWidth()/2)
                && touchPos.y > (height - (300 + playButton.getHeight())) && touchPos.y < (height - 300)) {
            return true;
        }
        return false;
    }

    public class NameListener implements Input.TextInputListener {

        @Override
        public void input(String text) {
            game.bridge.sendDataToController(Constants.M_SET_NAME, text);
            game.setScreen(new JHScreen(game));
            dispose();
        }

        @Override
        public void canceled() {

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
