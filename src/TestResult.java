
public class TestResult {
	public int truePositive;
	public int falsePositive;
	public int trueNegative;
	public int falseNegative;
	public int dataSize;
	public double precission;
	public double recall;
	
	public TestResult(){
		this.dataSize = 0;
		this.trueNegative = 0;
		this.truePositive = 0;
		this.falseNegative = 0;
		this.falsePositive = 0;
	}
	
	public void getPrecissionAndRecall(){
		this.precission = 0.0000001;
			if(truePositive > 0)
				precission = truePositive / (truePositive + falsePositive) ;
		
		this.recall = truePositive / (truePositive + falseNegative + 0.0000001);
	}
	public double GetAccuracy(){
		return (double)(this.truePositive + this.trueNegative)/this.dataSize;
	}
	public double f1Score(){
		this.getPrecissionAndRecall();
		return 2 * (precission*recall) / (precission + recall);
	}
}

