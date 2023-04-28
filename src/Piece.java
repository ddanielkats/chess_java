import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Piece {
    public String name;
    public PieceColor color;
    public int value;

    public int moveNumber;
    public ArrayList<int[]> legalMoves;
    public int[] pos = null;
    public BufferedImage image = null;


    public Piece(String name, PieceColor color, int value) {
        this.name = name;
        this.color = color;
        this.value = value;
        moveNumber = 1;
        legalMoves = new ArrayList<>();
        pos = new int[2];


        String imagePath = "piece_images/" + color + "_" + name + ".png";
        this.image = Utils.loadImage(imagePath);
    }

    public Piece(Piece original){
        this.name = original.name;
        this.color = original.color;
        this.value = original.value;
        this.moveNumber = original.moveNumber;
        this.legalMoves = new ArrayList<>(original.legalMoves);
        this.pos = original.pos;
        this.image = original.image;
    }


    public void move(int[] newPos, Piece[][] matrix) {
        matrix[pos[0]][pos[1]] = null;
        matrix[newPos[0]][newPos[1]] = this;
        this.pos = newPos;
    }

    protected void goToDir(Piece[][] matrix, int[] direction, ArrayList<int[]> arr) {
        int flag = 0;
        int[] step = new int[] {direction[0] - pos[0], direction[1] - pos[1]};
        for (int i = 0; i < 7; i++) {
            if (Utils.withinMatrix(direction)) {
                Piece value = matrix[direction[0]][direction[1]];

                    arr.add(direction.clone());
                    direction[0] += step[0];
                    direction[1] += step[1];
                    if (value != null)
                        break;
                }
            }
    }


    public abstract ArrayList<int[]> getPseudoMoves(Piece[][] matrix);

    public void getLegalMoves(Piece[][] matrix){
        ArrayList<int[]> pseudo_moves = getPseudoMoves(matrix);
        Piece value;

        ArrayList<int[]> moves = new ArrayList<>();
        for (int[] move : pseudo_moves){
            if (Utils.withinMatrix(move)) {
                if (Utils.matrixIn(matrix, move) != null && Utils.matrixIn(matrix, move).color == this.color)
                    continue;
                moves.add(move);
            }

        }

        legalMoves.clear();
        legalMoves = moves;

    }


    public void showLegalMoves(Graphics g, Piece[][] matrix) {

        g.setColor(new Color(0,0,0, 128));



        int radius = Constants.square_size / 6;
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5)); // set the stroke thickness

        for (int[] move : legalMoves) {
            if (Utils.matrixIn(matrix, move) != null){
                g.drawOval(move[1] * Constants.square_size,
                        move[0] * Constants.square_size,
                        Constants.square_size,
                        Constants.square_size);
                continue;
            }

            g.fillOval(move[1] * Constants.square_size + Constants.square_size / 2 - radius/2 ,
                    move[0] * Constants.square_size + Constants.square_size / 2 - radius/2,
                    radius,
                    radius);

        }
    }


    public static Piece getPiece(char letter, PieceColor color)
    {
        return switch (letter) {
            case 'K' -> new King(color);
            case 'N' -> new Knight(color);
            case 'B' -> new Bishop(color);
            case 'Q' -> new Queen(color);
            case 'R' -> new Rook(color);

            default -> new Pawn(color);
        };


    }



    public void draw(Graphics g) {
        g.drawImage(image, pos[1] * Constants.square_size, pos[0] * Constants.square_size, null);

    }

    @Override
    public String toString() {
        return color.name().charAt(0) + "_" + name.charAt(0);
    }
}
