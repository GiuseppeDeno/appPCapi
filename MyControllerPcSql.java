package com.example.demo;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.mail.MessagingException;

@Controller
public class MyControllerPcSql {
	
	@Autowired
	EmailService emailService;

    pcJdbcTemplate d1;
    ArrayList<pc> listaPc = new ArrayList<>();
    ArrayList<pcComprati> pcComprati = new ArrayList<>();
    
    //è l'array list per stampare il carrello ed aggiornare il database 
    


    @Autowired
    public MyControllerPcSql(pcJdbcTemplate d1) {
        this.d1 = d1;
        this.listaPc=d1.getLista();
    }

    @GetMapping("/")
    public String getIndex(Model model) {
        String nome = "pc";
        model.addAttribute("nome", nome);
        return "index";
    }

    @GetMapping("/listaPcStore")
    public String getLista(Model model) {
    	//per visualizzare totale a zero  ogni volta che entro nella pagina Store, altrimenti mi verrebbe null in quanto
    	// l'arraylist del carrello non è popolato.
    	 double somma = 0;
    	 model.addAttribute("somma",somma);
    	 
    	 
        
        listaPc = d1.getLista();
        model.addAttribute("lista", listaPc);
        return "store";
    }

    @GetMapping("/vaiMagazzino")
    public String getMagazzino(Model model) {
        listaPc = d1.getLista();
        model.addAttribute("lista", listaPc);
        return "stampaMagazzino"; // Assicurati che questo corrisponda al file HTML
    }

    // Per inserire un prodotto nel magazzino
    @PostMapping("/gestioneMagazzino")
    public String getPc(@RequestParam("nome") String nome,
                        @RequestParam("marca") String marca,
                        @RequestParam("descrizione") String descrizione,
                        @RequestParam("prezzo") Double prezzo,
                        @RequestParam("url") String url,
                        @RequestParam("qntMagazzino") int qntMagazzino,
                        @RequestParam("qntVenduti") int qntVenduti, Model m1) {
        // Inserimento nel database
        d1.insertPc(nome, marca, descrizione, prezzo, url, qntMagazzino, qntVenduti);

        // Aggiungi il prodotto alla lista
        pc pcSingolo = new pc(nome, marca, descrizione, prezzo, url, qntMagazzino, qntVenduti);
        listaPc.add(pcSingolo);

        // Ottieni di nuovo la lista aggiornata
        listaPc = d1.getLista();
        m1.addAttribute("lista", listaPc);

        // Ritorna la pagina di visualizzazione del magazzino
        return "stampaMagazzino";  
    }
    
    //metodo per rimuovere un prodotto dal magazino. ovvero tutto 

    @PostMapping("/rimuoviDalMagazzino")
    public String rimuoviDalMagazzino(@RequestParam("nome") String nome, Model m1) {
        // Rimuove il prodotto dalla lista in memoria
        listaPc.removeIf(pc -> pc.getNome().equals(nome));

        // RIMOZIONE del pc preso in base al nome tramite il metodo delete creato in jdbctemplate
        d1.delete(nome);  

        // Aggiorna la lista nel model
        m1.addAttribute("lista", listaPc);
        return "redirect:/vaiMagazzino";  //reindirizzo al caricamento del magazzino 
    }

    
    
    
   ////>>>>>>>>>>>>>>> metodi  per lo store
    
    
    //mappiamo il carrello //num è la quantita di prodotti . che mettero nelle card 
    

      @PostMapping("/aggiungiCarrello")
    public String compra(@RequestParam("nome") String nome, @RequestParam("num") int num) {
        // Controlla se il carrello ha già un elemento
        boolean found = false;

        if (num > 0) { // Abbiamo effettuato un acquisto
            
            for (pc pc : listaPc) {
                if (pc.getNome().equals(nome)) {
                   
                    if (pc.qntMagazzino >= num) {
                        // Controllo se il PC è già nel carrello
                        for (pcComprati pcC : pcComprati) {
                            if (pcC.getNome().equals(nome)) {
                                
                                pcC.setQnt(pcC.getQnt() + num);
                                found = true; 
                                
                                pcC.setQntVenduti(pcC.getQntVenduti() + num);
                                pcC.setQntMagazzino(pcC.getQntMagazzino()-num);
                               
                                break; 
                            }
                        }

                        // Se l'articolo non è ancora presente nel carrello
                        if (!found) {
                           
                            pcComprati pcAcquistato = new pcComprati(
                                pc.getNome(),
                                pc.getMarca(),
                                pc.getDescrizione(),
                                pc.getPrezzo(),
                                pc.getUrl(),
                                pc.getQntMagazzino(),
                                pc.getQntVenduti(),
                                num // Quantità acquistata
                            );

                           
                            pcComprati.add(pcAcquistato);
                        }
                    } else {
                        // condol log di "avviso " per il gestore del magazzino
                        System.out.println("Non ci sono abbastanza PC disponibili in magazzino.");
                    }
                }
            }
        }

        return "redirect:/funzioneCarrello"; // Reindirizza alla pagina del carrello
    }
    //pero cosi rimuovo tutti  i pc che si chiamano cosi. forse è meglio usare gli indici con un for sugli indici dell'array pcComprati
    @PostMapping("/rimuoviDalCarrello")
    public String rimuoviDalCarrello(@RequestParam("nome") String nome, Model m1) {
        System.out.println("Rimuovendo dal carrello: " + nome);
        
        ///è tipo le arrow function di javascript 
        boolean removed = pcComprati.removeIf(pc -> pc.getNome().equals(nome));
        if (removed) {
            System.out.println(nome + " è stato rimosso dal carrello.");
        } else {
            System.out.println(nome + " non è stato trovato nel carrello.");
        }

        return "redirect:/funzioneCarrello";
    }

    
    //funzione per stamapare il carrello IMPORTANTE 
    @GetMapping("/funzioneCarrello")
    public String stampaCarrello(Model m1) {
        double somma = 0;
       
        ArrayList<pcComprati> listaCarrello = new ArrayList<>(); 
        
        
       listaCarrello.clear();
        for (pcComprati pc : pcComprati) {
            somma += pc.getPrezzo() * pc.getQnt();
            listaCarrello.add(pc); 
        }
        
        m1.addAttribute("lista", listaPc); //altrimenti scompare dallo store
        
        m1.addAttribute("somma", somma);
        m1.addAttribute("carrello", listaCarrello); // Passa la lista al template IMPORTANTE . "carrello" va nel div sidebare e nel th:ech
        return "store"; 
    }

    
    
    

    
    
   //metodo per fare update delle quantita di pc (sotto ogni card) da acquistare 
    //Aggiorna la quantità (num) di un prodotto già selezionato nel carrello, se la quantità in magazzino (qntMagazzino) lo permette.
    @PostMapping("/cambiaQnt")
    public String rimuovi
    	
    	(@RequestParam("nome") String nome, @RequestParam("num") int num) {
    		for(pc pc : listaPc) {
    			if(pc.getNome().equals(nome)) {
    				for(pcComprati pc1 : pcComprati ){
    					if (pc1.getNome().equals(nome) && pc.qntMagazzino >= num) {
    						pc1.setQnt(num);
    						break;
    					}
    				}				
    			}
    		}
    		return "redirect:/funzioneCarrello"; //non mi dirigo alla pagina store ma richiamo la funzioneCarrello che me la ristampa 
    		//evitando il refresh della pagina 
    	}
    
    

    
    
    ///non usato 
    @PostMapping("/svuotaCarrello")
    public String svuotaCarrello() {
        pcComprati.clear(); // Rimuove tutti gli elementi dal carrello
        return "redirect:/funzioneCarrello"; // Reindirizza alla pagina del carrello vuoto
    }
    
    
    	
    ///CONFERMA ACQUISTO NEL CARRELLO 
    ///una funzione che al clic del bottone acquista reindirizza ad una pagina di avvenuto acquisto
    //è doppione della  dunzione stampaCarrello ma con un reindirizzamento ad una altra pagina 
    //nel crrello ci sara un bottone di confermaAcquisto con il mapping /confermaAcquisto
    @PostMapping("/confermaAcquisto")
     public String confermaAcquisto(Model m1) {
    	 
    	double somma =0;
    	 // Controlla se ci sono articoli nel carrello ovvero se la lista è nulla o vuota 
        if ( pcComprati.isEmpty()) {
            
           
        	
    		m1.addAttribute("lista", listaPc); //ricarico il catalogo 
    		m1.addAttribute("somma",0);
            return "store"; 
        }
        
        
    	
    	//PRENDE I DATI DEL CARRELLO OVVERO SOMMA E PCACQUISTATI E LI STAMPA NELLA PAGINA CONFERMAACQUISTO.HTML
       
    	for(pcComprati  pc : pcComprati) {
    		somma+= pc.getPrezzo()*pc.getQnt();
    		// metodo getQnt  appartiene a pcComprati
    	}
    	m1.addAttribute("somma", somma);
		m1.addAttribute("lista", pcComprati);
		
		
		return("confermaAcquisto");
    }
    	
    	

    
    @ResponseBody
    @PostMapping("/Recap")
    public String getResoconto(@RequestParam("mail") String mail) throws MessagingException {
        ArrayList<pcComprati> pcList = new ArrayList<>();
        double totalAmount = 0;
        //String recap = "Ecco cosa hai acquistato: ";
        
        for (pcComprati pc : pcComprati) {
            totalAmount += pc.getPrezzo() * pc.getQnt();
            pcList.add(pc);
            
            // Aggiornamento del database
            d1.updateQntVenduti(pc.getQnt(), pc.getNome());
            d1.updateQntMagazzino(pc.getQnt(), pc.getNome());
        }
        
        // Invia l'email
        emailService.sendEmailWithImage(mail, "Store PC - Acquisto", pcList, totalAmount);
        
        return "*Acquisto avvenuto con successo*";
    }

    
    	

    }
    	
    	
    
    
