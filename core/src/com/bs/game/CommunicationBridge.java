package com.bs.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ziyuanliu on 5/24/16.
 */



public class CommunicationBridge {
    public CommunicationCallBack view;
    public CommunicationCallBack controller;

    public void sendDataToView(int name, Object obj){
        view.onReceivedData(name, obj);
    }

    public void sendDataToController(int name, Object obj){
        controller.onReceivedData(name, obj);
    }
}
