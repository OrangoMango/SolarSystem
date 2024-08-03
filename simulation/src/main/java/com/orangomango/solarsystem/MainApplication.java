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
	private boolean paused = false;

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
		Util.loadConfig("/test.json", this.planets);
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
		} else if (this.keys.getOrDefault(KeyCode.SPACE, false)){
			this.paused = !this.paused;
			this.keys.put(KeyCode.SPACE, false);
		}

		if (this.keys.getOrDefault(KeyCode.DIGIT0, false)){
			this.currentPlanet = 0;
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
			this.currentPlanet = 4;
			this.keys.put(KeyCode.DIGIT4, false);
		} else if (this.keys.getOrDefault(KeyCode.DIGIT5, false)){
			this.currentPlanet = 5;
			this.keys.put(KeyCode.DIGIT5, false);
		} else if (this.keys.getOrDefault(KeyCode.DIGIT6, false)){
			this.currentPlanet = 6;
			this.keys.put(KeyCode.DIGIT6, false);
		} else if (this.keys.getOrDefault(KeyCode.DIGIT7, false)){
			this.currentPlanet = 7;
			this.keys.put(KeyCode.DIGIT7, false);
		} else if (this.keys.getOrDefault(KeyCode.DIGIT8, false)){
			this.currentPlanet = 8;
			this.keys.put(KeyCode.DIGIT8, false);
		} else if (this.keys.getOrDefault(KeyCode.DIGIT9, false)){
			this.currentPlanet = 9;
			this.keys.put(KeyCode.DIGIT9, false);
		} else if (this.keys.getOrDefault(KeyCode.Q, false)){
			this.currentPlanet = -1;
			this.keys.put(KeyCode.Q, false);
		}

		if (!this.paused){
			for (Planet planet : planets){
				planet.updatePosition(planets);
			}
			for (Planet planet : planets){
				planet.applyPosition();
			}
			this.timePassed += Util.TIMESTEP;
		}

		for (Planet planet : planets){
			if (!this.paused) planet.renderOrbit(gc, this.scale);
			planet.render(gc);
		}

		gc.restore();

		gc.setFill(Color.WHITE);
		gc.fillText(Util.getDate(this.timePassed)+String.format(" (1 frame = %.2f seconds)", Util.TIMESTEP), 20, 40);

		// Follow planet
		if (this.currentPlanet != -1 && this.currentPlanet < this.planets.size()){
			followPlanet(this.planets.get(this.currentPlanet));
			gc.setFill(Color.WHITE);
			gc.fillText(this.planets.get(this.currentPlanet).toString(), 20, 80);
		}
	}

	public static void main(String[] args){
		launch(args);
	}
}