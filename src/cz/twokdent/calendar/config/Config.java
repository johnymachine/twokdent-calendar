package cz.twokdent.calendar.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class Config {
	private static String filename = "config/config.xml";
	private static Config instance;
	private Properties properties;
	private Config(Properties properties){
		this.properties = properties;
	}
	public static synchronized void initialize(){
		
	}
    public static synchronized Config getInstance() throws InvalidPropertiesFormatException, IOException {
        if(instance == null){
        	Properties newProperties = new Properties();
        	FileInputStream fis = new FileInputStream(filename);
        	newProperties.loadFromXML(fis);
        	instance = new Config(newProperties);
        }
        return instance;
    }
	public Object getOrDefault(Object key, Object defaultValue) {
		return properties.getOrDefault(key, defaultValue);
	}
	public String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
}