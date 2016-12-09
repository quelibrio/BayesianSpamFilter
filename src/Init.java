import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Scanner;

public class Init {
	public static void main(String[] args) throws IOException {
		File dir = new File("bin\\Mails");
		System.out.println(dir.getAbsolutePath());
		Scanner s = null;
		File[] files = dir.listFiles();
		if(dir.isDirectory()) {
		for (File file : files) {
			try{
				s = new Scanner(file);
				
			    String path = file.getPath();
			    Mail mail; 

			    FileInputStream fis = new FileInputStream(file);
			    byte[] data = new byte[(int) file.length()];
			    fis.read(data);
			    fis.close();
			    String messageData = new String(data);
		    	mail = new Mail(true, messageData);
		    
			    //System.out.println(basePath);
				System.out.println(messageData);
			}
			catch(FileNotFoundException ex){
				
			}
		    finally{
		    	s.close();
		    }
		}
		}
	}
}
