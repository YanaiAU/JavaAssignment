public class Pawn extends ConcretePiece{
    public Pawn(ConcretePlayer owner) {
        super(owner);
        if (owner.isPlayerOne()){
            setType("♙");
        }
        else{
            setType("♟");
        }
    }

    public String toString() {
        return ((ConcretePlayer)this.getOwner()).getPlayerRole() + this.getNumber() + ": ";
    }
}
