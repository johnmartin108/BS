package com.bs.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

/**
 * Created by rcornew on 5/25/16.
 */
public class JoinScreen implements Screen {

    BSGame game;
    int width;
    int height;

    Stage stage;

    BitmapFont font;

    Boolean updated = false;

    private Texture backButton;

    private BitmapFont bitFont;
    TextButton.TextButtonStyle style;

    ArrayList peerlist = new ArrayList();
    ArrayList buttons = new ArrayList();

    TextButton.TextButtonStyle textButtonStyle;

    private ArrayList<String> hosts = new ArrayList<String>();
    String host;

    JoinScreen(BSGame g){
        game = g;
    }

    @Override
    public void show() {

        stage = new Stage();

        textButtonStyle = new TextButton.TextButtonStyle();
        font = new BitmapFont();

        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().scale(4.0f);

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        backButton = new Texture("back.png");
        bitFont = new BitmapFont();
        game.bridge.sendDataToController(Constants.M_DISCOVER_PEERS, null);
        if(!game.peerlist.isEmpty()){
            for(int i = 0; i < game.peerlist.size(); i++){
                final String newBtnName = "btn" + String.valueOf(i);
                String host = (String)game.peerlist.get(i);
                TextButton newBtn = new TextButton(newBtnName, textButtonStyle);
                newBtn.setPosition(400, 500+100*i);
                newBtn.addListener(new ClickListener(){
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        super.touchUp(event, x, y, pointer, button);
                        game.connectTo(newBtnName);
                    }
                });
                stage.addActor(newBtn);

            }
        }

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.3f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        game.batch.begin();
        bitFont.getData().setScale(10);
        bitFont.draw(game.batch, "Select a Host", 725, 1300);
        for (int i = 0; i < game.peerlist.size(); i ++){
            String peerName = (String)game.peerlist.get(i);
            bitFont.draw(game.batch, peerName, 725, 1100-i*100);
        }

        game.batch.draw(backButton, 10, height - backButton.getHeight());
        game.batch.end();





        if(Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

            clickInBack(touchPos);
        }

        //prefs.flush();

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