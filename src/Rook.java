import java.util.ArrayList;

public class Rook extends Piece {
    public Rook(PieceColor color) {
        super("rook", color, 5);

    }




    public ArrayList<int[]> getLegalMoves(Piece[][] matrix) {

        int row = this.pos[0];
        int col = this.pos[1];
        int[] right = {row, col + 1};
        int[] left = {row, col - 1};
        int[] up = {row - 1, col};
        int[] down = {row + 1, col};



        legalMoves.clear();
        this.goToDir(matrix, right);
        this.goToDir(matrix, down);
        this.goToDir(matrix, left);
        this.goToDir(matrix, up);


        return legalMoves;
    }


}