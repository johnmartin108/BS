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
import java.util.List;

import javax.xml.soap.Text;

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
	Texture backButton;
	Texture goButton;
	boolean isConnected;
	boolean isGameStarted = false;

	ArrayList<ArrayList<Card>> hands;
	ArrayList<Card> cardPile;
	ArrayList<Card> lastPlay;
	int curr_player;
	int prev_player;
	int ID;
	int targetRank;



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

		backButton = new Texture("back.png");
		goButton = new Texture("go.png");

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
//						boolean isConnected = (Boolean) obj;
//
//						if (isConnected){
//							status = "Connected to host";
//						}else{
//							status = "Disconnected to host";
//						}
						break;

					case Constants.M_HOST_STATUS:
						hostStatus = (String)obj;
						break;

					case Constants.M_DEVICE_CONNECTED:
						peerlist = (ArrayList<String>) obj;
						break;

					case Constants.M_CONNECTION_STATUS:
						isConnected = (Boolean)obj;
						break;
					default:
						break;

					case Constants.M_PLAYER_ID:
						ID = (Integer) obj;
						break;
					case Constants.M_HANDS:
						hands = (ArrayList<ArrayList<Card>>) obj;
						break;
					case Constants.M_TARGET_RANK:
						targetRank = (Integer) obj;
						break;
					case Constants.M_PREV_PLAYER:
						prev_player = (Integer) obj;
						break;
					case Constants.M_CARD_PILE:
						cardPile = (ArrayList<Card>) obj;
						break;
					case Constants.M_CURRENT_PLAYER:
						curr_player = (Integer) obj;
						break;
					case Constants.M_LAST_PLAY:
						lastPlay = (ArrayList<Card>) obj;
						break;

					//***** GAME FLOW MESSAGES *****
					case Constants.M_GAME_START:
						isGameStarted = true;
						break;
					case Constants.M_GAME_OVER:
						//TODO: protocol for finishing game
						int winner = (Integer) obj;
						break;
					case Constants.M_PLAYER_BS_CORRECT:
						//TODO: protocol for handling game events like incorrect BS calls
						cardPile = new ArrayList<Card>();
						break;
					case Constants.M_PLAYER_BS_INCORRECT:
						//TODO: protocol for handling incorrect BS - check variables curr_player and prev_player
						cardPile = new ArrayList<Card>();
						break;
					case Constants.M_PLAYER_TURN:
						if (ID == curr_player) {
							//do a turn
						}
						else {
							//display waiting screen
						}
						break;
					case Constants.M_PLAYER_TURN_START:
						//same as above, but there should be no option to call BS
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

	//TODO: need UI people to fill this in. uncomment and make screens show up accordingly. I'm assuming all game functions happen in playscreen. If not we should probably talk.
	//I'm not sure how to get the message data through correctly. But if nothing else this should give you an idea of
	//what things we need the UI to be able to do.

	public void onReceivedData(int name, Object obj) {

		switch (name) {
			//***** MESSAGES UPDATING VALUES *****





		}
	}

}
