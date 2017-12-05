package dm.project3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class DataSetReader {

	
	FileReader fr = null;
	String fileName = "";
	Map<Integer, String> labels ;
	Map<Integer, LinkedList<Double>> all_image_dimensions;
	int number_of_images;

	
	public DataSetReader(String filename) throws FileNotFoundException {
		
		this.fileName = filename;
		this.fr = new FileReader(new File(this.fileName));
		labels = new HashMap<>();
		all_image_dimensions = new HashMap<>();
		number_of_images = 0;

	}
	
	
	public void generateDataSet() throws Exception {
		fr = new FileReader(new File(fileName));
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		
		while(( line =br.readLine()) != null){
			double magnitude = 0;
			String[] splitLine = line.split(",");
			
			labels.put(number_of_images	, splitLine[0]);
			LinkedList<Double> dimensions = new LinkedList<>();
			LinkedList<Double> temp = new LinkedList<>();
			
			for(int i = 1; i<splitLine.length;i++) {
				double d = Double.parseDouble(splitLine[i]);
				temp.add(d);
				magnitude += d*d;
			}
			magnitude = Math.sqrt(magnitude);
			while(!temp.isEmpty()) {
				double d = temp.poll();
				dimensions.add(d/magnitude);
			}
			all_image_dimensions.put(number_of_images, dimensions);
			number_of_images++;
		}

	}
	
	public Map<Integer, LinkedList<Double>> getAll_image_dimensions() {
		return all_image_dimensions;
	}
	public Map<Integer, String> getLabels() {
		return labels;
	}
	public int getNumber_of_images() {
		return number_of_images;
	}

}
