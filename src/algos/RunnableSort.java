package algos;

import application.RectArray;

public abstract class RunnableSort implements Runnable {

	protected RectArray r;
	
	public RunnableSort(RectArray r) {
		this.r = r;
	}

	@Override
	public void run() {
		
	}
	
	protected void delay(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			System.err.println(e.getLocalizedMessage());
		}
	}
	
	protected void runSorted(int millis) {
		for (int i = 0; i < r.size(); ++i) {
			r.highlight(i);
			delay(millis);
		}
	}
	
}
