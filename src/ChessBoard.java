import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ChessBoard extends JFrame {
    private JPanel panel;
    private Piece[][] matrix;

    private int click_count;
    private int [] click_pos;
    public PieceColor turn;
    private Piece selected_piece;

    private static final BufferedImage brown_square = Utils.loadImage("square_images/brown_square.png");
    private static final BufferedImage white_square = Utils.loadImage("square_images/white_square.png");

    private ArrayList<Piece> white_pieces, black_pieces, all_pieces;
    public boolean white_in_check, black_in_check, white_in_mate, black_in_mate, stalemate;

    private final int search_depth = 1;

    public ChessBoard(Piece[][] mat) {
        setTitle("Chess Board");
        //getContentPane().setSize(square_size * 8, square_size * 8);
        //setSize(square_size * 8, square_size * 8);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.matrix = mat;
        click_count = 0;
        turn = PieceColor.WHITE;
        white_in_check = false;
        black_in_check = false;
        white_in_mate = false;
        black_in_mate = false;
        stalemate = false;
        selected_piece = null;
        white_pieces = new ArrayList<Piece>();
        black_pieces = new ArrayList<Piece>();
        all_pieces = new ArrayList<Piece>();



        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                Piece value = matrix[i][j];
                if (Utils.isPiece(value)) {
                    matrix[i][j].pos = new int[]{i, j};
                    matrix[i][j].getLegalMoves(matrix);
                    if (value.color == PieceColor.WHITE)
                        white_pieces.add(value);
                    else
                        black_pieces.add(value);
                }
            }
        }
        all_pieces.addAll(white_pieces);
        all_pieces.addAll(black_pieces);


        // Create a new JPanel to hold the chess board and pieces
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                draw(g);
            }
        };

        panel.setPreferredSize(new Dimension(Constants.square_size * 8, Constants.square_size * 8));
        getContentPane().add(panel, BorderLayout.CENTER);

        // Add a MouseAdapter to handle mouse clicks
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                processMouseEvent(e);

            }
        });


        pack();
    }

    public void processMouseEvent(MouseEvent e) {
        // Get the x and y coordinates of the mouse click
        int x = e.getX();
        int y = e.getY();

        // Determine which square on the board was clicked
        int row = y / Constants.square_size;
        int col = x / Constants.square_size;
        int [] mousePos = new int[] {row, col};

        if (!Utils.withinMatrix(mousePos))
            return;
        click_count ++;

        // Do something with the clicked square



        if (click_count == 1)
        {
            //System.out.println(mousePos[0] + ", " + mousePos[1]);
            selected_piece = matrix[mousePos[0]][mousePos[1]];
            if (selected_piece == null )//|| !turn.equals(selected_piece.color))
                click_count = 0;


        }

        if (click_count == 2){
            if (Utils.inList(selected_piece.legalMoves, mousePos)) {
                if (this.movePiece(selected_piece, mousePos))
                    turn = Utils.otherTeam(turn);
            }

        click_count = 0;
        selected_piece = null;
    }

    }

    public void updateBoard() {

        /*if (turn == PieceColor.BLACK) {
            System.out.println("minimax : " + (minimax(search_depth, false)));
            turn = PieceColor.WHITE;
            System.out.print("black pieces after function : ");
            Utils.printPieces(black_pieces);
        }*/



        panel.repaint();
    }

    public void draw(Graphics g) {
        // Draw the board
        int x, y;
        BufferedImage square;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                x = j * Constants.square_size;
                y = i * Constants.square_size;

                g.drawImage(
                        (i + j) % 2 == 0 ? white_square : brown_square,
                        x,
                        y,
                        null
                );
            }
        }


        // Draw the pieces
        for (Piece piece : all_pieces)
            piece.draw(g);
        //Utils.printPieces(black_pieces);
        //black_pieces.get(0).showLegalMoves(g, matrix);

        if (selected_piece != null)
            selected_piece.showLegalMoves(g, matrix);

    }




    public boolean simulateMove(Piece piece, int[] new_pos) {
        // copying original data
        boolean status = true;
        boolean white_check = this.white_in_check;
        boolean black_check = this.black_in_check;
        int[] original_pos = piece.pos;
        // make the move
        Piece target = matrix[new_pos[0]][new_pos[1]];
        piece.move(new_pos, this.matrix);
        // update the pieces legal moves
        for (Piece piece_ : all_pieces) {
            piece_.getLegalMoves(this.matrix);
        }

        this.calculateCheck();
        // return everything back to normal if it's king is in check after the move
        if ((piece.color == PieceColor.WHITE && this.white_in_check) ||
                (piece.color == PieceColor.BLACK && this.black_in_check)) {
            // System.out.println("can't make this move");
            status = false;
        }
        // return everything to normal because it's only a simulation and only returns bool
        piece.move(original_pos, this.matrix);
        matrix[new_pos[0]][new_pos[1]] = target;
        white_in_check = white_check;
        black_in_check = black_check;
        // update the pieces legal moves
        for (Piece piece_ : all_pieces) {
            piece_.getLegalMoves(this.matrix);
        }
        return status;
    }


    public void calculateCheck() {
        // potential checks
        white_in_check = false;
        black_in_check = false;
        for (Piece piece : all_pieces) {
            for (int[] move : piece.legalMoves) {
                Piece target = Utils.matrixIn(matrix, move);
                // if a king is in check
                if (Utils.isPiece(target) && target.name.equals("king") && target.color != piece.color) {
                    // System.out.println(target.getColor() + " king is in check");
                    if (target.color == PieceColor.WHITE)
                        white_in_check = true;
                    else
                        black_in_check = true;

                }
            }
        }

    }

    public void calculateMate(PieceColor color) {
        white_in_mate = false;
        black_in_mate = false;
        ArrayList<Piece> team = color == PieceColor.WHITE ? white_pieces : black_pieces;

        // try to find a move that saves the king
        for (Piece piece : team) {
            ArrayList<int []> lm = new ArrayList<>(piece.legalMoves);
            for (int[] move : lm) {
                if (this.simulateMove(piece, move)) {
                    return;
                }
            }
        }


        // mate
        if (color == PieceColor.WHITE) {
            white_in_mate = true;
        } else {
            black_in_mate = true;
        }
    }


    public boolean movePiece(Piece piece, int [] new_pos)
    {
        if (! this.simulateMove(piece, new_pos))
            return false;

        Piece killed_piece;
        if ( matrix[new_pos[0]][new_pos[1]] != null ) {
            killed_piece = matrix[new_pos[0]][new_pos[1]];
            if (killed_piece.color == PieceColor.WHITE)
                white_pieces.remove(killed_piece);
            else
                black_pieces.remove(killed_piece);
            all_pieces.remove(killed_piece);

        }

        piece.move(new_pos, matrix);



        for (Piece p : all_pieces) {
            p.getLegalMoves(matrix);
        }


        this.calculateCheck();

        calculateMate(PieceColor.WHITE);
        calculateMate(PieceColor.BLACK);

        stalemate = (white_in_mate && !white_in_check) || (black_in_mate && !black_in_check);
        if (stalemate) {
            white_in_mate = false;
            black_in_mate = false;
            System.out.println("stalemate");
        }
        if (!stalemate && (black_in_mate || white_in_mate))
            System.out.println("mate");



        return true;
    }



    int evaluateBoard(){
        int white_sum = 0;
        int black_sum = 0;

        for (Piece piece : white_pieces){
            white_sum += piece.value;
        }
        for (Piece piece : black_pieces){
            black_sum += piece.value;
        }

        return white_sum - black_sum;

    }


    int minimax(int depth, boolean isMaximizing){

        if (stalemate)
            return 0;

        if (white_in_mate)
            return Integer.MIN_VALUE;

        if (black_in_mate)
            return Integer.MAX_VALUE;

        if (depth == 0)
            return evaluateBoard();

        // piece, move
        Piece bestPiece = null, target = null;
        int [] bestMove = new int[2];
        int [] originalPos = new int[2];
        ArrayList<Piece> originalWhite = new ArrayList<>(white_pieces);
        ArrayList<Piece> originalBlack = new ArrayList<>(black_pieces);
        ArrayList<Piece> originalAll = new ArrayList<>(all_pieces);
        if (isMaximizing) {
            System.out.println("maximizing");
            int maxEval = Integer.MIN_VALUE;
            for (Piece piece : originalWhite) {
                ArrayList<int[]> originalLegal = new ArrayList<>(piece.legalMoves);
                for (int [] move : originalLegal) {
                    if (simulateMove(piece, move)) {
                        //remember before move
                        target = matrix[move[0]][move[1]];
                        originalPos = piece.pos;
                        boolean wc = white_in_check;
                        boolean bc = black_in_check;
                        boolean wm = white_in_mate;
                        boolean bm = black_in_mate;



                        movePiece(piece, move);
                        //updateBoard();
                        int evaluation = minimax(depth - 1, false);
                        if (evaluation > maxEval) {
                            maxEval = evaluation;
                            bestPiece = piece;
                            bestMove = move;
                        }


                        //unmake move
                        piece.move(originalPos, matrix);
                        white_in_check = wc;
                        black_in_check = bc;
                        white_in_mate = wm;
                        black_in_mate = bm;
                        if (target != null)
                        {
                            matrix[move[0]][move[1]] = target;
                            black_pieces.add(target);
                            all_pieces.add(target);
                        }

                    }
                }

            }

            return maxEval;


        }

        else {
            System.out.println("minimizing");
            int minEval = Integer.MAX_VALUE;
            for (Piece piece : originalBlack) {
                ArrayList<int[]> originalLegal = new ArrayList<>(piece.legalMoves);
                for (int [] move : originalLegal) {
                    if (simulateMove(piece, move)) {
                        //remember before move
                        target = matrix[move[0]][move[1]];
                        originalPos = piece.pos;
                        boolean wc = white_in_check;
                        boolean bc = black_in_check;
                        boolean wm = white_in_mate;
                        boolean bm = black_in_mate;

                        //make the move
                        movePiece(piece, move);
                        //updateBoard();
                        int evaluation = minimax( depth - 1, true);
                        if (evaluation < minEval) {
                            minEval = evaluation;
                            bestPiece = piece;
                            bestMove = move;
                        }

                        //unmake move
                        piece.move(originalPos, matrix);
                        white_in_check = wc;
                        black_in_check = bc;
                        white_in_mate = wm;
                        black_in_mate = bm;
                        if (target != null)
                        {
                            matrix[move[0]][move[1]] = target;
                            white_pieces.add(target);
                            all_pieces.add(target);
                        }
                    }
                }
                System.out.print("black pieces after all moves of : " + piece);
                System.out.println();
                Utils.printPieces(black_pieces);
            }




            if (depth == search_depth) {
                System.out.print("moving black to ");
                Utils.printMove(bestMove);
                movePiece(bestPiece, bestMove);

            }


            return minEval;
        }

    }


}