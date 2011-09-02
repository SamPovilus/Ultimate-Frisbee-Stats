package UltimateFrisbee.Stats;

public class PlayerWithPlayerPointID extends Player {
	long playerPointID = 0;
	public long getPlayerPointID() {
		return playerPointID;
	}
	public void setPlayerPointID(long playerPointID) {
		this.playerPointID = playerPointID;
	}
	public PlayerWithPlayerPointID(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	public PlayerWithPlayerPointID(String name,int number) {
		super(name,number);
		// TODO Auto-generated constructor stub
	}

}
