import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Hashtable;

public class Server {
    static DatagramSocket socket;
    static Hashtable<String, String> table;

    static int port, mcast_port;
    static InetAddress mcast_addr;

    public static void main(String[] args) throws Exception {
        Server.port = Integer.parseInt(args[0]);
        Server.mcast_addr = InetAddress.getByName(args[1]);
        Server.mcast_port = Integer.parseInt(args[2]);

        Server.advertise();

        table = new Hashtable<String, String>();
        
        Server.socket = new DatagramSocket(port);

        lookup();

    }

    private static void advertise() throws Exception {
        AdvertiseThread at = new AdvertiseThread();
        
        at.init(Server.mcast_addr, Server.mcast_port, Server.port);

        (new Thread(at)).start();

    }

    private static void lookup() throws Exception {
        DatagramPacket p;

        while(true){
            byte[] rbuf = new byte[256];
            p = new DatagramPacket(rbuf, rbuf.length);

            socket.receive(p);

            String[] received = handle_packet(p);

            String reply_string = parse_reply(received);

            InetAddress source = p.getAddress();
            int reply_port = p.getPort();

            byte[] sbuf = new byte[256];
            DatagramPacket reply = new DatagramPacket(sbuf, sbuf.length, source, reply_port);

            reply.setData(reply_string.getBytes(), 0, reply_string.getBytes().length);
            
            socket.send(reply);
        }

    }

    private static String[] handle_packet(DatagramPacket packet){
        byte[] rbuf = packet.getData();

        //not good nor works with all charsets but it's enough for now
        String str = new String(rbuf).split("\0")[0];

        System.out.println(str);

        return str.split(" ");
    }

    private static String parse_reply(String[] received){
        int result = Server.table.size();
        String s2, s1 = received[1];

        if(received[0].equals("REGISTER") || received[0].equals("register")){
            Server.table.put(received[1], received[2]);
            s2 = received[2];
        }
        else if(received[0].equals("LOOKUP") || received[0].equals("lookup")){
            s2 = Server.table.get(received[1]);
            if(s2 == null) s2 = "null";
        }
        else s2 = "";

        String reply = String.format("%d %s %s", result, s1, s2);

        return reply;
    }

}
