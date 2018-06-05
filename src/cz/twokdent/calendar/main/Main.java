package cz.twokdent.calendar.main;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import cz.twokdent.calendar.config.Config;
import cz.twokdent.calendar.datastructures.Schedule;
import cz.twokdent.calendar.exporter.VbsExporter;
import cz.twokdent.calendar.generator.HtmlGenerator;
import cz.twokdent.calendar.parser.XmlParser;
import cz.twokdent.calendar.uploader.FtpUploader;

public class Main {
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public static void main(String [ ] args) throws ParseException, ParserConfigurationException, SAXException, IOException{
		// Setup Logging
		try {
			LOGGER.log(Level.INFO, "Setting up Logger.");
			FileHandler logFile = new FileHandler("log.log");
			logFile.setFormatter(new SimpleFormatter());
	        LOGGER.addHandler(logFile);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Unable to initialize logging to file.", e);
		}
		
		// Initialize Configuration
		Config cfg = null;
		//DATE default values
		Integer dateDaysToPast = 10;
		Integer dateDaysToFuture = 10;
		
		//EXPORT default values
		File exportVbsEnvFile = new File("C:/Windows/SysWOW64/cscript.exe");
		File exportVbsExecFile = new File("calendar_export.vbs");
		File exportFolder = new File("export");
		File exportFile = new File("parse/reservation_data.xml");
		
		//PARSE default values
		File parseTypeFile = new File("parse/type_data.xml");
		
		//GENERATE default values
		File generateFolder = new File("generate");
		
		//FTP default values
		String ftpHost = "localhost";
		Integer ftpPort = 21;
		String ftpUsername = "Anonymouse";
		String ftpPassword = "";
		Boolean ftpUploadAll = false;
				
		try {
			LOGGER.log(Level.INFO, "Getting config variables.");
			cfg = Config.getInstance();
			//DATE
			dateDaysToPast = Integer.parseInt(cfg.getProperty("date_days_to_past"));
			dateDaysToFuture = Integer.parseInt(cfg.getProperty("date_days_to_future"));
			
			//EXPORT
			exportVbsEnvFile = new File(cfg.getProperty("export_vbs_env_file"));
			exportVbsExecFile = new File(cfg.getProperty("export_vbs_exec_file"));
			exportFolder = new File(cfg.getProperty("export_folder"));
			
			//PARSE
			parseTypeFile = new File(cfg.getProperty("parse_type_file"));
			
			//GENERATE
			generateFolder = new File(cfg.getProperty("generate_folder"));
			
			//FTP
			ftpHost = cfg.getProperty("ftp_host");
			ftpPort = Integer.parseInt(cfg.getProperty("ftp_port"));
			ftpUsername = cfg.getProperty("ftp_username");
			ftpPassword = cfg.getProperty("ftp_password");
			ftpUploadAll = Boolean.parseBoolean(cfg.getProperty("ftp_upload_all"));
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Could not load configuration.", e);
			return;
		}
		
		// Prepare dates
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1*dateDaysToPast);
		Date from = calendar.getTime();
		calendar.add(Calendar.DATE, dateDaysToPast + dateDaysToFuture);
		Date to = calendar.getTime();
		
		// Export schedule
		try {
			LOGGER.log(Level.INFO, "Exporting data.");
			exportFile = VbsExporter.export(exportVbsEnvFile, exportVbsExecFile, exportFolder, from, to);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Could not export data.", e);
			return;
		}
		
		// Parse schedule
		Schedule schedule;
		try {
			LOGGER.log(Level.INFO, "Parsing data.");
			schedule = XmlParser.parse(exportFile, parseTypeFile);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Could not parse reservations file.", e);
			return;
		}
		
		// Generate HTML
		List<File> generatedFiles;
		
		try {
			LOGGER.log(Level.INFO, "Generating HTML.");
			generatedFiles = HtmlGenerator.generate(generateFolder, schedule, from, to);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Could not generate HTML files.", e);
			return;
		}
		
		// Upload to FTP
		try {
			LOGGER.log(Level.INFO, "Uploading files.");
			if (ftpUploadAll) generatedFiles = new ArrayList<File>(Arrays.asList(generateFolder.listFiles()));
			FtpUploader.upload(ftpHost, ftpPort, ftpUsername, ftpPassword, generatedFiles);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Could not upload to FTP.", e);
			return;
		}
		
		// Exit
		LOGGER.log(Level.INFO, "Processing finished.");
	}
}
