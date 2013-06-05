package raa.pin;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
	
	private String textValue;
	//private Image imgValue;
	private double absX;
	private double absY;
	private double absZ;
	
	private BufferedImage img;
	private String absImgLoc;
	
	private boolean synced = true;
	public boolean isNew = false;
	
	public PinNote() {
		x = 0;
		y = 0;
		z = 0;
		type = PinNote.DEFAULT_TYPE;
		value = new String();
		isNew = true;
	}
	
	/**
	 * Called from PinPanelIndex
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param typeVal
	 * @param val
	 */
	public PinNote(double x1, double y1, double z1, String typeVal, String val) {
		x = x1;
		y = y1;
		z = z1;
		type = PinNote.typeVal2type(typeVal);
		if (type == PinNote.IMAGE_TYPE) {
            synced = false;
            value = val;
        }
        else if (type == PinNote.TEXT_TYPE) {
            textValue = null;
            value = val;
            synced = false;
        }
        else if (type == PinNote.ABSOLUTE_TYPE) {
            value = val;
            String[] splitVal = val.split(" ");
            absX = Double.parseDouble(splitVal[0]);
            absY = Double.parseDouble(splitVal[1]);
            absZ = Double.parseDouble(splitVal[2]);
        }
        else value = val;
		isNew = true;
	}
	
	public PinNote(double x1, double y1, double z1, int typ, String val) {
		x = x1;
		y = y1;
		z = z1;
		type = typ;
		if (type == PinNote.IMAGE_TYPE) {
		    synced = false;
		    value = val;
		}
		else if (type == PinNote.TEXT_TYPE) {
		    textValue = val;
		    value = null;
		    synced = false;
		}
		else if (type == PinNote.ABSOLUTE_TYPE) {
		    value = val;
		    String[] splitVal = val.split(" ");
		    absX = Double.parseDouble(splitVal[0]);
            absY = Double.parseDouble(splitVal[1]);
            absZ = Double.parseDouble(splitVal[2]);
		}
		else value = val;
		isNew = true;
	}
	
	public void setAbsImageLocation(String ail) {
	    absImgLoc = ail;
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
	
	public String getName() {
	    String name = new String("Default note");
	    if(type == PinNote.TEXT_TYPE) {
	        name = new String("Text note");
	    } else if(type==PinNote.ABSOLUTE_TYPE) {
	        name = new String("Absolute point note");
	    } else if (type == PinNote.IMAGE_TYPE) {
	        name = new String("Image note");
	    }
	    return String.format("%s <%f; %f; %f>", name, x, y, z);
	}
	
	public double getX() {
	    return x;
	}

	public double getY() {
        return y;
    }
	
	public double getZ() {
        return z;
    }
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String val) {
	    value = val;
	}
	
	public double getAbsXVal() {
	    return absX;
	}
	
	public double getAbsYVal() {
        return absY;
    }

    public double getAbsZVal() {
        return absZ;
    }
    
    public void setAbs(double x1, double y1, double z1) {
        absX = x1;
        absY = y1;
        absZ = z1;
        value = absX + " " + absY + " " + absZ;
    }
	
	public String getTextValue() {
		if(type == PinNote.TEXT_TYPE) {
			return textValue;
		}
		return null;
	}
	
	public void setTextValue(String tv) {
        if(type == PinNote.TEXT_TYPE) {
            textValue = tv;
        }
    }
	
	public double distanceToNote(PinNote other) {
	    return distanceTo(other.x, other.y, other.z);
	}
	
	public BufferedImage getImageValue() throws IOException {
		if(type == PinNote.IMAGE_TYPE) {
			if (img == null) {
			    System.out.println(absImgLoc);
			    img = ImageIO.read(new File(absImgLoc));
			}
			return img;
		}
		return null;
	}
	
	public int getType() {
	    return type;
	}
	
	public double distanceTo(double x1, double y1, double z1) {
		double distSq = Math.pow((x - x1), 2) + Math.pow((y - y1), 2) + Math.pow((z - z1), 2);
		return Math.sqrt(distSq);
	}
	
	private String dbl2str(double x) {
		String ret = (x + "").replace(',', '.');
		return ret.split("\\.").length > 1 ? ret : (ret + ".0");
	}
	
	@Override
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
	
	public boolean equals(Object o) {
	    if(o instanceof PinNote) {
	        PinNote note = (PinNote)o;
	        return note.x == x && note.y == y && note.z == z;
	    }
	    else return false;
	}
	
	public int compareTo(Object o) {
	    if(o instanceof PinNote) {
	        PinNote note = (PinNote)o;
	        if (note.x == x) {
	            if(note.y == y) {
	                if (note.z == z) return 0;
	                else return Double.compare(note.z, z);
	            }
	            else return Double.compare(note.y, y);
	        }
	        else return Double.compare(note.x, x);
	    }
	    else return -1;
	}

    public void markUnsynced() {
        synced = false;
    }
    
    public void markSynced() {
        synced = true;
    }
    
    public boolean isSynced() {
        return synced;
    }

    public void setAbs(String coo) {
        String[] splitVal = coo.split(" ");
        absX = Double.parseDouble(splitVal[0]);
        absY = Double.parseDouble(splitVal[1]);
        absZ = Double.parseDouble(splitVal[2]);
        value = coo;
    }
    
    public void clearImage() {
        img = null;
    }
}
