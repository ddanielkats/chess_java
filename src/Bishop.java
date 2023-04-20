import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(PieceColor color) {
        super("bishop", color, 3);

    }




    public ArrayList<int[]> getLegalMoves(Piece[][] matrix) {

        int row = this.pos[0];
        int col = this.pos[1];
        int[] topRight = {row - 1, col + 1};
        int[] topLeft = {row - 1, col - 1};
        int[] bottomRight = {row + 1, col + 1};
        int[] bottomLeft = {row + 1, col - 1};

        legalMoves.clear();
        this.goToDir(matrix, topRight);
        this.goToDir(matrix, topLeft);
        this.goToDir(matrix, bottomRight);
        this.goToDir(matrix, bottomLeft);

        return legalMoves;
    }


}