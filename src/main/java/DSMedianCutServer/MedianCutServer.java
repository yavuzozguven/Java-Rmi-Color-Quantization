package DSMedianCutServer;

import ij.ImagePlus;
import ij.process.ImageProcessor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MedianCutServer extends UnicastRemoteObject implements MedianCutServerInterface {
    protected MedianCutServer() throws RemoteException {
    }


    public static void main(String[] args) throws IOException {
        try{
            Registry registry = LocateRegistry.createRegistry(1100);
            registry.rebind("server",new MedianCutServer());
            System.out.println("MedianCut started.");
        }catch (Exception e){
            System.out.println(e);
        }
    }


    public byte[] convert(byte[] hugeimage, int TreeDepth) throws RemoteException {

        BufferedImage bufferedImage = createImageFromBytes(hugeimage);

        /*BufferedImage bufferedImage = new BufferedImage(590,393,BufferedImage.TYPE_3BYTE_BGR);
        bufferedImage.setData(Raster.createRaster(bufferedImage.getSampleModel(),new DataBufferByte(hugeimage,hugeimage.length),new Point()));*/


        ImagePlus imp = new ImagePlus("MedianCut",bufferedImage);
        ImageProcessor ip = imp.getProcessor();
        ip.snapshot();
        int[] pixels = (int[])ip.getPixels();
        imp.trimProcessor();

        ij.process.MedianCut mc = new ij.process.MedianCut(pixels,bufferedImage.getWidth(), bufferedImage.getHeight());
        Image image = mc.convert(TreeDepth);

        BufferedImage convertedImage = toBufferedImage(image);

        //byte[] converted = ((DataBufferByte) convertedImage.getRaster().getDataBuffer()).getData();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(convertedImage, "jpg", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] converted = baos.toByteArray();


        return converted;
    }

    public long getMillis(long millis) {
        long l = System.nanoTime();
        return l-millis;
    }

    private static BufferedImage createImageFromBytes(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

}
