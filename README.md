# appPCapi


Project Description

This project is an online application for a PC reseller, featuring two main sections: Warehouse Management and Store.

    Warehouse Management: The admin area enables adding new products by specifying the name, image URL, and quantity. Products are displayed as cards, each with a button allowing for easy removal from the catalog.

    Store: The customer-facing area allows users to select and purchase products by choosing a desired quantity, which is then added to the cart. Upon purchase confirmation, a summary receipt is generated and sent to the customer via email. Additionally, there is an automatically generated card that randomly displays video game images, titles, and products by using a public API.

Database Synchronization: All operations are synchronized with the database, which tracks the quantity of PCs in stock and those sold.

Technologies used:


    Thymeleaf: for HTML templating, supporting dynamic data display.
    Data JDBC: for database interaction, facilitating operations like insertion, selection, and deletion.
    Spring Web: for creating REST APIs, handling server-side requests and services.
    JavaMailSender: for automatic email generation to provide a purchase summary to the customer.
    Materialize CSS: for a responsive and modern user interface.
    Fetch API: to interact with external services and manage dynamic content, such as retrieving video game images and information for the Store.



![Immagine 2024-10-29 142447](https://github.com/user-attachments/assets/fc0c761e-96d3-46b0-a97d-3594bb203ce7)
![Immagine 2024-10-29 142614](https://github.com/user-attachments/assets/f86ddcbb-3b26-443a-8914-4b966c732aee)
![Immagine 2024-10-29 142651](https://github.com/user-attachments/assets/0019c952-fc73-45c8-bd39-794c19567364)
![Immagine 2024-10-29 142744](https://github.com/user-attachments/assets/d1be6243-8adf-4eee-ab64-271d836c7631)
![Immagine 2024-10-29 142829](https://github.com/user-attachments/assets/663c7413-4370-4535-8c7d-d4bd17ad1080)
![Immagine 2024-10-29 142938](https://github.com/user-attachments/assets/0ab4497c-fcbd-4db5-9d5a-2fff341c3398)
![Immagine 2024-10-29 143035](https://github.com/user-attachments/assets/695717eb-4120-452e-8b1c-b0fa7deb8aaa)
![email Immagine 2024-10-29 150959](https://github.com/user-attachments/assets/41f6bf5e-5440-4ec0-8527-ca5f79bd7836)


Notes to Run the App on Eclipse

The Spring Boot project should contain the following features:

    SPRING BOOT DEVTOOLS
    THYMELEAF
    SPRING WEB
    SPRING DATA JDBC
    JAVA MAIL SENDER
    MYSQL DRIVER
    
    


The app is connected to a MySQL table named tabellapc.

Steps:

    Create a table of PCs with the same name or change it in jdPctemplate.java.

    Modify the credentials with your own email and app-specific password to use the JavaMail service.

    Update the connection data to your database in the file DataBaseConfig.java.





    
