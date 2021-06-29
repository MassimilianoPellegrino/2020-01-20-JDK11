package it.polito.tdp.artsmia.model;

public class Arco {
	
	private Author a1;
	private Author a2;
	private int w;
	public Arco(Author a1, Author a2, int w) {
		super();
		this.a1 = a1;
		this.a2 = a2;
		this.w = w;
	}
	public Author getA1() {
		return a1;
	}
	public void setA1(Author a1) {
		this.a1 = a1;
	}
	public Author getA2() {
		return a2;
	}
	public void setA2(Author a2) {
		this.a2 = a2;
	}
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	@Override
	public String toString() {
		return a1+" - "+a2+" : "+w;
	}
	
	
	
	

}
