import static org.fusesource.jansi.Ansi.Color.BLUE;
import static org.fusesource.jansi.Ansi.Color.CYAN;
import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.MAGENTA;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.Color.WHITE;
import static org.fusesource.jansi.Ansi.Color.YELLOW;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;


public class Engine {
	

	public static final String ANSI_CLS = "\u001b[2J";
	
	static int numOfUsers = 5;
	static int numOfItems = 1;
	static int SIZE = 15;
	static int dropRate = 5;
	
	static ArrayList<String> items = new ArrayList<>(Arrays.asList("air freshener","apple","beef","bottle","bottle cap","bow","bowl","bread","bucket","button","camera","carrots","cat","CD","charger","checkbook","cinder block","coasters","conditioner","controller","cookie jar","couch","cup","deodorant","desk","door","drawer","eraser","eye liner","fake flowers","floor","flowers","food","fork","glass","glow stick","grid paper","hair brush","helmet","house","key chain","keyboard","lamp shade","leg warmers","lotion","magnet","mirror","model car","monitor","mop","mouse pad","mp3 player","needle","outlet","paint brush","paper","pen","perfume","phone","photo album","pillow","plastic fork","plate","puddle","purse","radio","remote","ring","rubber duck","rusty nail","scotch tape","screw","seat belt","shirt","sidewalk","sketch pad","slipper","socks","soda can","spoon","stockings","stop sign","sun glasses","teddies","thermostat","thread","tire swing","toe ring","toilet","tooth picks","toothbrush","toothpaste","truck","twister","USB drive","wagon","wallet","white out","window","zipper"));
	static ArrayList<String> names = new ArrayList<>(Arrays.asList("Jazmine", "Andria", "Marylou", "Artie", "Shanna", "Adriane", "Camie", "Kristi", "Lisette", "Elvis", "Rosaline", "Juan", "Kaila", "Martin", "Kyra", "Leonila", "Abby", "Moriah", "Lauryn", "Tai"));
	static ArrayList<Ansi.Color> colors =  new ArrayList<>(Arrays.asList(BLUE,CYAN,GREEN,MAGENTA,RED,WHITE,YELLOW));
	
	
	
	static Existence existence = Existence.getExistence();
	static Server server = Server.getServer();
	
	
	public static void main(String[] args) throws InterruptedException {
		AnsiConsole.systemInstall();
		
		//just randomizes the list of names and items
		Collections.shuffle(names);
		Collections.shuffle(items);
		
		//give existence the size of board variable
		existence.SIZE = SIZE;
		
		//set up users and their items.
		for (int j = 0; j < numOfUsers; j++) {
			Ansi.Color c = colors.get(j%7);
			Person newUser = new Person(names.get(j), c);
			newUser.registerUser();
			for (int i = 0; i < numOfItems; i++) {
				if(items.size() > 0) {
					newUser.addItem(items.get(0));
					items.remove(0);
				}
			}
		}
		
		//main loop for people moving and dropping items + updating the board.
		try {
			//press enter to quit 
			while (System.in.available() == 0) {
				existence.printBoard();
				TimeUnit.SECONDS.sleep(1);
				AnsiConsole.out.println(ANSI_CLS);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
