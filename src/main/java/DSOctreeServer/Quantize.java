package DSOctreeServer;

public class Quantize {
    final boolean QUICK = true;

    final int MAX_RGB = 255;
    final int MAX_NODES = 266817;
    int MAX_TREE_DEPTH = Level.Level1.getValue();

    // these are precomputed in advance
    int SQUARES[];
    int SHIFT[];

    Quantize() {
        SQUARES = new int[MAX_RGB + MAX_RGB + 1];
        for (int i= -MAX_RGB; i <= MAX_RGB; i++) {
            SQUARES[i + MAX_RGB] = i * i;
        }

        SHIFT = new int[MAX_TREE_DEPTH + 1];
        for (int i = 0; i < MAX_TREE_DEPTH + 1; ++i) {
            SHIFT[i] = 1 << (15 - i);
        }
    }


    public int[][] quantizeImage(int pixels[][], int max_colors) {
        Octree cube = new Octree(pixels, max_colors);
        cube.classification();
        cube.reduction();
        cube.assignment();
        return cube.result(cube.pixels, cube.colormap);
    }



}
