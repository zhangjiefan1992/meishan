package com.shandiangou.sdgprotocol.lib.demo;

import com.shandiangou.sdgprotocol.lib.Config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by linwenbin on 16/11/28.
 */
public class Server {

    private int port = Config.PORT;
    private ServerSocket serverSocket;

    public Server() throws Exception {
        serverSocket = new ServerSocket(port, 3);//显式设置连接请求队列的长度为3
        System.out.println("服务器启动!");
    }

    public void service() {
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                System.out.println("New connection accepted " + socket.getInetAddress() + ":" + socket.getPort());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        Thread.sleep(10000);
        server.service();
    }
}
