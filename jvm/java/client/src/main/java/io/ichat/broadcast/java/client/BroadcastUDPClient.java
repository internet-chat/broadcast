package io.ichat.broadcast.java.client;

import javax.swing.*;
import java.io.IOException;
import java.net.*;

public class BroadcastUDPClient {
    private static final int BUFFER_SIZE = 1024;
    private DatagramSocket socket;
    private InetAddress targetAddress;
    private short port;

    public static void main(String[] args) throws IOException {
        System.out.println("Launching UDP Broadcast client");
        System.out.println("udp socket recv. into buffers of 1024 bits");
        new BroadcastUDPClient(args);
    }

    public BroadcastUDPClient(String[] args) throws IOException {
        this.targetAddress = InetAddress.getByName(args[0]);
        this.port = Short.parseShort(args[1]);

        System.out.println("Using " + targetAddress + ":" + port + " as broadcast server.");

        this.socket = new DatagramSocket();
        Thread thread = new Thread(() -> {
            try {
                runCommandline();
            } catch (IOException e) {
                System.out.println("Failed to invoke BroadcastUDPClient::runCommandLine");
                throw new RuntimeException(e);
            }
        });
        thread.start();
        while (true) {
            DatagramPacket packet = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
            socket.receive(packet);
            System.out.println(new String(packet.getData()));
        }
    }

    private void sendPacket(byte[] addr, byte[] data) throws IOException {
        DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(""), 1);
        this.socket.send(packet);
    }

    private void sendMessage(byte[] data) throws IOException {
        DatagramPacket packet = new DatagramPacket(data, data.length, targetAddress, port);
        this.socket.send(packet);
    }

    private void runCommandline() throws IOException {
        while (true) {
            String input = JOptionPane.showInputDialog(null, "Enter a text: ");
            if (input == null) return;
            sendMessage(input.getBytes());
        }
    }


    public DatagramSocket getSocket() {
        return socket;
    }
}
