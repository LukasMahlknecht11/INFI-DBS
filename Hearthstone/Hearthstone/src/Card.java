import java.util.ArrayList;


public class Card extends DBObject{

	private int cardID;
	private String name;
	private int mana;
	private int attack;
	private int health;
	private ArrayList<Deck> decks;
	
	public Card(int cardID, String name, int mana, int attack, int health){
		this.cardID = cardID;
		this.name = name;
		this.mana = mana;
		this.attack = attack;
		this.health = health;
	}
	
	public int getCardID(){
		return this.cardID;
	}
	
	public void setCardID(int cardID){
		this.cardID = cardID;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public int getMana(){
		return this.mana;
	}
	
	public void setMana(int mana){
		this.mana = mana;
	}
	
	public int getAttack(){
		return this.attack;
	}
	
	public void setAttack(int attack){
		this.attack = attack;
	}
	
	public int getHealth(){
		return this.health;
	}
	
	public void setHealth(int health){
		this.health = health;
	}

	public void addDeck(Deck deck){
		decks.add(deck);
	}
	
	public ArrayList<Deck> getDecks(){
		if(this.decks == null)
			this.decks = DBManager.getInstance().readDecksFromCard(this);
		return this.decks;
	}
	
	public static ArrayList<Card> getCards(){
		
		return DBManager.getInstance().readCards();
	} 
	
	public String toString(){
		return "\tCardID: " + this.cardID + "\tCardname: " + this.name + "\tMana: " + this.mana + "\tAttack: " + this.attack + "\tHealth: " + this.health;
	}
	
	public void save(){
		DBManager.getInstance().saveCard(this);
	}
	
	public void delete(){
		DBManager.getInstance().deleteCard(this);
	}
}
