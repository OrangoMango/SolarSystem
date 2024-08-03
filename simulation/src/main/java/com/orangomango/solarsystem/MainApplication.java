package com.orangomango.solarsystem;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.HashMap;

public class MainApplication extends Application{
	private ArrayList<Planet> planets = new ArrayList<>();
	private HashMap<KeyCode, Boolean> keys = new HashMap<>();
	private long timePassed;
	private double cameraX, cameraY;
	private double scale = 1;
	private int currentPlanet = -1;

	@Override
	public void start(Stage stage){
		StackPane pane = new StackPane();
		Canvas canvas = new Canvas(Util.WIDTH, Util.HEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		pane.getChildren().add(canvas);

		canvas.setFocusTraversable(true);
		canvas.setOnKeyPressed(e -> this.keys.put(e.getCode(), true));
		canvas.setOnKeyReleased(e -> this.keys.put(e.getCode(), false));

		canvas.setOnScroll(e -> {
			if (e.getDeltaY() > 0){
				this.scale += 0.3;
			} else if (e.getDeltaY() < 0){
				this.scale -= 0.3;
			}

			this.scale = Math.max(0.1, this.scale);
		});

		// Create planets
		generatePlanets();

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

	private void generatePlanets(){
		this.planets.clear();
		Planet sun = new Planet("Sun", Color.YELLOW, 6.96e8, 1.989e30, 0, 0);
		Planet mercury = new Planet("Mercury", Color.ORANGE, 2.4e6, 3.30e23, 6.982e10, 4.79e4);
		Planet venus = new Planet("Venus", Color.LIME, 6e6, 4.86e24, 1.08e11, 3.5e4);
		Planet earth = new Planet("Earth", Color.CYAN, 6.3e6, 5.972e24, 1.50e11, 2.98e4);
		Planet moon = new Planet("Moon", Color.GRAY, 1.7e6, 7.35e22, 1.50e11+3.84e8, 2.98e4+1.022e3);
		Planet mars = new Planet("Mars", Color.RED, 3.4e6, 6.41e23, 2.49e11, 2.41e4);
		Planet jupiter = new Planet("Jupiter", Color.BROWN, 6.9e7, 1.89e27, 7.78e11, 1.31e4);
		Planet saturn = new Planet("Saturn", Color.WHITE, 5.82e7, 5.68e26, 1.43e12, 9.68e3);
		Planet uranus = new Planet("Uranus", Color.BLUE, 2.53e7, 8.68e25, 2.87e12, 6.8e3);
		Planet neptune = new Planet("Neptune", Color.CYAN, 2.46e7, 1.024e26, 4.5e12, 5.43e3);
		
		this.planets.add(sun);
		this.planets.add(mercury);
		this.planets.add(venus);
		this.planets.add(earth);
		this.planets.add(moon);
		this.planets.add(mars);
		this.planets.add(jupiter);
		this.planets.add(saturn);
		this.planets.add(uranus);
		this.planets.add(neptune);
	}

	private void followPlanet(Planet planet){
		Point2D pos = planet.convertPosition();
		this.cameraX = pos.getX()*this.scale-Util.WIDTH/2;
		this.cameraY = pos.getY()*this.scale-Util.HEIGHT/2;
	}

	private void update(GraphicsContext gc){
		gc.clearRect(0, 0, Util.WIDTH, Util.HEIGHT);
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, Util.WIDTH, Util.HEIGHT);

		gc.save();
		gc.translate(-this.cameraX, -this.cameraY);
		gc.scale(this.scale, this.scale);

		final double cameraSpeed = 50;
		if (this.keys.getOrDefault(KeyCode.W, false)){
			this.cameraY -= cameraSpeed;
		} else if (this.keys.getOrDefault(KeyCode.A, false)){
			this.cameraX -= cameraSpeed;
		} else if (this.keys.getOrDefault(KeyCode.S, false)){
			this.cameraY += cameraSpeed;
		} else if (this.keys.getOrDefault(KeyCode.D, false)){
			this.cameraX += cameraSpeed;
		}

		if (this.keys.getOrDefault(KeyCode.R, false)){
			this.scale = 1;
			this.cameraX = 0;
			this.cameraY = 0;
			generatePlanets();
			this.keys.put(KeyCode.R, false);
		}

		if (this.keys.getOrDefault(KeyCode.DIGIT0, false)){
			this.currentPlanet = -1;
			this.keys.put(KeyCode.DIGIT0, false);
		} else if (this.keys.getOrDefault(KeyCode.DIGIT1, false)){
			this.currentPlanet = 1;
			this.keys.put(KeyCode.DIGIT1, false);
		} else if (this.keys.getOrDefault(KeyCode.DIGIT2, false)){
			this.currentPlanet = 2;
			this.keys.put(KeyCode.DIGIT2, false);
		} else if (this.keys.getOrDefault(KeyCode.DIGIT3, false)){
			this.currentPlanet = 3;
			this.keys.put(KeyCode.DIGIT3, false);
		} else if (this.keys.getOrDefault(KeyCode.DIGIT4, false)){
			this.currentPlanet = 5;
			this.keys.put(KeyCode.DIGIT4, false);
		} else if (this.keys.getOrDefault(KeyCode.DIGIT5, false)){
			this.currentPlanet = 6;
			this.keys.put(KeyCode.DIGIT5, false);
		} else if (this.keys.getOrDefault(KeyCode.DIGIT6, false)){
			this.currentPlanet = 7;
			this.keys.put(KeyCode.DIGIT6, false);
		} else if (this.keys.getOrDefault(KeyCode.DIGIT7, false)){
			this.currentPlanet = 8;
			this.keys.put(KeyCode.DIGIT7, false);
		} else if (this.keys.getOrDefault(KeyCode.DIGIT8, false)){
			this.currentPlanet = 9;
			this.keys.put(KeyCode.DIGIT8, false);
		} else if (this.keys.getOrDefault(KeyCode.DIGIT9, false)){
			this.currentPlanet = 0; // Sun
			this.keys.put(KeyCode.DIGIT9, false);
		}

		for (Planet planet : planets){
			planet.updatePosition(planets);
		}
		for (Planet planet : planets){
			planet.applyPosition();
		}
		this.timePassed += Util.TIMESTEP;

		for (Planet planet : planets){
			planet.renderOrbit(gc, this.scale);
			planet.render(gc);
		}

		gc.restore();

		gc.setFill(Color.WHITE);
		gc.fillText(Util.getDate(this.timePassed)+String.format(" (1 frame = %.2f seconds)", Util.TIMESTEP), 20, 40);

		// Follow planet
		if (this.currentPlanet != -1){
			followPlanet(this.planets.get(this.currentPlanet));
			gc.setFill(Color.WHITE);
			gc.fillText(this.planets.get(this.currentPlanet).toString(), 20, 80);
		}
	}

	public static void main(String[] args){
		launch(args);
	}
}