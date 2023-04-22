public class Move {

    public Piece moving_piece;
    public int[] target_square;
    public int[] starting_square;

    public Move(Piece moving_piece, int[] target_square)
    {

        this.moving_piece = moving_piece;
        this.target_square = target_square;
        this.starting_square = moving_piece.pos;
    }

    public void makeMove(){


    }

    public void unmakeMove(){



    }

}
