package com.orangomango.solarsystem;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.animation.*;

import java.util.*;

public class MainApplication extends Application{
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 800;

	private double mouseX, mouseY;
	private List<Planet> planets = new ArrayList<>();
	private volatile boolean paused = false;

	@Override
	public void start(Stage stage){
		GridPane pane = new GridPane();
		pane.setPadding(new Insets(10, 10, 10, 10));
		pane.setHgap(5);
		pane.setVgap(5);
		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		GraphicsContext gc  = canvas.getGraphicsContext2D();
		pane.add(canvas, 0, 0);

		canvas.setOnMouseMoved(e -> {
			this.mouseX = e.getSceneX();
			this.mouseY = e.getSceneY();
		});

		VBox settings = new VBox();
		settings.setSpacing(5);
		pane.add(settings, 1, 0);
		TextField planetName = new TextField();
		planetName.setPromptText("Planet name");
		TextField mass = new TextField();
		mass.setPromptText("Mass");
		ColorPicker color = new ColorPicker();
		TextField radius = new TextField();
		radius.setPromptText("Radius");
		TextField xPos = new TextField();
		xPos.setPromptText("X position");
		TextField yPos = new TextField();
		yPos.setPromptText("Y position");
		TextField xVel = new TextField();
		xVel.setPromptText("X velocity");
		TextField yVel = new TextField();
		yVel.setPromptText("Y velocity");
		Button addPlanet = new Button("Add planet");
		addPlanet.setOnAction(e -> {
			double r = Double.parseDouble(radius.getText());
			double m = Double.parseDouble(mass.getText());
			double x = Double.parseDouble(xPos.getText());
			double y = Double.parseDouble(yPos.getText());
			double xv = Double.parseDouble(xVel.getText());
			double yv = Double.parseDouble(yVel.getText());
			Planet planet = new Planet(planetName.getText(), color.getValue(), r, m, x, y, xv, yv);
			this.planets.add(planet);
		});
		TextField updateIndex = new TextField();
		updateIndex.setPromptText("Index");
		Button updatePlanet = new Button("Update");
		updatePlanet.setOnAction(e -> {
			if (updateIndex.getText().startsWith(":")){
				Planet planet = this.planets.get(Integer.parseInt(updateIndex.getText().substring(1)));
				planetName.setText(planet.getName());
				radius.setText(Double.toString(planet.getRadius()));
				mass.setText(Double.toString(planet.getMass()));
				xPos.setText(Double.toString(planet.getX()));
				yPos.setText(Double.toString(planet.getY()));
				xVel.setText(Double.toString(planet.getXvel()));
				yVel.setText(Double.toString(planet.getYvel()));
			} else {
				double r = Double.parseDouble(radius.getText());
				double m = Double.parseDouble(mass.getText());
				double x = Double.parseDouble(xPos.getText());
				double y = Double.parseDouble(yPos.getText());
				double xv = Double.parseDouble(xVel.getText());
				double yv = Double.parseDouble(yVel.getText());
				Planet planet = this.planets.get(Integer.parseInt(updateIndex.getText()));
				planet.setRadius(r);
				planet.setMass(m);
				planet.setX(x);
				planet.setY(y);
				planet.setXvel(xv);
				planet.setYvel(yv);
			}
		});

		// Test
		this.planets.add(new Planet("Test", Color.CYAN, 25, 10, 300, 300, 0, 0));
		this.planets.add(new Planet("Test 2", Color.YELLOW, 10, 1, 300, 250, 1.1, 0));

		TextField gConstant = new TextField();
		gConstant.setPromptText("G");
		Button apply = new Button("Apply");

		Button pause = new Button("PAUSE");
		pause.setOnAction(e -> this.paused = true);
		Button resume = new Button("RESUME");
		resume.setOnAction(e -> this.paused = false);

		Button clearOrbits = new Button("Clear orbits");
		clearOrbits.setOnAction(e -> {
			for (Planet p : this.planets){
				p.orbit.clear();
			}
		});
		TextField planetIndex = new TextField();
		planetIndex.setPromptText("Index");
		TextField amount = new TextField();
		amount.setPromptText("Amount");
		Button makeOrbit = new Button("Make orbit");
		makeOrbit.setOnAction(e -> this.planets.get(Integer.parseInt(planetIndex.getText())).makeOrbit(this.planets, Integer.parseInt(amount.getText())));

		settings.getChildren().addAll(new HBox(3, gConstant, apply));
		settings.getChildren().addAll(new Separator(), planetName, mass, color, radius, new HBox(3, xPos, yPos), new HBox(3, xVel, yVel), new HBox(3, addPlanet), new HBox(3, updateIndex, updatePlanet));
		settings.getChildren().addAll(new Separator(), new HBox(3, pause, resume));
		settings.getChildren().addAll(new Separator(), clearOrbits, new HBox(3, planetIndex, makeOrbit));

		AnimationTimer loop = new AnimationTimer(){
			@Override
			public void handle(long time){
				update(gc);
			}
		};
		loop.start();

		Scene scene = new Scene(pane, WIDTH+300, HEIGHT+60);
		stage.setScene(scene);
		stage.setTitle("Solar System");
		stage.setResizable(false);
		stage.show();
	}

	private void update(GraphicsContext gc){
		gc.clearRect(0, 0, WIDTH, HEIGHT);
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, WIDTH, HEIGHT);

		for (int i = 0; i < this.planets.size(); i++){
			Planet planet = this.planets.get(i);
			if (!this.paused){
				planet.updateVelocity(this.planets);
			}
		}

		for (int i = 0; i < this.planets.size(); i++){
			Planet planet = this.planets.get(i);
			if (!this.paused){
				planet.updatePosition();
			}
			planet.render(gc);
		}

		gc.setFill(Color.WHITE);
		StringBuilder builder = new StringBuilder();
		builder.append("Debug info\n");
		builder.append("Mouse position: ").append(String.format("%.1f %.1f", this.mouseX, this.mouseY));
		gc.fillText(builder.toString(), 20, 20);
	}

	public static void main(String[] args){
		launch(args);
	}
}