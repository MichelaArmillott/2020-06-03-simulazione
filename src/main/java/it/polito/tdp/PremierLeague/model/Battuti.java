package it.polito.tdp.PremierLeague.model;

public class Battuti implements Comparable<Battuti> {
	private Player p;
	private Integer peso;
	public Battuti(Player p, Integer peso) {
		super();
		this.p = p;
		this.peso = peso;
	}
	public Player getP() {
		return p;
	}
	public void setP(Player p) {
		this.p = p;
	}
	public Integer getPeso() {
		return peso;
	}
	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return p.playerID+" "+p.name+" | "+peso;
	}
	@Override
	public int compareTo(Battuti other) {
		return -( this.peso.compareTo(other.peso) );
	}
	

}
