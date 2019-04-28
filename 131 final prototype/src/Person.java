import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

//to make the simulation work, a publicly stored color variable will help the map visualization
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;


public class Person {
	
	//size of board, got from Existence
	private int SIZE;
	public Ansi.Color color;
	
	private String name;
	private int userID;
	private int position;
	private ArrayList<Item> items = new ArrayList<>();
	private ArrayList<Item> lostItems = new ArrayList<>();
	
	Person(String str, Color c) {
		items = new ArrayList<Item>();
		name = str;
		SIZE = Existence.getExistence().SIZE;
		position = (int)(Math.random()*(SIZE*SIZE));
		color = c;
	}
	
	public int getPosition() {
		return position;
	}
	
	public int getID() {
		return userID;
	}
	
	public void registerUser() {
		//adds the user to the server's list of users as well as get userID
		this.userID = Server.getServer().registerUser(this);
	}
	
	public void addItem(String name) {
		Item item = new Item(name, color);
		items.add(item);	
		
		//the item's itemID is added directly by the server
		//might make more logical sense to have the item given a random ID on creation
		Server.getServer().registerItem(item);
	}
	
	public void loseItem() {
		//gets a random item from the user's items
		Item item = items.get(new Random().nextInt(items.size()));
		//item loses itself, sets up flags & position
		item.loseItem(position);
		//swap item to lost item list from item list
		lostItems.add(item);
		items.remove(item);
		
		Server.getServer().reportLostItem(item, userID);
	}
	
	public void recoverLostItem(int id, int pos) {
		//not yet implemented		
	}
	
	public void findItem(int foundID, int pos) {
		Iterator<Item> it = lostItems.iterator();
		//checks to see if found item is owners
		//in which case, don't need to report it to server
		//this is just to make it so an owner doesn't immediately find and pick up his own item.
		//could end up updating this so that there is a short delay before self pick up is possible
		boolean ownItem = false;
		while (it.hasNext() && ownItem == false) {
			Item i = it.next();
			if (i.getID() == foundID) {
				ownItem = true;
			}
		}
		if (!ownItem) {
			Server.getServer().reportFoundItem(foundID, pos);
		}
	}

	public void move() {
		int direction =  (int)(Math.random()*9);		
		int posX = position%SIZE;
		int posY = position/SIZE;
		//fix movement at borders by moving opposite direction from border
		if (posX == 0 && ( direction == 0 || direction == 3 || direction == 6)) {
			direction = 5;
		} else if (posY == 0 && (direction == 0 || direction == 1 || direction == 2)) {
			direction = 7;
		} else if (posY == SIZE - 1 && (direction == 6 || direction == 7 || direction == 8)) {
			direction = 1;
		} else if (posX == SIZE-1 && ( direction == 2 || direction == 5 || direction == 8)) {
			direction = 3;
		}
		
		//change position by correct amount depending on direction
		if (-1 < direction && direction < 3) {
			position = position - SIZE - (1-direction);
		} else if ( 2 < direction && direction < 6) {
			position = position - (4-direction);
		} else {
			position = position + SIZE - (7 - direction);
		}
	}
	
	public void exist() {
		this.move();
		if (this.items.size() > 0) {
			this.loseItem();
		}
		//IMPLEMENT - check notification of a found lost item
		//IMPLEMENT - change moving strategy to go to item
	}
	
	//debugging purposes - print out person info
	public void printPerson() {
		System.out.println("Name:	" + name + "	ID:	" + userID);
		Iterator<Item> it = items.iterator();
		while (it.hasNext()) {
			it.next().printItem();
		}
		System.out.println("----------------------");
	}
}
