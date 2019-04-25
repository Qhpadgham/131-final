import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;   
import org.fusesource.jansi.AnsiConsole;


public class TestServer {
	

	public static final String ANSI_CLS = "\u001b[2J";
	public static final String ANSI_HOME = "\u001b[H";
	public static final String ANSI_BOLD = "\u001b[1m";
	public static final String ANSI_AT55 = "\u001b[10;10H";
	public static final String ANSI_REVERSEON = "\u001b[7m";
	public static final String ANSI_NORMAL = "\u001b[0m";
	public static final String ANSI_WHITEONBLUE = "\u001b[37;44m";
	  
	public static void main(String[] args) throws InterruptedException {
		AnsiConsole.systemInstall();
		 
              
         
		
		ArrayList<String> items = new ArrayList<>(Arrays.asList("air freshener","apple","beef","bottle","bottle cap","bow","bowl","bread","bucket","button","camera","carrots","cat","CD","charger","checkbook","cinder block","coasters","conditioner","controller","cookie jar","couch","cup","deodorant","desk","door","drawer","eraser","eye liner","fake flowers","floor","flowers","food","fork","glass","glow stick","grid paper","hair brush","helmet","house","key chain","keyboard","lamp shade","leg warmers","lotion","magnet","mirror","model car","monitor","mop","mouse pad","mp3 player","needle","outlet","paint brush","paper","pen","perfume","phone","photo album","pillow","plastic fork","plate","puddle","purse","radio","remote","ring","rubber duck","rusty nail","scotch tape","screw","seat belt","shirt","sidewalk","sketch pad","slipper","socks","soda can","spoon","stockings","stop sign","sun glasses","teddies","thermostat","thread","tire swing","toe ring","toilet","tooth picks","toothbrush","toothpaste","truck","twister","USB drive","wagon","wallet","white out","window","zipper"));
		ArrayList<String> names = new ArrayList<>(Arrays.asList("Jazmine", "Andria"/*, "Marylou", "Artie", "Shanna", "Adriane", "Camie", "Kristi", "Lisette", "Elvis", "Rosaline", "Juan", "Kaila", "Martin", "Kyra", "Leonila", "Abby", "Moriah", "Lauryn", "Tai"*/));
		Server s = Server.getServer();
		Collections.shuffle(items);
		for (String name : names) {
			Person newUser = s.addUser(name);
			for (int i = 0; i < 5; i++) {
				if(items.size() > 0) {
					newUser.addItem(items.get(0));
					items.remove(0);
				}
			}
		}
		try {
			int count = 0;
			while (System.in.available() == 0) {
				if (count%5 == 0) {
					s.tryDrops();
				}
				count++;
				s.printBoard();
				TimeUnit.SECONDS.sleep(1);
				AnsiConsole.out.println(ANSI_CLS);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
