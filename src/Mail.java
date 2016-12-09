
public class Mail {
	public boolean spam;
	public String body;
	public Mail(boolean spam, String unformatted){
		this.spam = spam;
		this.body = unformatted;
		//TODO: Try this http://stackoverflow.com/questions/3444660/java-email-message-parser
//		String content = ...
//				Session s = Session.getDefaultInstance(new Properties());
//		InputStream is = new ByteArrayInputStream(content.getBytes());
//		MimeMessage message = new MimeMessage(s, is);
	}
}
