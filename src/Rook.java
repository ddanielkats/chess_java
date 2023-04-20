import java.util.ArrayList;

public class Rook extends Piece {
    public Rook(PieceColor color) {
        super("rook", color, 5);

    }




    public ArrayList<int[]> getPseudoMoves(Piece[][] matrix) {

        int row = this.pos[0];
        int col = this.pos[1];
        int[] right = {row, col + 1};
        int[] left = {row, col - 1};
        int[] up = {row - 1, col};
        int[] down = {row + 1, col};



        ArrayList<int[]> moves = new ArrayList<>();
        this.goToDir(matrix, right, moves);
        this.goToDir(matrix, down, moves);
        this.goToDir(matrix, left, moves);
        this.goToDir(matrix, up, moves);


        return moves;
    }


}