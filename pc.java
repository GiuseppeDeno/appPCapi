package com.example.demo;

public class pc {
   String nome;
   String marca;
   String descrizione;
   Double prezzo;
   String url;
   int qntMagazzino;
   int qntVenduti;

    
    public pc() {
        // Costruttore vuoto cosi posso fare  pc pc1 = new pc(); in pcJdbcTemplate
    }

    // Costruttore della classe pc
    public pc(String nome, String marca, String descrizione, Double prezzo, String url, int qntMagazzino, int qntVenduti) {
        this.nome = nome;
        this.marca = marca;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.url = url;
        this.qntMagazzino = qntMagazzino;
        this.qntVenduti = qntVenduti;
    }

    // Getter e Setter
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Double prezzo) {
        this.prezzo = prezzo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getQntMagazzino() {
        return qntMagazzino;
    }

    public void setQntMagazzino(int qntMagazzino) {
        this.qntMagazzino = qntMagazzino;
    }

    public int getQntVenduti() {
        return qntVenduti;
    }

    public void setQntVenduti(int qntVenduti) {
        this.qntVenduti = qntVenduti;
    }

    @Override
    public String toString() {
        return "pc [nome=" + nome + ", marca=" + marca + ", descrizione=" + descrizione + 
               ", prezzo=" + prezzo + ", url=" + url + ", qntMagazzino=" + qntMagazzino + 
               ", qntVenduti=" + qntVenduti + "]";
    }
}
