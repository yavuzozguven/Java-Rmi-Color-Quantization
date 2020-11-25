package DSClient;

import DSMedianCutServer.MedianCutServer;
import DSMedianCutServer.MedianCutServerInterface;
import DSOctreeServer.ImageOperations;
import DSOctreeServer.OctreeServerInterface;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLOutput;
import java.util.concurrent.TimeUnit;

public class Client {
    private JPanel mainPanel;
    private JButton upload;
    Object[] options = {"8", "16",
            "32","64","128","256"};

    public Client() {
        upload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView());
                int r = jFileChooser.showDialog(null,"OK");

                if(r == JFileChooser.APPROVE_OPTION){
                    int n = JOptionPane.showOptionDialog(mainPanel,
                            "Renk seviyesini seçin.",
                            "Dialog",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[2]);
                    File file = jFileChooser.getSelectedFile().getAbsoluteFile();
                    Client client = new Client();
                    client.connectOctreeServer(file,Integer.parseInt(options[n].toString()));
                    client.connectMedianCutServer(file,Integer.parseInt(options[n].toString()));

                }
            }
        });
    }

    public static void main(String[] args) throws IOException {
        JFrame jFrame = new JFrame("App");
        jFrame.setContentPane(new Client().mainPanel);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }


    private void connectOctreeServer(File file,int ColorLevel){
        try{
            long time = System.nanoTime();
            Registry registry = LocateRegistry.getRegistry("127.0.0.1",1099);
            OctreeServerInterface si = (OctreeServerInterface) registry.lookup("server");
            System.out.println("Connected to server.");

            ImageOperations i = new ImageOperations();

            BufferedImage hugeImage = ImageIO.read(file);

            int[][] result = i.convertTo2DUsingGetRGB(hugeImage);

            int[][] image = si.getImage(result,ColorLevel);


            BufferedImage bufferedImage = i.writeImage(image.length, image[0].length, image);
            bufferedImage = rotateClockwise90(bufferedImage);

            System.out.println("Converted.");

            ImageIcon icon = new ImageIcon(bufferedImage);
            JLabel label = new JLabel(icon);
            label.setText("Octree Server:"+(System.nanoTime()-time)/1000000+"ms");
            JOptionPane.showMessageDialog(null, label);


        }catch (Exception e){
            System.out.println(e);
            JLabel label = new JLabel();
            label.setText("Octree Server'ı başlatmanız gerekli.");
            JOptionPane.showMessageDialog(null, label);
        }
    }

    private void connectMedianCutServer(File file,int ColorLevel){
        try{
            long time = System.nanoTime();
            Registry registry = LocateRegistry.getRegistry("127.0.0.1",1100);
            MedianCutServerInterface si = (MedianCutServerInterface) registry.lookup("server");
            System.out.println("Connected to server.");

            BufferedImage bufferedImage = ImageIO.read(file);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", baos);
            byte[] result = baos.toByteArray();

            byte[] image = si.convert(result,ColorLevel);

            System.out.println("Converted.");




            ImageIcon icon = new ImageIcon(image);
            JLabel label = new JLabel(icon);
            label.setText("Median Cut Server:"+(System.nanoTime()-time)/1000000+"ms");
            JOptionPane.showMessageDialog(null, label);


        }catch (Exception e){
            System.out.println(e);
            JLabel label = new JLabel();
            label.setText("Median Cut Server'ı başlatmanız gerekli.");
            JOptionPane.showMessageDialog(null, label);
        }
    }

    public static BufferedImage rotateClockwise90(BufferedImage src) {
        int width = src.getWidth();
        int height = src.getHeight();

        BufferedImage dest = new BufferedImage(height, width, src.getType());

        Graphics2D graphics2D = dest.createGraphics();
        graphics2D.translate((height - width) / 2, (height - width) / 2);
        graphics2D.rotate(Math.PI / 2, height / 2, width / 2);
        graphics2D.drawRenderedImage(src, null);

        return dest;
    }

}
