
public class Site {
	
	private int ID;
	private String name;
	private double latitude;
	private double longitude;
	
	
	public Site(int id, String name, double latitude, double longitude) {
		this.ID = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public int getID() {
		return ID;
	}
	
	public void setID(int id) {
		this.ID = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	
	public String convertDMS(double deg, boolean isLatitude) {
		String symb = "";
		if(deg < 0) {
			if(isLatitude) {
				symb = "W";
			}
			else {
				symb = "S";
			}
		}
		else {
			if(isLatitude) {
				symb = "E";
			}
			else {
				symb = "N";
			}
		}
		
		deg = Math.abs(deg);
		double d = (int)deg;
		deg -= (int)deg;
		double min = deg * 60;
		double m = min - (int)min;
		min -= (int)min;
		double s = min * 60;
		
		return String.format("%.2f", d) + "dg " + String.format("%.2f", m) + "' " + String.format("%.2f", s) + "'' " + symb; 
	}
	
	public String toString() {
		return name + " " + convertDMS(latitude, true) + " " + convertDMS(longitude, false);
	}
}
