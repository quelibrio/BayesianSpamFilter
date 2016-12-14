import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


import javax.mail.MessagingException;

public class Init {
	public static void main(String[] args) throws Exception {
		/*ArrayList<Mail> mails = processDataFiles("bin\\Mails");
		NaiveBayes bayse = new NaiveBayes();
		bayse.Train(1,mails);
		Mail testMail = processDataFiles("bin\\TestMails").get(0);
		bayse.PredictIfSpamMultivariate(testMail);*/
		//=======================================
		ArrayList<Mail> AllMails = processDataFiles("bin\\Mails");
		double Accuracy=0;
		Accuracy=ModelValidations.StratifiedKFold(1,AllMails,10);
		System.out.println("Average Accuracy Overall: "+Accuracy);
		
		//Accuracy=ModelValidations.StratifiedKFold(1,AllMails,10);
		//System.out.println("Average Accuracy Overall: "+Accuracy);
		//Accuracy=ModelValidations.RandomClassifier(AllMails);
		//System.out.println("Average Accuracy Overall: "+Accuracy);
	}
	
	private static ArrayList<Mail> processDataFiles(String location) throws IOException, MessagingException {
		File dir = new File(location);
		System.out.println(dir.getAbsolutePath());
		ArrayList<Mail> mails = new ArrayList<Mail>();;
		File[] files = dir.listFiles();
		if(dir.isDirectory()) {
		for (File file : files) {
			try{
		
			    Mail parsedMail = GetMailFromFile(file);
			    mails.add(parsedMail);
					//System.out.println(parsedMail.body);
				}
				catch(FileNotFoundException ex){
					
				}
			    finally{
			    	
			    }
			}
		}
		return mails;
	}
	
	private static Mail GetMailFromFile(File file) throws IOException, MessagingException{
	    String path = file.getPath();
	    FileInputStream fis = new FileInputStream(file);
	    byte[] data = new byte[(int) file.length()];
	    fis.read(data);
	    fis.close();
	 
	    Mail mail; 
	    String messageData = new String(data);
	    if(path.contains("ham")){
	    	//Ham mail
	    	mail = new Mail(false, messageData);
	    }else{
	    	//Spam mail
	    	mail = new Mail(true, messageData);
	    }
	    return mail;
	}
	
}
