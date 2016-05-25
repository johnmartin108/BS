package com.bs.game;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import com.bs.game.BSGame;
import com.bs.game.communication.WifiDirectBroadcastReceiver;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AndroidLauncher extends AndroidApplication {
	public static CommunicationBridge bridge;
	WifiP2pManager mManager;
	WifiP2pManager.Channel mChannel;
	WifiDirectBroadcastReceiver mReceiver;
	IntentFilter mIntentFilter;

	private void connectTo(int deviceInd){
		WifiP2pDevice hostDevice = (WifiP2pDevice) mReceiver.peers.get(deviceInd);
		final WifiP2pConfig config = new WifiP2pConfig();
		config.deviceAddress = hostDevice.deviceAddress;
		config.wps.setup = WpsInfo.PBC;

		mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

			@Override
			public void onSuccess() {
				// WiFiDirectBroadcastReceiver will notify us. Ignore for now.
				Log.d("BSGAME", "connection success to "+config.toString());
			}

			@Override
			public void onFailure(int reason) {
				Log.d("BSGAME", "connection failure");
			}
		});
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bridge = new CommunicationBridge();
		bridge.controller = new CommunicationCallBack(){
			@Override
			public void onReceivedData(int name, Object obj) {
				super.onReceivedData(name, obj);
				switch (name){
					case Constants.M_CONNECT_TO:
						int index = (Integer)obj;
						connectTo(index);
						break;
					default:
						break;
				}
			}
		};

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new BSGame(bridge), config);

		mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mManager.initialize(this, getMainLooper(), null);
		becomeHost();

		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
	}

	protected void joinHost(){

	}

	protected void becomeHost(){
		try {
			mManager = (WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE);
			mChannel = mManager.initialize(this, getMainLooper(), new WifiP2pManager.ChannelListener() {
				@Override
				public void onChannelDisconnected() {
					mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
				}
			});
			Class[] paramTypes = new Class[3];
			paramTypes[0] = WifiP2pManager.Channel.class;
			paramTypes[1] = String.class;
			paramTypes[2] = WifiP2pManager.ActionListener.class;
			Method setDeviceName = mManager.getClass().getMethod("setDeviceName", paramTypes);
			setDeviceName.setAccessible(true);

			Object arglist[] = new Object[3];
			arglist[0] = mChannel;
			arglist[1] = "BS Host";
			arglist[2] = new WifiP2pManager.ActionListener() {

				@Override
				public void onSuccess() {
					Log.d("setDeviceName succeeded", "true");
				}

				@Override
				public void onFailure(int reason) {
					Log.d("setDeviceName failed", "true");
				}
			};
			setDeviceName.invoke(mManager, arglist);

		}catch (Exception e){
			Log.d("BSGAME", e.toString());
		}
	}

	/* register the broadcast receiver with the intent values to be matched */
	@Override
	protected void onResume() {
		super.onResume();
		mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, this);
		registerReceiver(mReceiver, mIntentFilter);
	}
	/* unregister the broadcast receiver */
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
	}
}


