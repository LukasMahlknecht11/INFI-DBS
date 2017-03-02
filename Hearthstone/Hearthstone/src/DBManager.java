import java.sql.*;
import java.util.ArrayList;

public class DBManager {
	
	private static DBManager instance;
	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_NAME = "Hearthstone";
	static final String DB_URL = "jdbc:mysql://localhost/" + DB_NAME;
	static final String USER = "root";
	static final String PASS = "";
	
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private String SQL = "";
	
	private DBManager()
	{
		try
		{
			Class.forName(JDBC_DRIVER);
			this.conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			System.out.println("\nSuccessfully connected to Database " + DB_NAME + "\n");
		}
		catch(SQLException se)
		{
			se.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static DBManager getInstance() 
	{
		if(DBManager.instance == null) 
			DBManager.instance = new DBManager();
	    return DBManager.instance;
	}
	
	public void closeConnection()
	{
		try
		{
			if(stmt != null)
				stmt.close();
			if(pstmt != null)
				pstmt.close();
			if(conn != null)
			conn.close();
			
		}
		catch(SQLException se)
		{
			se.printStackTrace();
		}
	}
	
	public ArrayList<Classes> readClasses(){
		ArrayList<Classes> classes = new ArrayList<Classes>();
		
		try
		{
			SQL = "SELECT * FROM Classes;";
			rs = stmt.executeQuery(SQL);
			
			while(rs.next())
			{
				classes.add(new Classes(rs.getInt("classID"), rs.getString("name"), rs.getInt("health"), rs.getString("heropower")));
			}
			
		}
		catch(SQLException se)
		{
			se.printStackTrace();
		}
		
		return classes;
	}
	
	public ArrayList<Deck> readDecksFromClass(Classes classes){
		ArrayList<Deck> decks = new ArrayList<Deck>();
		
		try{
			SQL = "SELECT * FROM Deck WHERE classID = " + classes.getClassID() + ";";
			rs = stmt.executeQuery(SQL);
			
			while(rs.next()){
				decks.add(new Deck(rs.getInt("deckID"), rs.getString("name"), rs.getInt("classID")));
			}
		}
		catch(SQLException se){
			se.printStackTrace();
		}
		
		return decks;
	}
	
	public ArrayList<Deck> readDecksFromCard(Card card){
		ArrayList<Deck> decks = new ArrayList<Deck>();
		
		try{
			SQL = "SELECT distinct cardsindecks.deckID, name, classID FROM cardsindecks JOIN deck USING(deckID) WHERE cardID = " + card.getCardID() + ";";
			rs = stmt.executeQuery(SQL);
			
			while(rs.next()){
				decks.add(new Deck(rs.getInt("deckID"), rs.getString("name"), rs.getInt("classID")));
			}
		}
		catch(SQLException se){
			se.printStackTrace();
		}
		
		return decks;
	}
	
	public ArrayList<Card> readCards(){
		ArrayList<Card> cards = new ArrayList<Card>();
		
		try{
			SQL = "SELECT * FROM card;";
			rs = stmt.executeQuery(SQL);
			
			while(rs.next()){
				cards.add(new Card(rs.getInt("cardID"), rs.getString("name"), rs.getInt("mana"), rs.getInt("attack"), rs.getInt("health")));
			}
		}
		catch(SQLException se){
			se.printStackTrace();
		}
		
		return cards;
	}
	
	public ArrayList<Card> readCardsFromDeck(Deck deck){
		ArrayList<Card> cards = new ArrayList<Card>();
		
		try{
			SQL = "SELECT distinct cardsindecks.cardID, name, mana, attack, health FROM cardsindecks JOIN card USING(cardID) WHERE deckID = " + deck.getDeckID() + ";";
			rs = stmt.executeQuery(SQL);
			
			while(rs.next()){
				cards.add(new Card(rs.getInt("cardID"), rs.getString("name"), rs.getInt("mana"), rs.getInt("attack"), rs.getInt("health")));
			}
		}
		catch(SQLException se){
			se.printStackTrace();
		}
		
		return cards;
	}
	
	public void saveCard(Card card){
		Card c = null;
		
		try
		{
			SQL = "SELECT * FROM card WHERE cardID=" + card.getCardID() + ";";
			rs = stmt.executeQuery(SQL);
			
			while(rs.next())
			{
				c = new Card(0, "", 0, 0 ,0);
			}
			
			if(c == null)
			{
				SQL = "INSERT INTO card (cardID, name, mana, attack, health) VALUES (?,?,?,?,?);";
				pstmt = conn.prepareStatement(SQL);
				pstmt.setInt(1, card.getCardID());
				pstmt.setString(2, card.getName());
				pstmt.setInt(3, card.getMana());
				pstmt.setInt(4, card.getAttack());
				pstmt.setInt(5, card.getHealth());
				pstmt.executeUpdate();
			}
			else
			{
				SQL = "UPDATE card SET name=?, mana=?, attack=?, health=? WHERE cardID=" + card.getCardID();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, card.getName());
				pstmt.setInt(2, card.getMana());
				pstmt.setInt(3, card.getAttack());
				pstmt.setInt(4, card.getHealth());
				pstmt.executeUpdate();
			}	
		}
		catch(SQLException se)
		{
			se.printStackTrace();
		}
	}
	
	public void saveDeck(Deck deck){
		Deck d = null;
		
		try
		{
			SQL = "SELECT * FROM deck WHERE deckID=" + deck.getDeckID() + ";";
			rs = stmt.executeQuery(SQL);
			
			while(rs.next())
			{
				d = new Deck(0, "", 0);
			}
			
			if(d == null)
			{
				SQL = "INSERT INTO deck (deckID, name, classID) VALUES (?,?,?);";
				pstmt = conn.prepareStatement(SQL);
				pstmt.setInt(1, deck.getDeckID());
				pstmt.setString(2, deck.getName());
				pstmt.setInt(3, deck.getClassID());
				pstmt.executeUpdate();
			}
			else
			{
				SQL = "UPDATE deck SET name=?, deckID=? WHERE deckID=" + deck.getDeckID();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, deck.getName());
				pstmt.setInt(2, deck.getClassID());
				pstmt.executeUpdate();
			}
			
			ArrayList<Card> cards = new ArrayList<Card>();
			cards = deck.getCards();
		
			for(Card c: cards)
			{
				c.save();
			}
		}
		catch(SQLException se)
		{
			se.printStackTrace();
		}
		
	}

	public void saveClass(Classes classes){
		Classes c = null;
		
		try
		{
			SQL = "SELECT * FROM classes WHERE classID=" + classes.getClassID() + ";";
			rs = stmt.executeQuery(SQL);
			
			while(rs.next())
			{
				c = new Classes(0, "", 0, "");
			}
			
			if(c == null)
			{
				SQL = "INSERT INTO classes (classID, name, health, heropower) VALUES (?,?,?,?);";
				pstmt = conn.prepareStatement(SQL);
				pstmt.setInt(1, classes.getClassID());
				pstmt.setString(2, classes.getName());
				pstmt.setInt(3, classes.getHealth());
				pstmt.setString(4, classes.getHeropower());
				pstmt.executeUpdate();
			}
			else
			{
				SQL = "UPDATE classes SET name=?, health=?, heropower=? WHERE classID=" + classes.getClassID();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, classes.getName());
				pstmt.setInt(2, classes.getHealth());
				pstmt.setString(3, classes.getHeropower());
				pstmt.executeUpdate();
			}
			
			ArrayList<Deck> decks = new ArrayList<Deck>();
			decks = classes.getDecks();
		
			for(Deck d: decks)
			{
				d.save();
			}
		}
		catch(SQLException se)
		{
			se.printStackTrace();
		}
	}
	
	public void deleteCard(Card card)
	{
		try
		{
			SQL = "DELETE FROM card WHERE cardID =" + card.getCardID() + ";";
			stmt.executeUpdate(SQL);
		}
		catch(SQLException se)
		{
			se.printStackTrace();
		}
	}

	public void deleteDeck(Deck deck)
	{
		try
		{
			SQL = "DELETE FROM deck WHERE deckID =" + deck.getDeckID() + ";";
			stmt.executeUpdate(SQL);
		}
		catch(SQLException se)
		{
			se.printStackTrace();
		}
	}

	public void deleteClass(Classes classes)
	{
		try
		{
			SQL = "DELETE FROM classes WHERE classID =" + classes.getClassID() + ";";
			stmt.executeUpdate(SQL);
		}
		catch(SQLException se)
		{
			se.printStackTrace();
		}
	}

}
