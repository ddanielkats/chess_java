import java.util.ArrayList;

public class Queen extends Piece {
    public Queen(PieceColor color) {
        super("queen", color, 9);

    }




    public ArrayList<int[]> getLegalMoves(Piece[][] matrix) {

        int row = this.pos[0];
        int col = this.pos[1];
        int[] right = {row, col + 1};
        int[] left = {row, col - 1};
        int[] up = {row - 1, col};
        int[] down = {row + 1, col};
        int[] topRight = {row - 1, col + 1};
        int[] topLeft = {row - 1, col - 1};
        int[] bottomRight = {row + 1, col + 1};
        int[] bottomLeft = {row + 1, col - 1};


        legalMoves.clear();
        this.goToDir(matrix, right);
        this.goToDir(matrix, down);
        this.goToDir(matrix, left);
        this.goToDir(matrix, up);
        this.goToDir(matrix, topRight);
        this.goToDir(matrix, topLeft);
        this.goToDir(matrix, bottomRight);
        this.goToDir(matrix, bottomLeft);

        return legalMoves;
    }


}