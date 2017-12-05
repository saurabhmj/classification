package dm.project3;

import java.util.LinkedList;
import java.util.Map;

public class Metadata {

	private Map<Integer, String> labels ;
	private Map<Integer, LinkedList<Double>> all_image_dimensions;
	private int number_of_images;
	
	public Metadata(Map<Integer, String> labels, Map<Integer, LinkedList<Double>> all_image_dimensions,
			int number_of_images) {
		super();
		this.labels = labels;
		this.all_image_dimensions = all_image_dimensions;
		this.number_of_images = number_of_images;
	}

	public Map<Integer, String> getLabels() {
		return labels;
	}

	public Map<Integer, LinkedList<Double>> getAll_image_dimensions() {
		return all_image_dimensions;
	}

	public int getNumber_of_images() {
		return number_of_images;
	}

	public Metadata() {
		// TODO Auto-generated constructor stub
	}
	
	public Metadata merge(Metadata m1, Metadata m2) {
		Metadata merged = new Metadata();
		
		return merged;
		
	}
	
	

}
