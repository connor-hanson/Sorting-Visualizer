package application;

import java.util.LinkedList;
import java.util.Random;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

// data structure holding the rectangle values. Gets sorted by each of the sorts
public class RectArray {

	// no need for a separate file
	private class Rectangle {

		private int value;
		private Color color;
		protected boolean switched;

		private Rectangle(int value) {
			this.value = value;
			this.color = Color.RED;
			this.switched = false;
		}
		
	}

	private Rectangle[] array;
	private int size;
	private boolean sorted;
	private GraphicsContext gc;

	public RectArray(int size, GraphicsContext gc) {
		this.array = new Rectangle[size];
		this.size = size;
		this.gc = gc;

		init();
	}

	public void swap(int a, int b) {

		// switch logic
		Rectangle temp = array[a];
		array[a] = array[b];
		array[b] = temp;
		
		array[a].switched = true;
		array[b].switched = true;
		
		draw();

	}

	private void init() {
		LinkedList<Rectangle> hat = new LinkedList<>();
		Random rand = new Random();

		// add in order
		for (int i = 1; i < size + 1; ++i) {
			hat.add(new Rectangle(i));
		}

		// put in array in mixed order
		for (int i = 0; i < size; ++i) {
			int removeIndex = rand.nextInt(hat.size());
			Rectangle r = hat.remove(removeIndex);

			array[i] = r;
		}
	}

	private void draw() {

		Platform.runLater(() -> {
			final int windowWidth = Visualizer.WINDOW_WIDTH;
			final int windowHeight = Visualizer.WINDOW_HEIGHT;
			final int rectWidth = windowWidth / size;
			final int heightMult = windowHeight / size;

			gc.clearRect(0, 0, windowWidth, windowHeight);

			int rectHeight;
			int xPos = 0;
			int yPos = windowHeight;

			for (int i = 0; i < size(); ++i) {
				Rectangle r = array[i];
				rectHeight = r.value * heightMult;
				
				//System.out.println(r.switched);
				
				if (r.switched) {
					gc.setFill(Color.DARKGREEN);
					r.switched = false;
				} else {
					gc.setFill(Color.DARKRED);
				}
				
				gc.fillRect(xPos, yPos - rectHeight, rectWidth, rectHeight);
				xPos += rectWidth;
//				System.out.println(
//						xPos + " " + yPos + " " + rectWidth + " " + rectHeight);
			}
		});
	}
	
	public void drawSwitch() {
		
	}
	
	// return value of array
	public int get(int index) {
		return array[index].value;
	}

	public int size() {
		return size;
	}

	public Rectangle[] getArray() {
		return array;
	}

	public boolean isSorted() {
		return sorted;
	}

	public void setSorted(boolean flag) {
		this.sorted = flag;
	}

}
