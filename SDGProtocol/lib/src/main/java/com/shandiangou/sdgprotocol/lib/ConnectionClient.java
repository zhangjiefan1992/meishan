package com.shandiangou.sdgprotocol.lib;

import com.shandiangou.sdgprotocol.lib.protocol.DataProtocol;

/**
 * Created by linwb on 16/11/15.
 */
public class ConnectionClient {

    private boolean isClosed;

    private ClientRequestTask mClientRequestTask;

    public ConnectionClient(RequestCallBack requestCallBack) {
        mClientRequestTask = new ClientRequestTask(requestCallBack);
        new Thread(mClientRequestTask).start();
    }

    public void addNewRequest(DataProtocol data) {
        if (mClientRequestTask != null && !isClosed)
            mClientRequestTask.addRequest(data);
    }

    public void closeConnect() {
        isClosed = true;
        mClientRequestTask.stop();
    }
}
