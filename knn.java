package dm.project3;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

public class knn {

	private static String validationDataFilePath = "F:\\Study\\MS\\CSCI_5523_Introduction_To_Data_Mining\\Project3\\data\\project_3\\rep1\\mnist_validation.csv";
	private static String trainDataFilePath = "F:\\Study\\MS\\CSCI_5523_Introduction_To_Data_Mining\\Project3\\data\\project_3\\rep1\\mnist_train.csv";
	private static String testDataFilePath = "F:\\Study\\MS\\CSCI_5523_Introduction_To_Data_Mining\\Project3\\data\\project_3\\rep1\\mnist_test.csv";
	

	public static void main(String[] args) throws Exception {

		DataSetReader validate_dataSetReader = new DataSetReader(validationDataFilePath);
		validate_dataSetReader.generateDataSet();
		Metadata validationData = new Metadata(validate_dataSetReader.getLabels(), validate_dataSetReader.getAll_image_dimensions(),
				validate_dataSetReader.getNumber_of_images());

		DataSetReader train_dataSetReader = new DataSetReader(trainDataFilePath);
		train_dataSetReader.generateDataSet();
		Metadata trainData = new Metadata(train_dataSetReader.getLabels(), train_dataSetReader.getAll_image_dimensions(),
				train_dataSetReader.getNumber_of_images());

		DataSetReader test_dataSetReader = new DataSetReader(testDataFilePath);
		train_dataSetReader.generateDataSet();
		Metadata testData = new Metadata(train_dataSetReader.getLabels(), train_dataSetReader.getAll_image_dimensions(),
				train_dataSetReader.getNumber_of_images());

		// Date d1 = new Date();
		int bestK = findBestK(validationData, trainData);

		System.out.println("Best value for K is : " + bestK);

		// Date d2 = new Date();
		// System.out.println("done in " + ((d2.getTime() - d1.getTime())/1000.0) );
	}

	private static int findBestK(Metadata validationData, Metadata trainData) {
		List<Integer> accumulativeListofAccuracies = new ArrayList<>();
		Map<Integer, LinkedList<Double>> validationMap = validationData.getAll_image_dimensions();
		Map<Integer, LinkedList<Double>> trainMap = trainData.getAll_image_dimensions();
		Map<Integer, String> trainLabels = trainData.getLabels();
		Map<Integer, String> validationLabels = validationData.getLabels();
		List<TreeSet<SimilarityScore>> globaltop20 = new ArrayList<>();
		int count = 0;
		List<Map<Integer, Integer>> accuracyList = new ArrayList<>();
		for (Entry<Integer, LinkedList<Double>> validationEntry : validationMap.entrySet()) {
			if (count % 300 == 0)
				System.out.println(count);
			count++;

			TreeSet<SimilarityScore> top20 = new TreeSet<>();
			for (int i = 0; i < 20; i++)
				top20.add(new SimilarityScore("-1", Double.NEGATIVE_INFINITY));

			for (Entry<Integer, LinkedList<Double>> trainEntry : trainMap.entrySet()) {
				SimilarityScore score = new SimilarityScore(trainLabels.get(trainEntry.getKey()),
						findSimilarity(trainEntry.getValue(), validationEntry.getValue()));
				if (top20.last().compareTo(score) == 1) {
					top20.add(score);
					top20.pollLast();
				}

			}
			globaltop20.add(top20);

			int actualLabel = Integer.parseInt(validationLabels.get(validationEntry.getKey()));
			Map<Integer, String> classLabels = findClassLabels(new TreeSet<>(top20));
			Map<Integer, Integer> accuracyMap = new HashMap<>();

			for (int i = 1; i <= 20; i++) {
				if (Integer.parseInt(classLabels.get(i)) == actualLabel)
					accuracyMap.put(i, 1);
				else
					accuracyMap.put(i, 0);
				if (Integer.parseInt(classLabels.get(i)) == -1)
					System.out.println("Negative spotted at " + count);

			}

			accuracyList.add(accuracyMap);

		}

		for (int i = 1; i <= 20; i++)
			accumulativeListofAccuracies.add(0);

		for (int i = 1; i <= 20; i++) {
			int countOfLabels = 0;
			for (Map<Integer, Integer> accuracyMap : accuracyList) {
				countOfLabels = accuracyMap.get(i) == 1 ? countOfLabels + 1 : countOfLabels;
			}
			accumulativeListofAccuracies.set(i - 1, countOfLabels);
		}

		List<Integer> sortedList = new ArrayList<>(accumulativeListofAccuracies);
		Collections.sort(sortedList);
		int maxValue = sortedList.get(sortedList.size() - 1);
		System.out.println(accumulativeListofAccuracies);
		System.out.println(accumulativeListofAccuracies.indexOf(maxValue));
		return accumulativeListofAccuracies.indexOf(maxValue) + 1;

	}

	private static Map<Integer, String> findClassLabels(TreeSet treeSet) {

		Map<Integer, String> mapOfCounts = new HashMap<>();

		for (int i = 1; i <= 20; i++) {
			TreeSet<SimilarityScore> localTreeSet = new TreeSet<>(treeSet);
			int n = i;
			List<SimilarityScore> firstNScores = new ArrayList<>();
			while (n != 0) {
				n--;
				firstNScores.add(localTreeSet.pollFirst());
			}

			String majorityLabel = findMajorityLabel(firstNScores);
			mapOfCounts.put(i, majorityLabel);
		}
		return mapOfCounts;
	}

	/*
	 * private static String findMajorityLabel(List<SimilarityScore> firstNScores) {
	 * List<Integer> counts = new ArrayList<>(firstNScores.size()); for(int
	 * i=0;i<20;i++) counts.add(0);
	 * 
	 * for(SimilarityScore s : firstNScores) { int label =
	 * Integer.parseInt(s.label); int currentScore = counts.get(label);
	 * currentScore++; //currentScore+=currentScore*s.getSimilarity();
	 * counts.set(label, currentScore); } List<Integer> sortedList = new
	 * ArrayList<>(counts); Collections.sort(sortedList); int maxValue =
	 * sortedList.get(sortedList.size()-1);
	 * 
	 * return String.valueOf(counts.indexOf(maxValue)); }
	 */

	private static String findMajorityLabel(List<SimilarityScore> firstNScores) {
		List<Double> counts = new ArrayList<>(firstNScores.size());
		for (int i = 0; i < 20; i++)
			counts.add(0.0);

		for (SimilarityScore s : firstNScores) {
			int label = Integer.parseInt(s.label);
			double currentScore = counts.get(label);
			currentScore += s.getSimilarity();
			// currentScore+=currentScore*s.getSimilarity();
			counts.set(label, currentScore);
		}
		/*
		 * List<Double> sortedList = new ArrayList<>(counts);
		 * Collections.sort(sortedList); double maxValue =
		 * sortedList.get(sortedList.size()-1);
		 */
		List<Integer> maxValueIndices = new ArrayList<>();
		double maxValue = Double.MIN_VALUE;
		for (double d : counts)
			maxValue = d > maxValue ? d : maxValue;
		for (int i = 0; i < counts.size(); i++) {
			if (counts.get(i) == maxValue)
				maxValueIndices.add(i);
		}

		if (maxValueIndices.size() > 1) {
			Random random = new Random();
			int randomIndex = random.nextInt(maxValueIndices.size() - 1);
			return String.valueOf(counts.indexOf(randomIndex));
		} else
			return String.valueOf(counts.indexOf(maxValue));
	}

	private static double findSimilarity(LinkedList<Double> trainEntry, LinkedList<Double> validationEntry) {
		double product = 0;

		LinkedList<Double> trainValues = new LinkedList<>(trainEntry);
		LinkedList<Double> validateValues = new LinkedList<>(validationEntry);

		while (!trainValues.isEmpty()) {
			product += trainValues.poll() * validateValues.poll();
		}
		return 1.0 / (1.0 - product);
	}

}
