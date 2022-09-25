package io.ichat.broadcast.java.server;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BroadcastUDPServer {
    private static final int BUFFER_SIZE = 1024;
    private DatagramSocket socket;
    private short port = 3003;
    private HashMap<InetAddress, Integer> addresses = new HashMap<>();

    public static void main(String[] args) throws IOException {
        new BroadcastUDPServer();
    }

    public BroadcastUDPServer() throws IOException {
        this.socket = new DatagramSocket(port);
        broadcast();
    }

    private void broadcast() throws IOException {
        while (true) {
            DatagramPacket packet = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
            socket.receive(packet);
            addresses.put(packet.getAddress(), packet.getPort());
            StringBuilder builder = new StringBuilder();
            builder.append("-- UDP/IP Packet by " + packet.getAddress() + ":" + packet.getPort() + "\n");
            builder.append("Packet Length: " + packet.getLength() + "\n");
            builder.append("Packet Offset: " + packet.getOffset() + "\n");
            builder.append("\n");
            builder.append(new String(packet.getData()) + "\n");
            builder.append("\n");
            builder.append("-- UDP/IP Packet end\n\n");
            for (InetAddress address : addresses.keySet()) {
                DatagramPacket response = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE, address, addresses.get(address));
                response.setData(builder.toString().getBytes());
                socket.send(response);
            }
        }
    }


}
