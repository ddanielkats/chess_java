import java.util.HashMap;

public class MoveMap {

    private MoveMap()
    {

    }

    private static String getSquare(int[] move)
    {

        char col = (char) ('a' + move[1]);
        String row = String.valueOf ((7 - move[0]));

        return "" + col + row;

    }

    public static String getMapMove(Piece piece, int[] move, Piece[][] matrix)
    {
        String result = "";

        String map_square = getSquare(move);
        if(piece.name.equals("pawn"))
            return map_square;
        Piece target = matrix[move[0]][move[1]];

        return "target_square";

    }

}
