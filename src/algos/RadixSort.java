package algos;

import java.util.ArrayList;
import java.util.LinkedList;

import application.RectArray;
import application.RectArray.Rectangle;

// take the array, put it into buckets 0-9 n times, 
// where n is the amount of digits in the largest value
public class RadixSort extends RunnableSort {

	public RadixSort(RectArray r) {
		super(r);
	}

	@Override
	public void run() {

		int degree = findDegree();
		// start with LSD, go to MSD
		LinkedList<Rectangle>[] buckets = new LinkedList[10]; // have to have a
																// warning

		// do this for each power of 10
		for (int i = 1; i < degree; i *= 10) {

			//System.out.println("N");
			// initialize lists
			for (int k = 0; k < 10; ++k) {
				buckets[k] = new LinkedList<>();
			}

			// iterate thru list
			for (int j = 0; j < r.size(); ++j) {
				System.out.println(r.get(j));
				// put into appropriate 'bucket'
				switch ((r.get(j) / i) % 10) {
				case 0:
					buckets[0].add(r.getRectangle(j));
					break;
				case 1:
					buckets[1].add(r.getRectangle(j));
					break;
				case 2:
					buckets[2].add(r.getRectangle(j));
					break;
				case 3:
					buckets[3].add(r.getRectangle(j));
					break;
				case 4:
					buckets[4].add(r.getRectangle(j));
					break;
				case 5:
					buckets[5].add(r.getRectangle(j));
					break;
				case 6:
					buckets[6].add(r.getRectangle(j));
					break;
				case 7:
					buckets[7].add(r.getRectangle(j));
					break;
				case 8:
					buckets[8].add(r.getRectangle(j));
					break;
				case 9:
					buckets[9].add(r.getRectangle(j));
					break;
				}
			}
			
			// now put back into the array
			int count = 0;
			// for each bucket
			for (int j = 0; j < 10; ++j) {
				// go thru and insert items into array
				for (int k = 0; k < buckets[j].size(); ++k) {
					r.insert(buckets[j].get(k), count);
					count += 1;
					delay(25);
				}
			}
		}
		// just to make it sound better
		runSorted(10);
	}

	// find the smallest power of 10 for which the size of the array fits into
	// only once. EX: size = 999 -> return 1000. size = 99 -> return 100.
	// size = 1001 -> return 10000. Smallest value returned can be 10
	private int findDegree() {
		int degree = 10;

		while (r.size() / degree > 0) {
			degree *= 10;
		}

		return degree;
	}

}
