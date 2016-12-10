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
	double PriorHam;
	double PriorSpam;
	
	public NaiveBayes(){
		this.spamWords = new HashMap<String, Integer>();
		this.hamWords = new HashMap<String, Integer>();
		this.allWords = new HashMap<String, Integer>();
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
			for(String word:mail.wordsMap.keySet()){
				if (allWords.containsKey(word)){
					allWords.put(word, allWords.get(word)+1);
				}
				else{
					allWords.put(word, 1);
				}
			}
		}
		
		this.PriorHam = (double)hamMailsCount / (hamMailsCount + spamMailsCount);
		this.PriorSpam = ((double)spamMailsCount) / (hamMailsCount + spamMailsCount);
		System.out.print("P(HAM): " + PriorHam +" P(SPAM) " + PriorSpam);
	}
	
	public double GetProbabilityWord(String word, boolean isSpam){
		//word = word.toLowerCase();
		
		int countInSpam = 0;
		if(spamWords.containsKey(word))
			countInSpam +=spamWords.get(word);
		
		double PosteriorSpam = ((double)countInSpam+1)/(hamMailsCount+spamMailsCount+2); // "+1" is for smoothing. 
		
		int countInHam =0;
		if(hamWords.containsKey(word))
			countInHam += hamWords.get(word);
		
		double PosteriorHam = ((double)countInHam+1)/(hamMailsCount+spamMailsCount+2);
		
		double PWord = (double)(countInHam + countInSpam+1)/(hamMailsCount + spamMailsCount+2); // "+1" is for smoothing.
				
		if(PWord == 0)
			return 0;
		
		/*double returnValue=(PosteriorSpam * PriorSpam) / PWord;
		if (returnValue>1.0){
			returnValue=1.0;
		}
		else if(returnValue<0.0){
			returnValue=0.0;
		}*/
		if(isSpam){
			//return (PosteriorSpam * PriorSpam) / PWord;
			return PosteriorSpam;
		}
		else{
			//return (PosteriorHam * PriorHam) / PWord;
			return PosteriorHam;
		}
		
	}
	
	public boolean PredictIfSpam(Mail mail){
		double spamProbability=1.0;
		double hamProbability=1.0;
		double spamWordLikelihood;
		double hamWordLikelihood;
		double spamlogLikelihood=0.0;
		double hamlogLikelihood=0.0;
		int tempCount=0;
		int maxcount=25;
		//int count=mail.wordsMap.keySet().size();
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
			tempCount++;
			if (tempCount==maxcount){
				System.out.println("Spam probability at count "+maxcount+": " + Math.exp(spamWordLikelihood));
				System.out.println("Ham probability at count "+maxcount+": " + Math.exp(hamWordLikelihood));
			}
		}
		spamProbability=spamlogLikelihood;
		hamProbability=hamlogLikelihood;
		System.out.println("Spam probability: " + spamProbability);
		System.out.println("Ham probability: " + hamProbability);
		//====
		//System.out.println("Total Spams "+spamWords.keySet().size()+" Total Hams " + hamWords.keySet().size());
		if(spamProbability>hamProbability){
			System.out.println("Email is SPAM");
			return true;
		}
		else{
			System.out.println("Email is HAM");
			return false;
		}
	}
}
