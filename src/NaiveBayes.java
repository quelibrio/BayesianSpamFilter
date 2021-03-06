import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NaiveBayes  implements Serializable {
	int spamTotal, hamTotal;
	int spamMailsCount, hamMailsCount;
	double PriorHam, PriorSpam;
	boolean useHeaders;
	
	public Map<String, Integer> spamWords;
	public Map<String, Integer> hamWords;
	public Map<String, Integer> allWords;
	
	//====Thing to add:
	/*
	 * 1*-StopWords (words that are too general)
	 * 2-Headers (domain names etc.)
	 * 3-Phrases (Combination of words)
	 * 4-likelihood ratios (leaning towards classifying ham than spam)
	 * 5-Combine multivariate and multinomial into one mixed classifer?
	 */
	
	public NaiveBayes(){
		useHeaders=true;
		
		spamMailsCount = 0;
		hamMailsCount = 0;
		spamTotal=0;
		hamTotal=0;
		PriorHam=0;
		PriorSpam=0;
		
		this.spamWords = new HashMap<String, Integer>();
		this.hamWords = new HashMap<String, Integer>();
		this.allWords = new HashMap<String, Integer>();
	}
	
	public void Train(int testType, List<Mail> mails){
		if (testType==0){
			TrainMultinomial(mails);
		}
		else if (testType==1){
			TrainMultivariate(mails);
		}
	}
	
	public void RemoveNeutralWords(){
		//clone spamwords:
		List<String> HamWordsCopy= new ArrayList<String>();
		for (String word: spamWords.keySet()){
			HamWordsCopy.add(word);
		}
		int count=0;
		double diffsum = 0.0;
		for(String word: HamWordsCopy){
			if (hamWords.containsKey(word)){
				double threshold = 0.80;
				double difference = (double)spamWords.get(word)/hamWords.get(word);
				if (threshold<difference && difference<1.0/threshold){
					spamWords.remove(word);
					hamWords.remove(word);
					count++;
					if (threshold<difference){
						diffsum+=difference;
					}
					else{
						diffsum+=(double)hamWords.get(word)/spamWords.get(word);;
					}
				}
			}
		}
		System.out.println("Removed " + count + " words in RemoveNeutralWords. Diff Average: "+diffsum/count);
	}
	
	public void TrainMultivariate(List<Mail> mails){
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
		
	public double GetProbabilityWordMultivariate(String word, boolean getSpam){
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
	
	
	public boolean PredictIfSpamMultivariate(Mail mail){
		double spamWordLikelihood;
		double hamWordLikelihood;
		
		double spamlogLikelihood=0.0;
		double hamlogLikelihood=0.0;

		for(String word: allWords.keySet()){
			if (mail.wordsMap.containsKey(word)){
				spamWordLikelihood=GetProbabilityWordMultivariate(word, true);
				hamWordLikelihood=GetProbabilityWordMultivariate(word, false);
			}
			else{
				spamWordLikelihood=1.0-GetProbabilityWordMultivariate(word, true);
				hamWordLikelihood=1.0-GetProbabilityWordMultivariate(word, false);
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
			return  ((double)countInSpam+1)/(spamTotal + spamWords.size()); // +1 and spamWords.size() are for smoothing. we have added 1 word for each word.
		}
		else{
			return ((double)countInHam+1)/(hamTotal + hamWords.size());
		}	
	}
	
	public boolean PredictIfSpamMultinomial(Mail mail){
		double spamWordLikelihood;
		double hamWordLikelihood;
		
		double spamlogLikelihood=0.0;
		double hamlogLikelihood=0.0;
		
		int totalOccurances = 0;
		for(String word: mail.wordsMap.keySet()){
			//==we only need to look for words in email. operation for words in allWords that are not in email is multiplying by 1 which is irrelevant.
			//=====
			//long factorail=CombinatoricsUtils.factorial(1);
			//System.out.println("Factorial for word: "+word+" is "+factorail);
			//spamWordLikelihood=GetProbabilityWordMultinomial(word, true)/factorail;
			spamWordLikelihood=Math.pow(GetProbabilityWordMultinomial(word, true), mail.wordsMap.get(word));
			hamWordLikelihood=Math.pow(GetProbabilityWordMultinomial(word, false), mail.wordsMap.get(word));
			//=====
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
		//==============
	}
}
