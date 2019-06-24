package it.polito.tdp.artsmia.model;

public class Adiacenza {

	private int o1;
	private int o2;
	private int peso;
	public Adiacenza(int o1, int o2, int peso) {
		super();
		this.o1 = o1;
		this.o2 = o2;
		this.peso = peso;
	}
	public int getO1() {
		return o1;
	}
	public void setO1(int o1) {
		this.o1 = o1;
	}
	public int getO2() {
		return o2;
	}
	public void setO2(int o2) {
		this.o2 = o2;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	
	
}
