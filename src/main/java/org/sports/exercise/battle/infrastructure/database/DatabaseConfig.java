package org.sports.exercise.battle.infrastructure.database;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    //we could also use an .env file, but chose application.properties instead
    private static final String CONFIG_FILE = "application.properties";

    public static Connection getConnection(){
        Properties properties = loadProperties();

        try{
            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");
            return DriverManager.getConnection(url, user, password);

        }catch(SQLException e){
            throw new RuntimeException("Failing to connect to database", e);
        }
    }

    //helper to read the application.properties file
    private static Properties loadProperties(){
        Properties properties = new Properties();

        try(InputStream inputStream = DatabaseConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)){

            if(inputStream == null){
                throw new RuntimeException("Could not find " + CONFIG_FILE);
            }

            properties.load(inputStream);
            return properties;

        }catch (IOException e){
            throw new RuntimeException("Could not load " + CONFIG_FILE);
        }
    }
}
