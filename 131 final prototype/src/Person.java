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
	
	// instances for movement strategy to not keep remaking them over and over
	public static MovementStrategy movementRandom = new MoveRandomly();
	public static MovementStrategy movementToItem = new MoveToItem();
	
	private String name;
	private int userID;
	private int position;
	private ArrayList<Item> items = new ArrayList<>();
	private ArrayList<Item> lostItems = new ArrayList<>();
	private ArrayList<Item> reportedItems = new ArrayList<>();
	private ArrayList<Item> locatedItems = new ArrayList<>();
	private Random random = new Random();
	private Server server;
	private MovementStrategy movement;
	private int destination;
	
	
	Person(String str, Color c) {
		items = new ArrayList<Item>();
		name = str;
		Existence.getExistence();
		SIZE = Existence.SIZE;
		position = random.nextInt(SIZE*SIZE);
		color = c;
		server = Server.getServer();
		movement = new MoveRandomly();
		destination = 0;
	}
	
	public int getPosition() {
		return position;
	}
	
	public int getID() {
		return userID;
	}
	
	public void registerUser() {
		//adds the user to the server's list of users as well as get userID
		this.userID = server.registerUser(this);
	}
	
	public void addItem(String name) {
		Item item = new Item(name, color);
		items.add(item);	
		
		//the item's itemID is added directly by the server
		//might make more logical sense to have the item given a random ID on creation
		server.registerItem(item);
	}
	
	private void loseItem() {
		//gets a random item from the user's items
		Item item = items.get(random.nextInt(items.size()));
		//item loses itself, sets up flags & position
		item.loseItem(position);
		//swap item to lost item list from item list
		lostItems.add(item);
		items.remove(item);
	}
	
	private void reportItem() {
		for (Iterator<Item> it = lostItems.iterator(); it.hasNext();) {
			Item rememberedItem = it.next();
			if (random.nextDouble() > 0.9) {
				server.reportLostItem(rememberedItem, userID);
				reportedItems.add(rememberedItem);
				it.remove();
			}
		}
	}

	//First int is item id, second is position
	public void update(Integer[] itemData) {
		for (Iterator<Item> it = reportedItems.iterator(); it.hasNext();) {
			Item reportedItem = it.next();
			if (reportedItem.getID() == itemData[0]) {
				locatedItems.add(reportedItem);
				it.remove();
			}
		}
	}
	
	public void reportNearbyItem(int foundID, int pos) {
		Iterator<Item> it = reportedItems.iterator();
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
			server.reportFoundItem(foundID, pos);
		}
	}
	
	private void pickUpItem() {
		Item foundItem = locatedItems.get(0);
		foundItem.isFound = false;
		items.add(foundItem);
		locatedItems.remove(0);
		server.reportItemRecovered(foundItem.getID());
	}


	
	public void exist() {
		this.reportItem();
		if (this.items.size() > 0 && (lostItems.size() + reportedItems.size() + locatedItems.size()) < 1) {
			this.loseItem();
		}
		if (locatedItems.size() > 0) {
			if (destination == position) {
				pickUpItem();
				destination = 0;
				movement = movementRandom;
			} else if (destination == 0) {
				destination = locatedItems.get(0).getPosition();
				movement = movementToItem;
				
			}
		}
		position = this.movement.move(position, SIZE, destination);
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

interface MovementStrategy {
	int move(int position, int SIZE, int destination);
}

class MoveRandomly implements MovementStrategy {

	public int move(int position, int SIZE, int destination) {

		int posX = position%SIZE;
		int posY = position/SIZE;
		int direction =  (int)(Math.random()*9);
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
		
		if (-1 < direction && direction < 3) {
			position = position - SIZE - (1-direction);
		} else if ( 2 < direction && direction < 6) {
			position = position - (4-direction);
		} else {
			position = position + SIZE - (7 - direction);
		}
		return position;
	}
}
	
class MoveToItem implements MovementStrategy {

	public int move(int position, int SIZE, int destination) {
		int posX = position%SIZE;
		int posY = position/SIZE;
		int itemX = destination%SIZE;
		int itemY = destination/SIZE;
		int direction;
		if (Math.abs(posX-itemX) > Math.abs(posY-itemY)) {
			direction = (posX-itemX > 0) ? 3 : 5;
		} else {
			direction = (posY-itemY > 0) ? 1 : 7;
		}
		if (-1 < direction && direction < 3) {
			position = position - SIZE - (1-direction);
		} else if ( 2 < direction && direction < 6) {
			position = position - (4-direction);
		} else {
			position = position + SIZE - (7 - direction);
		}
		return position;
	}
}


