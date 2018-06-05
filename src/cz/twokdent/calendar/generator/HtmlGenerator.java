package cz.twokdent.calendar.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.twokdent.calendar.datastructures.Doctor;
import cz.twokdent.calendar.datastructures.Reservation;
import cz.twokdent.calendar.datastructures.Schedule;

public class HtmlGenerator {
	public static List<File> generate(File generateFolder, Schedule schedule, Date from, Date to) throws FileNotFoundException, UnsupportedEncodingException {
		List<Reservation> reservations;
		File outputFile;
		List<File> generateFiles = new ArrayList<File>();
		for (Doctor doctor : schedule.getDoctors().values()) {
			reservations = schedule.getReservationsByDoctor(doctor, from, to);
			outputFile = new File(generateFolder, doctor.getId() + ".html");
			generateFiles.add(HtmlGenerator.generateFile(outputFile, reservations));
		}
		return generateFiles;
	}
	
	private static File generateFile(File outputFile, List<Reservation> reservations) throws FileNotFoundException, UnsupportedEncodingException {
		// Prepare
		PrintWriter pw = new PrintWriter(outputFile, "UTF-8");
		StringBuilder htmlBuilder = new StringBuilder();
		SimpleDateFormat idformat = new SimpleDateFormat("YYYYMMdd"); 
		SimpleDateFormat dateformat = new SimpleDateFormat("EEEE d. MMMMM YYYY"); 
		SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm");
		
		// Build HTML
		htmlBuilder.append("<!doctype html>");
		htmlBuilder.append("<html>");
		htmlBuilder.append("<head>");
		htmlBuilder.append("<meta charset=\"utf-8\">");
		htmlBuilder.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
		htmlBuilder.append("<title>Kalendáø</title>");
		
		htmlBuilder.append("<link rel=\"icon\" href=\"2Kico.png\" type=\"image/png\">");
		htmlBuilder.append("<link rel=\"stylesheet\" href=\"styles.css\">");
		htmlBuilder.append("<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css\" integrity=\"sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB\" crossorigin=\"anonymous\">");
		htmlBuilder.append("</head>");
		htmlBuilder.append("<body>");
		
		htmlBuilder.append("<div class=\"container\">");
		
		htmlBuilder.append("<table class=\"table table-sm\">");
		htmlBuilder.append("<thead class=\"thead-dark\">");
		htmlBuilder.append("<tr>");
		htmlBuilder.append("<th scope=\"col\">#</th>");
		htmlBuilder.append("<th scope=\"col\">Èas</th>");
		htmlBuilder.append("<th scope=\"col\">Pacient</th>");
		htmlBuilder.append("<th scope=\"col\">Poznámka</th>");
		htmlBuilder.append("<th scope=\"col\">Procedura</th>");
		htmlBuilder.append("</tr>");
		htmlBuilder.append("</thead>");
		htmlBuilder.append("<tbody>");
		
		//init row
		Date date = reservations.get(0).getFrom();
		Integer pointer = 1;
		htmlBuilder.append("<tr id=\"" + idformat.format(date) + "\" class=\"table-active\">");
		htmlBuilder.append("<th scope=\"row\"></th>");
		htmlBuilder.append("<td colspan=\"4\"><strong>" + dateformat.format(date) + "</strong></td>");
		htmlBuilder.append("</tr>");
		
		for (Reservation reservation: reservations) {
			if (!dateformat.format(date).equals(dateformat.format(reservation.getFrom()))) {
				date = reservation.getFrom();
				pointer = 1;
				htmlBuilder.append("<tr id=\"" + idformat.format(date) + "\" class=\"table-active\">");
				htmlBuilder.append("<th scope=\"row\"></th>");
				htmlBuilder.append("<td colspan=\"4\"><strong>" + dateformat.format(date) + "</strong></td>");
				htmlBuilder.append("</tr>");
			}
			htmlBuilder.append("<tr>");
			htmlBuilder.append("<th scope=\"row\">" + pointer + "</th>");
			htmlBuilder.append("<td>" + timeformat.format(reservation.getFrom()) + " - " + timeformat.format(reservation.getTo()) + "</td>");
			htmlBuilder.append("<td>" + reservation.getPatient().getName() + "</td>");
			htmlBuilder.append("<td>" + (reservation.getActivity()==null? "" :  reservation.getActivity()) + "</td>");
			htmlBuilder.append("<td class=\"type type-" + (reservation.getType()==null? "" :  reservation.getType().getId().toUpperCase()) +"\">" + (reservation.getType()==null? "" :  reservation.getType().getName()) + "</td>");
			htmlBuilder.append("</tr>");
			pointer++;
		}
		
		htmlBuilder.append("</tbody>");
		htmlBuilder.append("</table>");
		
		htmlBuilder.append("</div>");
		htmlBuilder.append("<script src=\"scripts.js\"></script>");
		htmlBuilder.append("</body>");
		htmlBuilder.append("</html>");
		
		// Write to file
		String html = htmlBuilder.toString();
		pw.write(html);
		pw.flush();
		pw.close();
		return outputFile;
	}
}
