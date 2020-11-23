package DSClient;

import DSOctreeServer.ImageOperations;
import DSOctreeServer.OctreeServerInterface;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLOutput;

public class Client {
    private JPanel mainPanel;
    private JButton upload;
    BufferedImage bufferedImage;

    public Client() {
        upload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView());
                int r = jFileChooser.showDialog(null,"OK");

                if(r == JFileChooser.APPROVE_OPTION){
                    File file = jFileChooser.getSelectedFile().getAbsoluteFile();
                    Client client = new Client();
                    client.connectOctreeServer(file);
                }
            }
        });
    }

    public static void main(String[] args) throws IOException {
        JFrame jFrame = new JFrame("App");
        jFrame.setContentPane(new Client().mainPanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }


    private void connectOctreeServer(File file){
        try{
            Registry registry = LocateRegistry.getRegistry("127.0.0.1",1099);
            OctreeServerInterface si = (OctreeServerInterface) registry.lookup("server");
            System.out.println("Connected to server.");

            ImageOperations i = new ImageOperations();

            BufferedImage hugeImage = ImageIO.read(file);

            int[][] result = i.convertTo2DUsingGetRGB(hugeImage);

            int[][] image = si.getImage(result,8);

            bufferedImage = i.writeImage(image.length, image[0].length, image);

            System.out.println("Converted.");

            ImageIcon icon = new ImageIcon(bufferedImage);
            JLabel label = new JLabel(icon);
            JOptionPane.showMessageDialog(null, label);
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
