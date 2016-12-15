import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleFeatureTree {
	int spamTotal, hamTotal;
	int spamMailsCount, hamMailsCount;
	double PriorHam, PriorSpam;
	boolean useHeaders;
	
	public Map<String, Integer> spamWords;
	public Map<String, Integer> hamWords;
	//public Map<String, Integer> spamWordsTest;
	//public Map<String, Integer> hamWordsTest;
	List<Mail> Testmails;
	List<Mail> Backupmails;
	
	double threshold;
	
	public SimpleFeatureTree(){
		useHeaders=true;
		
		spamMailsCount = 0;
		hamMailsCount = 0;
		spamTotal=0;
		hamTotal=0;
		PriorHam=0;
		PriorSpam=0;
		
		this.spamWords = new HashMap<String, Integer>();
		this.hamWords = new HashMap<String, Integer>();
		this.Testmails = new ArrayList<Mail>();
		this.Backupmails = new ArrayList<Mail>();
		
		threshold=2.0;
	}
	
	public void InitTrain(List<Mail> mails){
		for (Mail mail : mails) {
			Testmails.add(mail);
			Backupmails.add(mail);
		}
		Train(mails);
	}
	
	public void Train(List<Mail> mails){
		spamWords.clear();
		hamWords.clear();
		
		for (Mail mail : mails) {
			
			if(mail.isSpam == true){
				spamMailsCount++;
				for(String word:mail.wordsMap.keySet()){
					if (spamWords.containsKey(word)){
						spamWords.put(word, spamWords.get(word)+1);
					}
					else{
						spamWords.put(word, 1);
					}
				}
				if (useHeaders){
					
				}
			}
			else {
				hamMailsCount++;
				for(String word:mail.wordsMap.keySet()){
					if (hamWords.containsKey(word)){
						hamWords.put(word, hamWords.get(word)+1);
					}
					else{
						hamWords.put(word, 1);
					}
				}
			}
		}
	}
	
	public boolean Predict(Mail mail){
		//System.out.println("===SIMPLE FEATURE TREE PREDICT===");
		
		double diffmax=threshold+0.001;
		String wordmax="";
		int count=0;
		//System.out.println("predicting:");
		while (diffmax>threshold){
			//System.out.println("count: " + count + " diffmax: "+diffmax+" wordmax: "+wordmax);
			diffmax=threshold;
			wordmax="";
			for (String word: spamWords.keySet()){
				if (hamWords.containsKey(word)){
					if (diffmax<(double)spamWords.get(word)/hamWords.get(word)){
						diffmax=(double)spamWords.get(word)/hamWords.get(word);
						wordmax=word;
						//System.out.println("found wordmax: "+wordmax+" at count: "+count);
					}
				}
			}
			
			if (mail.wordsMap.containsKey(wordmax)){
				//System.out.println("RETURN TRUE=found word: "+wordmax+" at diff: "+diffmax+" in count: "+count);
				Reset();
				return true;
			}
			else{
				//make a new list of emails that do not contain wordmax and repeat.
				List<Mail> Newmails = new ArrayList<Mail>();
				for (Mail testmail : Testmails) {
					if (!testmail.wordsMap.containsKey(wordmax)){
						Newmails.add(testmail);
					}
				}
				Testmails=Newmails;
				Train(Testmails);
			}
			count++;
		}
		
		//System.out.println("RETURN FALSE=Could not find word at count: "+count);
		Reset();
		return false;
	}
	
	public void Reset(){
		Testmails.clear();
		for (Mail mail : Backupmails) {
			Testmails.add(mail);
		}
		//===
		Train(Testmails);
	}
	
}
