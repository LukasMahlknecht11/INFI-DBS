import java.util.ArrayList;


public class Deck extends DBObject{
	
	private int deckID;
	private String name;
	private ArrayList<Card> cards;
	private int classID;
	
	public Deck(int deckID, String name, int classID){
		this.deckID = deckID;
		this.name = name;
		this.classID = classID;
		
	}
	
	public int getDeckID(){
		return this.deckID;
	}
	
	public void setDeckID(int deckID){
		this.deckID = deckID;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public int getClassID(){
		return this.classID;
	}
	
	public void setClassID(int classID){
		this.classID = classID;
	}

	public void addCard(Card card){
		cards.add(card);
	}
	
	public ArrayList<Card> getCards(){
		if(this.cards == null)
			this.cards = DBManager.getInstance().readCardsFromDeck(this);
		return this.cards;
	}
	
	public String toString(){
		return "\tDeckID: " + this.deckID + "\tDeckname: " + this.name;
	}
	
	public void save(){
		DBManager.getInstance().saveDeck(this);
	}
	
	public void delete(){
		DBManager.getInstance().deleteDeck(this);
	}
}
