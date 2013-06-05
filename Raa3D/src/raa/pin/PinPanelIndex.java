package raa.pin;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PinPanelIndex {
	public static final String pattern = "(def|img|text|abs) (\\d\\.\\d) (\\d\\.\\d) (\\d\\.\\d) \"(.+)\""; 
	private LinkedList<PinNote> notes; // internal list
	
	private int[] counter;
	
	public PinPanelIndex() {
		notes = new LinkedList<PinNote>();
		counter = new int[4]; // for every pin note type
	}
	
	public void setCounter(int[] cnt) {
	    counter = cnt;
	}
	
	public void add(PinNote note) {
		notes.add(note);
		counter[note.getType()]++;
	}
	
	public int size() {
		return notes.size();
	}
	
	public LinkedList<PinNote> getNotes() {
	    return notes;
	}
	
	public PinNote getFirst() {
	    return notes.getFirst();
	}
	
	public static PinPanelIndex open(String fname) throws IOException {
		PinPanelIndex index = new PinPanelIndex();
		BufferedReader in = new BufferedReader(new FileReader(fname));
		String line;
		int[] cnt = new int[4];
		while((line=in.readLine()) != null) {
			// Extract comment
			String ref = line.split("#")[0];
			Pattern rgx = Pattern.compile(pattern);
			Matcher m = rgx.matcher(ref);
			System.out.println("baa");
			if(m.find()) {
			    System.out.println("buuu");
				try {
					String typ = m.group(1);
					String x = m.group(2);
					String y = m.group(3);
					String z = m.group(4);
					String val = m.group(5);
					PinNote note = new PinNote(Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z), typ, val);
					//System.out.println(note.getName());
					note.isNew = false;
					if(note.getType() == PinNote.TEXT_TYPE) {
					    note.markUnsynced();
					}
					else if(note.getType() == PinNote.IMAGE_TYPE) {
					    note.markUnsynced();
					}
					
					index.add(note);
					
					cnt[PinNote.typeVal2type(typ)]++;
					index.setCounter(cnt);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		in.close();
		return index;
	}
	
	@Override
    public String toString() {
		String out = new String();
		for (PinNote note : notes) {
			out += note.toString() + "\n";
		}
		return out;
	}
	
	public int count(int type) {
	    return counter[type];
	}
	
	public void remove(PinNote note) {
	    notes.remove(note);
	}
}

