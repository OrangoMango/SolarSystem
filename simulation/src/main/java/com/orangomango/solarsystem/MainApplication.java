package com.orangomango.solarsystem;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.animation.*;
import javafx.util.Duration;

import java.util.ArrayList;

public class MainApplication extends Application{
	private ArrayList<Planet> planets = new ArrayList<>();
	private long timePassed;

	@Override
	public void start(Stage stage){
		StackPane pane = new StackPane();
		Canvas canvas = new Canvas(Util.WIDTH, Util.HEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		pane.getChildren().add(canvas);

		// Create planets
		Planet sun = new Planet(Color.YELLOW, 6.96e8, 1.989e30, 0, 0);
		Planet earth = new Planet(Color.CYAN, 6.3e6, 5.972e24, 1.50e11, 2.98e4);
		Planet moon = new Planet(Color.GRAY, 1.7e6, 7.35e22, 1.50e11+3.84e8, 2.98e4+1.022e3);
		Planet mercury = new Planet(Color.ORANGE, 2.4e6, 3.30e23, 6.982e10, 4.79e4);
		Planet mars = new Planet(Color.RED, 3.4e6, 6.41e23, 2.49e11, 2.41e4);
		Planet venus = new Planet(Color.LIME, 6e6, 4.86e24, 1.08e11, 3.5e4);
		
		planets.add(sun);
		planets.add(earth);
		planets.add(moon);
		planets.add(mercury);
		planets.add(mars);
		planets.add(venus);

		Timeline loop = new Timeline(new KeyFrame(Duration.millis(1000.0/60), e -> update(gc)));
		loop.setCycleCount(Animation.INDEFINITE);
		loop.play();

		Scene scene = new Scene(pane, Util.WIDTH, Util.HEIGHT);
		scene.setFill(Color.BLACK);
		stage.setScene(scene);

		stage.setTitle("Solar System");
		stage.setResizable(false);
		stage.show();
	}

	private void update(GraphicsContext gc){
		gc.clearRect(0, 0, Util.WIDTH, Util.HEIGHT);
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, Util.WIDTH, Util.HEIGHT);

		for (Planet planet : planets){
			planet.updatePosition(planets);
		}
		for (Planet planet : planets){
			planet.applyPosition();
		}
		this.timePassed += Util.TIMESTEP;

		for (Planet planet : planets){
			planet.render(gc);
		}

		gc.setFill(Color.WHITE);
		gc.fillText(Util.getDate(this.timePassed), 20, 40);
	}

	public static void main(String[] args){
		launch(args);
	}
}