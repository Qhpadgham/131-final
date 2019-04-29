//to make the simulation work, a publicly stored color variable will help the map visualization
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;

public class Item {

	//size of board, got from Existence
	private int SIZE;
	public Ansi.Color color;
	//this found variable is to distinguish in the visualization between lost items that have and have not been found by another user yet, would not be needed in real code
	public boolean isFound;
	public boolean beenReported;
	
	private String name;
	private int itemID;
	private int position;

	
	public Item(String str, Color c) {
		name = str;
		Existence.getExistence();
		SIZE = Existence.SIZE;
		color = c;
		beenReported = false;
	}
	
	public int getID() {
		return itemID;
	}
	
	public int getPosition() {
		return position;
	}
		
	public void setID(int id) {
		itemID = id;
	}
	
	public void loseItem(int pos) {
		position = pos;
	}
	
	public boolean isInRange(int pos) {
		boolean closeX = Math.abs(position/SIZE - pos/SIZE) < 2;
		boolean closeY = Math.abs(position%SIZE - pos%SIZE) < 2;
		
		//if both the x and y coordinate are within 1 unit, that is considered close enough to find it
		if (closeX && closeY) {
			return true;
		} else {
			return false;
		}
	}
	
	
	//debugging purposes - print item to console
	public String toString() {
	    return String.format( "Item: %15s | ID: %-8d", name, itemID);
	}
	
	public void printItem() {
		System.out.println(this.toString());
	}
}
