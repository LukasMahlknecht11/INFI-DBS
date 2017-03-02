import java.util.ArrayList;


public class Classes {
	private int classID;
	private String name;
	private int health;
	private String heropower;
	private ArrayList<Deck> decks;
	
	public Classes(int classID, String name, int health, String heropower){
		this.classID = classID;
		this.name = name;
		this.health = health;
		this.heropower = heropower;
	}
	
	public int getClassID(){
		return this.classID;
	}
	
	public void setClassID(int classID){
		this.classID = classID;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public int getHealth(){
		return this.health;
	}
	
	public void setHealth(int health){
		this.health = health;
	}
	
	public String getHeropower(){
		return this.heropower;
	}
	
	public void setHeropower(String heropower){
		this.heropower = heropower;
	}
	
	public void addDeck(Deck deck){
		decks.add(deck);
	}
	
	public ArrayList<Deck> getDecks(){
		if(this.decks == null)
			this.decks = DBManager.getInstance().readDecksFromClass(this);
		return this.decks;
	}
	
	public String toString(){
		return "\tClassID: " + this.classID + "\tClass: " + this.name + "\tHealth: " + this.health + "\tHeropower: " + this.heropower;
	}
	
	public void save()
	{
		DBManager.getInstance().saveClass(this);
	}
	
	public void delete()
	{
		DBManager.getInstance().deleteClass(this);
	}
}
