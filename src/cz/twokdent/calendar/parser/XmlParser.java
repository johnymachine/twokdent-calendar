package cz.twokdent.calendar.parser;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cz.twokdent.calendar.datastructures.Doctor;
import cz.twokdent.calendar.datastructures.Patient;
import cz.twokdent.calendar.datastructures.Reservation;
import cz.twokdent.calendar.datastructures.Schedule;
import cz.twokdent.calendar.datastructures.Type;

public class XmlParser {
	
	public static Schedule parse(File reservationFile, File typeFile) throws ParserConfigurationException, SAXException, IOException, ParseException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		
		// Parse Types
		Document typeXml = dBuilder.parse(typeFile);
		typeXml.getDocumentElement().normalize();
		Document reservationXml = dBuilder.parse(reservationFile);
		reservationXml.getDocumentElement().normalize();
		
		NodeList typeNl = typeXml.getElementsByTagName("Type");	
		NodeList docNl = reservationXml.getElementsByTagName("Doktor");
		NodeList patNl = reservationXml.getElementsByTagName("Pacient");
		
		HashMap<String, Type> types = new HashMap<String, Type>();
		HashMap<String, Doctor> doctors = new HashMap<String, Doctor>();
		HashMap<String, Patient> patients = new HashMap<String, Patient>();
		HashMap<String, Reservation> reservations = new HashMap<String, Reservation>();
		
		// Parse Types
		Type type;
		for (int i = 0; i < typeNl.getLength(); i++) {
			type = XmlParser.parseType(typeNl.item(i).getAttributes());
			types.put(type.getId(), type);
		}
		
		// Parse Doctors
		Doctor doctor;
		for (int i = 0; i < docNl.getLength(); i++) {
			doctor = XmlParser.parseDoctor(docNl.item(i).getAttributes());
			doctors.put(doctor.getId(), doctor);
		}
		
		// Parse Patients and Reservations
		Element patEl;
		Patient patient;
		NodeList resNl;
		NamedNodeMap attributes;
		Reservation reservation;
		for (int i = 0; i < patNl.getLength(); i++) {
			patEl = (Element) patNl.item(i);
			patient = XmlParser.parsePatient(patEl);
			patients.put(patient.getId(), patient);
			resNl = patEl.getElementsByTagName("Objednavka");
			for (int j = 0; j < resNl.getLength(); j++) {
				attributes = resNl.item(j).getAttributes();
				reservation = XmlParser.parseReservation(attributes, patient, doctors, types);
				reservations.put(reservation.getId(), reservation);
			}
		}
		
		return new Schedule(doctors, patients, reservations, types);
	}
	
	private static Type parseType(NamedNodeMap attributes) {
		Node typeIdN = attributes.getNamedItem("id");
		Node typeNameN = attributes.getNamedItem("name");
		String typeId = (typeIdN) != null ? typeIdN.getNodeValue() : null;
		String typeName = (typeNameN) != null ? typeNameN.getNodeValue() : null;
		return new Type(typeId, typeName);
	}
	
	private static Doctor parseDoctor(NamedNodeMap attributes) {
		Node docIdN = attributes.getNamedItem("IDdoktor");
		Node docNameN = attributes.getNamedItem("jmeno");
		String docId = (docIdN) != null ? docIdN.getNodeValue() : null;
		String docName = (docNameN) != null ? docNameN.getNodeValue() : null;
		return new Doctor(docId, docName);
	}
	
	private static Patient parsePatient(Element patEl) {
		Node patIdN = patEl.getElementsByTagName("IDpacienta").item(0);
		Node patNameN = patEl.getElementsByTagName("pacient").item(0);
		Node patMailN = patEl.getElementsByTagName("mail").item(0);
		Node patPhoneN = patEl.getElementsByTagName("telefon").item(0);
		String patId = (patIdN) != null ? patIdN.getTextContent() : null;
		String patName = (patNameN) != null ? patNameN.getTextContent() : null;
		String patMail = (patMailN) != null ? patMailN.getTextContent() : null;
		String patPhone = (patPhoneN) != null ? patPhoneN.getTextContent() : null;
		return new Patient(patId, patName, patMail, patPhone);
	}
	
	private static Reservation parseReservation(NamedNodeMap attributes, Patient patient, HashMap<String, Doctor> doctors, HashMap<String, Type> types) throws ParseException {
		Node resIdN = attributes.getNamedItem("IDobjednavky");
		Node docIdN = attributes.getNamedItem("IDdoktor");
		Node resTypeN = attributes.getNamedItem("typ");
		Node resProcN = attributes.getNamedItem("cinnost");
		Node resIsDeletedN = attributes.getNamedItem("deleted");
		Node resDurationN = attributes.getNamedItem("trvani");
		
		String resId = (resIdN) != null ? resIdN.getNodeValue() : null;
		String docId = (docIdN) != null ? docIdN.getNodeValue() : null;
		String resType = (resTypeN) != null ? resTypeN.getNodeValue() : null;
		String resProc = (resProcN) != null ? resProcN.getNodeValue() : null;
		Boolean resIsDeleted = (resIsDeletedN) != null ? (resIsDeletedN.getNodeValue().compareTo("1")==0) : false;
		
		String[] tmpId = resId.replace(":", " ").split(" ");
		Date resFrom = (resIdN) != null ? new SimpleDateFormat("yyyyMMddHHmmss").parse(tmpId[1] + tmpId[2]) : null;
		Date resTo = null;
		if (resDurationN != null && resFrom != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(resFrom);
			cal.add(Calendar.MINUTE, Integer.parseInt(resDurationN.getNodeValue()));
			resTo = cal.getTime();
		}
		return new Reservation(resId, patient, doctors.get(docId), types.get(resType), resProc, resFrom, resTo, resIsDeleted);
	}
}
