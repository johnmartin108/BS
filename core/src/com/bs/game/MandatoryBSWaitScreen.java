package com.bs.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by johnmartin on 5/30/16.
 */
public class MandatoryBSWaitScreen implements Screen {
    //game vars
    SpriteBatch batch;
    private int numberPlayed;
    private String suitPlayed;
    private BitmapFont count;
    final BSGame game;
    private Texture backButton;
    private Texture goButton;
    private int width;
    private int height;
    private int ID;
    private float elapsed = 0;

    //screen constructor
    public MandatoryBSWaitScreen(BSGame game) {
        this.game = game;
        this.suitPlayed = Card.convertToStringRank(game.targetRank);
        this.numberPlayed = game.lastPlay.size();
    }

    @Override
    public void show() {

        //set game vars
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        batch = new SpriteBatch();
        count = game.font;

        backButton = new Texture("back.png");

    }

    @Override
    public void render(float delta) {
        //set screen based on state
        if (game.wrongBSCall || game.rightBSCall) {
            game.setScreen(new CalledBSScreen(game));
        }

        //clear screen and set color
        Gdx.gl.glClearColor(0.05f, 0.3f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //draw to screen
        batch.begin();
        if (game.prev_player == game.ID) {
            count.draw(batch, "You played " + numberPlayed + " " + suitPlayed + "\nYou are out of cards. " +
                    "\nWaiting for " + game.player_names.get(game.curr_player) + "" +
                    "\nto call BS!", 250, 1100);
        }
        else {
            count.draw(batch, game.player_names.get(game.prev_player) + " played " + numberPlayed + " " + suitPlayed
                    + "\nYou are out of cards.\nWaiting for " + game.player_names.get(game.curr_player) + " to call BS!", 250, 1100);
        }
        
        batch.end();

        //track user touch input
        if(Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            System.out.println(Gdx.input.getX() + " " + Gdx.input.getY());

            clickInBack(touchPos);

        }
    }

    //if in back button set screen
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
