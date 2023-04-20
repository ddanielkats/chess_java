import java.util.ArrayList;

public class King extends Piece {
    public King(PieceColor color) {
        super("king", color, 0);

    }




    public ArrayList<int[]> getPseudoMoves(Piece[][] matrix) {
        ArrayList<int[]> localMoves = new ArrayList<>();
        int row = this.pos[0];
        int col = this.pos[1];
        localMoves.add(new int[]{row, col + 1}); // right
        localMoves.add(new int[]{row - 1, col}); // up
        localMoves.add(new int[]{row, col - 1}); // left
        localMoves.add(new int[]{row + 1, col}); // down
        localMoves.add(new int[]{row - 1, col + 1}); // top right
        localMoves.add(new int[]{row - 1, col - 1}); // top left
        localMoves.add(new int[]{row + 1, col + 1}); // bottom right
        localMoves.add(new int[]{row + 1, col - 1}); // bottom left
        //Utils.printMoves(localMoves);
        ArrayList<int[]> validMoves = new ArrayList<>();
        Piece value;
        for (int [] move : localMoves){
            if (Utils.withinMatrix(move)){
                value = matrix[move[0]][move[1]];
                if (value != null && value.color.equals(color))
                    continue;
                validMoves.add(move);
            }
        }


        //Utils.printMoves(this.legalMoves);
        return  validMoves;
    }
}