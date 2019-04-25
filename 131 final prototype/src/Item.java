
public class Item {
	private String name;
	private int ownerID;
	private int itemID;
	private int position;
	public boolean isLost;
	public boolean isFound;
	public int SIZE;
	
	public Item(String str, int pos, int oID) {
		name = str;
		ownerID = oID;
		position = pos;
		isLost = false;
		SIZE = Server.getServer().SIZE;
	}
	
	public String getName() {
		return name;
	}
	
	public int getID() {
		return itemID;
	}
	
	public int getownerId() {
		return ownerID;
	}
	
	public void setID(int id) {
		itemID = id;
	}
	
	public void setPosition(int pos) {
		position = pos;
	}
	
	public int getPosition() {
		return position;
	}
	
	public boolean getIsLost() {
		return isLost;
	}
	
	public void changeIsLost() {
		isLost = isLost ? false : true;
	}
	
	public String toString() {
	    return String.format( "Item: %15s | ID: %-8d | isLost: %b", name, itemID, isLost );
	}
	
	public void printItem() {
		System.out.println(this.toString());
	}
	
	private boolean isInRange(int pos) {
		if ((position/SIZE == pos/SIZE) && (Math.abs(position-pos) > 1) ){
			return false;
		}
		if ( Math.abs(position - pos) < 2 && (position%SIZE == pos%SIZE)) {
			return true;
		} else if ( Math.abs(position - SIZE - pos) < 2) {
			return true;
		} else if ( Math.abs(position + SIZE - pos) < 2) {
			return true;
		} else {
			return false;
		}
	}
	
	public void pingPerson(Person p) {
		if (p.getID() != ownerID) {
			if (this.isInRange(p.getPosition())) {
				p.findItem(itemID, position);
			}
		}	}
	
	
}
