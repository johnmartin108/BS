package com.bs.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.List;


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
	boolean gameFinished = false;
	boolean rightBSCall = false;
	boolean wrongBSCall = false;

	ArrayList<ArrayList<Card>> hands;
	ArrayList<Card> cardPile = new ArrayList<Card>();
	ArrayList<Card> lastPlay = new ArrayList<Card>();
	ArrayList<String> player_names;
	int num_players;
	int curr_player;
	int prev_player;
	int ID;
	int targetRank = 1;
	int gameWinner;



	public BSGame(CommunicationBridge b){
		bridge = b;
	}


	@Override
	public void create() {
		batch = new SpriteBatch();
		stage = new Stage();

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arial.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 150;
		generator.scaleForPixelHeight(150);
		parameter.minFilter = Texture.TextureFilter.Nearest;
		parameter.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		parameter.kerning = true;
		font = generator.generateFont(parameter);
		generator.dispose();

		font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		backButton = new Texture("back.png");
		goButton = new Texture("go.png");

		prefs = Gdx.app.getPreferences(Constants.P_PREF_NAME);

		setScreen(new MainMenu(this));

//		setScreen(new PlayScreen(this));
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
						break;

					case Constants.M_CONNECT_TO_HOST:
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
					case Constants.M_NUM_PLAYERS:
						num_players = (Integer) obj;
						break;
					case Constants.M_PLAYER_NAMES:
						player_names = (ArrayList<String>) obj;
						break;

					//***** GAME FLOW MESSAGES *****
					case Constants.M_GAME_START:
						isGameStarted = true;
						break;
					case Constants.M_GAME_OVER:
						gameWinner = (Integer) obj;
						gameFinished = true;
						break;
					case Constants.M_PLAYER_BS_CORRECT:
						cardPile.clear();
						lastPlay.clear();
						rightBSCall = true;
						break;
					case Constants.M_PLAYER_BS_INCORRECT:
						cardPile.clear();
						lastPlay.clear();
						wrongBSCall = true;
						break;
					case Constants.M_PLAYER_TURN:
						break;
					case Constants.M_PLAYER_TURN_START:
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
