import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Init {
	public static void main(String[] args) {
		File dir = new File("bin\\Mails");
		System.out.println(dir.getAbsolutePath());
		Scanner s = null;
		File[] files = dir.listFiles();
		if(dir.isDirectory()) {
		for (File file : files) {
			try{
				s = new Scanner(file);
			    String basePath = file.getPath();
			    System.out.println(basePath);
				System.out.println(file.length());
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
