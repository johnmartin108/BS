package com.bs.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
public class PlayScreen implements Screen {

    SpriteBatch batch;
    ArrayList<Card> inputCards;
    HashMap<Card, CardInfo> cards;
    private int numberSelected;
    private String currRank;
    private String name;
    private BitmapFont count;
    final BSGame game;
    private Texture backButton;
    private int width;
    private int height;

    public PlayScreen(BSGame game) {
        this.game = game;
    }

    public PlayScreen(BSGame game, String name) {
        this.game = game;
        this.name = name;
    }

    private int clickInCard(Vector3 touchPos) {
        Iterator iter = cards.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry mapPair = (Map.Entry) iter.next();
            Card card = (Card) mapPair.getKey();
            CardInfo info = (CardInfo) mapPair.getValue();
            float x = info.getX();
            float y = info.getY();
            if (touchPos.x > info.getX() && touchPos.x < (info.getX() + card.getTexture().getWidth())
                    && (height - touchPos.y) > 0 && (height - touchPos.y) < (card.getTexture().getHeight())) {
                if (info.getY() != 30) {
                    info.setXY(info.getX(), 30);
                    numberSelected++;
                }
                else {
                    info.setXY(info.getX(), 0);
                    numberSelected--;
                }
            }

        }

        return -1;
    }

    @Override
    public void show() {

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        batch = new SpriteBatch();
        count = new BitmapFont();

        backButton = new Texture("back.png");
        currRank = "aces";

        cards = new HashMap();
        inputCards = new ArrayList();
        numberSelected = 0;
        int width = Gdx.graphics.getWidth()/4;

        inputCards.add(new Card("h", "a"));
        inputCards.add(new Card("c", "a"));
        inputCards.add(new Card("d", "a"));
        inputCards.add(new Card("s", "a"));

        for (int i = 0; i < inputCards.size(); i++) {
            cards.put(inputCards.get(i), new CardInfo(i*width, 0));
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.3f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        count.getData().setScale(10);
        count.draw(batch, "Play " + numberSelected + " " + currRank + "?", 750, 1000);
        count.draw(batch, "Player: " + name, width - 1000, height - 20);
        batch.end();
        batch.begin();
        Iterator iter = cards.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry mapPair = (Map.Entry) iter.next();
            Card card = (Card) mapPair.getKey();
            float x = cards.get(card).getX();
            float y = cards.get(card).getY();
            batch.draw(card.getTexture(), x, y);
        }
        batch.draw(backButton, 10, height - backButton.getHeight());
        batch.end();

        if(Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            System.out.println(Gdx.input.getX() + " " + Gdx.input.getY());

            clickInCard(touchPos);
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

    public class CardInfo {
        private float x;
        private float y;

        public CardInfo() {
            x = 0;
            y = 0;
        }

        public CardInfo(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public void setXY(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return this.x;
        }

        public float getY() {
            return this.y;
        }

    }

}
