import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModelValidations {
	public static double CrossValidateKFold(List<Mail> mails, int folds) {
		System.out.println("===Cross Validate K-Fold===");
		//=======
		Collections.shuffle(mails);
		//=======
		return KFoldTest(mails, folds);
	}
	
	private static double KFoldTest(List<Mail> mails, int folds) {
		
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
			//bayse.TrainMultinomial(TrainMails);
			double foldAccuracy = bayse.TestMails(TestMails);
			//System.out.println("Fold "+i+" Fold start "+foldStart+" foldEnd "+foldEnd+" mails.size()-1 "+(mails.size()-1));
			System.out.println("Accuracy for Fold "+i+": "+foldAccuracy);
			accuracySum+=foldAccuracy;
		}
		return accuracySum/folds;
	}
	
	public static double StratifiedKFold(List<Mail> mails, int folds) {
		System.out.println("===Stratified K-Fold===");
		List<Mail> HamMails= new ArrayList<Mail>();
		List<Mail> SpamMails= new ArrayList<Mail>();
		for (Mail mail : mails) {
			if (mail.isSpam){
				SpamMails.add(mail);
			}
			else{
				HamMails.add(mail);
			}
		}
		Collections.shuffle(HamMails);
		Collections.shuffle(SpamMails);
		List<Mail> StratifiedMails= new ArrayList<Mail>();
		for (int i=0;i<folds;i++){
			List<Mail> StratifiedSubMails= new ArrayList<Mail>();
			//===
			int foldStart=i*HamMails.size()/folds;
			int foldEnd=((i+1)*HamMails.size()/folds)-1;
			StratifiedSubMails.addAll(HamMails.subList(foldStart, foldEnd));
			//===
			foldStart=i*SpamMails.size()/folds;
			foldEnd=((i+1)*SpamMails.size()/folds)-1;
			StratifiedSubMails.addAll(SpamMails.subList(foldStart, foldEnd));
			//===
			Collections.shuffle(StratifiedSubMails);
			StratifiedMails.addAll(StratifiedSubMails);
		}
		
		return KFoldTest(StratifiedMails, folds);
	}
	
	public static double RandomClassifier(List<Mail> mails){
		System.out.println("===Random Classifier==");
		int count=0;
		for (Mail mail : mails) {
			boolean ClassifiedasSpam=false;
			if (Math.random()>0.5){
				ClassifiedasSpam=true;
			}
			//===
			if(mail.isSpam == ClassifiedasSpam){
				count++;
			}
		}
		return (double)count/mails.size();
	}
	
}
