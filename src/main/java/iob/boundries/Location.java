package iob.boundries;

public class Location {
	private Double lat;
	private Double lng;
	
	public Location() {
		super();
	}
	
	public Location(Double lat, Double lng) {
		super();
		this.lat = lat;
		this.lng = lng;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}

	@Override
	public String toString() {
		return lat + "-" + lng ;
	}
	
	public boolean isInRange(Location other, double distance) {
		if (other.lat < this.lat - distance || other.lat > this.lat + distance) { // check if lat isn't in range
			return false;
		}
		if (other.lng < this.lng - distance || other.lng > this.lng + distance) { // check if lat isn't in range
			return false;
		}

		return true;
	}
	
}
