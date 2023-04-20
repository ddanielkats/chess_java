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


        //Utils.printMoves(this.legalMoves);
        return  localMoves;
    }
}