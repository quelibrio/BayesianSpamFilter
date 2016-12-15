import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


import javax.mail.MessagingException;

public class filter {
	public static void main(String[] args) throws Exception {
		ModelSerialization serializer = new ModelSerialization("bayesModel.ser");
		//This part has to be used only for training and serializing to a file
		//It needs to be done once
		/*ArrayList<Mail> mails = processDataFiles("bin\\Mails");
		NaiveBayes bayse = new NaiveBayes();
		bayse.Train(0, mails);
		serializer.Serialize(bayse);*/

		if(args.length > 0){
			String path = args[0];
			NaiveBayes bayseDeserialized =  serializer.Deserialize();
			Mail testMail = processFilePath(path);
			boolean isSpam = bayseDeserialized.PredictIfSpamMultivariate(testMail);
			String hamOrSpam = isSpam ? "spam" : "ham";
			System.out.println(hamOrSpam + "\n");
		}
		//=======================================
		else{
			ArrayList<Mail> AllMails = null;
			AllMails = processDataFiles("bin\\Mails");
			
			double Accuracy = ModelValidations.StratifiedKFold(0 ,AllMails, 10);
			System.out.println("Average Accuracy Overall: "+Accuracy);
			//Accuracy=ModelValidations.StratifiedKFold(1,AllMails,10);
			//System.out.println("Average Accuracy Overall: "+Accuracy);
			//Accuracy=ModelValidations.RandomClassifier(AllMails);
			//System.out.println("Average Accuracy Overall: "+Accuracy);
		}

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
	
	public static Mail processFilePath(String filePath) throws IOException, MessagingException{
		Path path = Paths.get(filePath);
		Mail mail = GetMailFromFile(path.toFile());
		return mail;
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
