import java.util.ArrayList;

public class Move {

    public Piece moving_piece;
    public Piece target_piece;
    public int[] starting_square;
    public int[] target_square;

    public ArrayList<Piece> original_white;
    public ArrayList<Piece> original_black;
    public ArrayList<Piece> original_all;

    public ArrayList<Piece> white_pieces;
    public ArrayList<Piece> black_pieces;
    public ArrayList<Piece> all_pieces;

    public Move(Piece moving_piece, int[] target_square)
    {

        this.moving_piece = moving_piece;
        this.target_square = target_square;
        this.starting_square = moving_piece.pos;
    }



    public void makeMove(Piece[][] matrix, ArrayList<Piece> wp, ArrayList<Piece> bp, ArrayList<Piece> ap){
        target_piece = matrix[target_square[0]][target_square[1]];
        white_pieces = wp;
        black_pieces = bp;
        all_pieces = ap;
        original_white = new ArrayList<>(white_pieces);
        original_black = new ArrayList<>(black_pieces);
        original_all = new ArrayList<>(all_pieces);



        if ( target_piece != null ) {
            if (target_piece.color == PieceColor.WHITE)
                white_pieces.remove(target_piece);
            else
                black_pieces.remove(target_piece);
            all_pieces.remove(target_piece);

        }

        moving_piece.move(target_square, matrix);
        moving_piece.moveNumber += 1;

    }

    public void unmakeMove(Piece[][] matrix){
        white_pieces = original_white;
        black_pieces = original_black;
        all_pieces = original_all;

        moving_piece.move(starting_square, matrix);



    }

}
