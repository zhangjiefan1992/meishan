package com.shandiangou.sdgprotocol.lib;

import com.shandiangou.sdgprotocol.lib.protocol.DataProtocol;

/**
 * Created by linwenbin on 16/11/17.
 */
public interface ResponseCallback {

    void targetIsOffline(DataProtocol reciveMsg);

    void targetIsOnline(String clientIp);
}
