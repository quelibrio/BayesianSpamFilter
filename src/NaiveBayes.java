import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NaiveBayes {
	int spamWordsCount = 0;
	int hamWordsCount = 0;
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
				for (float f : mail.wordsMap.values()) {
					spamWordsCount += f;
				}
				spamWords.putAll(mail.wordsMap);
			}
			else {
				for (float f : mail.wordsMap.values()) {
					hamWordsCount += f;
				}
				hamWords.putAll(mail.wordsMap);
			}
		}
		
		this.PriorHam = (double)hamWordsCount / (hamWordsCount + spamWordsCount);
		this.PriorSpam = (double)spamWordsCount / (hamWordsCount + spamWordsCount);
		System.out.print("P(HAM): " + PriorHam +" P(SPAM) " + PriorSpam);
	}
	
	public double GetProbabilityWordInSpam(String word){
		word = word.toLowerCase();
		
		int countInSpam = 0;
		if(spamWords.containsKey(word))
			countInSpam +=spamWords.get(word);
		
		double PosteriorSpam = (double)countInSpam/(spamWordsCount);
		
		int countInHam =0;
		if(hamWords.containsKey(word))
			countInHam += hamWords.get(word);
		
		double PWord = (double)(countInHam + countInSpam)/(hamWordsCount + spamWordsCount);
		double PosteriorHam = (double)countInHam/(hamWordsCount);
				
		if(PWord == 0)
			return 0;
		
		return (PosteriorSpam * PriorSpam) / PWord;
	}
	
	public boolean PredictIfSpam(Mail mail){
		for(String word: mail.wordsMap.keySet()){
			System.out.println(word + ": " + GetProbabilityWordInSpam(word));
		}
		return false;
	}
}
