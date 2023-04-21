import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    private Utils() {
        // Private constructor to prevent instantiation
    }

    public static  boolean withinMatrix(int[] coords)
    {
        return coords[0] <= 7 && coords[1] <= 7 && coords[0] >= 0 && coords[1] >= 0;
    }

    public static Piece matrixIn(Piece[][] matrix, int[] coords)
    {
        return matrix[coords[0]][coords[1]];
    }

    public static boolean isPiece(Object other)
    {
        return other instanceof Piece;
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public static BufferedImage loadImage(String path)
    {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(path));
            image = resize(image, Constants.square_size, Constants.square_size);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;


    }

    public static int[] coordsToMatrix(int[] coords) {
        // Determine which square on the board was clicked
        int row = coords[1] / Constants.square_size;
        int col = coords[0] / Constants.square_size;
        return  new int[] {row, col};

    }

    public static PieceColor otherTeam(PieceColor team) {
        return team == PieceColor.BLACK ? PieceColor.WHITE : PieceColor.BLACK;
    }

    public static void printMatrix(Piece[][] matrix) {
        System.out.println();
        for (Piece[] row : matrix) {
            for (Piece piece : row) {
                if (piece == null){
                    System.out.print("null ");
                    continue;
                }
                System.out.print(piece.toString() + " ");

            }
            System.out.println();
        }
    }

    public static void printPieces(ArrayList<Piece> arr){
        if (arr.size() == 0){
            System.out.println("empty list");
            System.exit(0);
        }

        for (Piece piece : arr){
            System.out.print(piece + ", ");
        }
        System.out.println();

    }


    public static void printMove(int[] arr){
        System.out.println("[" + arr[0] + ", " + arr[1] + "]");
    }
    public static void printMoves(ArrayList<int[]> arrList) {
        if (arrList.size() == 0) {
            System.out.println("array is empty");
            return;
        }
        for (int [] move : arrList) {
            printMove(move);
        }
    }

    public static boolean inList(ArrayList<int[]> arrList, int[] arr) {
        for (int[] elem : arrList) {
            if (Arrays.equals(elem, arr)) {
                return true;
            }
        }
        return false;
    }


    /*public static Piece[][] copyMat(Piece[][] arr){
        Piece[][] newArray = new Piece[8][8];

        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (arr[i][j] == null){
                    newArray[i][j] = null;
                    continue;
                }
                newArray[i][j] = new Piece(arr[i][j]);

            }

        }

        return  newArray;


    }*/

    static ArrayList<Piece> shuffleArray(ArrayList<Piece> array) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        ArrayList<Piece> ar = new ArrayList<>(array);
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.size() - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            Piece a = ar.get(index);
            ar.set(index, ar.get(i));
            ar.set(i, a);
        }
        return ar;
    }

}