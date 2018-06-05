package cz.twokdent.calendar.exporter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VbsExporter {
public static File export(File vbsEnvFile, File vbsExecFile, File exportFolder, Date from, Date to) throws IOException {
	for (File file : exportFolder.listFiles()) {
		file.delete();
	}
	SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
	String cmd = "cmd /C" + " " + vbsEnvFile.getAbsolutePath() + " \"" + vbsExecFile.getAbsolutePath() + "\" \"" + exportFolder.getAbsolutePath() + "\" " + df.format(from) + " " + df.format(to);
	Process p = Runtime.getRuntime().exec(cmd);
	if(p.getErrorStream().read() != -1) throw new IOException("Error executing command: " + cmd);
	String filename = df.format(from) + "000000" + "_" + df.format(to) + "000000" + ".xml";
	return new File(exportFolder, filename);
}

}
