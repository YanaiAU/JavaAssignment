
public class ConcretePlayer implements Player{

    private final boolean isPlayerOne;
    private int wins;
    private String playerRole;
    private int isWinner;
    public ConcretePlayer(Boolean playerTurn){
        this.isPlayerOne = playerTurn;
        this.isWinner = -1;
        setPlayerRole();
    }

    @Override
    public boolean isPlayerOne() {
        return isPlayerOne;
    }

    @Override
    public int getWins() {
        return this.wins;
    }

    public void addWin() {
        this.isWinner = 1;
        this.wins++;
    }

    public void setIsWinner(int isWinner){
        this.isWinner = isWinner;
    }

    public int getIsWinner(){
        return this.isWinner;
    }

    private void setPlayerRole(){
        if(this.isPlayerOne){this.playerRole = "D";} else { this.playerRole = "A";}
    }

    public String getPlayerRole() {
        return this.playerRole;
    }
}
