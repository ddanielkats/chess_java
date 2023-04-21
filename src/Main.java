import java.awt.event.MouseEvent;

public class Main {

    // Create the pieces and place them on the board
     static Piece[][] test = new Piece[][]{
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, new Knight(PieceColor.BLACK), null, null, null, null, null, null},
            {null, null, new Queen(PieceColor.WHITE),new King(PieceColor.WHITE), null, null, null, null},
            {null, null, null, null, null, null, null, null}
     };

    static Piece[][] normal = {
            {new Rook(PieceColor.BLACK), new Knight(PieceColor.BLACK), new Bishop(PieceColor.BLACK), new Queen(PieceColor.BLACK), new King(PieceColor.BLACK), new Bishop(PieceColor.BLACK), new Knight(PieceColor.BLACK), new Rook(PieceColor.BLACK)},
            {new Pawn(PieceColor.BLACK), new Pawn(PieceColor.BLACK), new Pawn(PieceColor.BLACK), new Pawn(PieceColor.BLACK), new Pawn(PieceColor.BLACK), new Pawn(PieceColor.BLACK), new Pawn(PieceColor.BLACK), new Pawn(PieceColor.BLACK)},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {new Pawn(PieceColor.WHITE), new Pawn(PieceColor.WHITE), new Pawn(PieceColor.WHITE), new Pawn(PieceColor.WHITE), new Pawn(PieceColor.WHITE), new Pawn(PieceColor.WHITE), new Pawn(PieceColor.WHITE), new Pawn(PieceColor.WHITE)},
            {new Rook(PieceColor.WHITE), new Knight(PieceColor.WHITE), new Bishop(PieceColor.WHITE),new Queen(PieceColor.WHITE) , new King(PieceColor.WHITE), new Bishop(PieceColor.WHITE), new Knight(PieceColor.WHITE), new Rook(PieceColor.WHITE)}
    };



    public static void main(String[] args) {
        Piece[][] matrix = normal;
        ChessBoard board = new ChessBoard(matrix);
        board.setVisible(true);
        Piece selected_piece = null;
        while (true)
        {
            board.updateBoard();
        }

    }
}
