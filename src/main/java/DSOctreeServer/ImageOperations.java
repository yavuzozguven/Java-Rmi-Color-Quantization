package DSOctreeServer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author CB.EN.P2CSE16001
 */
public class ImageOperations {
    public int[][] convertTo2DUsingGetRGB(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] result = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                result[row][col] = image.getRGB(col, row);
            }
        }

        return result;
    }

    public BufferedImage writeImage(int r, int c, int[][] res) {
        String path = System.getProperty("user.home") + "/Desktop/xx.png";
        BufferedImage image = new BufferedImage(r, c, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < r; x++) {
            for (int y = 0; y < c; y++) {
                image.setRGB(x, y, res[x][y]);
            }
        }



        return image;
    }
}
