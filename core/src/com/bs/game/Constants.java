package com.bs.game;

/**
 * Created by ziyuanliu on 5/24/16.
 */
public class Constants {
    public static final String N_SERVICE_NAME = "_BSH_";
    public static final String P_PREF_NAME = "My Preferences";
    public static final String P_PEERS_UPDATED = "P_PEERS_UPDATED";

    //setup messages
    public static final int M_SET_NAME = 0;
    public static final int M_DISCOVER_PEERS = 1;
    public static final int M_PEER_LIST = 2;
    public static final int M_CONNECT_TO = 3;
    public static final int M_CONNECT_TO_HOST = 4;
    public static final int M_BECOME_HOST = 5;
    public static final int M_SET_DIFF = 6;
    public static final int M_START_GAME = 7;
    public static final int M_HOST_STATUS = 8;
    public static final int M_DEVICE_CONNECTED = 9;
    public static final int M_CONNECTION_STATUS = 10;

    //messages from host
    public static final int M_PLAYER_TURN = 16;
    public static final int M_PLAYER_TURN_START = 11;
    public static final int M_PLAYER_BS_INCORRECT = 12;
    public static final int M_PLAYER_BS_CORRECT = 13;
    public static final int M_GAME_OVER = 14;
    public static final int M_GAME_START = 15;

    //messages to host
    public static final int M_CALL_BS = 20;
    public static final int M_PLAY_CARDS = 21;

    //variable updatem essages
    public static final int M_HANDS = 30;
    public static final int M_CURRENT_PLAYER = 31;
    public static final int M_PREV_PLAYER = 32;
    public static final int M_TARGET_RANK = 33;
    public static final int M_CARD_PILE = 34;
    public static final int M_PLAYER_ID = 35;
    public static final int M_LAST_PLAY = 36;
    public static final int M_NUM_PLAYERS = 37;
    public static final int M_PLAYER_NAMES = 38;



}
