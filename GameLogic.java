import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameLogic implements PlayableLogic{
    private final int BOARD_SIZE = 11;
    private final ConcretePlayer player1 = new ConcretePlayer(true);
    private final ConcretePlayer player2 = new ConcretePlayer(false);
    private ConcretePiece[][] board = new ConcretePiece[BOARD_SIZE][BOARD_SIZE];
    private Position[][] positionsBoard = new Position[BOARD_SIZE][BOARD_SIZE];
    private boolean secondPlayerTurn = true;
    private int attackerPawnsLeft = 24;
    private List<ConcretePiece> pieces = new ArrayList<>();
    private List<Position> positions = new ArrayList<>();
    private static ConcretePlayer winner;
    private boolean gameFinished;

    public GameLogic(){
        reset();
    }

    private void setBoard(){
        String[][] stringBoard = new String[][]{
                {"", "", "", "♟", "♟", "♟", "♟", "♟", "", "", ""},
                {"", "", "", "", "", "♟", "", "", "", "", ""},
                {"", "", "", "", "", "", "", "", "", "", ""},
                {"♟", "", "", "", "", "♙", "", "", "", "", "♟"},
                {"♟", "", "", "", "♙", "♙", "♙", "", "", "", "♟"},
                {"♟", "♟", "", "♙", "♙", "♔", "♙", "♙", "", "♟", "♟"},
                {"♟", "", "", "", "♙", "♙", "♙", "", "", "", "♟"},
                {"♟", "", "", "", "", "♙", "", "", "", "", "♟"},
                {"", "", "", "", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "♟", "", "", "", "", ""},
                {"", "", "", "♟", "♟", "♟", "♟", "♟", "", "", ""},
        };
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                String pieceType = stringBoard[col][row];
                switch (pieceType) {
                    case "♟" -> {
                        ConcretePiece piece = new Pawn(this.player2);
                        board[row][col] = piece;
                    }
                    case "♔" -> {
                        ConcretePiece piece = new King(this.player1);
                        board[row][col] = piece;
                    }
                    case "♙" -> {
                        ConcretePiece piece = new Pawn(this.player1);
                        board[row][col] = piece;
                    }
                    case "0", "" -> board[row][col] = null;
                }
            }
        }
        setPiecesNumbers();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if(board[row][col] != null) {
                    this.pieces.add(board[row][col]);
                }
            }
        }
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                positionsBoard[row][col] = new Position(row, col);
                if(board[row][col] != null) {
                    positionsBoard[row][col].addMovedOnIt(board[row][col]);
                }
                this.positions.add(positionsBoard[row][col]);
            }
        }
    }

    private void setPiecesNumbers(){
        String[][] locations = new String[][]{
                {"", "", "", "1", "2", "3", "4", "5", "", "", ""},
                {"", "", "", "", "", "6", "", "", "", "", ""},
                {"", "", "", "", "", "", "", "", "", "", ""},
                {"7", "", "", "", "", "1", "", "", "", "", "8"},
                {"9", "", "", "", "2", "3", "4", "", "", "", "10"},
                {"11", "12", "", "5", "6", "7", "8", "9", "", "13", "14"},
                {"15", "", "", "", "10", "11", "12", "", "", "", "16"},
                {"17", "", "", "", "", "13", "", "", "", "", "18"},
                {"", "", "", "", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "19", "", "", "", "", ""},
                {"", "", "", "20", "21", "22", "23", "24", "", "", ""}
        };
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if(board[col][row] != null) {
                    board[col][row].setNumber(locations[row][col]);
                }
            }
        }
    }

    private boolean kingCornerPieceWin(){
        // Check if the king is in one of the four corner squares using the string matrix
        if ((board[0][0] != null && board[0][0].getType().equals("♔")) ||
                (board[10][10] != null && board[10][10].getType().equals("♔")) ||
                (board[10][0] != null && board[10][0].getType().equals("♔")) ||
                (board[0][10] != null && board[0][10].getType().equals("♔")))  {
            this.player1.addWin();
            winner = this.player1;
            return true;
        }
        return false;
    }

    private boolean kingSurroundedWin(){
        // Check if the king is surrounded by enemy pawns
        int kingX = 0;
        int kingY = 0;

        // Find the king's position in stringBoard
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[col][row] != null && board[col][row].getType().equals("♔")) {
                    kingX = col;
                    kingY = row;
                    break;
                }
            }
        }
        // Check if the king is surrounded by enemies from all four sides (excluding corners)
        boolean surrounded = true;
        int[] dx = {0, 0, -1, 1}; // Up, down, left, right
        int[] dy = {-1, 1, 0, 0};
        for (int i = 0; i < 4; i++) {
            int targetX = kingX + dx[i];
            int targetY = kingY + dy[i];
            if (targetX >= 0 && targetX < BOARD_SIZE && targetY >= 0 && targetY < BOARD_SIZE) {
                if (board[targetX][targetY] == null || !board[targetX][targetY].getType().equals("♟")) {
                    surrounded = false;
                    break;
                }
            }
        }
        if (surrounded) { this.player2.addWin();}
        winner = this.player2;
        return surrounded;
    }

    private boolean allAttackerPawnsCaptured(){
        if (this.attackerPawnsLeft == 0){
            this.player1.addWin();
            winner = this.player2;
            return true;
        }
        return false;
    }

    private void EatNeighbors(Position pos) {
        int x = pos.getX(), y = pos.getY();
        ConcretePiece piece = board[x][y];
        if (piece.getType().equals(("♔"))){return;}
        String neighbor = "";
        String doubleNeighbor = "";
        if (piece.getType().equals("♙")){
            neighbor = "♟";
            doubleNeighbor = "♙";
        }
        if (piece.getType().equals("♟")){
            neighbor = "♙";
            doubleNeighbor = "♟";
        }
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};
        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            if (newX >= 0 && newX <= 10 && newY >= 0 && newY <= 10 && board[newX][newY] != null &&
                    board[newX][newY].getType().equals(neighbor)) {
                if ((newX == 0 && x == 1) || (newX == 10 && x == 9) ||
                        (newY == 0 && y == 1) || (newY == 10 && y == 9)) {
                    board[newX][newY] = null;
                    piece.addKill();
                    if(neighbor.equals("♟")){ this.attackerPawnsLeft--;}
                } else {
                    int nextX = x + 2 * dx[i];
                    int nextY = y + 2 * dy[i];
                    if (nextX >= 0 && nextX <= 10 && nextY >= 0 && nextY <= 10 && ((board[nextX][nextY] != null &&
                            board[nextX][nextY].getType().equals(doubleNeighbor)) || ((nextX == 0 && nextY == 0)) ||
                            (nextX == 10 && nextY == 0) || (nextX == 0 && nextY == 10) || (nextX == 10 && nextY == 10))){
                        board[newX][newY] = null;
                        piece.addKill();
                        if(neighbor.equals("♟")){ this.attackerPawnsLeft--;}
                    }
                }
            }
        }
    }

    private void printStats(){
        this.pieces.sort(new MovesComparator());
        for (ConcretePiece piece : this.pieces) {
            if(!(piece.moves.isEmpty())) {
                System.out.println(piece + piece.movesToString());
            }
        }
        printStars();
        this.pieces.sort(new KillsComparator());
        for (ConcretePiece piece : this.pieces) {
            if(piece.getKills() != 0) {
                System.out.println(piece + Integer.toString(piece.getKills()) + " kills");
            }
        }
        printStars();
        this.pieces.sort(new DistComparator());
        for (ConcretePiece piece : this.pieces) {
            if (piece != null && piece.getDist() != 0) {
                System.out.println(piece + Integer.toString(piece.getDist()) + " squares");
            }
        }
        printStars();
        this.positions.sort(new MovedOnItComparator());
        for (Position pos : this.positions) {
            if (pos != null && pos.getMovedOnIt() > 1) {
                System.out.println(pos + Integer.toString(pos.getMovedOnIt()) + " pieces");
            }
        }
        printStars();
    }

    private void printStars()
    {
        for (int i = 0;i<75;i++) {System.out.print("*");}
        System.out.print("\n");
    }

    @Override
    public boolean move(Position a, Position b) {
        ConcretePiece piece = (ConcretePiece) getPieceAtPosition(a);
        // There is no piece at the starting position.
        if (piece == null) {return false;}

        // Position b is on one of the edge squares
        if (!piece.getType().equals("♔") && ((b.getX() == 0 || b.getX() == 10) && (b.getY() == 0 || b.getY() == 10))){
            return false;
        }

        // The other player's turn.
        if ((piece.getOwner() != (this.secondPlayerTurn ? this.player2 : this.player1))) {return false;}

        // Calculate the row and column differences to check if it's a straight line move.
        int rowDiff = Math.abs(b.getX() - a.getX());
        int colDiff = Math.abs(b.getY() - a.getY());

        if ((rowDiff == 0 && colDiff > 0) || (rowDiff > 0 && colDiff == 0)) {
            // It's a straight line move.
            // Now, check if the path is clear (no pieces in between).
            int rowStep = Integer.compare(b.getX(), a.getX());
            int colStep = Integer.compare(b.getY(), a.getY());

            int currentRow = a.getX() + rowStep;
            int currentCol = a.getY() + colStep;

            while (currentRow != b.getX() || currentCol != b.getY()) {
                if (!(board[currentRow][currentCol] == null)) {
                    // There's a piece in the path; the move is blocked.
                    return false;
                }
                currentRow += rowStep;
                currentCol += colStep;
            }

            if (board[b.getX()][b.getY()] == null) {
                board[a.getX()][a.getY()] = null;
                board[b.getX()][b.getY()] = piece;
                this.secondPlayerTurn = !secondPlayerTurn;
                if(piece.getMoveCounter() == 0){piece.addMove(new Position(a.getX(), a.getY()));}
                piece.addMove(new Position(b.getX(), b.getY()));
                piece.addDist(Math.abs(b.getX()-a.getX() +b.getY() - a.getY()));
                EatNeighbors(b);
                positionsBoard[b.getX()][b.getY()].addMovedOnIt(piece);
                if(kingCornerPieceWin() || kingSurroundedWin() || allAttackerPawnsCaptured()){
                    printStats();
                    this.gameFinished = true;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public Piece getPieceAtPosition(Position position) {
        return board[position.getX()][position.getY()];
    }

    @Override
    public Player getFirstPlayer() {
        return this.player1;
    }

    @Override
    public Player getSecondPlayer() {
        return this.player2;
    }

    @Override
    public boolean isGameFinished() {
        return this.gameFinished;
    }

    @Override
    public boolean isSecondPlayerTurn() {
        return secondPlayerTurn;
    }

    @Override
    public void reset() {
        this.board = new ConcretePiece[BOARD_SIZE][BOARD_SIZE];
        this.secondPlayerTurn = true;
        this.attackerPawnsLeft = 24;
        this.gameFinished = false;
        setBoard();
    }

    @Override
    public void undoLastMove() {
    }

    @Override
    public int getBoardSize() {
        return BOARD_SIZE;
    }

    static class MovesComparator implements Comparator<ConcretePiece> {
        public int compare(ConcretePiece p1, ConcretePiece p2) {
            if(p1.getOwner() == GameLogic.winner && p2.getOwner() != GameLogic.winner){return -1;}
            if(p1.getOwner() != GameLogic.winner && p2.getOwner() == GameLogic.winner){return 1;}
            if(p1.getMoves().size() == p2.getMoves().size()){
                return Integer.compare(p1.getNumber(), p2.getNumber());
            }
            return Integer.compare(p1.getMoves().size(), p2.getMoves().size());
        }
    }
    static class KillsComparator implements Comparator<ConcretePiece> {
        public int compare(ConcretePiece p1, ConcretePiece p2) {
            if(p1.getKills() == p2.getKills()){
                if(p1.getNumber() == p2.getNumber()){
                    if(p1.getOwner() == GameLogic.winner && p2.getOwner() != GameLogic.winner){return -1;}
                    if(p1.getOwner() != GameLogic.winner && p2.getOwner() == GameLogic.winner){return 1;}
                }
                return Integer.compare(p1.getNumber(), p2.getNumber());
            }
            return -Integer.compare(p1.getKills(), p2.getKills());
        }
    }
    static class DistComparator implements Comparator<ConcretePiece> {
        public int compare(ConcretePiece p1, ConcretePiece p2) {
            if(p1.getDist() == p2.getDist()){
                return Integer.compare(p1.getNumber(), p2.getNumber());
            }
            return -Integer.compare(p1.getDist(), p2.getDist());
        }
    }
    static class MovedOnItComparator implements Comparator<Position> {
        public int compare(Position p1, Position p2) {
            if(p1.getMovedOnIt() == p2.getMovedOnIt()){
                if(p1.getX() == p2.getX()){
                    return Integer.compare(p1.getY(), p2.getY());
                }
                return Integer.compare(p1.getX(), p2.getX());
            }
            return -Integer.compare(p1.getMovedOnIt(), p2.getMovedOnIt());
        }
    }
}
