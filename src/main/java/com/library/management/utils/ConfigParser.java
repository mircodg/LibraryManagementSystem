package com.library.management.utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


import java.io.File;
import java.io.FileNotFoundException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class ConfigParser {
    private String dbUrl;
    private String dbUsername;
    private String dbPass;
    private int serverPort;

    public ConfigParser(){
        try {
            File file = new File("src/main/java/com/library/management/config/config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            Element dbConfig = (Element) doc.getElementsByTagName("database").item(0);
            this.dbUrl = dbConfig.getElementsByTagName("url").item(0).getTextContent();
            this.dbUsername = dbConfig.getElementsByTagName("username").item(0).getTextContent();
            this.dbPass = dbConfig.getElementsByTagName("password").item(0).getTextContent();

            Element serverConfig = (Element) doc.getElementsByTagName("server").item(0);
            this.serverPort = Integer.parseInt(serverConfig.getElementsByTagName("port").item(0).getTextContent());
        }catch (FileNotFoundException e){
            System.err.println("File not found");
        } catch(Exception e){
            System.err.println("An error occurred while parsing the config file, error: " + e.getMessage());
        }

    }

    public int getServerPort() {
        return serverPort;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUsername;
    }

    public String getDbPass() {
        return dbPass;
    }


}
