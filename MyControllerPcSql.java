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
//    <form action="/rimuoviDalMagazzino" method="post">
//    <input type="hidden" name="nome" value="${pc.nome}">
//    <button type="submit">Rimuovi</button>
//</form>
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
    
    //facciamo un reindirizzamento ad un altra funzione mappata con /carrello che mi stampa il carrello
    //cosi non abbiamo bisogni di fare il refresh della pagina ad ogni cambiamento del carrello 
    @PostMapping("/aggiungiCarrello")
    public String compra(@RequestParam("nome") String nome, @RequestParam("num") int num) {
    	
    	if(num>0)//abbiamo effettuato un acquisto
    		//itero per trovare il nome dell oggetto e la quantita 
    		for (pc  pc: listaPc) {
    			
    			if(pc.getNome().equals(nome)) {
    				//controlliamo se quei pc sono disponibili in magazzino
    				 if(pc.qntMagazzino>=num) {
    					 //metto il pc nell'array list di pcAcquistati che poi verra stampato nel carrello
    					// utilizziamol l'oggetto pc esistente per creare un oggetto pcComprati
    	                    pcComprati pcAcquistato = new pcComprati(
    	                        pc.getNome(),
    	                        pc.getMarca(),
    	                        pc.getDescrizione(),
    	                        pc.getPrezzo(),
    	                        pc.getUrl(),
    	                        pc.getQntMagazzino(),
    	                        pc.getQntVenduti(),
    	                        num // quantità acquistata)
    	                        );
    	                    
    	                
    	                    pcComprati.add(pcAcquistato); // lo aggiungo al carrello dove viene visualizzato 
    				 }
    				
    			}
    		}
    	return "redirect:/funzioneCarrello";
    	 //return "redirect:/store";
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

    
    
    
//    @GetMapping("/funzioneCarrello")
//    public String stampaCarrello(Model m1) {
//    	double somma= 0;
//    	for(pcComprati  pc : pcComprati) {
//    		somma+= pc.getPrezzo()*pc.getQnt();
//    		// metodo getQnt  appartiene a pcComprati
//    	}
//    	m1.addAttribute("somma", somma);
//		m1.addAttribute("lista", pcComprati);
//		return("store");
//    		
//    	}
//    
    
    
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
    
    
    /// metodo per rimuovere tutto dal carrello. nel carrello metto un bottone conil mapping svuotacarrello 
//    <form action="/svuotaCarrello" method="post">
//    <button type="submit">Svuota Carrello</button>
//</form>
    
    
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
            
           // m1.addAttribute("messaggio", "Il carrello è vuoto. Non puoi confermare l'acquisto.");
        	
    		m1.addAttribute("lista", listaPc); //ricarico il catalogo 
    		m1.addAttribute("somma",0);
            return "store"; 
        }
        
        
        
        ///ma voglio aggiornare il db con la quantita di pc venduti e in magazino 
        //ma qui devo fare un UPDATE del database d1.updatePc(num.get(i), listaPc.get(i).nome)
        //mi serve nel pcJdbcTemplate un metodo update che mi cambia la quantita di pcVenduti e Acquistati
        //for(int 1=0; i<num.size(); i++{
        //
        // if(num.get(i)!=0)
       //  {
        //	 d1.updatePc(num.get(i), listaPc.getPC(i).mome)
        	 
        // }
        
        
    	
    	//PRENDE I DATI DEL CARRELLO OVVERO SOMMA E PCACQUISTATI E LI STAMPA NELLA PAGINA CONFERMAACQUISTO.HTML
       
    	for(pcComprati  pc : pcComprati) {
    		somma+= pc.getPrezzo()*pc.getQnt();
    		// metodo getQnt  appartiene a pcComprati
    	}
    	m1.addAttribute("somma", somma);
		m1.addAttribute("lista", pcComprati);
		
		
		return("confermaAcquisto");
    }
    	
    	
   /* 	
    @ResponseBody
    @PostMapping("/Recap")
   public String getResoconto(@RequestParam("mail") String mail) throws MessagingException     {
	   
    	ArrayList<String> url = new ArrayList<>();
    	
      
       String  recap="Ecco cosa hai acquistato: ";
    	double somma= 0;
    	for(pcComprati  pc : pcComprati) {
    		somma+= pc.getPrezzo()*pc.getQnt();
    		recap+= pc+"  ,";
    		//per un messaggio piu strutturato ? 
    		//recap+= pc.getMarca()+ "," + pc.getNome()+"/n" + pc.getDescrizione()+"/n"+ url.add(pc.url);
    	//	emailService.sendEmailWithImage(mail, "Store PC", recap); //all posso di quello di sotto
    		
    		
    		
    		
    		//update della banca dati per la quantita Venduti 
    		
    		d1.updateQntVenduti(pc.getQnt(), pc.nome);
    		//cambio quantita nel magazzino
    		d1.updateQntMagazzino(pc.getQnt(), pc.nome);
    		
    		url.add(pc.url);
    		// metodo getQnt  appartiene a pcComprati
    	}
    	
    	recap+= "Totale da pagare  :"+somma;
    	
    	//emailService.sendEmailWithImage(mail, "Store PC ", recap, url);
    	
    	return("*Acquisto avvenuto con successo*");
    	
    	
    	
    }  */
    
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

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    	
    //// mandare la tabella dei prodotti . ovviamente devo cancellare il metodo di sopra altrimenti va in conflitto 
    
  /*  @ResponseBody
    @PostMapping("/Recap")
    public String getResocontoBis(@RequestParam("mail") String mail) throws MessagingException {
        
        ArrayList<String> url = new ArrayList<>();
        
        ///creo un oggtto StringBuilder che sara una tabella.lo chiamo HtmlTable
        StringBuilder htmlTable = new StringBuilder();
        double somma = 0;

        // intestazioni anche con  l'immagine
        htmlTable.append("<table style='border-collapse: collapse; width: 100%;'>");
        htmlTable.append("<tr><th style='border: 1px solid #dddddd; padding: 8px;'>Immagine</th>")
                 .append("<th style='border: 1px solid #dddddd; padding: 8px;'>Nome</th>")
                 .append("<th style='border: 1px solid #dddddd; padding: 8px;'>Marca</th>")
                 .append("<th style='border: 1px solid #dddddd; padding: 8px;'>Descrizione</th>")
                 .append("<th style='border: 1px solid #dddddd; padding: 8px;'>Quantità</th>")
                 .append("<th style='border: 1px solid #dddddd; padding: 8px;'>Prezzo</th>")
                 .append("<th style='border: 1px solid #dddddd; padding: 8px;'>Totale</th></tr>");
        
        for (pcComprati pc : pcComprati) {
            double totalePc = pc.getPrezzo() * pc.getQnt();
            somma += totalePc;

            // le varie righe appese sempre all htmlTable
            htmlTable.append("<tr>")
                     .append("<td style='border: 1px solid #dddddd; padding: 8px; text-align: center;'>")
                     .append("<img src='cid:image").append(url.size()).append("' style='height: 100px;'/>")
                     .append("</td>")
                     .append("<td style='border: 1px solid #dddddd; padding: 8px;'>").append(pc.getNome()).append("</td>")
                     .append("<td style='border: 1px solid #dddddd; padding: 8px;'>").append(pc.getMarca()).append("</td>")
                     .append("<td style='border: 1px solid #dddddd; padding: 8px;'>").append(pc.getDescrizione()).append("</td>")
                     .append("<td style='border: 1px solid #dddddd; padding: 8px;'>").append(pc.getQnt()).append("</td>")
                     .append("<td style='border: 1px solid #dddddd; padding: 8px;'>€").append(pc.getPrezzo()).append("</td>")
                     .append("<td style='border: 1px solid #dddddd; padding: 8px;'>€").append(totalePc).append("</td>")
                     .append("</tr>");
            
            // Aggiornament database 
            d1.updateQntVenduti(pc.getQnt(), pc.getNome());
            d1.updateQntMagazzino(pc.getQnt(), pc.getNome());
            
            // immagini che vengono richiamate grazie al cid 
            url.add(pc.getUrl());
        }
        
        // totale da pagare
        htmlTable.append("<tr><td colspan='6' style='padding: 8px; text-align: right; font-weight: bold;'>Totale da pagare:</td>")
                 .append("<td style='border: 1px solid #dddddd; padding: 8px;'>€").append(somma).append("</td></tr>")
                 .append("</table>");

        // IMP  uso il metodo sendEmailTabella scritto nel Email Service 
        emailService.sendEmailTabella(mail, "Store PC - Resoconto Acquisto", htmlTable.toString(), url);
        return "*Acquisto avvenuto con successo*";
    }
    //fine resoconto email
    
    */
    
    
    	
    	
    }
    	
    	
    
    
    
    
    
    
    
    
    
    
    
























//nota bene "redirect:/carrello"
//
//Quando un metodo nel controller restituisce una stringa con il prefisso "redirect:", Spring interpreta questa stringa come un'istruzione per fare un reindirizzamento HTTP (HTTP Redirect) a un’altra rotta o URL. In questo caso:
//
//    "redirect:/carrello":
//        Fa un reindirizzamento alla rotta /carrello, attivando quindi il metodo printCarrello() del controller.
//        Questo processo dice al browser del client di effettuare una nuova richiesta HTTP per accedere alla pagina del carrello.
//
//Perché Usare redirect
//
//In un flusso di acquisto come quello del tuo esempio, il redirect è usato per assicurare che, dopo ogni modifica nel carrello (aggiunta, rimozione, o aggiornamento di quantità), il client visualizzi il carrello aggiornato senza ricaricare manualmente la pagina. Questo è utile anche per prevenire problemi di "refresh" delle pagine dopo un'azione, evitando di reinviare accidentalmente i dati di una richiesta POST se l'utente aggiorna la pagina.
//Esempio di Flusso Usando "redirect:/carrello"
//
//    Aggiunta al Carrello:
//        Quando l'utente invia il form per aggiungere un PC al carrello, il metodo buy() viene invocato.
//        Dopo aver aggiunto il PC alla lista pcSelezionati, buy() restituisce "redirect:/carrello".
//        Il client è quindi indirizzato alla rotta /carrello, e Spring invoca printCarrello() per mostrare il carrello aggiornato.
//
//    Rimozione di un Prodotto:
//        Se l'utente rimuove un prodotto, il metodo removeProduct() viene chiamato.
//        Dopo aver rimosso il prodotto, il metodo restituisce "redirect:/carrello", assicurando che il client veda il carrello aggiornato.
//
//    Modifica della Quantità di un Prodotto:
//        Con l'aggiornamento della quantità tramite modifica, si ritorna sempre "redirect:/carrello", per visualizzare le modifiche.