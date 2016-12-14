
public class TestResult {
	public int truePositive;
	public int falsePositive;
	public int trueNegative;
	public int falseNegative;
	public int dataSize;
	public double precision;
	public double recall;
	
	public TestResult(){
		this.dataSize = 0;
		this.trueNegative = 0;
		this.truePositive = 0;
		this.falseNegative = 0;
		this.falsePositive = 0;
	}
	
	public void getPrecissionAndRecall(){
		//this.precission = 0.0000001;
			//if(truePositive > 0)
				precision = truePositive / (truePositive + falsePositive) ;
		
		recall = truePositive / (truePositive + falseNegative);
	}
	public double GetAccuracy(){
		return (double)(this.truePositive + this.trueNegative)/this.dataSize;
	}
	public double f1Score(){
		//this.getPrecissionAndRecall();
		//return 2 * (precision*recall) / (precision + recall);
		return 2 * (double)truePositive / (2*truePositive+falseNegative+falsePositive);
	}
	
	public double f1ScoreHam(){
		return 2 * (double)trueNegative / (2*trueNegative+falseNegative+falsePositive);
	}
}

