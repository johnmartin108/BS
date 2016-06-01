package com.bs.game;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.bluelinelabs.logansquare.LoganSquare;
import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Callbacks.SalutDeviceCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class AndroidLauncher extends AndroidApplication implements SalutDataCallback{
	public static CommunicationBridge bridge;
	private Salut network;
	public static String playerName;
	public static boolean isHost;
    public static boolean hasStarted;

    WifiManager wifiManager;
    List peerlist = new ArrayList<String>();
    public static String hostStatus = "";

    //game state variables
    private ArrayList<ArrayList<Card>> hands;
    private ArrayList<String> player_names = new ArrayList<String>();

    private ArrayList<Card> cardPile;
    private ArrayList<Card> last_play;
    private int num_players = 0;
    private int curr_player = 0;
    private int prev_player = 0;
    private int targetRank = 0;
    private boolean emptyHand = false;

	private static final String TAG = "BSGAME";

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

	//handling incoming messages
		bridge = new CommunicationBridge();
        initialize(new BSGame(bridge), config);

		bridge.controller = new CommunicationCallBack(){
			@Override
			public void onReceivedData(int name, Object obj) {
				super.onReceivedData(name, obj);
                		Message m;
				switch (name){

		    //communications setup messages
                    case Constants.M_SET_NAME:
                        playerName = (String)obj;
                        break;

					case Constants.M_CONNECT_TO:
						int index = (Integer)obj;
                        SalutDevice host = network.foundDevices.get(index);
                        connectWithHost(host);

                        isHost = false;
						break;

                    case Constants.M_DISCOVER_PEERS:
                        discoverServices();
                        break;

                    case Constants.M_BECOME_HOST:
                        startHost();
                        break;

                    //Game flow
                    case (Constants.M_START_GAME):
                    	//start the game if you're host
                        if (isHost) {
                            num_players = 1;
                            if (network != null) {
                                num_players += network.registeredClients.size();
                            }

		 	    //every device needs to know the number of players
                            Message nm = new Message();
                            nm.eventType = Constants.M_NUM_PLAYERS;
                            nm.numPlayers = num_players;
                            network.sendToAllDevices(nm, new SalutCallback() {
                                @Override
                                public void call() {

                                }
                            });

                            bridge.sendDataToView(Constants.M_NUM_PLAYERS, num_players);


                            startGame();
                        }
                        break;
                        
                    //handling BS calls
                    case (Constants.M_CALL_BS):
                    	
                    	//if you're not the host, just relay the information to the host
                        if (!isHost) {
                            m = new Message();
                            m.eventType = Constants.M_CALL_BS;
                            network.sendToHost(m, new SalutCallback() {
                                @Override
                                public void call() {

                                }
                            });
                            break;
                        }
                        else {
                            handleBSCall();
                            break;
                        }

		    //handle requests to play cards
                    case Constants.M_PLAY_CARDS:
                        ArrayList<Card> playedCards = (ArrayList<Card>) obj;
			
			//if you're not the host, just request the move
                        if (!isHost) {
                            m = new Message();
                            m.eventType = Constants.M_PLAY_CARDS;
                            m.playedCards = Card.toCardsDump(playedCards);
                            network.sendToHost(m, new SalutCallback() {
                                @Override
                                public void call() {

                                }
                            });
                        }
                        
                        //on the host side, compute the new hands, and send a message
                        //to other devices updating them accordingly
                        else {
                            //compute remaining cards
                            ArrayList<Card> newHand = new ArrayList<Card>();
                            boolean add;
                            for (Card c: hands.get(curr_player)) {
                                add = true;
                                for (Card d: playedCards) {
                                    if (c.valueOf() == d.valueOf() && c.suitOf().equals(d.suitOf())) {
                                        add = false;
                                        break;
                                    }
                                }
                                if (add) newHand.add(c);
                            }
                            
			    //new game state is triggered by an empty hand
                            if (newHand.isEmpty()) {
                                emptyHand = true;
                            }

                            hands.set(curr_player, newHand);
                            cardPile.addAll(playedCards);
                            last_play = playedCards;
                            nextTurn();
                        }
                        break;

					default:
						break;
				}
			}
		};
    }
    
    //network setup
    public void startHost(){
        isHost = true;
        wifiManager.setWifiEnabled(false);
        wifiManager.setWifiEnabled(true);

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SupplicantState supState;
                        do {
                            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                            supState = wifiInfo.getSupplicantState();
                            if (supState.toString()!=hostStatus){
                                hostStatus = supState.toString();
                                bridge.sendDataToView(Constants.M_HOST_STATUS, hostStatus);
                            }
                        }while (supState != SupplicantState.COMPLETED);
                        becomeHost();
                    }
                }, 2000);
            }
        });
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(5);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    //more network setup - just uses Salut callbacks
    public void startNetwork(){
        //establish connection
        SalutDataReceiver dataReceiver = new SalutDataReceiver(this, this);
        SalutServiceData serviceData = new SalutServiceData(Constants.N_SERVICE_NAME, 50487, playerName);
        network = new Salut(dataReceiver, serviceData, new SalutCallback() {
            @Override
            public void call() {
                Log.e(TAG, "Sorry, but this device does not support WiFi Direct.");
            }
        });
        Toast.makeText(this.getBaseContext(), (CharSequence)"network started", Toast.LENGTH_LONG).show();

    }

    public void connectWithHost(SalutDevice host){
        final SalutDevice finalHost = host;
        Log.d(TAG, "Attempting to connect with host "+host.instanceName);
        final ScheduledExecutorService worker =
                Executors.newSingleThreadScheduledExecutor();
        network.registerWithHost(host, new SalutCallback() {
            @Override
            public void call() {
                // success on connection
                Log.d(TAG, "connection success.");
                bridge.sendDataToView(Constants.M_CONNECTION_STATUS, true);
                Toast.makeText(getBaseContext(), (CharSequence)"Connected with "+finalHost.readableName, Toast.LENGTH_LONG).show();
            }
        }, new SalutCallback() {
            @Override
            public void call() {
                // failure to connect
                Log.e(TAG, "Oh no! FAILED TO CONNECT TO HOST:"+finalHost.instanceName);
            }
        });
    }

	public void discoverServices(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startNetwork();
                network.discoverWithTimeout(new SalutCallback() {
                    @Override
                    public void call() {
                        Log.d(TAG, "Look at all these devices! " + network.foundDevices.toString());

                        peerlist.clear();
                        for (SalutDevice dev: network.foundDevices){
                            peerlist.add(dev.readableName);
                        }

                        // update the ui here
                        bridge.sendDataToView(Constants.M_PEER_LIST, peerlist);
                    }

                }, new SalutCallback() {
                    @Override
                    public void call() {
                        Log.d(TAG, "Bummer, we didn't find anyone. ");
                    }
                }, 5000);
            }
        });

	}

	public void becomeHost(){
        if (!hasStarted){
            isHost = true;
            startNetwork();
            network.startNetworkService(new SalutDeviceCallback() {
                @Override
                public void call(SalutDevice device) {
                    Log.d(TAG, device.readableName + " has connected!");
                    bridge.sendDataToView(Constants.M_DEVICE_CONNECTED, network.getReadableRegisteredNames());
                    Toast.makeText(getBaseContext(), (CharSequence) device.readableName + " has connected!", Toast.LENGTH_LONG).show();

//                    Message myMessage = new Message();
//                    myMessage.message = "connected your name is "+device.instanceName;
//                    network.sendToDevice(device, myMessage, new SalutCallback() {
//                        @Override
//                        public void call() {
//                            Log.e(TAG, "Oh no! The data failed to send.");
//                        }
//                    });

                }
            }, new SalutCallback() {
                @Override
                public void call() {
                    Toast.makeText(getBaseContext(), (CharSequence)" Host service created!", Toast.LENGTH_LONG).show();

                }
            }, new SalutCallback() {
                @Override
                public void call() {
                    Toast.makeText(getBaseContext(), (CharSequence) " Host service failed to create!", Toast.LENGTH_LONG).show();

                }
            });

            Toast.makeText(this.getBaseContext(), (CharSequence)"you're host now", Toast.LENGTH_LONG).show();

        }
    }

	//handle incoming messages - non-host side
	//this is when the device receives a message from another device
	//the message handling above is for when the device receives a message from another process on the same device
	@Override
	public void onDataReceived(Object data) {
        Log.d(TAG, "Received network data.");
        try
        {
            String str = (String) data;
            Message newMessage = LoganSquare.parse(str, Message.class);

            switch (newMessage.eventType) {

                //non-host messages just get forwarded to the respective views
                case Constants.M_PLAYER_ID:
                    bridge.sendDataToView(newMessage.eventType, newMessage.PlayerID);
                    break;
                case Constants.M_GAME_OVER:
                    bridge.sendDataToView(newMessage.eventType, newMessage.PlayerID);
                    break;
                case Constants.M_NUM_PLAYERS:
                    bridge.sendDataToView(newMessage.eventType, newMessage.numPlayers);
                    break;
                case Constants.M_GAME_START:
                    bridge.sendDataToView(Constants.M_PLAYER_NAMES, newMessage.playerNames);
                    bridge.sendDataToView(Constants.M_HANDS, Card.fromHandsDump(newMessage.cardsInHands));
                    bridge.sendDataToView(Constants.M_CURRENT_PLAYER, newMessage.currentPlayerTurn);
                    bridge.sendDataToView(Constants.M_GAME_START, null);
                    break;
                case Constants.M_PLAYER_BS_CORRECT:
                    bridge.sendDataToView(Constants.M_HANDS, Card.fromHandsDump(newMessage.cardsInHands));
                    bridge.sendDataToView(Constants.M_CURRENT_PLAYER, newMessage.CallerID);
                    bridge.sendDataToView(Constants.M_PREV_PLAYER, newMessage.PlayerID);
                    bridge.sendDataToView(Constants.M_PLAYER_BS_CORRECT, null);
                    break;
                case Constants.M_PLAYER_BS_INCORRECT:
                    bridge.sendDataToView(Constants.M_HANDS, Card.fromHandsDump(newMessage.cardsInHands));
                    bridge.sendDataToView(Constants.M_CURRENT_PLAYER, newMessage.CallerID);
                    bridge.sendDataToView(Constants.M_PREV_PLAYER, newMessage.PlayerID);
                    bridge.sendDataToView(Constants.M_PLAYER_BS_INCORRECT, null);
                    break;
                case Constants.M_PLAYER_TURN:
                    bridge.sendDataToView(Constants.M_HANDS, Card.fromHandsDump(newMessage.cardsInHands));
                    bridge.sendDataToView(Constants.M_CURRENT_PLAYER, newMessage.PlayerID);
                    bridge.sendDataToView(Constants.M_PREV_PLAYER, newMessage.CallerID);
                    bridge.sendDataToView(Constants.M_CARD_PILE, Card.fromCardsDump(newMessage.cardPile));
                    bridge.sendDataToView(Constants.M_LAST_PLAY, Card.fromCardsDump(newMessage.prevPlay));
                    bridge.sendDataToView(Constants.M_PLAYER_TURN, null);
                    break;
                case Constants.M_PLAYER_TURN_START:
                    bridge.sendDataToView(Constants.M_HANDS, Card.fromHandsDump(newMessage.cardsInHands));
                    bridge.sendDataToView(Constants.M_CURRENT_PLAYER, newMessage.PlayerID);
                    bridge.sendDataToView(Constants.M_CARD_PILE, Card.fromCardsDump(newMessage.cardPile));
                    bridge.sendDataToView(Constants.M_TARGET_RANK, newMessage.targetRank);
                    bridge.sendDataToView(Constants.M_PLAYER_TURN_START, null);
                    break;

                //***** HOST OPERATIONS *****
                case (Constants.M_PLAY_CARDS):
                    
                    //handling card plays is the same as it was above for the host case
                    ArrayList<Card> playedCards = Card.fromCardsDump(newMessage.playedCards);
                    ArrayList<Card> newHand = new ArrayList<Card>();

                    boolean add;
                    for (Card c: hands.get(curr_player)) {
                        add = true;
                        for (Card d: playedCards) {
                            if (c.valueOf() == d.valueOf() && c.suitOf().equals(d.suitOf())) {
                                add = false;
                                break;
                            }
                        }
                        if (add) newHand.add(c);
                    }

                    if (newHand.isEmpty()) {
                        emptyHand = true;
                    }

                    hands.set(curr_player, newHand);
                    cardPile.addAll(playedCards);
                    last_play = playedCards;

                    nextTurn();
                    break;

                case (Constants.M_CALL_BS):
                    handleBSCall();

                    break;

                default:
                    break;

            }
        }
        catch (IOException ex)
        {
            Log.e(TAG, "Failed to parse network data.");
        }
    }

    //helper function to handle start-of-game operations
    public void startGame() {
        int ID = 0;
        hands = new ArrayList<ArrayList<Card>>();
        Deck d = new Deck();
        Card c;
        cardPile = new ArrayList<Card>();
        targetRank = 1;
        player_names = new ArrayList<String>();

	//deal the cards
        while ((c = d.nextCard()) != null) {
            if (hands.size() <= ID) {
                hands.add(new ArrayList<Card>());
            }
            
            //whoever gets the ace of spades takes the first turn
            if (c.valueOf() == 1 && c.suitOf().equals("s")) {
                curr_player = ID;
            }
            
            //this is obvi unnecessarily complicated - just testing serialization
            hands.get(ID).add(new Card(c.toSerializable()));
            ID = (ID + 1) % num_players;

        }


        Message m;
        ID = 0;
        player_names.add(playerName);
        bridge.sendDataToView(Constants.M_PLAYER_ID, ID++);

	//send every player a unique ID - this is how the game knows whose turn it is
	//and who played last
        if (network != null) {
            for (SalutDevice device : network.registeredClients) {
                m = new Message();
                player_names.add(device.readableName);
                m.PlayerID = ID++;
                m.eventType = Constants.M_PLAYER_ID;
                network.sendToDevice(device, m, new SalutCallback() {
                    @Override
                    public void call() {

                    }
                });
            }

            try {Thread.sleep(500);} catch (InterruptedException e) {}

	    //tell everyone the start state of the game - hands, who goes first, etc.
            m = new Message();
            m.eventType = Constants.M_GAME_START;
            m.cardsInHands = Card.toHandsDump(hands);
            m.currentPlayerTurn = curr_player;
            m.playerNames = player_names;
            network.sendToAllDevices(m, new SalutCallback() {
                @Override
                public void call() {

                }
            });
        }

        bridge.sendDataToView(Constants.M_PLAYER_NAMES, player_names);
        bridge.sendDataToView(Constants.M_HANDS, hands);
        bridge.sendDataToView(Constants.M_CURRENT_PLAYER, curr_player);
        bridge.sendDataToView(Constants.M_GAME_START, null);

        try {
            Thread.sleep(200);
        }
        catch (InterruptedException e) {}

    }

    //move on to the next player (if BS wasn't called on the last play)
    public void nextTurn() {
        if (network != null) {
        	
            //update game state and communicate changes
            Message m = new Message();
            m.eventType = Constants.M_PLAYER_TURN;
            prev_player = curr_player;
            curr_player = (curr_player + 1) % num_players;
            m.cardPile = Card.toCardsDump(cardPile);
            m.prevPlay = Card.toCardsDump(last_play);
            m.cardsInHands = Card.toHandsDump(hands);
            m.PlayerID = curr_player;
            m.CallerID = prev_player;
            network.sendToAllDevices(m, new SalutCallback() {
                @Override
                public void call() {

                }
            });
        }

        //host needs data too
        bridge.sendDataToView(Constants.M_HANDS, hands);
        bridge.sendDataToView(Constants.M_CURRENT_PLAYER, curr_player);
        bridge.sendDataToView(Constants.M_PREV_PLAYER, prev_player);
        bridge.sendDataToView(Constants.M_CARD_PILE, cardPile);
        bridge.sendDataToView(Constants.M_LAST_PLAY, last_play);
        bridge.sendDataToView(Constants.M_PLAYER_TURN, null);
    }

    //start a new turn (if BS was called last time)
    public void newTurn() {
        if (network != null) {
            Message m = new Message();
            m.eventType = Constants.M_PLAYER_TURN_START;
            m.cardPile = Card.toCardsDump(cardPile);
            m.cardsInHands = Card.toHandsDump(hands);
            m.PlayerID = curr_player;
            targetRank = (targetRank % 13) + 1;  //13 -> 1 -> 2 ...
            m.targetRank = targetRank;
            network.sendToAllDevices(m, new SalutCallback() {
                @Override
                public void call() {

                }
            });
        }

        //send same message to host
        bridge.sendDataToView(Constants.M_HANDS, hands);
        bridge.sendDataToView(Constants.M_CURRENT_PLAYER, curr_player);
        bridge.sendDataToView(Constants.M_TARGET_RANK, targetRank);
        bridge.sendDataToView(Constants.M_CARD_PILE, cardPile);
        bridge.sendDataToView(Constants.M_PLAYER_TURN_START, null);
    }

    public void handleBSCall() {
    	
    	//was the BS call correct?
        boolean telling_truth = true;
        for (Card c: last_play) {
            if (c.valueOf() != targetRank) {
                telling_truth = false;
            }
        }

        if (telling_truth) {
            //if a player truthfully played their last card, the game is over
            if (emptyHand) {
                endGame();
                //wait for UI to catch up
                try {Thread.sleep(200);} catch (InterruptedException e) {}
            }
            
            //update the players on the result of the BS call
            hands.get(curr_player).addAll(cardPile);
            Message m = new Message();
            m.eventType = Constants.M_PLAYER_BS_INCORRECT;
            m.CallerID = curr_player;
            m.PlayerID = prev_player;
            m.cardsInHands = Card.toHandsDump(hands);
            network.sendToAllDevices(m, new SalutCallback() {
                @Override
                public void call() {

                }
            });

            bridge.sendDataToView(Constants.M_HANDS, hands);
            bridge.sendDataToView(Constants.M_CURRENT_PLAYER, curr_player);
            bridge.sendDataToView(Constants.M_PREV_PLAYER, prev_player);
            bridge.sendDataToView(Constants.M_PLAYER_BS_INCORRECT, null);
        }

        else {
            //same process for correct BS calls - the cards just go to the previous player and not the current one
            emptyHand = false;
            hands.get(prev_player).addAll(cardPile);
            Message m = new Message();
            m.eventType = Constants.M_PLAYER_BS_CORRECT;
            m.CallerID = curr_player;
            m.PlayerID = prev_player;
            m.cardsInHands = Card.toHandsDump(hands);
            network.sendToAllDevices(m, new SalutCallback() {
                @Override
                public void call() {

                }
            });

            //send info to host as well
            bridge.sendDataToView(Constants.M_HANDS, hands);
            bridge.sendDataToView(Constants.M_CURRENT_PLAYER, curr_player);
            bridge.sendDataToView(Constants.M_PREV_PLAYER, prev_player);
            bridge.sendDataToView(Constants.M_PLAYER_BS_CORRECT, null);
        }

        try {
            Thread.sleep(200);
        }
        catch (InterruptedException e) {}


        if (!emptyHand) {
            cardPile.clear();
            newTurn();
        }
    }

    //protocol for finishing the game
    public void endGame() {
        if (network != null) {
            Message m = new Message();
            m.eventType = Constants.M_GAME_OVER;
            m.PlayerID = prev_player;
            network.sendToAllDevices(m, new SalutCallback() {
                @Override
                public void call() {

                }
            });
        }

        bridge.sendDataToView(Constants.M_GAME_OVER, prev_player);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(isHost&&network!=null) {
            network.stopNetworkService(true);
        }else if (network!=null){
            network.unregisterClient(false);
        }
    }
}


