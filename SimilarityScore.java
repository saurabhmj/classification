package dm.project3;

import java.util.SortedSet;
import java.util.TreeSet;

public class SimilarityScore implements Comparable<SimilarityScore>{

	String label;
	double similarity;
	public SimilarityScore(String label, double similarity) {
		super();
		this.label = label;
		this.similarity = similarity;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public double getSimilarity() {
		return similarity;
	}
	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
	@Override
	public int compareTo(SimilarityScore o) {
		if(o.getSimilarity() > this.similarity )
			return 1;
		
		return -1;
	}
	
	@Override
	public String toString() {
		
		return "label:"+label+"  score:" + similarity;
	}
/*	@Override
	public boolean equals(Object arg0) {
		
		SimilarityScore s = (SimilarityScore) arg0;
		if(s.label==label && s.similarity==similarity) {return true;}
		return super.equals(arg0);
	}*/
	
	/*public static void main(String[] args) {
		TreeSet<SimilarityScore> s = new TreeSet<>();
		
		s.add(new SimilarityScore(1, 12));
		s.add(new SimilarityScore(2, 124));
		s.add(new SimilarityScore(3, 126));
		s.add(new SimilarityScore(4, 123));
		s.add(new SimilarityScore(5, 121));
		System.out.println(s);
		for(SimilarityScore sc : s) {
			System.out.println(sc.similarity);
		}
		
		
	}*/
	
	
	

}
