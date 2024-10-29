package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

/* JdbcTemplate è una classe di Spring per interagire con il database in modo semplificato.
 * Fornisce metodi come update per query INSERT, DELETE, UPDATE e query per le SELECT.
 * JdbcTemplate si basa sulle configurazioni di connessione al database specificate nel file di configurazione Spring.
 */

@Component
// Grazie a questo è possibile utilizzare l'iniezione delle dipendenze con @Autowired
public class pcJdbcTemplate {

    // Oggetto JdbcTemplate per eseguire query e update sul database.
    private JdbcTemplate jdbcTemplateObject;

    /*
     * Metodo per iniettare l'istanza di JdbcTemplate nella classe.
     * @Autowired indica a Spring di fornire automaticamente un'istanza di JdbcTemplate.
     * Autowired in Spring indica che un’istanza di JdbcTemplate deve essere iniettata automaticamente.
     * In questo caso, Spring creerà automaticamente un'istanza di JdbcTemplate (se è configurato correttamente)
     * e la fornirà al setter setJdbcTemplateObject.
     */
    @Autowired
    public void setJdbcTemplateObject(JdbcTemplate jdbcTemplateObject) {
        this.jdbcTemplateObject = jdbcTemplateObject;
    }

    /*
     * Metodo per inserire un nuovo pc nel database.
     * Prende in input i valori necessari per la tabella e restituisce un intero,
     * che indica il numero di righe modificate (inserite).
     */
    public int insertPc(String nome, String marca, String descrizione, double prezzo, String url, int qntMagazzino, int qntVenduti) {
        String query = "INSERT INTO tabellapc (nome, marca, descrizione, prezzo, url, qntMagazzino, qntVenduti) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplateObject.update(query, nome, marca, descrizione, prezzo, url, qntMagazzino, qntVenduti);
    }

    /*
     * Metodo per eliminare un pc dal database in base al nome.
     * Restituisce il numero di righe eliminate.
     */
    public int delete(String nome) {
        String query = "DELETE FROM tabellapc WHERE nome = ?";
        return jdbcTemplateObject.update(query, nome);
    }

    /*
     * Metodo per ottenere una lista di tutti i pc presenti nel database.
     * Utilizza un ResultSetExtractor per convertire il ResultSet in un ArrayList di oggetti pc.
     */
    public ArrayList<pc> getLista() {
        String query = "SELECT * FROM tabellapc";

        return jdbcTemplateObject.query(query, new ResultSetExtractor<ArrayList<pc>>() {
            @Override
            public ArrayList<pc> extractData(ResultSet rs) throws SQLException, DataAccessException {
                ArrayList<pc> listaPc = new ArrayList<>();

                // Itera sui risultati della query e crea un nuovo oggetto pc per ciascun record.
                while (rs.next()) {
                    pc pc1 = new pc();
                    pc1.setNome(rs.getString("nome"));
                    pc1.setMarca(rs.getString("marca"));
                    pc1.setDescrizione(rs.getString("descrizione"));
                    pc1.setPrezzo(rs.getDouble("prezzo"));
                    pc1.setUrl(rs.getString("url"));
                    pc1.setQntMagazzino(rs.getInt("qntMagazzino"));
                    pc1.setQntVenduti(rs.getInt("qntVenduti"));

                    listaPc.add(pc1);
                }

                return listaPc;
            }
        });
    }

    
    
    /*
     * Metodo per fare update delle quantità vendute in base al numero e al nome del pc.
     */
    public int updateQntVenduti(int num, String nome) {
        String query = "UPDATE tabellapc SET qntVenduti = qntVenduti + ? WHERE nome = ?";
        return jdbcTemplateObject.update(query, num, nome);
    }
    
    /* update  prodotti in magazzino */
    public int updateQntMagazzino(int num, String nome) {
        String query = "UPDATE tabellapc SET qntMagazzino = qntMagazzino - ? WHERE nome = ?";
        return jdbcTemplateObject.update(query, num, nome);
    }
    
}
