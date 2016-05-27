package com.bs.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by nick on 5/24/16.
 */
public class CalledBSScreen implements Screen {

    SpriteBatch batch;
    private int ID1;
    private int ID2;
    private BitmapFont count;
    final BSGame game;
    private Texture backButton;
    private int width;
    private int height;
    private int ID;

    public CalledBSScreen(BSGame game) {
        this.game = game;
    }

    public CalledBSScreen(BSGame game, int ID1, int ID2) {
        this.game = game;
        this.ID1 = ID1;
        this.ID2 = ID2;
    }

    @Override
    public void show() {

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        batch = new SpriteBatch();
        count = new BitmapFont();

        backButton = game.backButton;

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        game.setScreen(new PlayScreen(game));
                    }
                },
                5000
        );

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.3f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        count.getData().setScale(10);
        count.draw(batch, ID1 + " called BS on " + ID2 + "\nThey were right!\n" + ID2 + " collected the pile.", 500, 1000);
        batch.end();

        if(Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            System.out.println(Gdx.input.getX() + " " + Gdx.input.getY());

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

    //TODO: need UI people to fill this in. uncomment and make screens show up accordingly.
//    public void onReceivedData(int name, Object obj) {
//        String str = (String) obj;
//        Message m = LoganSquare.parse(str, Message.class);
//        switch (name) {
//            case Constants.M_PLAYER_ID:
//                ID = m.PlayerID;
//                break;
//            case Constants.M_GAME_OVER:
//                //TODO: protocol for finishing game
//                break;
//            case Constants.M_PLAYER_BS_CORRECT:
//                //TODO: protocol for handling game events like incorrect BS calls
//                hands = m.cardsInHands;
//                inputCards = hands.get(ID);
//                break;
//            case Constants.M_PLAYER_BS_CORRECT:
//                hands = m.cardsInHands;
//                inputCards = hands.get(ID);
//                break;
//            case Constants.M_PLAYER_TURN:
//                hands = m.cardsInHands;
//                inputCards = hands.get(ID);
//                if (ID = m.PlayerID) {
//                    //do a turn
//                }
//                else {
//                    //display waiting screen
//                }
//                break;
//            case Constants.M_PLAYER_TURN_START:
//                //same as above, but there should be no option to call BS
//                break;
//
//
//
//
//        }
//    }

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
