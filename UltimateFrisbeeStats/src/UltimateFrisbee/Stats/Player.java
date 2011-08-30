package UltimateFrisbee.Stats;

import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable {
	String name;
	public String getName() {
		return name;
	}
	public int getNumber() {
		return number;
	}


	int number = -1;
	private Player(Parcel in){
		this.name=in.readString();
		this.number=in.readInt();
	}
	public Player(String name, int number) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.number = number;
	}
	public Player(String name){
		this.name = name;
	}
	@Override
	public String toString(){
		String retString = "";
		if(this.number != -1){
			retString = name + ", " + number;
		}else{
			retString = name;
		}
		return retString;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int arg1) {
		out.writeString(name);
		out.writeInt(number);
	}
	
	public static final Parcelable.Creator<Player> CREATOR
	= new Parcelable.Creator<Player>() {
		public Player createFromParcel(Parcel in) {
			return new Player(in);
		}

		public Player[] newArray(int size) {
			return new Player[size];
		}
	};
	public String getSQLPrimaryKey() {
		// TODO Auto-generated method stub
		return "\"" + name + "\"";
	}
	
}
