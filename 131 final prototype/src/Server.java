import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



public class Server {
	
	//Singleton
	private static Server server = new Server();
	
	//integer is userID
	private HashMap<Integer, Person> users = new HashMap<>();
	
	//integer is itemID
	private HashMap<Integer, Item> items = new HashMap<>();
	
	//first integer is itemID, second integer is the owner's ID
	private HashMap<Integer, Integer> lostItems = new HashMap<>();
	
	//list of people to report to about their lost items. First int is item id, second is position
	private ArrayList<Integer[]> reportList = new ArrayList<>();
	
	//used to generate IDs, not actually important otherwise
	private int numUsers = 0;
	private int numItems = 0;
	
	private Server() {}
	
	public static Server getServer() {
		return server;
	}
	
	//user ids are just generated in order, could change to some random number gen or hash
	public int registerUser(Person user) {
		int userID = numUsers;
		users.put(userID, user);
		numUsers++;
		return userID;
	}
	
	//item ids are just generated in order, could change to some random number gen or hash
	public void registerItem(Item item) {
		item.setID(numItems);
		items.put(numItems, item);
		numItems++;
	}
	
	public void reportLostItem(Item item, int userID) {
		lostItems.put(item.getID(), userID);
	}
	
	public void reportFoundItem(int itemID, int position) {
		Item reportedItem = items.get(itemID);
		if (lostItems.containsKey(itemID) && reportedItem.beenReported == false) {
			items.get(itemID).beenReported = true;
			//add person to list of people to report found items to
			Integer[] itemInfo = {lostItems.get(itemID), position};
			reportList.add(itemInfo);
			
			//this line is only needed for visualization to change from . to !, would not be needed in real program
			reportedItem.isFound = true;
		}
	}
	
	public void reportItemRecovered(int itemID) {
		lostItems.remove(itemID);
	}
	
	public void printUsers() {
		for(Map.Entry<Integer, Person> entry : users.entrySet()) {
		    Person value = entry.getValue();
		    value.printPerson();
		    
		} 
	}
	
	public void updateLosers() {
		for (Iterator<Integer[]> it = reportList.iterator(); it.hasNext();) {
			Integer[] itemData = it.next();
			Person loser = users.get(lostItems.get(itemData[0]));
			loser.update(itemData);
			it.remove();
		}
	}
	
	
	//these getter functions only exist to make the simulation work, you would not have these in real life
	public HashMap<Integer, Person> getUsers() {
		return users;
	}
	
	public HashMap<Integer, Item> getItems() {
		return items;
	}
	
	public HashMap<Integer, Integer> getLostItems() {
		return lostItems;
	}		
}

