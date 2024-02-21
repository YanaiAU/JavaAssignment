import java.util.ArrayList;
import java.util.List;

public class Position {
    private final int x;
    private final int y;
    private int movedOnIt;
    private List<ConcretePiece> piecesThatMovedOnIt = new ArrayList<>();

    public Position(int x, int y){
        this.x = x;
        this.y = y;
        this.movedOnIt = 0;
    }

    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }

    public void addMovedOnIt(ConcretePiece piece){
        if (!piecesThatMovedOnIt.contains(piece)) {
            piecesThatMovedOnIt.add(piece);
            this.movedOnIt++;
        }
    }

    public int getMovedOnIt(){
        return this.movedOnIt;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}