import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Auslesen
{
	static ArrayList<Datensatz> min = new ArrayList<Datensatz>();
	static ArrayList<Datensatz> max = new ArrayList<Datensatz>();

	static long x1, x2;
	static int y1, y2;

	public static void main( String args[] )
	{
		Connection c = null;
		Statement stmt = null;

		ArrayList<ArrayList> hauptdaten = new ArrayList<ArrayList>();
		ArrayList<Datensatz> datenpromonat;
		ArrayList<Datensatz> alledaten = new ArrayList<Datensatz>();
		ArrayList<String> monatsnamen = new ArrayList<String>();
		Datensatz data = new Datensatz();
		int jahreszahl = 2016;
		int anzahlmonate = 12;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:verbrauch.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT * FROM strom_verbrauch ORDER BY datum ASC;" );
			while ( rs.next() ) {
				long datum = rs.getLong("datum");
				int value = rs.getInt("value");
				java.util.Date time = new java.util.Date(datum*1000);

				if(time.getYear() == (jahreszahl-1900)){

					alledaten.add(new Datensatz(time, value, datum));

					System.out.print("Datum = " + time);
					System.out.print("\tValue = " + value);
					System.out.println("\tUX = " + datum);
				}else{
					//System.out.println("Falsches Jahr");
				}
			}

			for(int i = 0; i < anzahlmonate; i++){								//daten Klassifizieren
				datenpromonat = new ArrayList<Datensatz>();
				for(int j = 0; j < alledaten.size(); j++){
					if(alledaten.get(j).getDatum().getMonth() == i){
						datenpromonat.add(alledaten.get(j));
					}
				}
				hauptdaten.add(datenpromonat);
			}

			for(int j = 0; j < anzahlmonate; j++){								//min und max wert des monats herausholen
				if(hauptdaten.get(j).size() > 1){
					min.add((Datensatz) hauptdaten.get(j).get(0));
					max.add((Datensatz) hauptdaten.get(j).get(hauptdaten.get(j).size()-1));
				}else{
					min.add(data);
					max.add(data);
				}
			}
			
			//monatsnamen in arraylit speichern
			monatsnamen.add("Jaenner");
			monatsnamen.add("Februar");
			monatsnamen.add("Maerz");
			monatsnamen.add("April");
			monatsnamen.add("Mai");
			monatsnamen.add("Juni");
			monatsnamen.add("Juli");
			monatsnamen.add("August");
			monatsnamen.add("September");
			monatsnamen.add("Oktober");
			monatsnamen.add("November");
			monatsnamen.add("Dezember");
			
			FileWriter fw = new FileWriter("D:\\Documents\\workspace\\serverstartverbrauch\\WebContent\\output.txt");
		    BufferedWriter bw = new BufferedWriter(fw);
		    
			for(int i =0; i < anzahlmonate; i++){									//für jeden monat berechnung des absolutwertes
				if(hauptdaten.get(i).size() > 1){
				    
				    bw.write(monatsnamen.get(i) + " " + (int)berechnungLinearInterpolation(i, jahreszahl));
				    bw.newLine();
				   
					System.out.println(berechnungLinearInterpolation(i, jahreszahl));
				}else{
					bw.write(monatsnamen.get(i) + " " + 0);
				    bw.newLine();
				    System.out.println("0");
				}	
			}
			
			bw.close();
			rs.close();
			stmt.close();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Operation done successfully");

	}
	public static boolean schaltJahrBerechnen(int x){

		if((x % 4) == 0) {
			if(((x % 100) == 0) && ((x % 4) == 0)){
				if(((x % 400) == 0) && ((x % 100) == 0) && ((x % 4) == 0)){
					return true;
				}
				return false;
			}
			return true;
		}else{
			return false;
		}

	}
	
	public static double berechnungLinearInterpolation(int monat, int jahr){
		
		double deltax, deltay;
		double k, d;
		double werterstertag, wertletztertag, absolut;
		long erstertag, letztertag;
		jahr = jahr-1900;
		
		x1 = min.get(monat).getDatum().getTime()/1000;
		y1 = min.get(monat).getValue();
		x2 = max.get(monat).getDatum().getTime()/1000;
		y2 = max.get(monat).getValue();
		
		deltax = x2-x1;
		deltay = y2-y1;
		k = deltay/deltax;
		d = ((-k)*x1)+y1;
		
		java.util.Date first = new java.util.Date(jahr, monat, 01, 00, 00, 00);
		erstertag = first.getTime()/1000;
		werterstertag = k * erstertag + d;
		
		if(monat == 1){//feber
			if(schaltJahrBerechnen(jahr) == true ) {
				java.util.Date last = new java.util.Date(jahr, monat, 29, 23, 59, 59);
				letztertag = last.getTime()/1000;
				wertletztertag = k * letztertag + d;
			}else{
				java.util.Date last = new java.util.Date(jahr, monat, 28, 23, 59, 59);
				letztertag = last.getTime()/1000;
				wertletztertag = k * letztertag + d;
			}
		}
		else if((monat == 0) || (monat == 2) || (monat == 4) || (monat == 6) || (monat == 7) || (monat == 9) || (monat == 11)){//Jänner, März, Mai, Juli, August, Okt, Dez
			java.util.Date last = new java.util.Date(jahr, monat, 31, 23, 59, 59);
			letztertag = last.getTime()/1000;
			wertletztertag = k * letztertag + d;
		}else{
			java.util.Date last = new java.util.Date(jahr, monat, 30, 23, 59, 59);
			letztertag = last.getTime()/1000;
			wertletztertag = k * letztertag + d;
		}
		
		absolut = wertletztertag-werterstertag;
		
		return absolut;
	}
}
