package com.kas4.espush.search.udp;

import android.content.Context;
import android.util.Log;

import com.kas4.espush.app.Constant;
import com.kas4.espush.search.LocalSearchManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by kas4 QQ: 1504368178 on 15/8/28.
 */
public class UdpManager {


    private static UdpManager instance;


    Context context;


    private UdpManager(Context context) {
        this.context = context;

    }


    public static UdpManager getInstance(Context context) {
        if (instance == null) {
            instance = new UdpManager(context);
        }

        return instance;
    }

    static String broadcastAddrStr = "192.168.0.255";

    static int port = 6543;


    final String MSG_BEEP = "BEEP";
    final String MSG_BEEPOK = "BEEPOK";
    final String MSG_SSIDOK = "SSIDOK";


    private DatagramChannel udpserver;

    public void sendBeep() {
        LocalSearchManager.sendBroadcast(context, Constant.TYPE_MSG_SEARCH_BEEP);

        String msg = MSG_BEEP;

        byte[] b = CodeUtil.asciiToBytes(msg);

        ByteBuffer buf = ByteBuffer.allocate(b.length);
        buf.clear();
        buf.put(b);
        buf.flip();

        try {
            udpserver.send(buf, new InetSocketAddress(getBroadcastAddr(broadcastAddrStr), port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendData(String fromIP, int fromPort) {
        LocalSearchManager.sendBroadcast(context, Constant.TYPE_MSG_SEARCH_SEND_DATA);

        MSG_CFG o = LocalSearchManager.getInstance(context).getMsg();

        byte[] b = o.buildMSG();

        Log.d("out_________________", "" + b.length);

        ByteBuffer buf = ByteBuffer.allocate(b.length);
        buf.clear();
        buf.put(b);
        buf.flip();

        try {
            udpserver.send(buf, new InetSocketAddress(fromIP, fromPort));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void shutUDP() {
        try {
            flag = false;
            isListen = false;
            udpserver.close();
            udpserver = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean isListen = false;
    boolean flag = true;

    private int packetCounter = 0;

    public static String getBroadcastAddr(String defAddr) throws SocketException {
        System.setProperty("java.net.preferIPv4Stack", "true");
        for (Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces(); niEnum.hasMoreElements(); ) {
            NetworkInterface ni = niEnum.nextElement();
            if (!ni.isLoopback()) {
                for (InterfaceAddress interfaceAddress : ni.getInterfaceAddresses()) {
                    if (interfaceAddress.getBroadcast() != null) {
                        return interfaceAddress.getBroadcast().toString().substring(1);
                    }
                }
            }
        }
        return defAddr;
    }


    public void startUDPListen() {

        if (isListen)
            return;


        Selector selector = null;
        try {
            SocketAddress localportUDP = new InetSocketAddress(port);


            udpserver = DatagramChannel.open();
            udpserver.socket().bind(localportUDP);

            udpserver.configureBlocking(false);


            selector = Selector.open();
            udpserver.register(selector, SelectionKey.OP_READ);


        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);
        receiveBuffer.clear();
        while (flag) {
            try {

                isListen = true;
                if (selector == null) break;
                selector.select();

                Set keys = selector.selectedKeys();


                for (Iterator i = keys.iterator(); i.hasNext(); ) {

                    SelectionKey key = (SelectionKey) i.next();
                    i.remove();


                    Channel c = (Channel) key.channel();


                    if (key.isReadable() && c == udpserver) {

                        DatagramSocket udpSocket;
                        DatagramPacket udpPacket;

                        byte[] buffer = new byte[2048];
                        // Create a packet to receive data into the buffer
                        udpPacket = new DatagramPacket(buffer, buffer.length);


                        udpSocket = udpserver.socket();

                        receiveBuffer.clear();

                        InetSocketAddress clientAddress = (InetSocketAddress) udpserver.receive(receiveBuffer);

                        if (clientAddress != null) {

                            String fromAddress = clientAddress.getAddress().getHostAddress();

                            packetCounter++;


                            int received = receiveBuffer.position();
                            byte[] bufferConvert = new byte[received];

                            System.arraycopy(receiveBuffer.array(), 0, bufferConvert, 0, bufferConvert.length);


                            // by zj data
                            String fromIP = clientAddress.getAddress().getHostAddress();

                            int fromPort = clientAddress.getPort();

                            byte[] data = bufferConvert;


                            String msgIn = CodeUtil.toAscii(data);

                            if (msgIn.equals(MSG_BEEPOK)) {
                                sendData(fromIP, fromPort);

                            } else if (msgIn.equals(MSG_SSIDOK)) {
                                LocalSearchManager.sendBroadcast(context, Constant.TYPE_MSG_SEARCH_SEND_OK);
                            }

                        }
                    }
                }
            } catch (java.io.IOException e) {

            } catch (Exception e) {

            }
        }
    }


    public static void main(String[] args) throws Exception {


    }
}
