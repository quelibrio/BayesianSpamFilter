import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

public class ModelValidations {
	//testTypes: 0-Multinomial, 1-Multivariate.
	
	public static double CrossValidateKFold(int testType, List<Mail> mails, int folds) throws Exception {
		System.out.println("===Cross Validate K-Fold===");
		//=======
		Collections.shuffle(mails);
		//=======
		return KFoldTest(testType,mails, folds);
	}
	
	private static double KFoldTest(int testType, List<Mail> mails, int folds) throws Exception {
		//testTypes: 0-Multinomial, 1-Multivariate.
		double accuracySum=0.0;
		double f1Sum=0.0;
		double hamf1Sum=0.0;
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

			TestResult result = TrainAndTest(testType,TrainMails,TestMails);
			//bayse.Train(testType,TrainMails);
			//foldAccuracy = bayse.TestMails(testType,TestMails);
			//System.out.println("Fold "+i+" Fold start "+foldStart+" foldEnd "+foldEnd+" mails.size()-1 "+(mails.size()-1));
			//System.out.println("TP TN FP FN for "+ i + ": " + result.truePositive + " " + result.trueNegative + " " + result.falsePositive + " " + result.falseNegative );
			System.out.println("Accuracy for Fold " + i + ": " + result.GetAccuracy());
			//System.out.println("F1 for Fold "+ i + ": " +result.f1Score());
			accuracySum+=result.GetAccuracy();
			f1Sum+=result.f1Score();
			hamf1Sum+=result.f1ScoreHam();
		}
		System.out.println("F1 (Spam) Overall " + f1Sum/folds);
		System.out.println("F1 (Ham) Overall " + hamf1Sum/folds);
		return accuracySum/folds;
	}
	
	public static double StratifiedKFold(int testType, List<Mail> mails, int folds) throws Exception {
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
		//Collections.shuffle(HamMails);
		//Collections.shuffle(SpamMails);
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
			//Collections.shuffle(StratifiedSubMails);
			StratifiedMails.addAll(StratifiedSubMails);
		}
		
		return KFoldTest(testType,StratifiedMails, folds);
	}
	
	public static TestResult TrainAndTest(int testType, List<Mail> trainMails, List<Mail> testMails) throws Exception{
		//ModelSerialization serializer = new ModelSerialization("bayesModel.ser");
		//NaiveBayes bayse = serializer.Deserialize();
		NaiveBayes bayse = new NaiveBayes();
		SimpleFeatureTree ftree = new SimpleFeatureTree();
		if (testType!=0 && testType!=1 && testType!=2){
			throw new Exception("Unspecified Test Type");
		}
		else{
			if (testType==2){
				ftree.InitTrain(trainMails);
			}
			else{
				bayse.Train(testType,trainMails);
				//bayse.RemoveNeutralWords();
			}
			TestResult result= new TestResult();
			result.dataSize = testMails.size();
			
			for (Mail mail : testMails) {
				boolean prediction;
				if (testType==0){
					prediction = bayse.PredictIfSpamMultinomial(mail);
				}
				else if (testType==1){
					prediction = bayse.PredictIfSpamMultivariate(mail);
				}
				else{
					prediction = ftree.Predict(mail);
				}
				//========================================
				if (mail.isSpam && prediction){
					result.truePositive++;
					//ham:TN
				}
				else if (!mail.isSpam && !prediction){
					result.trueNegative++;
					//ham:TP
				}
				else if (!mail.isSpam && prediction){
					result.falsePositive++;
					//ham:FN
				}
				else{
					result.falseNegative++;
					//ham:FP
				}
			}
			result.getPrecissionAndRecall();
			
			//System.out.println("Precission and recall" +  " " + result.precision + " " + result.recall);
			//return (double)(result.truePositive + result.trueNegative)/mails.size();
			return result;
		}
	}
	
	/*public static TestResult TestMails(int testType, List<Mail> mails){
		NaiveBayes bayse = new NaiveBayes();
		
	}*/
	
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
