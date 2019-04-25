import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;


public class Server {
	
	
	public int SIZE = 5;
	
	private static Server server = new Server();
	private static ArrayList<Ansi.Color> colors =  new ArrayList<>(Arrays.asList(BLUE,CYAN,GREEN,MAGENTA,RED,WHITE,YELLOW));
	private Object[][] board = new Object[SIZE][SIZE];
	private HashMap<Integer, Person> users = new HashMap<>();
	private HashMap<Integer, Item> lostItems = new HashMap<>();
	private int numUsers = 0;
	private int numItems = 0;
	
	private Server() {}
	
	public static Server getServer() {
		return server;
	}
	
	public Person addUser(String name) {
		Ansi.Color c = colors.get(0);
		Person newUser = new Person(name, numUsers, c);
		colors.remove(0);
		users.put(numUsers, newUser);
		numUsers++;
		return newUser;
	}
	
	public int getNewItemID() {
		int itemID = numItems;
		numItems++;
		return itemID;
	}
	
	public boolean reportLostItem(Item item, int userID) {
		if (!lostItems.containsKey(item.getID())) {
			lostItems.put(item.getID(), item);
			return false;
		} else {
			return true; 
		}
	}
	
	public void reportFoundItem(int itemID, int position) {
		if (lostItems.containsKey(itemID)) {
			Item item = lostItems.get(itemID);
			item.isFound = true;
			Person loser = users.get(item.getownerId());
			//send message to loser about the location of their lost item
			loser.recoverLostItem(itemID, position);
			//lostItems.remove(itemID);
		}
	}
	
	public Person getUser(int userID) {
		return users.get(userID);
	}
	
	public void printUsers() {
		for(Map.Entry<Integer, Person> entry : users.entrySet()) {
		    Person value = entry.getValue();
		    value.printPerson();
		    
		} 
	}
	
	public void printBoard() {
		for (int k = 0; k < SIZE; k++) {
			Arrays.fill(board[k], "-");
		}
		for(Map.Entry<Integer, Item> entry : lostItems.entrySet()) {
		    Item value = entry.getValue();
		    for(Map.Entry<Integer, Person> person : users.entrySet()) {
			    Person p = person.getValue();
			    value.pingPerson(p);
		    }
		    int pos = value.getPosition();	
		    int y = pos/SIZE;
		    int x = pos%SIZE;
		    board[x][y] = value;
		}
		System.out.println();
		
		for(Map.Entry<Integer, Person> entry : users.entrySet()) {
		    Person value = entry.getValue();
		    int pos = value.getPosition();	
		    int y = pos/SIZE;
		    int x = pos%SIZE;
		    board[x][y] = value;
		    value.move();
		} 
		System.out.println();
		
		
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (board[i][j] instanceof Person) {
					Ansi.Color c = ((Person) board[i][j]).getColor();
					System.out.print(" " + ansi().fg(c).a(((Person) board[i][j]).getID()).reset() + " ");
				} else if (board[i][j] instanceof Item) {
					Ansi.Color c = server.getUser(((Item) board[i][j]).getownerId()).getColor();
					if (((Item) board[i][j]).isFound) {
						System.out.print(" " + ansi().fg(c).a("!").reset() + " ");
					} else {

						System.out.print(" " + ansi().fg(c).a(".").reset() + " ");
					}
				} else {
					System.out.print(" " + board[i][j] + " ");
				}
			}
			System.out.println();
		}
	}
	
	
	public void tryDrops() {
		for(Map.Entry<Integer, Person> entry : users.entrySet()) {
		    Person value = entry.getValue();
			if ((Math.random()) > .15) {
				value.loseItem();
			}
		}
	}
}
