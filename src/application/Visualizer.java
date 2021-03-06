package application;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import algos.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*
 * File name: Visualizer.java
 * Project: Sorting Visualizer
 * Author: Connor Hanson
 * 
 * This class displays most of the GUI and handles the threading
 */
public class Visualizer extends Application {
	protected static int WINDOW_WIDTH = 500;
	protected static int WINDOW_HEIGHT = 500;
	private static int NUM_RECTANGLES = 250;
	private Stage stage;
	private GraphicsContext gc;

	public static void main(String[] args) {
		launch(args);
	}

	// start the user on a screen prompting them to choose which kind of sorting
	// they would liek to see
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane bp = new BorderPane();
		Scene selectionScene = new Scene(bp, WINDOW_WIDTH, WINDOW_HEIGHT);
		stage = primaryStage;

		VBox selectionBox = new VBox(10);

		Label prompt = new Label("Choose sort:");
		selectionBox.getChildren().add(prompt);

		Button bubbleSort = new Button("Bubble Sort");
		bubbleSort.setOnAction(e -> {
			String bubble = "bubble";
			switchScene(bubble);

		});

		Button selectionSort = new Button("Selection sort");
		selectionSort.setOnAction(e -> {
			String selection = "selection";
			switchScene(selection);
		});

		Button quickSort = new Button("Quick Sort");
		quickSort.setOnAction(e -> {
			String quick = "quick";
			switchScene(quick);
		});

		Button radixSort = new Button("Radix Sort");
		radixSort.setOnAction(e -> {
			String radix = "radix";
			switchScene(radix);
		});

		Button testMidi = new Button("Test MIDI");
		testMidi.setOnAction(e -> {
			testMidi();
		});

		VBox optionsBox = new VBox(10);

		// let user choose number of rectangles to sort, defaults to 100
		Label nRectangles = new Label("Input number of rectangles to sort");
		TextField nInput = new TextField(); // change to slider?

		Button confirm = new Button("Press to confirm");
		confirm.setOnAction(e -> {
			try {
				int num = Integer.parseInt(nInput.getText());
				if (num < 10 || num > WINDOW_WIDTH) {
					Alert al = new Alert(AlertType.WARNING);
					al.setContentText(
							"Cannot sort less than 10, or more than the window width ("
									+ WINDOW_WIDTH
									+ "), or more than the window height("
									+ WINDOW_HEIGHT + ")");
					al.showAndWait();
				} else {
					NUM_RECTANGLES = num;
				}
			} catch (NumberFormatException ex) {

			}
		});

		optionsBox.getChildren().addAll(nRectangles, nInput, confirm);
		bp.setRight(optionsBox);

		selectionBox.getChildren().addAll(bubbleSort, selectionSort, quickSort,
				radixSort, testMidi);

		bp.setCenter(selectionBox);

		stage.setScene(selectionScene);
		stage.show();
	}

	public void switchScene(String sortType) {
		BorderPane bp = new BorderPane();
		Scene sortScene = new Scene(bp, WINDOW_WIDTH, WINDOW_HEIGHT);
		Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.gc = canvas.getGraphicsContext2D();

		Button back = new Button("Back");
		back.setOnAction(e -> {
			try {
				start(stage);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		bp.getChildren().add(canvas);
		bp.setTop(back);

		RectArray rectangles = new RectArray(NUM_RECTANGLES, gc);

		switch (sortType) {
		case "bubble":
			startThread(new BubbleSort(rectangles));
			break;

		case "quick":
			startThread(new QuickSort(rectangles));
			break;

		case "selection":
			startThread(new SelectionSort(rectangles));
			break;

		case "radix":
			startThread(new RadixSort(rectangles));
			break;
		}

		stage.setScene(sortScene);
		stage.show();
	}

	public void startThread(RunnableSort sort) {
		Thread sortThread = new Thread(sort);
		sortThread.setDaemon(true);
		sortThread.start();
	}

	private void setupMidi() {
		try {
			Synthesizer synth = MidiSystem.getSynthesizer();
			synth.open();
			final MidiChannel[] mc = synth.getChannels();
			Instrument[] inst = synth.getDefaultSoundbank().getInstruments();
			synth.loadInstrument(inst[0]);
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void testMidi() {
		try {
			Synthesizer synth = MidiSystem.getSynthesizer();
			synth.open();
			final MidiChannel[] mc = synth.getChannels();
			Instrument[] inst = synth.getDefaultSoundbank().getInstruments();
			synth.loadInstrument(inst[0]);
			mc[0].noteOn(60, 600);
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
