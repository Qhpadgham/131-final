import static org.fusesource.jansi.Ansi.ansi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.fusesource.jansi.Ansi;

public class Existence {


	public static int SIZE;
	private Object[][] board;
	
	private static Existence existence = new Existence();
	private Existence() {
		Engine.getEngine();
		SIZE = Engine.SIZE;
		board = new Object[SIZE][SIZE];
	}
	public static Existence getExistence() {
		return existence;
	}
	
	public void printBoard() {
		
		//Existence IRL doesn't need to get this information, but this simulation of existence does unfortunately.
		HashMap<Integer, Person> users = Server.getServer().getUsers();
		HashMap<Integer, Item> items = Server.getServer().getItems();
		HashMap<Integer, Integer> lostItems = Server.getServer().getLostItems();;
		HashMap<Integer, Integer> reportedItems = Server.getServer().getReportedItems();
		
		//this resets the board to be populated again for the next update
		for (int k = 0; k < SIZE; k++) {
			Arrays.fill(board[k], "-");
		}
		
		//this section is two parts: 
		//	a) first, for every item in the lost items collection it checks to see if a person is close enough
		//  a) if they are, then the person will find the item and report it to the server
		//
		//  b) secondly, it adds each item that is currently lost to the map so they can be displayed
		for(Map.Entry<Integer, Integer> entry1 : lostItems.entrySet()) {
			int lostItemID = entry1.getKey();
		    Item item = items.get(lostItemID);
		    for(Map.Entry<Integer, Person> entry2 : users.entrySet()) {
			    Person p = entry2.getValue();
			    boolean personFoundItem = item.isInRange(p.getPosition());
			    if(personFoundItem) {
			    	p.reportNearbyItem(lostItemID, item.getPosition());
			    }	    
		    }
		    int pos = item.getPosition();	
		    int x = pos/SIZE;
		    int y = pos%SIZE;
		    board[x][y] = item;
		}
		
		//Similar to the 2nd part above, this is adding every reported item to the map so they can be displayed
		for(Map.Entry<Integer, Integer> entry1 : reportedItems.entrySet()) {
			int reportedItemID = entry1.getKey();
		    Item item = items.get(reportedItemID);
		    int pos = item.getPosition();	
		    int x = pos/SIZE;
		    int y = pos%SIZE;
		    board[x][y] = item;
		}
		
		
		//Similar to the 2nd part above, this is adding every user to the map so they can be displayed.
		for(Map.Entry<Integer, Person> entry : users.entrySet()) {
		    Person person = entry.getValue();
		    int pos = person.getPosition();	
		    int x = pos/SIZE;
		    int y = pos%SIZE;
		    board[x][y] = person;
		    // .exist() handles the person's movement, changes to movement strategy if recovering an item, and losing items
		    person.exist();
		} 		
		
		//this whole section is for drawing to the screen and coloring
		//need to set it up so userIDs with 2 digits don't mess up spacing
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (board[i][j] instanceof Person) {
					Ansi.Color c = ((Person) board[i][j]).color;
					System.out.print(" " + ansi().fg(c).a(((Person) board[i][j]).getID()).reset() + " ");
				} else if (board[i][j] instanceof Item) {
					Ansi.Color c = ((Item) board[i][j]).color;
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

}
