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
 * This class implements the version of the quicksort algorithm presented in the lecture.   
 *
 */

public class QuickSorter extends AbstractSorter
{
	
	// Other private instance variables if you need ... 
		
	/** 
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *   
	 * @param pts   input array of integers
	 */
	public QuickSorter(Point[] pts)
	{
		super(pts);
	}
		

	/**
	 * Carry out quicksort on the array points[] of the AbstractSorter class.  
	 * 
	 */
	@Override 
	public void sort()
	{
		quickSortRec(0, points.length - 1);
	}
	
	
	/**
	 * Operates on the subarray of points[] with indices between first and last. 
	 * 
	 * @param first  starting index of the subarray
	 * @param last   ending index of the subarray
	 */
	private void quickSortRec(int first, int last)
	{
		if (first >= last) {
			return;
		}
		int p = partition(first, last);
		quickSortRec(first, p);
		quickSortRec(p + 1, last);
	}
	
	
	/**
	 * Operates on the subarray of points[] with indices between first and last.
	 * 
	 * @param first
	 * @param last
	 * @return
	 */
	private int partition(int first, int last)
	{
		boolean done = false;
		Point pivot = new Point(points[first + ((last - first) / 2)].getX(), points[first + ((last - first) / 2)].getY());
		while (!done) {
			while (pointComparator.compare(points[first], pivot) < 0) {
				++first;
			}
			
			while (pointComparator.compare(pivot, points[last]) < 0) {
				--last;
			}
			
			if (first >= last) {
				done = true;
			}
			else {
				swap(first, last);
				++first;
				--last;
			}
		}
		return last;
	}	
		


	
	// Other private methods if needed ...
}
