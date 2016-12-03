package com.shandiangou.sdgprotocol.lib.demo;

import com.shandiangou.sdgprotocol.lib.Config;

import javax.net.SocketFactory;
import java.net.ConnectException;
import java.net.Socket;

/**
 * Created by linwenbin on 16/11/28.
 */
public class Client {

    public static void main(String[] args) throws Exception {

        boolean isConnected;
        String host = "127.0.0.1";
        int port = Config.PORT;
        Socket socket = null;
        try {
            socket = SocketFactory.getDefault().createSocket(host, port);
            isConnected = true;
            System.out.println("连接成功！");
        } catch (ConnectException e) {
            isConnected = false;
            e.printStackTrace();
            System.out.println("连接失败！");
        }

        if (!isConnected) {
            return;
        }

        Thread.sleep(5000);

        socket.close();
        System.out.println("断开连接！");
    }
}
