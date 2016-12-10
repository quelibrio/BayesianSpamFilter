import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NaiveBayes {
	int spamMailsCount = 0;
	int hamMailsCount = 0;
	public Map<String, Integer> spamWords;
	public Map<String, Integer> hamWords;
	double PriorHam;
	double PriorSpam;
	
	public NaiveBayes(){
		this.spamWords = new HashMap<String, Integer>();
		this.hamWords = new HashMap<String, Integer>();
	}
	
	
	public void Train(List<Mail> mails){
		for (Mail mail : mails) {
			
			if(mail.spam == true){
				spamMailsCount++;
				for(String word:mail.wordsMap.keySet()){
					if (spamWords.containsKey(word)){
						spamWords.put(word, spamWords.get(word)+1);
					}
					else{
						spamWords.put(word, 1);
					}
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
		
		//this.PriorHam = (double)hamMailsCount / (hamMailsCount + spamMailsCount);
		this.PriorSpam = ((double)spamMailsCount+2) / (hamMailsCount + spamMailsCount+2);
		System.out.print("P(HAM): " + PriorHam +" P(SPAM) " + PriorSpam);
	}
	
	public double GetProbabilityWordInSpam(String word){
		//word = word.toLowerCase();
		
		int countInSpam = 0;
		if(spamWords.containsKey(word))
			countInSpam +=spamWords.get(word);
		
		double PosteriorSpam = ((double)countInSpam+1)/(spamMailsCount+2); // "+1" is for smoothing.
		
		int countInHam =0;
		if(hamWords.containsKey(word))
			countInHam += hamWords.get(word);
		
		double PWord = (double)(countInHam + countInSpam+1)/(hamMailsCount + spamMailsCount+2); // "+1" is for smoothing.
		//double PosteriorHam = (double)countInHam/(hamMailsCount);
				
		if(PWord == 0)
			return 0;
		
		return (PosteriorSpam * PriorSpam) / PWord;
	}
	
	public boolean PredictIfSpam(Mail mail){
		double spamProbability=1.0;
		double hamProbability=1.0;
		double wordLikelihood;
		int tempCount=0;
		//int count=mail.wordsMap.keySet().size();
		for(String word: spamWords.keySet()){
			if (mail.wordsMap.containsKey(word)){
				wordLikelihood=GetProbabilityWordInSpam(word);
				if(GetProbabilityWordInSpam(word)==0.0){
					System.out.println("word: " + word + " is zero.");
				}
				//
			}
			else{
				wordLikelihood=1.0-GetProbabilityWordInSpam(word);
				if(GetProbabilityWordInSpam(word)==1.0){
					System.out.println("word: " + word + " is one.");
				}
			}
			spamProbability*=wordLikelihood;
		}
		System.out.println("Spam probability: " + spamProbability);
		//====
		for(String word: hamWords.keySet()){
			if (mail.wordsMap.containsKey(word)){
				wordLikelihood=GetProbabilityWordInSpam(word);
				//
			}
			else{
				wordLikelihood=1.0-GetProbabilityWordInSpam(word);
			}
			hamProbability*=wordLikelihood;
		}
		System.out.println("Ham probability: " + hamProbability);
		//===
		if(spamProbability>hamProbability){
			return true;
		}
		else{
			return false;
		}
	}
}
