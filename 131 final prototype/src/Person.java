import java.util.ArrayList;
import java.util.Iterator;

import org.fusesource.jansi.Ansi;

public class Person {
	
	private int SIZE;
	
	@SuppressWarnings("unused")
	private String name;
	private int userID;
	private int position;
	private ArrayList<Item> items;
	private int numItems;
	private boolean lostItem;
	private Ansi.Color color;
	
	Person(String str, int id, Ansi.Color c) {
		items = new ArrayList<Item>();
		name = str;
		userID = id;
		numItems = 0;
		SIZE = Server.getServer().SIZE;
		position = (int)(Math.random()*(SIZE*SIZE));
		lostItem = false;
		color = c;
	}
	
	public Ansi.Color getColor() {
		return color;
	}
	
	public int getPosition() {
		return position;
	}
	
	public int getID() {
		return userID;
	}
	
	public void addItem(String str) {
		Item newItem = new Item(str,  position, userID);
		Server s = Server.getServer();
		int itemID = s.getNewItemID();
		newItem.setID(itemID);
		items.add(newItem);	
		numItems++;
	}
	
	public void removeItem(String str) {
		Iterator<Item> it = items.iterator();
		while (it.hasNext()) {
			Item i = it.next();
			if (i.getName() == str && i.getIsLost() == false) {
				it.remove();
			}
		}
	}
	
	public void loseItem() {
		if (!lostItem) {
			lostItem = true;
			int random = (int)(Math.random() *numItems);
			Item item = items.get(random);
			item.setPosition(position);
			Server s = Server.getServer();
			boolean alreadyLost = s.reportLostItem(item, userID);
			if (!alreadyLost) {
				item.changeIsLost();
			}
		}
	}
	
	public void recoverLostItem(int id, int pos) {
		//go find item at position
		
		//change item id to found;
		/*
		Iterator<Item> it = items.iterator();
		while (it.hasNext()) {
			Item i = it.next();
			if (i.getID() == id) {
				i.changeIsLost();
				lostItem = true;
			}
		}	
		*/	
		
	}
	
	
	public void findItem(int foundID, int pos) {
		Iterator<Item> it = items.iterator();
		//checks to see if found item is owners
		//in which case, don't need to report it to server
		boolean ownItem = false;
		while (it.hasNext()) {
			Item i = it.next();
			if (i.getID() == foundID) {
				ownItem = true;
			}
		}
		if (!ownItem) {
			Server s = Server.getServer();
			s.reportFoundItem(foundID, pos);
		}
	}
	
	public void printPerson() {
		System.out.println("Name:	" + name + "	ID:	" + userID);
		Iterator<Item> it = items.iterator();
		while (it.hasNext()) {
			it.next().printItem();
		}
		System.out.println("----------------------");
	}
	
	public void move() {
		int direction =  (int)(Math.random()*9);		
		
		//fix movement at borders
		if (position%SIZE == 0 && ( direction == 0 || direction == 3 || direction == 6)) {
			direction = 5;
		} else if (-1 < position && position < SIZE && (direction == 0 || direction == 1 || direction == 2)) {
			direction = 7;
		} else if ((SIZE*SIZE - SIZE - 1) < position && position < (SIZE*SIZE) && (direction == 6 || direction == 7 || direction == 8)) {
			direction = 1;
		} else if (position%SIZE == (SIZE-1) && ( direction == 2 || direction == 5 || direction == 8)) {
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
}
