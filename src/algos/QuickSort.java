package algos;

import application.RectArray;

public class QuickSort extends RunnableSort {
		
	public QuickSort(RectArray r) {
		super(r);
	}

	@Override
	public void run() {
		quickSort(0, r.size() - 1, r);
		r.setSorted(true);
	}
	
	private void quickSort(int lo, int hi, RectArray r) {
		if (lo < hi) {
			int partition = partition(lo, hi, r);
			
			quickSort(lo, partition - 1, r);
			quickSort(partition + 1, hi, r);
		}
	}
	
	private int partition(int lo, int hi, RectArray r) {
		int pivot = r.get(hi);
		
		int i = lo - 1;
		
		for (int j = lo; j <= hi - 1; j++) {
			if (r.get(j) <= pivot) {
				i++;
				r.swap(i, j);
				delay(25);
			}
		}
		delay(25);
		r.swap(i + 1, hi);
		return i + 1;
	}

	
	
}
