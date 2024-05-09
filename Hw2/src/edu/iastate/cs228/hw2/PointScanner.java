package edu.iastate.cs228.hw2;

import java.io.File;

/**
 * 
 * @author Gautham Pullela
 *
 */

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * * @author Gabriel Kiveu
 */

/**
 * 
 * This class sorts all the points in an array of 2D points to determine a
 * reference point whose x and y coordinates are respectively the medians of the
 * x and y coordinates of the original points.
 * 
 * It records the employed sorting algorithm as well as the sorting time for
 * comparison.
 *
 */
public class PointScanner {
	private Point[] points;

	private Point medianCoordinatePoint; // point whose x and y coordinates are respectively the medians of
											// the x coordinates and y coordinates of those points in the array
											// points[].
	private Algorithm sortingAlgorithm;
		String fileName;
	protected long scanTime; // execution time in nanoseconds.

	/**
	 * This constructor accepts an array of points and one of the four sorting
	 * algorithms as input. Copy the points into the array points[].
	 * 
	 * @param pts input array of points
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	public PointScanner(Point[] pts, Algorithm algo) throws IllegalArgumentException {
		if (pts == null || pts.length == 0) {
			throw new IllegalArgumentException();
		} else {
			sortingAlgorithm = algo;
			points = new Point[pts.length];
			for (int i = 0; i < pts.length; ++i) {
				points[i] = new Point(pts[i].getX(), pts[i].getY());
			}
		}
	}

	/**
	 * This constructor reads points from a file.
	 * 
	 * @param inputFileName
	 * @throws FileNotFoundException
	 * @throws InputMismatchException if the input file contains an odd number of
	 *                                integers
	 */
	protected PointScanner(String inputFileName, Algorithm algo) throws FileNotFoundException, InputMismatchException {
		Scanner fileScnr;
		int numInputs = 0;
		ArrayList<Integer> inputList = new ArrayList<Integer>();
		File file = new File(inputFileName);
		fileScnr = new Scanner(file);
		while (fileScnr.hasNextInt()) {
			inputList.add(fileScnr.nextInt());
			++numInputs;
		}
		fileScnr.close();
		if (numInputs % 2 != 0)
			throw new InputMismatchException();

		points = new Point[inputList.size() / 2];
		for (int i = 0; i < inputList.size(); i += 2) {
			points[i / 2] = new Point(inputList.get(i), inputList.get(i + 1));
		}

		sortingAlgorithm = algo;
	}

	/**
	 * Carry out two rounds of sorting using the algorithm designated by
	 * sortingAlgorithm as follows:
	 * 
	 * a) Sort points[] by the x-coordinate to get the median x-coordinate. b) Sort
	 * points[] again by the y-coordinate to get the median y-coordinate. c)
	 * Construct medianCoordinatePoint using the obtained median x- and
	 * y-coordinates.
	 * 
	 * Based on the value of sortingAlgorithm, create an object of SelectionSorter,
	 * InsertionSorter, MergeSorter, or QuickSorter to carry out sorting.
	 * 
	 * @param algo
	 * @return
	 */
	public void scan() {
		Point xMedianCoordPoint;
		Point yMedianCoordPoint;
		AbstractSorter aSorter;
		long scanStart;
		long scanEnd;

		switch (sortingAlgorithm) {
		case SelectionSort:
			aSorter = new SelectionSorter(points);
			break;
		case InsertionSort:
			aSorter = new InsertionSorter(points);
			break;
		case MergeSort:
			aSorter = new MergeSorter(points);
			break;
		case QuickSort:
			aSorter = new QuickSorter(points);
			break;
		default:
			aSorter = new SelectionSorter(points);
		}
		scanTime = 0;

		aSorter.setComparator(0);
		scanStart = System.nanoTime();
		aSorter.sort();
		scanEnd = System.nanoTime();
		scanTime = scanEnd - scanStart;
		aSorter.getPoints(points);
		xMedianCoordPoint = new Point(aSorter.getMedian().getX(), aSorter.getMedian().getY());

		aSorter.setComparator(1);
		scanStart = System.nanoTime();
		aSorter.sort();
		scanEnd = System.nanoTime();
		scanTime += scanEnd - scanStart;
		aSorter.getPoints(points);
		yMedianCoordPoint = new Point(aSorter.getMedian().getX(), aSorter.getMedian().getY());
		medianCoordinatePoint = new Point(xMedianCoordPoint.getX(), yMedianCoordPoint.getY());

		// create an object to be referenced by aSorter according to sortingAlgorithm.
		// for each of the two
		// rounds of sorting, have aSorter do the following:
		//
		// a) call setComparator() with an argument 0 or 1.
		//
		// b) call sort().
		//
		// c) use a new Point object to store the coordinates of the
		// medianCoordinatePoint
		//
		// d) set the medianCoordinatePoint reference to the object with the correct
		// coordinates.
		//
		// e) sum up the times spent on the two sorting rounds and set the instance
		// variable scanTime.

	}

	/**
	 * Outputs performance statistics in the format:
	 * 
	 * <sorting algorithm> <size> <time>
	 * 
	 * For instance,
	 * 
	 * selection sort 1000 9200867
	 * 
	 * Use the spacing in the sample run in Section 2 of the project description.
	 */
	public String stats() 
	{
		String stats = null;


		if(sortingAlgorithm == Algorithm.MergeSort)
		{
			stats = "<merge sort>" + "         " + points.length + "  " + scanTime;
		}
		if(sortingAlgorithm == Algorithm.InsertionSort)
		{
			stats = "<insertion sort>" + "     " + points.length + "  " + scanTime;
		}
		if(sortingAlgorithm == Algorithm.SelectionSort)
		{
			stats = "<selection sort>" + "     " + points.length + "  " + scanTime;
		}
		if(sortingAlgorithm == Algorithm.QuickSort)
		{
			stats = "<quick sort>" + "         " + points.length + "  " + scanTime;
		}




		return stats;
	}

	/**
	 * Write MCP after a call to scan(), in the format "MCP: (x, y)" The x and y
	 * coordinates of the point are displayed on the same line with exactly one
	 * blank space in between.
	 */
	@Override
	public String toString() {
		return "MCP: (" + medianCoordinatePoint.getX() + ", " + medianCoordinatePoint.getY() + ")";
	}

	/**
	 * 
	 * This method, called after scanning, writes point data into a file by
	 * outputFileName. The format of data in the file is the same as printed out
	 * from toString(). The file can help you verify the full correctness of a
	 * sorting result and debug the underlying algorithm.
	 * 
	 * @throws FileNotFoundException
	 */
	public void writeMCPToFile() throws FileNotFoundException 
	{
		// TODO
		try {
			FileWriter file = new FileWriter(fileName);

			file.write(toString());

			file.close();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}


	}

}
