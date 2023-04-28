import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MoveMap {

    private MoveMap()
    {

    }

    public static String getLetter(int n)
    {
        return "" + (char) ('a' + n);
    }

    public static int getNumber(char letter)
    {
        return (int) (Character.toLowerCase(letter) - 'a');
    }


    public static int[] getMove(String move)
    {
        move = move.replaceAll("x", "");
        int row = 8 - (move.charAt(1) - '0');
        int col = getNumber(move.charAt(0));

        return new int[] {row, col};
    }

    public static String getSquare(int[] move)
    {

        char col = (char) ('a' + move[1]);
        String row = String.valueOf ((8 - move[0]));
        return "" + col + row;

    }

    public static String getMapMove(Piece piece, int[] move, Piece[][] matrix)
    {
        String result = "";
        char first_letter = Character.toUpperCase(piece.name.charAt(0));
        if (piece.name.equals("knight"))
            first_letter = 'N';

        String map_square = getSquare(move);
        //doesn't take piece
        if (matrix[move[0]][move[1]] == null) {
            if (piece.name.equals("pawn"))
                return map_square;

            return first_letter + map_square;
        }
        //takes piece
        else {
            if (piece.name.equals("pawn"))
                //for example cxd4 -> pawn on c takes d4
                return getLetter(piece.pos[1]) + "x" + map_square;

            else
                return first_letter + "x" + map_square;
        }


    }

    public static String getNextMove(String sequence) {
        String filename = "first_moves.txt";
        String nextMove = null;

        try {


            // Open the file and create a BufferedReader to read its contents
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            String line;
            String[] sequence_words = sequence.split(" ");
            while ((line = reader.readLine()) != null) {
                // Check if the line contains the input sequence
                if (line.contains(sequence)) {
                    // Split the line into an array of words
                    String[] words = line.split(" ");
                    nextMove = words[sequence_words.length];

                    // Stop searching after finding the first matching line
                    break;
                }
            }

            // Close the BufferedReader and return the next move (if found)
            reader.close();

        } catch (IOException e){
            System.err.println("Error reading file: " + e.getMessage());
        }


        return nextMove;
    }

}
