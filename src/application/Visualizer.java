package application;

import algos.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Visualizer extends Application {
	protected static final int WINDOW_WIDTH = 400;
	protected static final int WINDOW_HEIGHT = 400;
	private static final int NUM_RECTANGLES = 20;
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

		selectionBox.getChildren().addAll(bubbleSort, selectionSort, quickSort);

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
		}
		
		stage.setScene(sortScene);
		stage.show();
	}
	
	public void startThread(Runnable sort) {
		Thread sortThread = new Thread(sort);
		sortThread.setDaemon(true);
		sortThread.start();
	}

}
