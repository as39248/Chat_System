package client.frameworks_and_drivers.communication_manager;

import server.frameworks_and_drivers.communication_manager.ComManagerUser;
import server.frameworks_and_drivers.communication_manager.Constants;
import server.frameworks_and_drivers.communication_manager.IfComManager;
import server.frameworks_and_drivers.communication_manager.Receiver;
import server.frameworks_and_drivers.communication_manager.Tools;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class comManager implements IfComManager
{
    int port;
    ComManagerUser user;
    public static DatagramSocket socket;

    public comManager()
    {
    }

    /**
     * This method starts comManager.
     @param port The port for receiving messages.
     @param  user The user of comManager (i.e. inputSorter).
     */
    public void init(int port, ComManagerUser user)
    {
        try
        {
            //socket to receive data packet
            socket = new DatagramSocket(port);
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }


        this.port = port;
        this.user = user;
        server.frameworks_and_drivers.communication_manager.Receiver receiver = new Receiver(user);
        receiver.start();
    }

    /**
     * This method sends a String to the designated address over the internet.
     @param msg The message to be sent, in String format.
     @param peerIP The destination ip.
     @param peerPort The destination port.
     */
    @Override
    public void send(String peerIP, int peerPort, String msg) {
        InetAddress peerAddr = null;
        try {
            peerAddr = InetAddress.getByName(peerIP);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        int sliceLen = 0;
        byte[] data = msg.getBytes(StandardCharsets.UTF_8);
        int totalSlices = (int) Math.ceil((double) data.length / server.frameworks_and_drivers.communication_manager.Constants.PACKET_LEN);
        String msgId = String.valueOf(server.frameworks_and_drivers.communication_manager.Tools.generateID());

        for(int j=0; j<totalSlices; j++)
        {
            if(j+1<totalSlices)
            {
                //  setting slice length to 128
                sliceLen = server.frameworks_and_drivers.communication_manager.Constants.PACKET_LEN;
            }
            else
            {
                //  packet data length for the remaining portion
                sliceLen = data.length - (j)* server.frameworks_and_drivers.communication_manager.Constants.PACKET_LEN;
            }

            byte[] a = Tools.getBytes(data, j* server.frameworks_and_drivers.communication_manager.Constants.PACKET_LEN, sliceLen);

            String toByte  = server.frameworks_and_drivers.communication_manager.Constants.SEPARATOR + server.frameworks_and_drivers.communication_manager.Constants.SLICE_PACKET + server.frameworks_and_drivers.communication_manager.Constants.SEPARATOR + msgId + server.frameworks_and_drivers.communication_manager.Constants.SEPARATOR + totalSlices + server.frameworks_and_drivers.communication_manager.Constants.SEPARATOR+ j + Constants.SEPARATOR;

            byte[] b = toByte.getBytes(StandardCharsets.UTF_8);

            byte[] c = new byte[a.length + b.length];
            System.arraycopy(a, 0, c, 0, a.length);
            System.arraycopy(b, 0, c, a.length, b.length);

            DatagramPacket packet = new DatagramPacket(c,0,c.length, peerAddr, peerPort);
            try {
                socket.send(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
