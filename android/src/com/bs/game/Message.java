package com.bs.game;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * this is a inter device message payload serializer
 * Created by ziyuanliu on 5/25/16.
 */

@JsonObject
public class Message {
    public static final int E_DECK = 0;

    @JsonField
    public String eventType;
}
