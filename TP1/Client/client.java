import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class client {

    static InetAddress host;
    static int port;
    static String operationString;
    static DatagramSocket socket;
    static String[] operands;

    public static void main(String[] args) throws Exception {
        client.host = InetAddress.getByName(args[0]);
        client.port = Integer.parseInt(args[1]);
        client.operationString = args[2];
        
        client.socket = new DatagramSocket();

        operands = new String[2];

        operands[0] = args[3];
        if(operationString.equals("register") || operationString.equals("REGISTER")) operands[1] = args[4];

        client.makeOperation();
    }

    private static void makeOperation() throws Exception {
        DatagramPacket p;
        
        byte[] sbuf = new byte[256];
        DatagramPacket op = new DatagramPacket(sbuf, sbuf.length, client.host, client.port);
        
        String opString = client.makeOpString();

        op.setData(opString.getBytes(), 0, opString.getBytes().length);
        
        socket.send(op);
        
        
        byte[] rbuf = new byte[256];
        p = new DatagramPacket(rbuf, rbuf.length);
        
        socket.receive(p);
        String[] received = handle_packet(p);
        
        String output = String.format("Client: %s %s %s : %s -> %s %s", client.operationString, client.operands[0], ((client.operationString.equals("register") || client.operationString.equals("REGISTER")) ? client.operands[1] : ""), received[0], received[1], received[2]);
        System.out.println(output);
    }

    private static String makeOpString(){
        String str;
        if(client.operationString.equals("register") || client.operationString.equals("REGISTER")){
            str = String.format("%s %s %s", client.operationString, client.operands[0], client.operands[1]);
        }
        else{
            str = String.format("%s %s", client.operationString, client.operands[0]);
        }
        return str;
    }

    private static String[] handle_packet(DatagramPacket packet){
        byte[] rbuf = packet.getData();

        //not good nor works with all charsets but it's enough for now
        String str = new String(rbuf).split("\0")[0];

        System.out.println(str);

        return str.split(" ");
    }
    
}