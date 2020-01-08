package application;

import java.util.LinkedList;
import java.util.Random;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

// data structure holding the rectangle values. Gets sorted by each of the sorts
public class RectArray {

	// no need for a separate file
	public class Rectangle {

		private int value, div, remainder;
		protected boolean switched;

		private Rectangle(int value) {
			this.value = value;
			this.switched = false;
			this.div = size / 100; // exclude first and last 14 notes, they
									// sound bad
			this.remainder = size % 100;
		}

		// div is the amount of times 128 cleanly fits into the array
		private void playSound() {
			//System.out.println(div);

			mc[0].noteOn((value / (div + 1)) + remainder, 65);
			// +1 so that it doesn't reach/repeat highest pitch if it doesn't
			// divide cleanly (and prevent /0). 65 == fortissimo
		}

	}

	private Rectangle[] array;
	private int size;
	private boolean sorted;
	private GraphicsContext gc;
	private MidiChannel[] mc;

	public RectArray(int size, GraphicsContext gc) {
		this.array = new Rectangle[size];
		this.size = size;
		this.gc = gc;

		setupMidi();
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
	
	public void insert(Rectangle r, int a) {
		array[a] = r;
		r.switched = true;
		draw();
	}

	public void highlight(int a) {
		array[a].switched = true;
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
			final double rectWidth = windowWidth / size;
			final double heightMult = windowHeight / size;
			final int div = size / 128;

			gc.clearRect(0, 0, windowWidth, windowHeight);

			int rectHeight;
			int xPos = 0;
			int yPos = windowHeight;

			for (int i = 0; i < size(); ++i) {
				Rectangle r = array[i];
				rectHeight = (int) (r.value * heightMult);

				// System.out.println(r.switched);

				if (r.switched) {
					gc.setFill(Color.DARKGREEN);
					r.playSound();
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

	private void setupMidi() {
		try {
			Synthesizer synth = MidiSystem.getSynthesizer();
			synth.open();
			mc = synth.getChannels();
			Instrument[] inst = synth.getDefaultSoundbank().getInstruments();
			//System.err.println(inst.length);
			synth.loadInstrument(inst[25]); // guitar sound (25 -32)
			mc[0].programChange(25);
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

	// return value of array
	public int get(int index) {
		return array[index].value;
	}
	
	public Rectangle getRectangle(int index) {
		return array[index];
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
