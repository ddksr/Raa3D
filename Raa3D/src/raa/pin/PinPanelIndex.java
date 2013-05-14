package raa.pin;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PinPanelIndex {
	public static final String pattern = "(def|img|text|abs) (\\d\\.\\d) (\\d\\.\\d) (\\d\\.\\d) \"(.+)\""; 
	private LinkedList<PinNote> notes; // internal list
	
	public PinPanelIndex() {
		notes = new LinkedList<PinNote>();
	}
	
	public void add(PinNote note) {
		notes.add(note);
	}
	
	public int size() {
		return notes.size();
	}
	
	
	public static PinPanelIndex open(String fname) throws IOException {
		PinPanelIndex index = new PinPanelIndex();
		BufferedReader in = new BufferedReader(new FileReader(fname));
		String line;
		while((line=in.readLine()) != null) {
			// Extract comment
			String ref = line.split("#")[0];
			Pattern rgx = Pattern.compile(pattern);
			Matcher m = rgx.matcher(ref);
			if(m.find()) {
				try {
					String typ = m.group(1);
					String x = m.group(2);
					String y = m.group(3);
					String z = m.group(4);
					String val = m.group(5);
					PinNote note = new PinNote(Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z), typ, val);
					index.add(note);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		in.close();
		return index;
	}
	
	public String toString() {
		String out = new String();
		for (PinNote note : notes) {
			out += note.toString() + "\n";
		}
		return out;
	}
}

