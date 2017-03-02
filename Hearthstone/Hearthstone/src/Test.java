import java.util.ArrayList;
import java.util.Scanner;


public class Test {
	public static void main(String[] args){
		
		ArrayList<Classes> classes;
		ArrayList<Deck> decks;
		ArrayList<Card> cards;
		Scanner s = new Scanner(System.in);
		String joice;
		cards = Card.getCards();
		
		System.out.println("Hearthstone database:");
		classes = DBManager.getInstance().readClasses();
		
		System.out.print("Wollen Sie alle Klassen ausgeben? [y, n]: ");
		joice = s.next();
		if(joice.equals("y")){
			System.out.println("Show all Classes:");
			for(Classes c: classes){
				System.out.println(c.toString());
			}
		}
		
		System.out.println("");
		System.out.print("Wollen Sie alle Decks ausgeben? [y, n]: ");
		joice = s.next();
		if(joice.equals("y")){
			System.out.println("Show all Decks:");
			for(Classes c: classes){
				decks = c.getDecks();

				for(Deck d: decks){
					System.out.println(d.toString());
				}
			}
		}

		System.out.println("");
		System.out.print("Wollen Sie alle Karten ausgeben? [y, n]: ");
		joice = s.next();
		if(joice.equals("y")){
			System.out.println("Show all Cards:");

			for(Card cc: cards){
				System.out.println(cc.toString());
				/*
				if(cc.getName().equals("NZoth")){
					cc.setName("NZoth");
					cc.save();
				}
				*/
			}
		}
		
		System.out.println("\nSonderabfragen: ");
		
		
		for(Classes c: classes){
			decks = c.getDecks();

			for(Deck d: decks){
				
				if(c.getClassID() == 1){
					System.out.println(d.toString());
				}
			}
		}
		
		System.out.println("");
		
		for(Classes c: classes){

			if(c.getClassID() == 1){
				decks = c.getDecks();

				for(Deck d: decks){
					cards = d.getCards();

					for(Card cc: cards){

						if(c.getClassID() == 1){
							System.out.println(cc.toString());
						}
					}
				}
			}
		}
		
		System.out.println("");
		
		for(Classes c: classes){
			if(c.getClassID() == 1){

				for(Card cc: cards){
					if(cc.getCardID() == 3){
						decks = cc.getDecks();

						for(Deck d: decks){
							System.out.println(d.toString());
						}
					}
				}
			}
		}
		
		
		s.close();
		DBManager.getInstance().closeConnection();
	}
}
