import java.util.ArrayList;
import java.util.Objects;

public class ConcretePiece implements Piece{
    private ConcretePlayer owner;
    private String type;
    protected ArrayList<Position> moves;
    private int number;
    private int kills;
    private int dist;
    private int moveCounter = 0;

    // Constructor to initialize the piece with an owner and type
    public ConcretePiece(ConcretePlayer owner) {
        this.owner = owner;
        this.kills = 0;
        this.dist = 0;
        this.moves = new ArrayList<>();
    }

    @Override
    public Player getOwner() {
        return owner;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setNumber(String number) {
        if(Objects.equals(number, "")){return;}
        this.number = Integer.parseInt(number);
    }

    public int getNumber() {
        return this.number;
    }

    public void addMove(Position move){
        this.moves.add(move);
        this.moveCounter++;
    }

    public int getKills(){
        return this.kills;
    }

    public void addKill(){
        this.kills++;
    }

    public int getMoveCounter(){
        return this.moveCounter;
    }
    public ArrayList<Position> getMoves(){ return this.moves;}

    public String movesToString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < moves.size(); i++) {
            Position move = moves.get(i);
            sb.append(move);
            if (i < moves.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public int getDist() {
        return dist;
    }

    public void addDist(int addedDist) {
        this.dist += addedDist;
    }
}
