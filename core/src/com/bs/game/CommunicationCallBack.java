package com.bs.game;

/**
 * Created by ziyuanliu on 5/24/16.
 */
interface CallBack {
    public void onReceivedData(int name, Object obj);
}

class CommunicationCallBack implements CallBack{
    public void onReceivedData(int name, Object obj){
    }
}