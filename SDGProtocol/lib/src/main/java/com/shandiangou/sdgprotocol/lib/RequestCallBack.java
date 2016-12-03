package com.shandiangou.sdgprotocol.lib;


import com.shandiangou.sdgprotocol.lib.protocol.BasicProtocol;

/**
 * Created by linwb on 16/11/15.
 */
public interface RequestCallBack {

    void onSuccess(BasicProtocol msg);

    void onFailed(int errorCode, String msg);

}