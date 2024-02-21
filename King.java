public class King extends ConcretePiece{
    public King(ConcretePlayer owner) {
        super(owner);
        if (owner.isPlayerOne()){ setType("♔");}
    }

    public String toString() {
        return "K" + this.getNumber() + ": ";
    }
}
