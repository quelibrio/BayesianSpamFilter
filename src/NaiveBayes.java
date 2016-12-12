import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NaiveBayes {
	int spamMailsCount = 0;
	int hamMailsCount = 0;
	public Map<String, Integer> spamWords;
	public Map<String, Integer> hamWords;
	public Map<String, Integer> allWords;
	int spamTotal;
	int hamTotal;
	double PriorHam;
	double PriorSpam;
	
	//====Thing to add:
	/*
	 * 1-StopWords (words that are too general)
	 * 2-Headers (domain names etc.)
	 * 3-Phrases (Combination of words)
	 */
	
	public NaiveBayes(){
		this.spamWords = new HashMap<String, Integer>();
		this.hamWords = new HashMap<String, Integer>();
		this.allWords = new HashMap<String, Integer>();
		spamTotal=0;
		hamTotal=0;
	}
	
	public void Train(List<Mail> mails){
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
			//===Add every word to allWords
			for(String word:mail.wordsMap.keySet()){
				if (allWords.containsKey(word)){
					allWords.put(word, allWords.get(word)+1);
				}
				else{
					allWords.put(word, 1);
				}
			}
		}
	}
		
	public double GetProbabilityWord(String word, boolean getSpam){
		//if getSpam is true, we return the probability of word being spam. otherwise we return the probability of word being ham.
		//probability of word being spam is = number of spam emails containing the word / total number of emails.
		int countInSpam=0;
		if(spamWords.containsKey(word))
			countInSpam =spamWords.get(word);

		int countInHam=0;
		if(hamWords.containsKey(word))
			countInHam = hamWords.get(word);

		if(getSpam){
			return ((double)countInSpam+1)/(spamMailsCount+1); // "+1" is for smoothing. We have added 1 email that has all the words.
		}
		else{
			return ((double)countInHam+1)/(hamMailsCount+1);
		}	
	}
	
	public double TestMails(List<Mail> mails){
		int count=0;
		for (Mail mail : mails) {
			if(mail.isSpam == PredictIfSpam(mail)){
				count++;
			}
		}
		return (double)count/mails.size();
	}
	
	
	public boolean PredictIfSpam(Mail mail){
		double spamWordLikelihood;
		double hamWordLikelihood;
		
		double spamlogLikelihood=0.0;
		double hamlogLikelihood=0.0;

		for(String word: allWords.keySet()){
			if (mail.wordsMap.containsKey(word)){
				spamWordLikelihood=GetProbabilityWord(word, true);
				hamWordLikelihood=GetProbabilityWord(word, false);
			}
			else{
				spamWordLikelihood=1.0-GetProbabilityWord(word, true);
				hamWordLikelihood=1.0-GetProbabilityWord(word, false);
			}
			spamlogLikelihood+=Math.log(spamWordLikelihood);
			hamlogLikelihood+=Math.log(hamWordLikelihood);

		}
		//===
		if(spamlogLikelihood>hamlogLikelihood){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void TrainMultinomial(List<Mail> mails){
		for (Mail mail : mails) {
			
			if(mail.isSpam == true){
				spamMailsCount++;
				for(String word:mail.wordsMap.keySet()){
					if (spamWords.containsKey(word)){
						spamWords.put(word, spamWords.get(word) + mail.wordsMap.get(word));
					}
					else{
						spamWords.put(word, mail.wordsMap.get(word));
					}
					spamTotal+=mail.wordsMap.get(word);
				}
			}
			else {
				hamMailsCount++;
				for(String word:mail.wordsMap.keySet()){
					if (hamWords.containsKey(word)){
						hamWords.put(word, hamWords.get(word) + mail.wordsMap.get(word));
					}
					else{
						hamWords.put(word, mail.wordsMap.get(word));
					}
					hamTotal+=mail.wordsMap.get(word);
				}
			}
			for(String word:mail.wordsMap.keySet()){
				if (allWords.containsKey(word)){
					allWords.put(word, allWords.get(word) + mail.wordsMap.get(word));
				}
				else{
					allWords.put(word, + mail.wordsMap.get(word));
				}
			}
		}
		
		this.PriorHam = (double)hamMailsCount / (hamMailsCount + spamMailsCount);
		this.PriorSpam = ((double)spamMailsCount) / (hamMailsCount + spamMailsCount);
	}
	
	public double GetProbabilityWordMultinomial(String word, boolean getSpam){
		//getSpam sets it to return the probability of the word being seen
		int countInSpam = 0;
		if(spamWords.containsKey(word))
			countInSpam =spamWords.get(word);

		int countInHam =0;
		if(hamWords.containsKey(word))
			countInHam = hamWords.get(word);

		if(getSpam){
			return  ((double)countInSpam+1)/(spamTotal + spamWords.size()); // +1 and spamWords.size() are for smoothing.
		}
		else{
			return ((double)countInHam+1)/(hamTotal + hamWords.size());
		}	
	}
	
	public boolean PredictIfSpamMultinomial(Mail mail){
		int totalOccurances = 0;
		for(String word: allWords.keySet()){
			int occurances = 0;
			if(mail.wordsMap.containsKey(word)){
				occurances = mail.wordsMap.get(word);
			}	
		}
		return false;
	}
}
