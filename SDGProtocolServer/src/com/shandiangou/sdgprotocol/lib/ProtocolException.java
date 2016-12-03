package com.shandiangou.sdgprotocol.lib;

/**
 * Created by linwb on 16/11/15.
 */
public class ProtocolException extends Exception {

    public ProtocolException(String detailMessage) {
        super(detailMessage);
    }

    public ProtocolException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ProtocolException(Throwable throwable) {
        super(throwable);
    }
}
