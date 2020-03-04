
import java.rmi.RemoteException;

public class RMI_server extends Server implements RMI_interface {

    @Override
    public String sayHello() throws RemoteException {
        String str = null;

        try{
            str = String.format("%s %d", Server.get_host_addr(), Server.port);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return str;
    }
    
}
