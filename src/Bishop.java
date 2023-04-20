import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(PieceColor color) {
        super("bishop", color, 3);

    }




    public ArrayList<int[]> getPseudoMoves(Piece[][] matrix) {

        int row = this.pos[0];
        int col = this.pos[1];
        int[] topRight = {row - 1, col + 1};
        int[] topLeft = {row - 1, col - 1};
        int[] bottomRight = {row + 1, col + 1};
        int[] bottomLeft = {row + 1, col - 1};


        ArrayList<int[]> moves = new ArrayList<>();
        this.goToDir(matrix, topRight, moves);
        this.goToDir(matrix, topLeft, moves);
        this.goToDir(matrix, bottomRight, moves);
        this.goToDir(matrix, bottomLeft, moves);

        return moves;
    }


}