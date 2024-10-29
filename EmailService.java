package com.example.demo;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.FileSystemResource;

@Service
public class EmailService {

	/*
	//INIETTA la classe  JaveMailsender 
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmailWithImage(String to, String subject, String text , ArrayList<String> imagePaths) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        
        //oggetto mimemessageHelper consente di scrivere la mail come se fosse una pagina html ed ha dei metodi 
        //consente di prendere lo stringBuilder che  è il codice html 
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true); {
        	 helper.setTo(to);
             helper.setSubject(subject);
             ///cambiare questa parte  per dire da chi è mandata la mail 
             
             helper.setFrom("giuseppedeno@gmail.com");

           
             StringBuilder htmlContent = new StringBuilder("<html><body>");
             htmlContent.append("<p>").append(text).append("</p>");
             
        
             
           
         // Aggiungi le immagini con le regole CSS inline
             for (int i = 0; i < imagePaths.size(); i++) {
                 htmlContent.append("<img src='").append(imagePaths.get(i)).append("' style='width: 300px !important; height:200px!important; margin: 10px;'/>");
             }

             
             htmlContent.append("</body></html>");
             
             //oggetto helper si prende il codice html e manda la mail chiamando send su mailSender
             helper.setText(htmlContent.toString(), true); // 'true' per indicare che il testo è in HTML

            // Aggiungi ogni immagine come allegato inline con un Content ID unico
/*////             for (int i = 0; i < imagePaths.size(); i++) {
////                 // Costruisci il percorso relativo per l'immagine
////                 ClassPathResource image = new ClassPathResource(imagePaths.get(i));
////                 // Aggiungi l'immagine come allegato inline con un Content ID unico
////                 helper.addInline("image" + i, image);
//                  
        //     }*/
             
          
            // mailSender.send(mimeMessage);
  //       }
 //    }
    
//    */
 /*   
    public void sendEmailTabella(String to, String subject, String htmlContent, ArrayList<String> imagePaths) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom("giuseppedeno@gmail.com");

        // Crea il corpo dell'email con il contenuto HTML
        StringBuilder htmlEmail = new StringBuilder("<html><body>");
        htmlEmail.append("<h3>Riepilogo del tuo acquisto</h3>");
        htmlEmail.append(htmlContent);  // Tabella HTML con i dettagli dei prodotti
        htmlEmail.append("</body></html>");
        
        helper.setText(htmlEmail.toString(), true); 
        
        // tramite un for aggiungo  ogni immagine come Content ID ovvero gli DO UN INDICE crescente da cid image0 a quante sono le immagini totali
        //la salviamo nell'oggetto filesystemresource  con la chiave cid
        for (int i = 0; i < imagePaths.size(); i++) {
            String cid = "image" + i;
            ///importante salvare nel filesystemresurce il percoso immagine con la "chiave " indice
            helper.addInline(cid, new FileSystemResource(imagePaths.get(i)));
        }
        
        // si usa il metodo addInline("image0", imageResource), 
        //image0 diventa il CID per quell'immagine, che può essere poi richiamata con src="cid:image0".
        //new FileSystemResource(imagePaths.get(i)) Passa il percorso dell’immagine come risorsa dal file system
        mailSender.send(mimeMessage);
    }
    */
	@Autowired
	private JavaMailSender mailSender;
	//serve usare LIST che p la lista dei pc comprati

	public void sendEmailWithImage(String to, String subject, ArrayList<pcComprati> pcList, double totalAmount) throws MessagingException {
	    MimeMessage mimeMessage = mailSender.createMimeMessage();
	    
	    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
	    helper.setTo(to);
	    helper.setSubject(subject);
	    helper.setFrom("giuseppedeno@gmail.com");

	    
	    StringBuilder htmlContent = new StringBuilder("<html><body>");
	    htmlContent.append("<h3>Ecco cosa hai acquistato:</h3>");
	    htmlContent.append("<table style='border-collapse: collapse; width: 100%;'>")
	               .append("<tr>")
	               .append("<th style='border: 1px solid #ddd; padding: 8px;'>Marca</th>")
	               .append("<th style='border: 1px solid #ddd; padding: 8px;'>Nome</th>")
	               .append("<th style='border: 1px solid #ddd; padding: 8px;'>Descrizione</th>")
	               .append("<th style='border: 1px solid #ddd; padding: 8px;'>Quantità</th>")
	               .append("<th style='border: 1px solid #ddd; padding: 8px;'>Prezzo</th>")
	               .append("<th style='border: 1px solid #ddd; padding: 8px;'>Immagine</th>")
	               .append("</tr>");

	    // posso aggiungere i dati qui perche ho cmq l'array list dei pcComprati. che è lo stesso nel controller getResoconto
	    for (pcComprati pc : pcList) {
	        htmlContent.append("<tr>")
	                   .append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(pc.getMarca()).append("</td>")
	                   .append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(pc.getNome()).append("</td>")
	                   .append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(pc.getDescrizione()).append("</td>")
	                   .append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(pc.getQnt()).append("</td>")
	                   .append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(pc.getPrezzo()).append("euro </td>")
	                   .append("<td style='border: 1px solid #ddd; padding: 8px;'><img src='").append(pc.getUrl())
	                   .append("' style='width: 100px; height: 80px;'></td>")
	                   .append("</tr>");
	    }

	    // Totale
	    htmlContent.append("</table>");
	    htmlContent.append("<p><strong>Totale da pagare: </strong>").append(totalAmount).append("euro </p>");
	    htmlContent.append("</body></html>");

	    helper.setText(htmlContent.toString(), true); // 'true' per indicare che il testo è in HTML
	    mailSender.send(mimeMessage);
	}


    
}