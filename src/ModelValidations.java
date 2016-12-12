import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModelValidations {
	public static double CrossValidateKFold(List<Mail> mails, int folds) {
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
			bayse.TrainMultinomial(TrainMails);
			double foldAccuracy = bayse.TestMails(TestMails);
			//System.out.println("Fold "+i+" Fold start "+foldStart+" foldEnd "+foldEnd+" mails.size()-1 "+(mails.size()-1));
			System.out.println("Accuracy for Fold "+i+": "+foldAccuracy);
			accuracySum+=foldAccuracy;
		}
		return accuracySum/folds;
	}

}
