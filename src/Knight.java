import java.util.ArrayList;

public class Knight extends Piece {

    public Knight(PieceColor color) {
        super("knight", color, 3);
    }

    public ArrayList<int[]> getPseudoMoves(Piece[][] matrix) {
        // as in an array
        int row = pos[0];
        int col = pos[1];


        ArrayList<int[]> localMoves = new ArrayList<>();

        // above knight
        localMoves.add(new int[] {row - 2, col + 1}); // two up one right
        localMoves.add(new int[] {row - 1, col + 2}); // one up two right
        localMoves.add(new int[] {row - 2, col - 1}); // two up one left
        localMoves.add(new int[] {row - 1, col - 2}); // one up two left
        // below knight
        localMoves.add(new int[] {row + 2, col + 1}); // two down one right
        localMoves.add(new int[] {row + 1, col + 2}); // one down two right
        localMoves.add(new int[] {row + 2, col - 1}); // two down one left
        localMoves.add(new int[] {row + 1, col - 2}); // one down two left

        ArrayList<int[]> moves = new ArrayList<>();
        for (int[] move : localMoves){
            if (Utils.withinMatrix(move)) {
                if (Utils.matrixIn(matrix, move) != null && Utils.matrixIn(matrix, move).color == this.color)
                    continue;
                moves.add(move);
            }

        }


        //Utils.printMoves(this.legalMoves);
        return  moves;
    }
}
