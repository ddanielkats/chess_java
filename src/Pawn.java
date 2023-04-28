import java.util.ArrayList;

public class Pawn extends Piece {
    int up_direction;
    public Pawn(PieceColor color) {
        super("pawn", color, 1);
        up_direction = color == PieceColor.WHITE ? -1 : 1;

    }

    public ArrayList<int[]> getPseudoMoves(Piece[][] matrix) {
        ArrayList<int[]> localMoves = new ArrayList<>();

        int row = this.pos[0];
        int col = this.pos[1];

        int [] up_one = new int[] {row + up_direction, col};
        int [] up_two = new int[] {row + 2*up_direction, col};
        int [] top_right = new int[]{row + up_direction, col + 1};
        int [] top_left = new int[]{row + up_direction, col - 1};


        //move up one squares
        if (Utils.withinMatrix(up_one) && Utils.matrixIn(matrix, up_one) == null) {
            localMoves.add(up_one);
            //move up two square
            if (Utils.withinMatrix(up_two) &&moveNumber == 1 && Utils.matrixIn(matrix, up_two) == null)
                localMoves.add(up_two);
        }

        //eat diagonally
        if (Utils.withinMatrix(top_right) &&
                Utils.matrixIn(matrix, top_right) != null  &&
                !Utils.matrixIn(matrix, top_right).color.equals(color))
            localMoves.add(top_right);

        if (Utils.withinMatrix(top_left) &&
                Utils.matrixIn(matrix, top_left) != null  &&
                !Utils.matrixIn(matrix, top_left).color.equals(color))
            localMoves.add(top_left);

        return  localMoves;
    }
}
