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
