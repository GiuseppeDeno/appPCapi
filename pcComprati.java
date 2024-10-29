package com.example.demo;



public class pcComprati extends pc{
	public int qntComprato;

	public pcComprati(String nome, String marca, String descrizione, Double prezzo, String url, int qntMagazzino, int qntVenduti ,int qntComprato) {
		super(nome, marca, descrizione, prezzo, url ,qntMagazzino, qntVenduti);
	     this.qntComprato=qntComprato;
		
	}

	@Override
	public String toString() {
		return   qntComprato + ", nome=" + nome + ", marca=" + marca + ", descrizione="
				+ descrizione + ", prezzo=" + prezzo+"";
	}

	//crea confusione  perche return qntComprato 
	public int getQnt() {
		return qntComprato;
	}

	public void setQnt(int qnt) {
		this.qntComprato = qnt;
	}
	

}
