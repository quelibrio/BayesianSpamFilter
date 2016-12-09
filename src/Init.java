import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Scanner;

public class Init {
	public static void main(String[] args) throws IOException {
		File dir = new File("bin\\Mails");
		System.out.println(dir.getAbsolutePath());
	
		File[] files = dir.listFiles();
		if(dir.isDirectory()) {
		for (File file : files) {
			try{
			    ArrayList<Mail> mails = new ArrayList<Mail>();
		
			    Mail parsedMail = GetMailFromFile(file);
			    mails.add(parsedMail);
					System.out.println(parsedMail.body);
				}
				catch(FileNotFoundException ex){
					
				}
			    finally{
			    	
			    }
			}
		}
	}
	
	private static Mail GetMailFromFile(File file) throws IOException{
	    String path = file.getPath();
	    FileInputStream fis = new FileInputStream(file);
	    byte[] data = new byte[(int) file.length()];
	    fis.read(data);
	    fis.close();
	    
	    Mail mail; 
	    String messageData = new String(data);
	    if(path.contains("spam")){
	    	//Spam mail
	    	mail = new Mail(true, messageData);
	    }else{
	    	//Ham mail
	    	mail = new Mail(false, messageData);
	    }
	    return mail;
	}
	
}
