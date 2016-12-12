import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.mail.MessagingException;

public class Init {
	public static void main(String[] args) throws IOException, MessagingException {
		ArrayList<Mail> mails = processDataFiles("bin\\Mails");
		NaiveBayes bayse = new NaiveBayes();
		bayse.Train(mails);
		Mail testMail = processDataFiles("bin\\TestMails").get(0);
		bayse.PredictIfSpam(testMail);
		//=======================================
		ArrayList<Mail> AllMails = processDataFiles("bin\\Mails");
		double kFoldAccuracy=CrossValidateKFold(AllMails,10);
		System.out.println("Average Accuracy Overall: "+kFoldAccuracy);
	}
	
	private static double CrossValidateKFold(List<Mail> mails, int folds) {
		//=======
		Collections.shuffle(mails);
		//=======
		System.out.println("===Stratified K-Fold===");
		double accuracySum=0.0;
		for (int i=0;i<folds;i++){
			int foldStart=i*mails.size()/folds;
			int foldEnd=((i+1)*mails.size()/folds)-1;
			List<Mail> TestMails=mails.subList(foldStart, foldEnd);
			List<Mail> TrainMails= new ArrayList<Mail>();
			if(i>0){
				TrainMails.addAll(mails.subList(0, foldStart-1));
			}
			if(i<folds-1){
				TrainMails.addAll(mails.subList(foldEnd+1, mails.size()-1));
			}
			NaiveBayes bayse = new NaiveBayes();
			bayse.Train(TrainMails);
			double foldAccuracy = bayse.TestMails(TestMails);
			//System.out.println("Fold "+i+" Fold start "+foldStart+" foldEnd "+foldEnd+" mails.size()-1 "+(mails.size()-1));
			System.out.println("Accuracy for Fold "+i+": "+foldAccuracy);
			accuracySum+=foldAccuracy;
		}
		return accuracySum/folds;
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
