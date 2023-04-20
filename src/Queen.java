import java.util.ArrayList;

public class Queen extends Piece {
    public Queen(PieceColor color) {
        super("queen", color, 9);

    }




    public ArrayList<int[]> getPseudoMoves(Piece[][] matrix) {

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



        ArrayList<int[]> moves = new ArrayList<>();
        this.goToDir(matrix, right, moves);
        this.goToDir(matrix, down, moves);
        this.goToDir(matrix, left, moves);
        this.goToDir(matrix, up, moves);
        this.goToDir(matrix, topRight, moves);
        this.goToDir(matrix, topLeft, moves);
        this.goToDir(matrix, bottomRight, moves);
        this.goToDir(matrix, bottomLeft, moves);

        return moves;
    }


}