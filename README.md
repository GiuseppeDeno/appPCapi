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


![Immagine 2024-10-29 142744](https://github.com/user-attachments/assets/c8c0a32b-f7ac-4a45-bca8-60cb8cc994e7)
![Immagine 2024-10-29 142651](https://github.com/user-attachments/assets/762a3b7f-cf24-4508-bac9-ee2183d2a911)
![Immagine 2024-10-29 142614](https://github.com/user-attachments/assets/bb1a9204-10ca-474d-a23e-901dac0f0e3f)
![Immagine 2024-10-29 142447](https://github.com/user-attachments/assets/1b3d2909-d8d1-4270-bcf4-b9fa9dc2db60)
![email Immagine 2024-10-29 150959](https://github.com/user-attachments/assets/566329ab-c571-4e0d-ace0-5e9690bda8b8)
![Immagine 2024-10-29 143035](https://github.com/user-attachments/assets/46629df4-c806-480f-8f79-569e6fe495bf)
![Immagine 2024-10-29 142938](https://github.com/user-attachments/assets/d657c84b-319f-48b2-8aae-fd35e50c4c6e)
![Immagine 2024-10-29 142829](https://github.com/user-attachments/assets/5d0379c5-8511-4f02-b353-fde55930c4a8)

    
