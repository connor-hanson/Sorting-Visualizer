package algos;

import application.RectArray;
import javafx.application.Platform;

public class BubbleSort implements Runnable {

	private RectArray r;

	public BubbleSort(RectArray r) {
		this.r = r;
	}

	@Override
	public void run() {
		for (int i = 0; i < r.size() - 1; ++i) {
			for (int j = 0; j < r.size() - i - 1; ++j) {
				
				delay(10);

				if (r.get(j) > r.get(j + 1)) {
					r.swap(j, j + 1);
				}
			}
		}
		r.setSorted(true);
	}
	
	private void delay(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
