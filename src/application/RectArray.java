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
	// basically an integer that makes sound
	public class Rectangle {

		private int value, div;
		private boolean switched;

		private Rectangle(int value) {
			this.value = value; // value to be sorted
			this.switched = false; // color green if true
			this.div = size / 100 + 14; // exclude first and last 14 notes, they
			// sound bad
		}

		// 128 = sound range
		// div is the amount of times 128 cleanly fits into the array
		private void playSound() {
			mc[0].noteOn((value / (div + 1)) + 14, 65);
			// +1 so that it doesn't reach/repeat highest pitch if it doesn't
			// divide cleanly (and prevent /0). 65 == fortissimo
		}

	}

	private Rectangle[] array;
	private int size;
	private boolean sorted;
	private GraphicsContext gc;
	private MidiChannel[] mc; // has 16 channels, only need to use 1 to play a
								// note

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

		// make sure that rectangle gets colored green
		array[a].switched = true;
		array[b].switched = true;

		draw();

	}

	// essentially swap with half the steps
	// insert rectangle r into array[a]
	public void insert(Rectangle r, int a) {
		array[a] = r;
		r.switched = true;
		draw();
	}

	// FIXME no need for this anymore
	public void highlight(int a) {
		array[a].switched = true;
		draw();
	}

	// create array then mix it
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

	// draw he entire array, highlighting switched rectangles as green and the
	// rest as red
	private void draw() {

		// needed because JavaFX threading is dumb
		Platform.runLater(() -> {
			final double windowWidth = Visualizer.WINDOW_WIDTH;
			final double windowHeight = Visualizer.WINDOW_HEIGHT;

			// width of each rectangle
			final double rectWidth = windowWidth / size;
			// multiplier by which each rectangle will have its value multiplied
			// by
			final double heightMult = ((double) windowHeight) / ((double) size);

			gc.clearRect(0, 0, windowWidth, windowHeight);

			int rectHeight;
			int xPos = 0;
			int yPos = (int) windowHeight;

			for (int i = 0; i < size(); ++i) {
				Rectangle r = array[i]; // temp value
				// cast to int because causes issues with calculating yPos
				rectHeight = (int) (r.value * heightMult);

				if (r.switched) {
					gc.setFill(Color.DARKGREEN);
					r.playSound();
					r.switched = false;
				} else {
					gc.setFill(Color.DARKRED);
				}

				// ypos - rectheight because JavaFx calculate rectangles upside
				// down. So ypos - height makes them look like they're coming
				// from the bottom of the frame
				gc.fillRect(xPos, yPos - rectHeight, rectWidth, rectHeight);
				// offset new xpos by size of each rectangle
				xPos += rectWidth;
			}
		});
	}

	private void setupMidi() {
		try {
			Synthesizer synth = MidiSystem.getSynthesizer(); // system sound
			synth.open();
			mc = synth.getChannels();
			// gives all instruments that come with standard midi stiff
			Instrument[] inst = synth.getDefaultSoundbank().getInstruments();
			synth.loadInstrument(inst[25]); // guitar sound (25 -32)
			mc[0].programChange(25); // program change is essential or no sound
										// plays
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
