package raa.pin;

public class PinNote {
	public static final int DEFAULT_TYPE = 0;
	public static final int IMAGE_TYPE = 1;
	public static final int TEXT_TYPE = 2;
	public static final int ABSOLUTE_TYPE = 3;
	//public static final int EXTERNAL_TYPE = 3;
	
	
	private double x;
	private double y;
	private double z;
	private int type;
	private String value;
	
	public PinNote() {
		x = 0;
		y = 0;
		z = 0;
		type = PinNote.DEFAULT_TYPE;
		value = new String();
	}
	
	public PinNote(double x1, double y1, double z1, String typeVal, String val) {
		x = x1;
		y = y1;
		z = z1;
		type = PinNote.typeVal2type(typeVal);
		value = val;
	}
	
	public PinNote(double x1, double y1, double z1, int typ, String val) {
		x = x1;
		y = y1;
		z = z1;
		type = typ;
		value = val;
	}
	
	public static int typeVal2type(String val) {
		if(val.equals("def")) return 0;
		if(val.equals("img")) return 1;
		if(val.equals("text")) return 2;
		if(val.equals("abs")) return 3;
		return 0;
	}
	
	public static String type2typeVal(int t) {
		String[] vals = {"def", "img", "text", "abs"};
		return t >= 0 && t < vals.length ? vals[t] : "def";
	}
	
	public String getValue() {
		return value;
	}
	
	public String getTextValue() {
		if(type == PinNote.TEXT_TYPE) {
			//TODO
		}
		return null;
	}
	public String getImageValue() {
		if(type == PinNote.IMAGE_TYPE) {
			//TODO
		}
		return null;
	}
	
	public double distanceTo(double x1, double y1, double z1) {
		double distSq = Math.pow((x - x1), 2) + Math.pow((y - y1), 2) + Math.pow((z - z1), 2);
		return Math.sqrt(distSq);
	}
	
	private String dbl2str(double x) {
		String ret = (x + "").replace(',', '.');
		return ret.split("\\.").length > 1 ? ret : (ret + ".0");
	}
	
	public String toString() {
		return String.format("%s %s %s %s \"%s\"", PinNote.type2typeVal(type), dbl2str(x), dbl2str(y), dbl2str(z), value);
	}
	
	public static PinNote newImgNote(double x1, double y1, double z1, String loc) {
		return new PinNote(x1, y1, z1, PinNote.IMAGE_TYPE, loc);
	}	
	public static PinNote newAbsNote(double x1, double y1, double z1, String coo) {
		return new PinNote(x1, y1, z1, PinNote.ABSOLUTE_TYPE, coo);
	}	
	public static PinNote newTextNote(double x1, double y1, double z1, String text) {
		return new PinNote(x1, y1, z1, PinNote.TEXT_TYPE, text);
	}	
	public static PinNote newNote(double x1, double y1, double z1, String anything) {
		return new PinNote(x1, y1, z1, PinNote.DEFAULT_TYPE, anything);
	}
}
