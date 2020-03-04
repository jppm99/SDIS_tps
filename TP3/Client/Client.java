
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

class Client {

    static InetAddress server_addr;
    static int server_port;
    static String operationString, host_name, remote_object_name;
    static DatagramSocket socket;
    static String[] operands;

    public static void main(String[] args) throws Exception {
        Client.host_name = args[0];
        Client.remote_object_name = args[1];
        Client.operationString = args[2];
        
        String[] received_mcast = Client.getServer(Client.host_name, Client.remote_object_name);

        server_addr = InetAddress.getByName(received_mcast[0]);
        server_port = Integer.parseInt(received_mcast[1]);
        
        Client.socket = new DatagramSocket();

        operands = new String[2];

        operands[0] = args[3];
        if(operationString.equals("register") || operationString.equals("REGISTER")) operands[1] = args[4];

        Client.makeOperation();
    }

    private static String[] getServer(String host, String remote_object) throws Exception {

        Registry registry = LocateRegistry.getRegistry(host);
        RMI_interface stub = (RMI_interface) registry.lookup(remote_object);
        String response = stub.sayHello();

        //not the best, won't work on all charsets
        response = response.split("\0")[0];

        return response.split(" ");
    }

    private static void makeOperation() throws Exception {
        DatagramPacket p;
        
        byte[] sbuf = new byte[256];
        DatagramPacket op = new DatagramPacket(sbuf, sbuf.length, Client.server_addr, Client.server_port);
        
        String opString = Client.makeOpString();

        op.setData(opString.getBytes(), 0, opString.getBytes().length);
        
        socket.send(op);
        
        
        byte[] rbuf = new byte[256];
        p = new DatagramPacket(rbuf, rbuf.length);
        
        socket.receive(p);
        String[] received = handle_packet(p);
        
        String output = String.format("Client: %s %s %s : %s -> %s %s", Client.operationString, Client.operands[0], ((Client.operationString.equals("register") || Client.operationString.equals("REGISTER")) ? Client.operands[1] : ""), received[0], received[1], received[2]);
        System.out.println(output);
    }

    private static String makeOpString(){
        String str;
        if(Client.operationString.equals("register") || Client.operationString.equals("REGISTER")){
            str = String.format("%s %s %s", Client.operationString, Client.operands[0], Client.operands[1]);
        }
        else{
            str = String.format("%s %s", Client.operationString, Client.operands[0]);
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