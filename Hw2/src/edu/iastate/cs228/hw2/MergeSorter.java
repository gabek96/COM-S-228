package edu.iastate.cs228.hw2;

import java.io.FileNotFoundException;
import java.lang.NumberFormatException; 
import java.lang.IllegalArgumentException; 
import java.util.InputMismatchException;

/**
 *  
 * @author Gabriel Kiveu
 *
 */

/**
 * 
 * This class implements the mergesort algorithm.   
 *
 */

public class MergeSorter extends AbstractSorter
{
	// Other private instance variables if needed
	
	/** 
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *  
	 * @param pts   input array of integers
	 */
	public MergeSorter(Point[] pts) 
	{
		super(pts);
	}


	/**
	 * Perform mergesort on the array points[] of the parent class AbstractSorter. 
	 * 
	 */
	@Override 
	public void sort()
	{
		mergeSortRec(points);
	}

	
	/**
	 * This is a recursive method that carries out mergesort on an array pts[] of points. One 
	 * way is to make copies of the two halves of pts[], recursively call mergeSort on them, 
	 * and merge the two sorted subarrays into pts[].   
	 * 
	 * @param pts	point array 
	 */
	private void mergeSortRec(Point[] pts)
	{
		if ((pts.length - 1) > 0) {
			int leftEnd = (pts.length - 1) / 2;
			Point[] leftPts = new Point[leftEnd + 1];
			Point[] rightPts = new Point[pts.length - 1 - leftEnd];
			
			for (int h = 0; h < leftPts.length; ++h) {
				leftPts[h] = new Point(pts[h].getX(),pts[h].getY());
			}
			for (int h = 0; h < rightPts.length; ++h) {
				rightPts[h] = new Point(pts[leftEnd + 1 + h].getX(), pts[leftEnd + 1 + h].getY());
			}
			mergeSortRec(leftPts);
			mergeSortRec(rightPts);
			merge(pts, leftPts, rightPts);
			if (pts.length == points.length) {
				for (int i = 0; i < pts.length; ++i) {
					points[i] = new Point(pts[i].getX(), pts[i].getY());
				}
			}
		}
		
	}
	
	private void merge(Point[] pts, Point[] leftPts, Point[] rightPts) {
		Point[] mergePoints = new Point[pts.length];
		int mergePos = 0;
		int leftPos = 0;
		int rightPos = 0;
		while (leftPos <= (leftPts.length - 1) && rightPos <= (rightPts.length - 1)) {
			if (pointComparator.compare(leftPts[leftPos], rightPts[rightPos]) < 0) {
				mergePoints[mergePos] = new Point(leftPts[leftPos].getX(), leftPts[leftPos].getY());
				++leftPos;
			}
			else {
				mergePoints[mergePos] = new Point(rightPts[rightPos].getX(), rightPts[rightPos].getY());
				++rightPos;
			}
			++mergePos;
		}
		
		while (leftPos <= (leftPts.length - 1)) {
			mergePoints[mergePos] = new Point(leftPts[leftPos].getX(), leftPts[leftPos].getY());
			++leftPos;
			++mergePos;
		}
		
		while (rightPos <= (rightPts.length - 1)) {
			mergePoints[mergePos] = new Point(rightPts[rightPos].getX(), rightPts[rightPos].getY());
			++rightPos;
			++mergePos;
		}
		
		for (mergePos = 0; mergePos < mergePoints.length; ++mergePos) {
			pts[mergePos] = new Point(mergePoints[mergePos].getX(), mergePoints[mergePos].getY());
		}
	}	

	
	// Other private methods if needed ...

}
