package com.bs.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

public class BSGame extends Game {
	SpriteBatch batch;
	Texture img;
	static CommunicationBridge bridge;
	ArrayList peerlist = new ArrayList();
	ArrayList buttons = new ArrayList();

    Preferences prefs;

	Stage stage;

	BitmapFont font;
	String status = "";

	TextButton.TextButtonStyle textButtonStyle;
	String hostStatus = "";

	public BSGame(CommunicationBridge b){
		bridge = b;
	}


	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		font = new BitmapFont();
		stage = new Stage();

		prefs = Gdx.app.getPreferences(Constants.P_PREF_NAME);

		setScreen(new MainMenu(this));

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

                        prefs.putBoolean(Constants.P_PEERS_UPDATED, true);

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
						break;

					case Constants.M_HOST_STATUS:
						hostStatus = (String)obj;
						break;
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
		super.render();
	}

}
