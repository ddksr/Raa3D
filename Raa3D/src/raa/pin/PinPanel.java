package raa.pin;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class PinPanel {
	public static final String EXT = "r3dp";
	public static String tmpLoc = "tmp";
	private String fileLoc = null;
	private String id;
	
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
	}
	
	public PinPanel(String nid) {
		id = nid;
		refresh();
	}
	
	private void refresh() {
		String loc = tmpLoc + File.separator + id;
		try {
			index = PinPanelIndex.open(loc + File.separator + "index");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setFileName(String fname) {
		fileLoc = fname;
		String[] parts = fname.split("\\.");
		if(! parts[parts.length-1].equals(PinPanel.EXT)) fileLoc += "." + PinPanel.EXT;
	}

	public void save() throws Exception {
		String loc = tmpLoc + File.separator + id;
		// Refresh index
		PrintWriter pw = new PrintWriter(new FileWriter(loc + File.separator + "index"));
		pw.print(index.toString());
		pw.close();
		
		
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
		
		return new PinPanel(nid);
	}
	
	public void close() throws Exception {
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
	}
}
