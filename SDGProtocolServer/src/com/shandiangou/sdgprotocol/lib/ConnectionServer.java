package com.shandiangou.sdgprotocol.lib;

import com.shandiangou.sdgprotocol.lib.protocol.DataProtocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by linwb on 16/11/15.
 */
public class ConnectionServer {

    private static boolean isStart = true;
    private static ServerResponseTask serverResponseTask;

    public ConnectionServer() {

    }

    public static void main(String[] args) {

        ServerSocket serverSocket = null;
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            serverSocket = new ServerSocket(Config.PORT);
            while (isStart) {
                Socket socket = serverSocket.accept();
                serverResponseTask = new ServerResponseTask(socket,
                        new ResponseCallback() {

                            @Override
                            public void targetIsOffline(DataProtocol reciveMsg) {// 对方不在线，存db
                                if (reciveMsg != null) {
                                    System.out.println(reciveMsg.getData());
                                }
                            }

                            @Override
                            public void targetIsOnline(String clientIp) {
                                System.out.println(clientIp + " is onLine");
                                System.out.println("-----------------------------------------");
                            }
                        });

                if (socket.isConnected()) {
                    executorService.execute(serverResponseTask);
                }
            }

            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    isStart = false;
                    serverSocket.close();
                    if (serverSocket != null)
                        serverResponseTask.stop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /********************************test**********************************/

//        int r1 = 2;
//        int v1 = 5;
//
//        byte r = (byte) r1;
//        byte v = (byte) v1;
//        byte g = (byte) genVer(r, v, 5);
//        int r2 = parseRes(g);
//        int v2 = parseVer(g);

//        byte[] cd = genContentData();
//        int r2 = parseReserved(cd);
//        int r3 = parseContentData(cd, 0, 5);
//        int r4 = parseContentData(cd, 1, 5);
//        int v2 = parseVersion(cd);
//        int v3 = parseContentData(cd, 0, 3);
//        int v4 = parseContentData(cd, 1, 3);
//
//        System.out.println("length: " + cd.length);
//        System.out.println("r2: " + r2);
//        System.out.println("r3: " + r3);
//        System.out.println("r4: " + r4);
//        System.out.println("v3: " + v3);
//        System.out.println("v2: " + v2);
//        System.out.println("v4: " + v4);

//        byte[] a = {69, 123,'a', 'b', 'c', -128, 127};
//        System.out.println(parseMsgId(a));
//        System.out.println(parseVersion(a));
//        System.out.println(parseType(a));

//        long long1 = 2333;
//        byte[] bytesLong = longToBytes(long1); //byte转long
//        for (int i = 0; i < bytesLong.length; i++) {
//            System.out.println(i + " : " + bytesLong[i]);
//        }
//        long long2 = bytesToLong(bytesLong);   //long转byte
//        System.out.println("long2=" + long2);

//        byte[] cd = genContentData();
//        for (int i = 0; i < cd.length; i++) {
//            System.out.println(i + " : " + cd[i]);
//        }
//        System.out.println("---------------");
//        int result1 = parseLength(cd);
//        int result2 = byteArrayToInt(cd, 0, 2);
//        System.out.println("result1=" + result1);
//        System.out.println("result2=" + result2);

//        String aa = "as1728sjue7s7w0mes53b2df";
//        System.out.println(aa.getBytes().length);
//        System.out.println(aa.length());
    }

    public static int byteArrayToInt(byte[] b, int offset, int bytecount) {
        int intValue = 0;
        for (int i = offset; i < bytecount; i++) {
            intValue += (b[i] & 0xFF) << (8 * (3 - i));
        }
        return intValue;
    }

    public static int parseLength(byte[] data) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.put(data, 0, 2);
        buffer.flip();
        byte[] temp = buffer.array();
        for (int i = 0; i < temp.length; i++) {
            System.out.println(i + " : " + temp[i]);
        }
        return SocketUtil.byteArrayToInt(temp);
    }

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0, x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    public static long parseMsgId(byte[] data) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(data, 0, data.length);
        buffer.flip();  //need flip
        return buffer.getLong();
    }

    public static byte[] genContentData() {
        byte reserved = 2;
        byte version = 5;
        byte[] ver = {(byte) genVer(reserved, version, 5)};
        byte[] ver2 = {(byte) genVer(reserved, version, 5)};

        ByteArrayOutputStream baos = new ByteArrayOutputStream(1);
        baos.write(ver, 0, 1);
        baos.write(ver2, 0, 1);
        return baos.toByteArray();
    }

    public static int parseContentData(byte[] data, int pos, int offset) {
        byte r = data[pos];
        if (offset == 3) {
            return ((r << 3) & 0xFF) >> 3;
        } else {
            return (r >> offset) & 0xFF;
        }
    }

    public static int parseReserved(byte[] data) {
        byte r = data[0];
        return (r >> 5) & 0xFF;
    }

    public static int parseVersion(byte[] data) {
        byte r = data[0];
        return ((r << 3) & 0xFF) >> 3;
    }

    /**
     * 从数据中解析出数据帧类型
     *
     * @param data
     * @return
     */
    public static String parseType(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        return new String(data, 0, 1);
    }


    public static int genVer(byte r, byte v, int vLen) {
        int num = 0;
        int rLen = 8 - vLen;
        for (int i = 0; i < rLen; i++) {
            num += (((r >> (rLen - 1 - i)) & 0x1) << (7 - i));
        }
        return num + v;
    }

    public static int parseVer(byte b) {
        return ((b << 3) & 0xFF) >> 3;
    }

    public static int parseRes(byte b) {
        return (b >> 5) & 0xFF;
    }
}
