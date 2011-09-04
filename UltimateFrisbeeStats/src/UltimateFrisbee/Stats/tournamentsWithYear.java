package UltimateFrisbee.Stats;

public class tournamentsWithYear {
	String name;
	int year;
	public tournamentsWithYear(String name, int year) {
		this.year = year;
		this.name = name;
	}
	@Override
	public String toString(){
		return this.name + ", " + this.year;
	}

	public String getName(){
		return name;
	}
}
