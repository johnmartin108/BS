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

    private ArrayList<ArrayList<Card>> hands;
    private Map<Integer, SalutDevice> player_devices;

    private ArrayList<Card> cardPile;
    private ArrayList<Card> last_play;
    private int num_players = 0;
    private int curr_player = 0;
    private int prev_player = 0;
    private int targetRank = 0;

	private static final String TAG = "BSGAME";

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		bridge = new CommunicationBridge();
        initialize(new BSGame(bridge), config);

		bridge.controller = new CommunicationCallBack(){
			@Override
			public void onReceivedData(int name, Object obj) {
				super.onReceivedData(name, obj);
				switch (name){

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

					default:
						break;
				}
			}
		};
    }

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
                }, 1000);
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

    public void startNetwork(){
        //establish connection
        SalutDataReceiver dataReceiver = new SalutDataReceiver(this, this);
        SalutServiceData serviceData = new SalutServiceData(Constants.N_SERVICE_NAME, 50488, playerName);
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

	@Override
	public void onDataReceived(Object data) {
        Log.d(TAG, "Received network data.");
        try
        {
            String str = (String) data;
            Message newMessage = LoganSquare.parse(str, Message.class);

            switch (newMessage.eventType) {

                //only will receive these messages if isHost
                case (Constants.M_START_GAME):
                    if (isHost) {
                        startGame();
                    }
                    break;
                case (Constants.M_CALL_BS):
                    boolean telling_truth = true;
                    for (Card c: last_play) {
                        if (c.valueOf() != targetRank) {
                            telling_truth = false;
                        }
                    }

                    if (telling_truth) {
                        if (hands.get(prev_player).isEmpty()) {
                            endGame();
                        }
                        else {
                            hands.get(curr_player).addAll(cardPile);
                            Message m = new Message();
                            m.eventType = Constants.M_PLAYER_BS_INCORRECT;
                            m.CallerID = curr_player;
                            m.PlayerID = prev_player;
                            m.cardsInHands = hands;
                            network.sendToAllDevices(m, new SalutCallback() {
                                @Override
                                public void call() {

                                }
                            });

                            bridge.sendDataToView(m.eventType, m);
                        }
                    }

                    else {
                        hands.get(prev_player).addAll(cardPile);
                        Message m = new Message();
                        m.eventType = Constants.M_PLAYER_BS_CORRECT;
                        m.CallerID = curr_player;
                        m.PlayerID = prev_player;
                        m.cardsInHands = hands;
                        network.sendToAllDevices(m, new SalutCallback() {
                            @Override
                            public void call() {

                            }
                        });

                        bridge.sendDataToView(m.eventType, m);
                    }

                    cardPile = new ArrayList<Card>();
                    newTurn();

                    break;

                case Constants.M_PLAY_CARDS:
                    hands = newMessage.cardsInHands;
                    cardPile.addAll(newMessage.playedCards);
                    last_play = newMessage.playedCards;
                    nextTurn();
                    break;

                //non-host messages just get forwarded to the respective views
                default:
                    bridge.sendDataToView(newMessage.eventType, newMessage);

            }
//            Log.d(TAG, newMessage.message);  //See you on the other side!
            //Do other stuff with data.

//            CharSequence chars = newMessage.message;
  //          Toast.makeText(this.getBaseContext(), chars, Toast.LENGTH_LONG).show();
        }
        catch (IOException ex)
        {
            Log.e(TAG, "Failed to parse network data.");
        }
	}

    public void startGame() {
        int ID = 0;
        hands = new ArrayList<ArrayList<Card>>();
        Deck d = new Deck();
        Card c;
        while ((c = d.nextCard()) != null) {
            if (hands.size() <= ID) {
                hands.add(new ArrayList<Card>());
            }
            if (c.valueOf() == 1 && c.suitOf().equals("s")) {
                curr_player = ID;
            }
            hands.get(ID).add(c);
            ID = (ID + 1) % num_players;

        }

        ID = 0;
        Message m = new Message();
        m.eventType = Constants.M_PLAYER_ID;
        m.PlayerID= ID++;
        bridge.sendDataToView(Constants.M_PLAYER_ID, m);

        for (SalutDevice device: network.registeredClients) {
            m = new Message();
            player_devices.put(ID, device);
            m.PlayerID = ID++;
            m.eventType = Constants.M_PLAYER_ID;
            network.sendToDevice(device, m, new SalutCallback() {
                @Override
                public void call() {

                }
            });
        }

        m = new Message();
        m.eventType = Constants.M_GAME_START;
        m.cardsInHands = hands;
        m.currentPlayerTurn = curr_player;
        network.sendToAllDevices(m, new SalutCallback() {
            @Override
            public void call() {

            }
        });

        newTurn();
    }

    public void nextTurn() {
        Message m = new Message();
        m.eventType = Constants.M_PLAYER_TURN;
        prev_player = curr_player;
        curr_player = (curr_player+1) % num_players;
        m.cardPile = cardPile;
        m.cardsInHands = hands;
        m.PlayerID = curr_player;
        network.sendToAllDevices(m, new SalutCallback() {
            @Override
            public void call() {

            }
        });
        bridge.sendDataToView(Constants.M_PLAYER_TURN, m);
    }

    public void newTurn() {
        Message m = new Message();
        m.eventType = Constants.M_PLAYER_TURN_START;
        m.cardPile = cardPile;
        m.cardsInHands = hands;
        m.PlayerID = curr_player;
        targetRank = (targetRank % 13) + 1;  //13 -> 1 -> 2 ...
        network.sendToAllDevices(m, new SalutCallback() {
            @Override
            public void call() {

            }
        });
        bridge.sendDataToView(Constants.M_PLAYER_TURN_START, m);
    }

    public void endGame() {
        Message m = new Message();
        m.eventType = Constants.M_GAME_OVER;
        m.PlayerID = prev_player;
        network.sendToAllDevices(m, new SalutCallback() {
            @Override
            public void call() {

            }
        });
        bridge.sendDataToView(Constants.M_GAME_OVER, m);

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


