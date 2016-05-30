package com.bs.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
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

    
    private int numberSelected;
    private String name;
    private String currRank;
    private BitmapFont count;
    final BSGame game;
    private Texture backButton;
    private Texture goButton;
    private int width;
    private int height;
    private int offset;

    private Set<Card> selectedCard;

    private Stage stage;

    public PlayScreen(BSGame game) {
        this.game = game;
        this.hands = game.hands;
        this.inputCards = game.hands.get(game.ID);
        this.currRank = convertToStringRank(game.targetRank);
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
//        inputCards = new ArrayList();
//        numberSelected = 0;
//
//
//        inputCards.add(new Card("h", "a"));
//        inputCards.add(new Card("c", "a"));
//        inputCards.add(new Card("d", "a"));
//        inputCards.add(new Card("s", "a"));
//        inputCards.add(new Card("d", "j"));
//        inputCards.add(new Card("s", "k"));

//        inputCards = new Deck().getCards();
        int width = Gdx.graphics.getWidth()/inputCards.size();
        offset = inputCards.size() >= 26 ? 200 : 0;

        for (int i = 0; i < inputCards.size(); i++) {
            final Card card = inputCards.get(i);
            card.loadTexture();
            Texture texture = card.getTexture();
            final Image img = new Image(texture);
            img.setScale(0.5f);

            CardInfo cardInfo = new CardInfo(i*width, 0);

            float x = i * width;
            float y = cardInfo.getY() + offset;

            cardInfo.setXY(x, y - offset);

            if (offset > 0){
                cardInfo.setBack(true);
            }

            if (i+1 == 30) {
                offset = 0;
                i = 0;
            }

            img.addListener(new ClickListener(){
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    if (selectedCard.contains(card)){
                        selectedCard.remove(card);
                        img.setPosition(img.getX(), img.getY() - 200);

                    }else{
                        selectedCard.add(card);
                        img.setPosition(img.getX(), img.getY() + 200);
                    }
                }
            });
//            cards.put(inputCards.get(i), new CardInfo(i*width, 0));

            img.setPosition(x, y - offset);
            stage.addActor(img);


            Image backButtonImage = new Image(backButton);
            backButtonImage.setPosition(10, height - backButton.getHeight());
            stage.addActor(backButtonImage);

            Image goButtonImage = new Image(goButton);
            goButtonImage.setPosition(Gdx.graphics.getWidth() / 2 - goButton.getWidth() / 2, 950);
            stage.addActor(goButtonImage);


            //add start and back button



//            // lets add card pile here
//            Texture t = new Texture("decks/large/deck_4_large.png");
//
//            for (Card c: game.cardPile){
//                Image image = new Image(t);
//
//                // set their position here
//
//            }
//
//            int inc = 0;
//            int ctr = (game.ID + 1 + inc)%game.hands.size();
//            while(ctr != game.ID){
//                ArrayList<Card> hand = game.hands.get(ctr);
//
//                for (Card c: hand){
//                    // draw their hand
//
//                }
//
//                i++;
//                ctr = (game.ID + 1 + inc)%game.hands.size();
//            }
        }

        Gdx.app.log("BSGame", currRank);
        Gdx.app.log("BSGame", inputCards.toString());
    }

    public void drawInstruction(){
        batch.begin();
        count.draw(batch, "Play " + numberSelected + " " + currRank + "?", 750, 1350);
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

        if(Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
//            System.out.println(Gdx.input.getX() + " " + Gdx.input.getY());

//            clickInCard(touchPos);
            clickInBack(touchPos);
            clickInGo(touchPos);
        }
    }

//    private int clickInCard(Vector3 touchPos) {
//        Iterator iter = cards.entrySet().iterator();
//        float maxX = -1;
//        CardInfo selected = null;
//        while (iter.hasNext()) {
//            Map.Entry mapPair = (Map.Entry) iter.next();
//            Card card = (Card) mapPair.getKey();
//            CardInfo info = (CardInfo) mapPair.getValue();
//            float x = info.getX();
//            float y = info.getY();
//            if (info.getBack()) {
//                if (touchPos.x > info.getX() && touchPos.x < (info.getX() + card.getTexture().getWidth())
//                        && (height - touchPos.y) > card.getTexture().getHeight() && (height - touchPos.y) < (card.getTexture().getHeight() + 200)) {
//                    if (info.getX() > maxX) {
//                        maxX = info.getX();
//                        selected = info;
//                    }
//                }
//            }
//            else {
//                if (info.getX() == 0) System.out.println(touchPos.x + " should be between " + info.getX() + " and " + (info.getX() + card.getTexture().getWidth()));
//                if (touchPos.x > info.getX() && touchPos.x < (info.getX() + card.getTexture().getWidth())
//                        && (height - touchPos.y) > 0 && (height - touchPos.y) < (card.getTexture().getHeight())) {
//                    if (info.getX() > maxX) {
//                        maxX = info.getX();
//                        selected = info;
//                    }
//                }
//            }
//
//        }
//
//        if (selected != null && selected.getY() != 30) {
//            selected.setXY(selected.getX(), 30);
//            numberSelected++;
//            selected.setChosen();
//        }
//        else if (selected != null) {
//            selected.setXY(selected.getX(), 0);
//            numberSelected--;
//            selected.setChosen();
//        }
//        return -1;
//    }

    private void clickInBack(Vector3 touchPos) {
        if (touchPos.x > 10 && touchPos.x < backButton.getWidth()
                && (height - touchPos.y) > (height - backButton.getHeight()) && (height - touchPos.y) < height) {
            game.setScreen(new MainMenu(game));
        }
    }

    private void clickInGo(Vector3 touchPos) {
        if (touchPos.x > width / 2 - goButton.getWidth() / 2 && touchPos.x < width / 2 + goButton.getWidth() / 2
                && (height - touchPos.y) > 950 && (height - touchPos.y) < 950 + goButton.getHeight()) {
            ArrayList<Card> play = new ArrayList<Card>(selectedCard);

            if (play.isEmpty()) {
                return;
            }
            game.bridge.sendDataToController(Constants.M_PLAY_CARDS, play);

            if (cards.size() == selectedCard.size()) {
                game.setScreen(new MandatoryBSWaitScreen(game));
            }
            else {
                game.setScreen(new PlayWaitScreen(game, numberSelected, currRank));
            }
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

    public String convertToStringRank(int rank) {
        switch(rank) {
            case 1:
                return "aces";
            case 2:
                return "twos";
            case 3:
                return "threes";
            case 4:
                return "fours";
            case 5:
                return "fives";
            case 6:
                return "sixes";
            case 7:
                return "sevens";
            case 8:
                return "eights";
            case 9:
                return "nines";
            case 10:
                return "tens";
            case 11:
                return "jacks";
            case 12:
                return "queens";
            case 13:
                return "kings";
        }
        return "";
    }

}
