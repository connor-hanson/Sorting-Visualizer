package algos;

import application.RectArray;

public class SelectionSort implements Runnable {

	private RectArray r;
	
	public SelectionSort(RectArray r) {
		this.r = r;
	}

	@Override
	public void run() {
		int minIndex;

		for (int i = 0; i < r.size() - 1; ++i) {
			minIndex = i;
			for (int j = i + 1; j < r.size(); ++j) {
				if (r.get(j) < r.get(minIndex)) {
					r.swap(j, minIndex);
					delay(100);
				}
			}
		}
		
		r.setSorted(true);
	}
	
	private void delay(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			
		}
	}

}
