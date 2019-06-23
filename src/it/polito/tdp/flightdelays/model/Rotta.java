package it.polito.tdp.flightdelays.model;

import com.javadocmd.simplelatlng.LatLng;

public class Rotta {
	
	private Airport a1;
	private LatLng coorA1;
	private Airport a2;
	private LatLng coorA2;
	private double media;
	public Rotta(Airport a1, LatLng coorA1, Airport a2, LatLng coorA2, double media) {
		super();
		this.a1 = a1;
		this.coorA1 = coorA1;
		this.a2 = a2;
		this.coorA2 = coorA2;
		this.media = media;
	}
	public Airport getA1() {
		return a1;
	}
	public void setA1(Airport a1) {
		this.a1 = a1;
	}
	public LatLng getCoorA1() {
		return coorA1;
	}
	public void setCoorA1(LatLng coorA1) {
		this.coorA1 = coorA1;
	}
	public Airport getA2() {
		return a2;
	}
	public void setA2(Airport a2) {
		this.a2 = a2;
	}
	public LatLng getCoorA2() {
		return coorA2;
	}
	public void setCoorA2(LatLng coorA2) {
		this.coorA2 = coorA2;
	}
	public double getMedia() {
		return media;
	}
	public void setMedia(double media) {
		this.media = media;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a1 == null) ? 0 : a1.hashCode());
		result = prime * result + ((a2 == null) ? 0 : a2.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rotta other = (Rotta) obj;
		if (a1 == null) {
			if (other.a1 != null)
				return false;
		} else if (!a1.equals(other.a1))
			return false;
		if (a2 == null) {
			if (other.a2 != null)
				return false;
		} else if (!a2.equals(other.a2))
			return false;
		return true;
	}
	

}
