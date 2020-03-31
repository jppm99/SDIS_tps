import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class AdvertiseThread implements Runnable {
    String content;
    DatagramPacket packet;
    DatagramSocket socket;

    public void init(InetAddress m_addr, int m_port, int s_port) throws Exception {

        this.socket = new DatagramSocket();

        String host_addr = InetAddress.getLocalHost().getHostAddress();

        this.content = String.format("%s %s\0", host_addr, s_port);

        byte[] buf = new byte[256];
        this.packet = new DatagramPacket(buf, buf.length, m_addr, m_port);

        packet.setData(this.content.getBytes(), 0, this.content.getBytes().length);
    }

    public void run() {
        try {
            // do stuff
            this.socket.send(this.packet);
            System.out.println("multicast: " + this.content);

            // sleep
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // recursive
        this.run();
    }
}