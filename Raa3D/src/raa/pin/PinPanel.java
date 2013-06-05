package raa.pin;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class PinPanel {
	public static final String EXT = "r3dp";
	public static String tmpLoc = "tmp";
	private String fileLoc = null;
	private String id;
	private boolean hasChanges;
	
	public boolean pointsChanged = true;
	
	public PinPanelIndex index;
	
	public PinPanel() { // used for new
		id = PinPanel.generate_id();
		index = new PinPanelIndex();
		try {
			create();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hasChanges = true;
	}
	
	public PinPanel(String nid, String location) {
		id = nid;
		fileLoc = location;
		try {
            refresh();
        } catch(IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		hasChanges = false;
	}
	
	public boolean hasChanges() {
	    return hasChanges;
	}
	
	public void setFileLocation(String loc) {
	    fileLoc = loc;
	}
	
	private void refresh() throws IOException {
		String loc = tmpLoc + File.separator + id;
		try {
			index = PinPanelIndex.open(loc + File.separator + "index");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Sync text notes
		for (PinNote note : index.getNotes()) {
		    if(!note.isSynced()) {
		        switch(note.getType()) {
		            case PinNote.TEXT_TYPE:
		                BufferedReader in = new BufferedReader(new FileReader(loc + File.separator + "txt" + File.separator + note.getValue()));
		                
		                String row = new String();
		                String txt = new String();
		                while((row = in.readLine()) != null) {
		                    txt += row + "\n";
		                }
		                note.setTextValue(txt);
		                in.close();
		                note.markSynced();
		                break;
		            case PinNote.IMAGE_TYPE:
		                note.setAbsImageLocation(getNoteFilePath(note));
		                note.markSynced();
		                break;
		        }
		    }
		}
	}
	
	public void setFileName(String fname) {
		fileLoc = fname;
		String[] parts = fname.split("\\.");
		if(! parts[parts.length-1].equals(PinPanel.EXT)) 
		    fileLoc += "." + PinPanel.EXT;
	}

	public void save() throws Exception {
		String loc = tmpLoc + File.separator + id;
		// Refresh index
		PrintWriter pw = new PrintWriter(new FileWriter(loc + File.separator + "index"));
		pw.print(index.toString());
		System.out.println("len: " + index.size());
		System.out.println("vsebina: " + index.toString());
		pw.close();
		
		System.out.println("Saving pin panel to " + fileLoc);
		
		int bytesIn;
		byte[] readBuffer = new byte[2156];
		
		if (fileLoc != null) {
			// Zip contents from tempLoc
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(fileLoc)); 
			
			File dirImg = new File(loc + File.separator + "img");
			File dirText = new File(loc + File.separator + "txt");
			File indx = new File(loc + File.separator + "index");
			
			for (File f : dirImg.listFiles()) {
				FileInputStream fis = new FileInputStream(f);
				ZipEntry ent = new ZipEntry("img" + File.separator + f.getName());
				zos.putNextEntry(ent);
				while((bytesIn = fis.read(readBuffer)) != -1) { 
	                zos.write(readBuffer, 0, bytesIn); 
	            } 
				fis.close();
			}
			for (File f : dirText.listFiles()) {
				FileInputStream fis = new FileInputStream(f);
				ZipEntry ent = new ZipEntry("txt" + File.separator + f.getName());
				zos.putNextEntry(ent);
				while((bytesIn = fis.read(readBuffer)) != -1) { 
	                zos.write(readBuffer, 0, bytesIn); 
	            } 
				fis.close();
			}
			FileInputStream fis = new FileInputStream(indx);
			ZipEntry ent = new ZipEntry("index");
			zos.putNextEntry(ent);
			while((bytesIn = fis.read(readBuffer)) != -1) { 
                zos.write(readBuffer, 0, bytesIn); 
            } 
			fis.close();
			
			// save to fileLoc
			zos.close();
			
			hasChanges = false;
		}
		else {
			throw new Exception("File location not specified!");
		}
	}
	
	private static String generate_id() {
		return UUID.randomUUID().toString();
	}
	
	private void create() throws IOException {
		PrintWriter out;
		String loc = tmpLoc + File.separator + id;
		
		new File(loc).mkdir();
		
		out = new PrintWriter(new FileWriter(loc + File.separator + "index"));
		out.print("");
		out.close();
		
		new File(loc + File.separator + "img").mkdir();
		new File(loc + File.separator + "txt").mkdir();
		
	}
	
	public static PinPanel open(String fname) throws IOException {
		String[] parts = fname.split("\\.");
		if(! parts[parts.length-1].equals(PinPanel.EXT)) {
			fname = new String(fname + "." + PinPanel.EXT);
		}
		
		byte[] buffer = new byte[1024];
		String nid = PinPanel.generate_id();
		String loc = tmpLoc + File.separator + nid;
		
		//create output directory is not exists
    	File folder = new File(loc);
    	if(! folder.exists()){
    		folder.mkdir();
    	}
 
    	//get the zip file content
    	ZipInputStream zis = new ZipInputStream(new FileInputStream(fname));
    	//get the zipped file list entry
    	ZipEntry ze = zis.getNextEntry();
 
    	while(ze!=null){
 
    	   String zFileName = ze.getName();
    	   
           File newFile = new File(loc + File.separator + zFileName);
            //create all non exists folders
            //else you will hit FileNotFoundException for compressed folder
            new File(newFile.getParent()).mkdirs();
 
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
            	fos.write(buffer, 0, len);
            }
            
            fos.close();   
            ze = zis.getNextEntry();
    	}
 
        zis.closeEntry();
    	zis.close();
		
    	// Create directories that do not exist
    	File img = new File(loc + File.separator + "img");
    	if(! img.exists()) img.mkdir();
        File txt = new File(loc + File.separator + "txt");
    	if(! txt.exists()) txt.mkdir();
    	
		return new PinPanel(nid, fname);
	}
	
	public void destroy() throws Exception {
		String loc = tmpLoc + File.separator + id;
		
		File dirImg = new File(loc + File.separator + "img");
		File dirTxt = new File(loc + File.separator + "txt");
		
		if(dirImg.exists()) {
			for (File f : dirImg.listFiles()) {
				f.delete();
			}
		}
		if(dirTxt.exists()) {
			for (File f : dirTxt.listFiles()) {
				f.delete();
			}
		}
		dirImg.delete();
		dirTxt.delete();
		new File(loc + File.separator + "index").delete();
		new File(loc).delete();
		pointsChanged = true;
	}
	
	public boolean close() throws Exception {
	    if (hasChanges) {
	        System.out.println("Pin panel has unsaved changes. Destroy?");
	        return false;
	    }
	    else {
	        destroy();
	        return true;
	    }
	}
	
	public void addNew(PinNote note) throws Exception {
	    pointsChanged = true;
	    String loc = tmpLoc + File.separator + id;
	    switch(note.getType()) {
            case PinNote.TEXT_TYPE:
                note.setValue(getNextTextFileName());
                PrintWriter out = new PrintWriter(new FileWriter(loc + File.separator + "txt" + File.separator + note.getValue()));
                out.print(note.getTextValue());
                note.markSynced();
                out.close();
                break;
            case PinNote.IMAGE_TYPE:
                // Note: note value will change after next steps
                String imgLoc = note.getValue();
                String[] parts = imgLoc.split("\\.");
                note.setValue(getNextImageFileName(parts[parts.length - 1]));
                
                // Copy image to tmp/
                InputStream inStream = new FileInputStream(imgLoc);
                OutputStream outStream = new FileOutputStream(loc + File.separator + "img" + File.separator + note.getValue());
                byte[] buf = new byte[1024];
                int len;
                while ((len = inStream.read(buf)) > 0) {
                   outStream.write(buf, 0, len);
                }
                inStream.close();
                outStream.close();
                
                note.setAbsImageLocation(loc + File.separator + "img" + File.separator + note.getValue());
            default:
                break;
        }
	    hasChanges = true;
	    note.isNew = false;
	    index.add(note);
	}

	public LinkedList<PinNote> getNotes() {
        return index.getNotes();
    }

    public PinNote getNearest(double x, double y, double z) {
        if(index.size() == 0) return null;
        PinNote nearest = index.getFirst();
        double min = nearest.distanceTo(x, y, z);
        for(PinNote note : index.getNotes()) {
            double min1 = note.distanceTo(x,y,z);
            if (min1 < min) {
                min = min1;
                nearest = note;
            }
        }
        return nearest;
    }

    public void sync() throws IOException {
        for (PinNote note : index.getNotes()) {
            if(! note.isSynced()) {
                switch(note.getType()) {
                    case PinNote.TEXT_TYPE:
                        PrintWriter out = new PrintWriter(new FileWriter(note.getValue()));
                        out.print(note.getTextValue());
                        note.markSynced();
                        out.close();
                        break;
                    case PinNote.IMAGE_TYPE:
                        //TODO: image sync
                        //TODO: image close (in note)
                        break;
                    default:
                        break;
                }
            }
        }
        
    }
    
    public String getNextTextFileName() {
        return index.count(PinNote.TEXT_TYPE) + ".txt";
    }
    
    public String getNextImageFileName(String ext) {
        return index.count(PinNote.IMAGE_TYPE) + "." + ext;
    }

    public boolean hasFileName() {
        System.out.println(fileLoc);
        return fileLoc != null && fileLoc.length() > 0;
    }
    
    public void removeNote(PinNote note) {
        index.remove(note);
        String noteFile = getNoteFilePath(note);
        if(noteFile != null) new File(noteFile).delete();
        hasChanges = true;
        pointsChanged = true;
    }
    
    public String getNoteFilePath(PinNote note) {
        String loc = tmpLoc + File.separator + id;
        if(note.getType() == PinNote.IMAGE_TYPE) {
            return loc + File.separator + "img" + File.separator + note.getValue();
        }
        else if (note.getType() == PinNote.TEXT_TYPE) {
            return loc + File.separator + "txt" + File.separator + note.getValue();
        }
        return null;
    }

    public void markChanges() {
        hasChanges = true;
    }
}
