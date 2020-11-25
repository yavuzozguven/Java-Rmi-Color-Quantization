package DSOctreeServer;

import java.awt.image.BufferedImage;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OctreeServerInterface extends Remote {
    public int[][] getImage(int[][] hugeimage,int TreeDepth) throws RemoteException;
    public long getMillis(long millis) throws RemoteException;
}
