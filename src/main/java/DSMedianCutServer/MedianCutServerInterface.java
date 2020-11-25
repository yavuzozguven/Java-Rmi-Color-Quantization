package DSMedianCutServer;

import java.awt.image.BufferedImage;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MedianCutServerInterface extends Remote {
    public byte[] convert(byte[] hugeimage, int TreeDepth) throws RemoteException;
    public long getMillis(long millis) throws RemoteException;
}
