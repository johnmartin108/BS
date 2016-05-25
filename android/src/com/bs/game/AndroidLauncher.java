package com.bs.game;

import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Callbacks.SalutDeviceCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;

import java.util.ArrayList;
import java.util.List;


public class AndroidLauncher extends AndroidApplication implements SalutDataCallback{
	public static CommunicationBridge bridge;
	private Salut network;
	public static String playerName;
	public static boolean isHost;

	private static final String TAG = "BSGAME";

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bridge = new CommunicationBridge();
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
                        becomeHost();
                        isHost = true;
                        break;

					default:
						break;
				}
			}
		};

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new BSGame(bridge), config);
	}

    public void startNetwork(){
        //establish connection
        SalutDataReceiver dataReceiver = new SalutDataReceiver(this, this);
        SalutServiceData serviceData = new SalutServiceData(Constants.N_SERVICE_NAME, 50489, playerName);
        network = new Salut(dataReceiver, serviceData, new SalutCallback() {
            @Override
            public void call() {
                Log.e(TAG, "Sorry, but this device does not support WiFi Direct.");
            }
        });
    }

    public void connectWithHost(SalutDevice host){
        network.registerWithHost(host, new SalutCallback() {
            @Override
            public void call() {
                // success on connection
            }
        }, new SalutCallback() {
            @Override
            public void call() {
                // failure to connect
            }
        });

    }

	public void discoverServices(){
		network.discoverWithTimeout(new SalutCallback() {
			@Override
			public void call() {
				Log.d(TAG, "Look at all these devices! " + network.foundDevices.toString());

                List peerlist = new ArrayList<String>();
                for (SalutDevice dev: network.foundDevices){
                    peerlist.add(dev.deviceName);
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

	public void becomeHost(){
		network.startNetworkService(new SalutDeviceCallback() {
			@Override
			public void call(SalutDevice device) {
				Log.d(TAG, device.readableName + " has connected!");
			}
		});

	}

	@Override
	public void onDataReceived(Object o) {

	}
}


