import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

public class Mail {
	public boolean spam;
	public String body;
	public Enumeration<Header> headers; 
	public Map<String, Integer> wordsMap;
	
	public Mail(boolean spam, String unformatted) throws MessagingException, IOException{
		this.wordsMap = new HashMap<>();
		this.spam = spam;
		Properties props = new Properties();
	    props.put("mail.smtp.host", "my-mail-server");
		Session s = Session.getDefaultInstance(props);
		InputStream is = new ByteArrayInputStream(unformatted.getBytes());
		MimeMessage message = new MimeMessage(s, is);
		message.getAllHeaderLines();
		
		this.body = message.getContent().toString();
		String[] words = body.split("\\W+");
		for (String w : words) {
		       Integer n = wordsMap.get(w);
		       n = (n == null) ? 1 : ++n;
		       wordsMap.put(w, n);
		}
		 
		//For printing purposes
		int end = body.length() > 200 ? 200 : body.length();
		System.out.println("><><><><><><><><><><><><>Start of message<><><><><><><><><><><><><><><");
		System.out.println("Message size: " + message.getSize() + "Word count: " + words.length + "Different words: " + wordsMap.keySet().size());
		System.out.println(body.substring(0, end));
		System.out.println("-----//---------//---------");

	  
	   
		//Extract headers
		for (Enumeration<Header> e = message.getAllHeaders(); e.hasMoreElements();) {
		    Header h = e.nextElement();
		    System.out.println(h.getName());
		    
		    //" " +   h.getValue()
		}
		System.out.println("><><><><><><><><><><><><>End of message<><><><><><><><><><><><><><><");
	}
}
