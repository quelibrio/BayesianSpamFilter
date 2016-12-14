import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class ModelSerialization {
	private String fileName;
	public ModelSerialization(String serializationFile){
		this.fileName = serializationFile;
	}
	public void Serialize(Object object){
		 try {
			 File file = new File(this.fileName);
		        FileOutputStream f = new FileOutputStream(file);
		        ObjectOutputStream s = new ObjectOutputStream(f);
		        Object toWrite = ((NaiveBayes)object).spamWords;
		        s.writeObject(toWrite);
		        toWrite = ((NaiveBayes)object).hamWords;
		        s.writeObject(toWrite);
		        toWrite = ((NaiveBayes)object).allWords;
		        s.writeObject(toWrite);
		        toWrite = ((NaiveBayes)object).spamMailsCount;
		        s.writeObject(toWrite);
		        toWrite = ((NaiveBayes)object).hamMailsCount;
		        s.writeObject(toWrite);
		        toWrite = ((NaiveBayes)object).spamTotal;
		        s.writeObject(toWrite);
		        toWrite = ((NaiveBayes)object).hamTotal;
		        s.writeObject(toWrite);
		        toWrite = ((NaiveBayes)object).PriorSpam;
		        s.writeObject(toWrite);
		        toWrite = ((NaiveBayes)object).PriorHam;
		        s.writeObject(toWrite);
		        toWrite = ((NaiveBayes)object).useHeaders;
		        s.writeObject(toWrite);
		        s.close();
		       }catch(Exception ex){
		    	   
		       }
	}
	public NaiveBayes Deserialize() throws IOException, ClassNotFoundException{
		  File file = new File(this.fileName);
		    FileInputStream f = new FileInputStream(file);
		    ObjectInputStream s = new ObjectInputStream(f);
		    NaiveBayes bayes = new NaiveBayes();
		    bayes.spamWords = (HashMap<String, Integer>) s.readObject();
		    bayes.hamWords = (HashMap<String, Integer>) s.readObject();
		    bayes.allWords = (HashMap<String, Integer>) s.readObject();
		    bayes.spamMailsCount = (int) s.readObject();
		    bayes.hamMailsCount = (int) s.readObject();
		    bayes.spamTotal = (int) s.readObject();
		    bayes.hamTotal = (int) s.readObject();
		    bayes.PriorSpam = (double) s.readObject();
		    bayes.PriorHam = (double) s.readObject();
		    bayes.useHeaders = (boolean) s.readObject();
		  
		    s.close();
		    return bayes;
	}
}
