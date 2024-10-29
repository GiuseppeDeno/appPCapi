package com.example.demo;




import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/*
 * Annotazione configuration significa che questo è un file di configurazione
 * qui andiamo a definire quali sono i dati di configuraziona al nostra database
 */
@Configuration
public class DatabaseConfig {
	
	@Bean
	public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/---your db ");
        dataSource.setUsername("root");
        dataSource.setPassword("");
        return dataSource;
    }

}
