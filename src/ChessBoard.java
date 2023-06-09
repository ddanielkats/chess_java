import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ChessBoard extends JFrame {
    private JPanel panel;
    public Piece[][] matrix;

    private int click_count;
    private int [] click_pos;
    public PieceColor turn;
    private Piece selected_piece;

    private static final BufferedImage brown_square = Utils.loadImage("square_images/brown_square.png");
    private static final BufferedImage white_square = Utils.loadImage("square_images/white_square.png");

    private ArrayList<Piece> white_pieces, black_pieces, all_pieces;
    public boolean white_in_check, black_in_check, white_in_mate, black_in_mate, stalemate;

    public String move_sequence = "";
    int [] start_move;
    int [] end_move;

    int move_num;


    public ChessBoard(Piece[][] mat) {
        setTitle("Chess Board");
        //getContentPane().setSize(square_size * 8, square_size * 8);
        //setSize(square_size * 8, square_size * 8);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        this.matrix = mat;
        click_count = 0;
        move_num = 0;
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
        white_pieces = Utils.shuffleArray(white_pieces);
        black_pieces = Utils.shuffleArray(black_pieces);


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
        // put it in the middle of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point middle = new Point(screenSize.width / 2, screenSize.height / 2);
        Point newLocation = new Point(middle.x - (this.getWidth() / 2),
                middle.y - (this.getHeight() / 2));
        this.setLocation(newLocation);
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
            selected_piece = matrix[mousePos[0]][mousePos[1]];
            if ((selected_piece == null ) || turn != selected_piece.color){
                click_count = 0;
                selected_piece = null;
            }


        }

        if (click_count == 2){
            if (Utils.inList(selected_piece.legalMoves, mousePos)) {
                if (this.movePiece(selected_piece, mousePos, false))
                    change_turn();
            }

        click_count = 0;
        selected_piece = null;
    }

    panel.repaint();

    }



    public void showLastMove(Graphics g)
    {
        if (start_move == null || end_move == null)
            return;

        g.setColor(new Color(75,75,75, 120));
        g.fillRect(start_move[1] * Constants.square_size, start_move[0] * Constants.square_size, Constants.square_size, Constants.square_size);
        g.fillRect(end_move[1] * Constants.square_size, end_move[0] * Constants.square_size, Constants.square_size, Constants.square_size);
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




                Font myFont = new Font ("Courier New", Font.BOLD, 20);
                g.setFont(myFont);

                Graphics2D g2 = (Graphics2D) g;
                if (i == 7)
                    g2.drawString(MoveMap.getLetter(j), x + 85, y + 95);

                if (j == 0)
                    g2.drawString(String.valueOf(8 - i), x + 5, y + 20);



            }
        }
        showLastMove(g);


        // Draw the pieces
        ArrayList<Piece> ap = new ArrayList<>(all_pieces);
        for (Piece piece : ap)
            piece.draw(g);
        //black_pieces.get(0).showLegalMoves(g, matrix);

        if (selected_piece != null)
            selected_piece.showLegalMoves(g, matrix);

    }

    public void immediate_paint(){
        panel.paintImmediately(new Rectangle(0, 0, Constants.square_size * 8, Constants.square_size * 8));
    }



    public void makeBotMove() {

        white_pieces = Utils.shuffleArray(white_pieces);
        black_pieces = Utils.shuffleArray(black_pieces);

        if (move_num > 0 && move_num <= 10){
            makeMapMove();
            return;
        }


        long startTime = System.currentTimeMillis();

        if (turn == PieceColor.BLACK) {
            System.out.println("minimax : " + (minimax(Constants.search_depth, Integer.MIN_VALUE, Integer.MAX_VALUE, false)));
        }



        if (turn == PieceColor.WHITE && Constants.against_self) {
            System.out.println("minimax : " + (minimax(Constants.search_depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true)));
        }


        long endTime = System.currentTimeMillis();
        long codeTime = endTime - startTime;
        if (codeTime < 1000) {
            try {
                Thread.sleep(1000 - codeTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



    }

    public void change_turn()
    {
        turn = Utils.otherTeam(turn);
        selected_piece = null;
        immediate_paint();
        //this.getContentPane().repaint();
        if (! Constants.do_minimax)
            return;
        if (Constants.against_self || turn == PieceColor.BLACK)
            makeBotMove();


    }


    public void makeMapMove(){
        String move_str = MoveMap.getNextMove(move_sequence);
        if (move_str == null)
        {
            move_num = 11;
            makeBotMove();
            return;
        }

        System.out.println(move_str);
        char first_letter = move_str.charAt(0);

        int[] new_pos = MoveMap.getMove( move_str.substring(move_str.length() - 2));
        int col = new_pos[1];
        Piece moving_piece = null;
        // find which piece to move
        // pawn move
        if (move_str.length() == 2){
            for (int i = 0; i < 7 ; i++){
                moving_piece = matrix[i][col];
                if (moving_piece != null && moving_piece.name.equals("pawn") && moving_piece.color == turn){
                    break;
                }

            }

        }


        else if(move_str.length() > 2){
            //other piece class

            Piece sim = Piece.getPiece(first_letter, Utils.otherTeam(turn));
            sim.pos = new_pos;
            Piece p;
            // simulate a piece in the end square,
            // ift one of it's legal moves contains the same class the that's the piece to move
            for (int[] m : sim.getPseudoMoves(matrix)) {
                p = matrix[m[0]][m[1]];
                if (p != null &&
                        p.name.equals(sim.name)) {
                    //found the piece that has to move to that square
                    moving_piece = p;
                    break;
                }

            }
        }
        // move the piece
        movePiece(moving_piece, new_pos, false);
        change_turn();

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
        if (target != null){
            if (target.color == PieceColor.WHITE)
                white_pieces.remove(target);
            else
                black_pieces.remove(target);
            all_pieces.remove(target);
        }


        // update the pieces legal moves
        for (Piece piece_ : all_pieces) {
            piece_.getLegalMoves(this.matrix);
        }

        this.calculateCheck();
        // if the king is still in check after the move
        if ((piece.color == PieceColor.WHITE && this.white_in_check) ||
                (piece.color == PieceColor.BLACK && this.black_in_check)) {
            status = false;
        }
        // return everything to normal because it's only a simulation and only returns bool
        if (target != null){
            if (target.color == PieceColor.WHITE)
                white_pieces.add(target);
            else
                black_pieces.add(target);
            all_pieces.add(target);
        }
        piece.move(original_pos, matrix);
        matrix[new_pos[0]][new_pos[1]] = target;
        white_in_check = white_check;
        black_in_check = black_check;
        // update the pieces legal moves
        for (Piece piece_ : all_pieces) {
            piece_.getLegalMoves(matrix);
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
                    if (target.color == PieceColor.WHITE)
                        white_in_check = true;
                    else
                        black_in_check = true;

                }
            }
        }

    }

    public void calculateMate(PieceColor color) {
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
            black_in_mate = false;
        } else {
            black_in_mate = true;
            white_in_mate = false;
        }
    }


    public boolean movePiece(Piece piece, int [] new_pos, boolean inSearch)
    {
        if ( !inSearch && !this.simulateMove(piece, new_pos))
            return false;

        if (!inSearch) {
            move_sequence = move_sequence + MoveMap.getMapMove(piece, new_pos, matrix) + " ";
        }
        start_move = piece.pos;
        end_move = new_pos;


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
        piece.moveNumber ++;
        move_num ++;


        for (Piece p : all_pieces) {
            p.getLegalMoves(matrix);
        }


        this.calculateCheck();

        calculateMate(PieceColor.WHITE);
        calculateMate(PieceColor.BLACK);

        stalemate = (white_in_mate && !white_in_check) || (black_in_mate && !black_in_check);
        if (stalemate) {
            System.out.println("stalemate");
        }
        //if (!stalemate && (black_in_mate || white_in_mate))
        //    System.out.println("mate");



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


    int minimax(int depth, int alpha, int beta,  boolean isMaximizing){

        if (stalemate)
            return 0;

        if (white_in_mate) {
            return Integer.MIN_VALUE;
        }
        if (black_in_mate) {
            return Integer.MAX_VALUE;
        }
        if (depth == 0)
            return evaluateBoard();

        if (Constants.show_calculations)
            immediate_paint();

        // piece, move
        Piece bestPiece = null, target = null;
        int [] bestMove = new int[2];
        int [] originalPos = new int[2];
        ArrayList<Piece> originalWhite = new ArrayList<>(white_pieces);
        ArrayList<Piece> originalBlack = new ArrayList<>(black_pieces);
        boolean flag = false;

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (Piece piece : originalWhite) {
                ArrayList<int[]> originalLegal = new ArrayList<>(piece.legalMoves);
                for (int [] move : originalLegal) {
                    if (simulateMove(piece, move) && !flag) {
                        //remember before move
                        target = matrix[move[0]][move[1]];
                        originalPos = piece.pos;
                        boolean wc = white_in_check;
                        boolean bc = black_in_check;
                        boolean wm = white_in_mate;
                        boolean bm = black_in_mate;



                        movePiece(piece, move, true);
                        int evaluation = minimax(depth - 1, alpha, beta, false);
                        if (evaluation > maxEval) {
                            maxEval = evaluation;
                            bestPiece = piece;
                            bestMove = move;
                        }

                        if (evaluation > alpha)
                            alpha = evaluation;

                        if (beta <= alpha) {
                            flag = true;
                        }

                        //unmake move
                        piece.move(originalPos, matrix);
                        white_in_check = wc;
                        black_in_check = bc;
                        white_in_mate = wm;
                        black_in_mate = bm;
                        piece.moveNumber --;
                        if (target != null)
                        {
                            matrix[move[0]][move[1]] = target;
                            black_pieces.add(target);
                            all_pieces.add(target);
                        }
                        for (Piece piece_ : all_pieces)
                            piece_.getLegalMoves(matrix);
                    }
                }

            }

            if (depth == Constants.search_depth && turn == PieceColor.WHITE) {
                movePiece(bestPiece, bestMove, false);
                change_turn();

            }

            return maxEval;


        }

        else {
            int minEval = Integer.MAX_VALUE;
            for (Piece piece : originalBlack) {
                ArrayList<int[]> originalLegal = new ArrayList<>(piece.legalMoves);
                for (int [] move : originalLegal) {
                    if (simulateMove(piece, move) && !flag) {
                        //remember before move
                        target = matrix[move[0]][move[1]];
                        originalPos = piece.pos;
                        boolean wc = white_in_check;
                        boolean bc = black_in_check;
                        boolean wm = white_in_mate;
                        boolean bm = black_in_mate;

                        //make the move
                        movePiece(piece, move, true);
                        int evaluation = minimax( depth - 1, alpha, beta, true);
                        if (evaluation < minEval) {
                            minEval = evaluation;
                            bestPiece = piece;
                            bestMove = move;
                        }

                        if (evaluation < beta)
                            beta = evaluation;
                        if (beta <= alpha) {
                            flag = true;
                        }

                        //unmake move
                        piece.move(originalPos, matrix);
                        white_in_check = wc;
                        black_in_check = bc;
                        white_in_mate = wm;
                        black_in_mate = bm;
                        piece.moveNumber --;
                        if (target != null)
                        {
                            matrix[move[0]][move[1]] = target;
                            white_pieces.add(target);
                            all_pieces.add(target);
                        }
                        for (Piece piece_ : all_pieces)
                            piece_.getLegalMoves(matrix);
                    }
                }

            }


            if (depth == Constants.search_depth && turn == PieceColor.BLACK) {
                movePiece(bestPiece, bestMove, false);
                change_turn();

            }


            return minEval;
        }

    }





}