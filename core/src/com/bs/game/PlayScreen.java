package com.bs.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by nick on 5/24/16.
 */
public class PlayScreen implements Screen {

    SpriteBatch batch;

    ArrayList<Card> inputCards;
    ArrayList<ArrayList<Card>> hands;
    HashMap<Card, CardInfo> cards;
    int num_players;

    
    private int numberSelected;
    private String name;
    private String currRank;
    private BitmapFont count;
    final BSGame game;
    private Texture backButton;
    private Texture goButton;
    private int width;
    private int height;

    private Set<Card> selectedCard;

    private Stage stage;

    public PlayScreen(BSGame game) {
        this.game = game;
        this.hands = game.hands;
        this.inputCards = game.hands.get(game.ID);
        this.num_players = game.num_players;

        Collections.sort(inputCards, new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                if (c2.valueOf() == c1.valueOf()) {
                    return 0;
                }
                else if (c2.valueOf() < c1.valueOf()) {
                    return -1;
                }
                else {
                    return 1;
                }
            }
        });
        this.currRank = Card.convertToStringRank(game.targetRank);
    }

    public PlayScreen(BSGame game, String name) {
        this.game = game;
        this.name = name;
    }

    @Override
    public void show() {
        stage = new Stage();
        selectedCard = new HashSet<Card>();

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        batch = game.batch;
        count = game.font;

        backButton = game.backButton;

        goButton = game.goButton;

        cards = new HashMap();
        numberSelected = 0;

        int width = Gdx.graphics.getWidth()/inputCards.size();

        for (int i = 0; i < inputCards.size(); i++) {
            final Card card = inputCards.get(i);
            card.loadTexture();
            Texture texture = card.getTexture();
            final Image img = new Image(texture);
            img.setScale(0.4f);

            CardInfo cardInfo = new CardInfo(i*width, 0);

            float x = i * width;
            float y = 0;

            cardInfo.setXY(x, y );

            img.addListener(new ClickListener(){
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    if (selectedCard.contains(card)){
                        numberSelected--;
                        selectedCard.remove(card);
                        img.setPosition(img.getX(), img.getY() - 100);

                    }else{
                        numberSelected++;
                        selectedCard.add(card);
                        img.setPosition(img.getX(), img.getY() + 100);
                    }
                }
            });
//            cards.put(inputCards.get(i), new CardInfo(i*width, 0));

            img.setPosition(x, y);
            stage.addActor(img);

            //add start and back button
            Image backButtonImage = new Image(backButton);
            backButtonImage.setPosition(10, height - backButton.getHeight());
            backButtonImage.addListener(new ClickListener(){
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    game.setScreen(new MainMenu(game));
                }
            });
            stage.addActor(backButtonImage);

            Image goButtonImage = new Image(goButton);
            goButtonImage.setPosition(4*Gdx.graphics.getWidth() / 5 - goButton.getWidth() / 2, 950);
            goButtonImage.addListener(new ClickListener(){
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    ArrayList<Card> play = new ArrayList<Card>(selectedCard);

                    if (play.isEmpty()) {
                        return;
                    }
                    game.bridge.sendDataToController(Constants.M_PLAY_CARDS, play);

                    if (inputCards.size() == selectedCard.size()) {
                        game.setScreen(new MandatoryBSWaitScreen(game));
                    }
                    else {
                        game.setScreen(new PlayWaitScreen(game, selectedCard.size(), currRank));
                    }
                }
            });
            stage.addActor(goButtonImage);

        }

        // lets add card pile here
        Texture t = new Texture("decks/large/deck_4_large.png");


        Gdx.app.log("BSGAME", "size:: "+game.cardPile.size());
        Image pileImage = new Image(t);
        pileImage.setScale(0.5f);
        pileImage.setPosition(Gdx.graphics.getWidth()/2-0.4f*pileImage.getWidth()/2, Gdx.graphics.getHeight()/2-0.4f*pileImage.getHeight()/2);
        stage.addActor(pileImage);
        stage.addActor(new TextImg(""+game.cardPile.size(),
                Gdx.graphics.getWidth()/2-0.4f*pileImage.getWidth()/2, Gdx.graphics.getHeight()/2-0.4f*pileImage.getHeight()/2 + 200));

        int me = game.ID;
        int incr = game.ID + 1;
        int ctr = incr%num_players;
        while(ctr != game.ID) {
            Image playerPile = new Image(t);
            playerPile.setScale(0.4f);

            float x = 0, y = 0, textx = 0, texty = 0;

            switch (incr-me){
                case 1:
                    //next player
                    y = Gdx.graphics.getHeight()/2-0.4f*playerPile.getHeight()/2;
                    texty = y + 200;
                    textx = x + 200;
                    break;

                case 2:
                    //next next player
                    x = Gdx.graphics.getWidth()/2-0.4f*playerPile.getWidth()/2;
                    y = Gdx.graphics.getHeight() - 0.4f*playerPile.getHeight();
                    textx = x - 200;
                    texty = y - 200;

                    break;
                case 3:
                    //next next nexy player
                    x = Gdx.graphics.getWidth() - 0.4f*playerPile.getWidth();
                    y = Gdx.graphics.getHeight()/2 - 0.4f*playerPile.getHeight()/2;
                    texty = y + 200;
                    textx = x - 1000;
                    break;

            }
            Gdx.app.log("BSGAME", incr-me+" :: "+x+" "+y+" "+playerPile.getHeight());

            playerPile.setPosition(x, y);
            stage.addActor(playerPile);
            stage.addActor(new TextImg(""+game.hands.get(ctr).size(), x, y+200));
            stage.addActor(new TextImg(game.player_names.get(ctr), textx, texty));



            ++incr;
            ctr = incr%num_players;
        }
        
        Gdx.app.log("BSGame", currRank);
        Gdx.app.log("BSGame", inputCards.toString());
    }

    public void drawInstruction(){
        batch.begin();
        count.draw(batch, "Play " + numberSelected + " " + currRank + "?", 1500, 1350, 100, Align.right, false);
        batch.end();
    }

    public void drawCards(){
        batch.begin();

        stage.draw();

        batch.end();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.3f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.input.setInputProcessor(stage);

        drawInstruction();
        drawCards();
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
        private boolean chosen;
        private boolean back;

        public CardInfo() {
            x = 0;
            y = 0;
            chosen = false;
            back = false;
        }

        public CardInfo(float x, float y) {
            this.x = x;
            this.y = y;
            chosen = false;
            back = false;
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

        public void setChosen() {
            this.chosen = !this.chosen;
        }

        public void setBack(boolean b) {
            this.back = b;
        }

        public boolean getBack() {
            return back;
        }
    }

    public class TextImg extends Actor {

        BitmapFont font = game.font;
        String text;
        float x;
        float y;

        public TextImg(String text, float x, float y){
            this.text = text;
            this.x = x;
            this.y = y;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            font.draw(batch, text, x, y);
            //Also remember that an actor uses local coordinates for drawing within
            //itself!
        }

    }

}
