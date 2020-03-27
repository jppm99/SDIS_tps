
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMI_interface extends Remote {
    String sayHello() throws RemoteException;
}