import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.jsoup.Jsoup;

public class Mail {
	public boolean isSpam;
	public String body;
	public Enumeration<Header> headers; 
	public Map<String, Integer> wordsMap;
	boolean insertSubject;
	
	public Mail(boolean spam, String unformatted) throws MessagingException, IOException{
		insertSubject=true;
		this.wordsMap = new HashMap<>();
		this.isSpam = spam;
		
		Properties props = new Properties();
	    props.put("mail.smtp.host", "my-mail-server");
		Session s = Session.getDefaultInstance(props);
		InputStream is = new ByteArrayInputStream(unformatted.getBytes());
		MimeMessage message = new MimeMessage(s, is);
		message.getAllHeaderLines();
		
		String parsedBody = message.getContent().toString();
		
		//System.out.println("><><><><><><><><><><><><>Start of message<><><><><><><><><><><><><><><" + this.isSpam);
	
		String htmlStripped = Jsoup.parse(parsedBody).text();
	    //String stemmedContent = Stemmer.stemString(htmlStripped);
		this.body =  htmlStripped;
		//==========================================================
		//===adding subject to body
		for (Enumeration<Header> e = message.getAllHeaders(); e.hasMoreElements();) {
		    Header h = e.nextElement();
		    if(h.getName().contains("Subject")){
		    	body=body+" "+h.getValue();
		    }
		    if(h.getName().contains("From") && h.getValue().contains("@")){
		    	String[] splitted = h.getValue().split("@");
				wordsMap.put(splitted[1], 20);
		    }
		}
		//==========================================================
		
		String[] words = body.split("\\W+");
		///Skipping words decrease accuracy
		//ArrayList<String> stopWords = getStopWords();

		for (String w : words) {
			   w = w.toLowerCase();
//			   if(isStopWord(w, stopWords))
//				   continue;
			   w=Stemmer.stemString(w);
			   
			   /*if (!w.matches(".*\\d.*")){
			   }*/
			   Integer n = wordsMap.get(w);
			   n = (n == null) ? 1 : ++n;
			   wordsMap.put(w, n);
		}
		//System.out.println("==!!!WORDMAP AS IT IS:" + wordsMap);
		 
		//For printing purposes
		int end = body.length() > 200 ? 200 : body.length();
		
		//System.out.println("Message size: " + message.getSize() + "Word count: " + words.length + "Different words: " + wordsMap.keySet().size());
		//System.out.println(body.substring(0, end));
		//System.out.println("-----//---------//---------");
	   
		//Extract headers
		for (Enumeration<Header> e = message.getAllHeaders(); e.hasMoreElements();) {
		    Header h = e.nextElement();
		    //if(h.getName().contains("From") && h.getValue().contains("@"))
		    	//System.out.println(h.getName() + " $#$ " +    h.getValue());

		}
		//System.out.println("><><><><><><><><><><><><>End of message<><><><><><><><><><><><><><><");
	}
	public ArrayList<String> getStopWords(){
		   ArrayList<String> words = new ArrayList<String>();
		   try {
				File file = new File("bin/stopwords.txt");
				FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					words.add(line);
				}
				fileReader.close();

				
			} catch (IOException e) {
				e.printStackTrace();
			}
		   return words;
	}
	public boolean isStopWord(String word, ArrayList<String> stopWords) throws FileNotFoundException{
		   return stopWords.contains(word.toLowerCase());
	}
}
