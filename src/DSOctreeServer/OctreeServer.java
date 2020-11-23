package DSOctreeServer;


import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class OctreeServer extends UnicastRemoteObject implements OctreeServerInterface {

    protected OctreeServer() throws RemoteException {
    }



    public static void main(String[] args) {
        try{
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("server",new OctreeServer());
            System.out.println("OctreeServer started.");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public int[][] getImage(int[][] hugeimage,int TreeDepth) throws RemoteException {


        Quantize q = new Quantize();
        q.MAX_TREE_DEPTH = TreeDepth;
        int res[][] = q.quantizeImage(hugeimage, q.MAX_RGB);


        return res;
    }
}
