package com.bs.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by rcornew on 5/25/16.
 */
public class JHScreen implements Screen{

    private BSGame game;
    private Stage stage;

    private Texture joinBtn;
    private Texture hostBtn;
    private Texture backButton;

    int width;
    int height;
    Vector3 touchPos = new Vector3();

    private BitmapFont bitFont;


    JHScreen(BSGame g){
        game = g;
    }

    @Override
    public void show() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        joinBtn = new Texture("join.png");
        hostBtn = new Texture("host.png");
        backButton = new Texture("back.png");

        bitFont = new BitmapFont();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.3f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //System.out.println("HELLLLLLLLLLO");

        game.batch.begin();
        bitFont.getData().setScale(10);
        bitFont.draw(game.batch, "Join or Host a Game?", 525, 1300);
        game.batch.draw(joinBtn, width/2 - joinBtn.getWidth()/2, 700);
        game.batch.draw(hostBtn, width/2 - hostBtn.getWidth()/2, 300);
        game.batch.draw(backButton, 10, height - backButton.getHeight());
        game.batch.end();



        if(Gdx.input.justTouched()){
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            System.out.println(String.valueOf(touchPos.x) + String.valueOf(touchPos.y));

            clickInBack(touchPos);

            if (inJoinButton(touchPos)) {
                game.setScreen(new JoinScreen(game));
                dispose();
            }
            else if(inHostButton(touchPos)){
                game.setScreen(new HostScreen(game));
                dispose();
            }
        }

    }

    private void clickInBack(Vector3 touchPos) {
        if (touchPos.x > 10 && touchPos.x < backButton.getWidth()
                && (height - touchPos.y) > (height - backButton.getHeight()) && (height - touchPos.y) < height) {
            game.setScreen(new MainMenu(game));
            dispose();
        }
    }

    private boolean inJoinButton(Vector3 touchPos) {
        if (touchPos.x > (width/2 - joinBtn.getWidth()/2) && touchPos.x < (width/2 + joinBtn.getWidth()/2)
                && touchPos.y > (height - (700 + joinBtn.getHeight())) && touchPos.y < (height - 700)) {
            return true;
        }
        return false;
    }

    private boolean inHostButton(Vector3 touchPos) {
        if (touchPos.x > (width/2 - hostBtn.getWidth()/2) && touchPos.x < (width/2 + hostBtn.getWidth()/2)
                && touchPos.y > (height - (300 + hostBtn.getHeight())) && touchPos.y < (height - 300)) {
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
        joinBtn.dispose();
        hostBtn.dispose();
    }
}
