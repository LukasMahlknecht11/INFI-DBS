
public class Datensatz {

	private java.util.Date datum;
	private int value;
	private long ux;
	
	public Datensatz(){
		this.datum = null;
		this.value = 0;
		this.ux = 0;
	}
	
	public Datensatz(java.util.Date datum, int value, long ux){
		this.datum = datum;
		this.value = value;
		this.ux = ux;
	}
	
	public java.util.Date getDatum() {
		return datum;
	}
	public void setDatum(java.util.Date datum) {
		this.datum = datum;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public long getUx() {
		return ux;
	}
	public void setUx(long ux) {
		this.ux = ux;
	}

	public String toString(){
		return "Datum: " + datum + "\tValue: " + value + "\tUnix: " + ux;
	}
}
