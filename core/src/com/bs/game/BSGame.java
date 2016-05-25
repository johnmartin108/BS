package com.bs.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BSGame extends Game {
	SpriteBatch batch;
	Texture img;
	static CommunicationBridge bridge;
	ArrayList peerlist = new ArrayList();
	ArrayList buttons = new ArrayList();

	Stage stage;

	BitmapFont font;
	String status = "";

	TextButton.TextButtonStyle textButtonStyle;

	public BSGame(CommunicationBridge b){
		bridge = b;
	}


	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		font = new BitmapFont();
		stage = new Stage();

		font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		font.getData().scale(4.0f);

		textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.font = font;

		final BSGame self = this;

		bridge.view = new CommunicationCallBack(){
			@Override
			public void onReceivedData(int name, Object obj) {
				super.onReceivedData(name, obj);
				switch (name){
					case Constants.M_PEER_LIST:
						peerlist = (ArrayList)obj;
						System.out.println(peerlist);
						status = "devices are "+peerlist.toString();

						buttons.clear();
						for (int i = 0; i < peerlist.size(); i ++){
							final String btnName = (String)peerlist.get(i);
							TextButton newBtn = new TextButton(btnName, textButtonStyle);
							newBtn.setPosition(400, 500+100*i);
							newBtn.addListener(new ClickListener(){
								@Override
								public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
									super.touchUp(event, x, y, pointer, button);
									self.connectTo(btnName);
								}
							});
							buttons.add(newBtn);
						}

						break;

					case Constants.M_CONNECT_TO_HOST:
						boolean isConnected = (Boolean) obj;

						if (isConnected){
							status = "Connected to host";
						}else{
							status = "Disconnected to host";
						}
					default:
						break;
				}

			}
		};

		loadScreenDefault();
	}

	public void connectTo(String devName){
		int index = peerlist.indexOf(devName);
		if (index!=-1){
			this.bridge.sendDataToController(Constants.M_CONNECT_TO, index);
		}
	}

	public void loadScreenDefault(){
		stage.clear();
		TextButton hostBtn = new TextButton("Become Host", textButtonStyle);
		hostBtn.setPosition(400, 500+100);
		hostBtn.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);

			}
		});

		TextButton joinBtn = new TextButton("Join Host", textButtonStyle);
		joinBtn.setPosition(400, 500+200);
		joinBtn.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);

			}
		});

		stage.addActor(hostBtn);
		stage.addActor(joinBtn);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.5f, 0.32f, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.input.setInputProcessor(stage);

		batch.begin();

		batch.draw(img, 0, 0);
		batch.end();

		stage.draw();


//
		if(Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
//			System.out.println(Gdx.input.getX()+" "+Gdx.input.getY());
		}
	}

}
