package com.orangomango.solarsystem;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.animation.*;
import javafx.scene.input.MouseButton;

import java.util.*;
import java.io.*;

public class MainApplication extends Application{
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 800;

	private double mouseX, mouseY;
	private List<Planet> planets = new ArrayList<>();
	private volatile boolean paused = false;
	private double cameraX, cameraY;
	private double scale = 1;
	private double startDragX, startDragY;
	private Planet selectedPlanet = null;

	@Override
	public void start(Stage stage){
		GridPane pane = new GridPane();
		pane.setPadding(new Insets(10, 10, 10, 10));
		pane.setHgap(5);
		pane.setVgap(5);
		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		GraphicsContext gc  = canvas.getGraphicsContext2D();
		pane.add(canvas, 0, 1);

		canvas.setOnMouseMoved(e -> {
			this.mouseX = e.getX();
			this.mouseY = e.getY();
		});

		canvas.setOnMousePressed(e -> {
			this.startDragX = e.getX();
			this.startDragY = e.getY();
			for (Planet planet : this.planets){
				Point2D pos = new Point2D(planet.getX(), planet.getY());
				if (pos.distance(e.getX()/this.scale+this.cameraX, e.getY()/this.scale+this.cameraY) < planet.getRadius()){
					this.selectedPlanet = planet;
					break;
				}
			}

			if (e.getButton() == MouseButton.MIDDLE){
				if (this.selectedPlanet != null){
					this.planets.remove(this.selectedPlanet);
					this.selectedPlanet = null;
				}
			}
		});

		canvas.setOnMouseDragged(e -> {
			if (e.getButton() == MouseButton.PRIMARY){
				if (this.selectedPlanet != null){
					this.selectedPlanet.setX(e.getX()/this.scale+this.cameraX);
					this.selectedPlanet.setY(e.getY()/this.scale+this.cameraY);
				}
			} else if (e.getButton() == MouseButton.SECONDARY){
				this.cameraX += this.startDragX-e.getX();
				this.cameraY += this.startDragY-e.getY();
				this.startDragX = e.getX();
				this.startDragY = e.getY();
			}
		});

		canvas.setOnMouseReleased(e -> {
			this.selectedPlanet = null;
		});

		canvas.setOnScroll(e -> {
			if (e.getDeltaY() > 0){
				this.scale += 0.1;
			} else if (e.getDeltaY() < 0){
				this.scale -= 0.1;
			}
		});

		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");
		MenuItem saveMenuItem = new MenuItem("Save");
		saveMenuItem.setOnAction(e -> {
			FileChooser chooser = new FileChooser();
			File file = chooser.showSaveDialog(stage);
			if (file != null){
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(file));
					for (Planet planet : this.planets){
						writer.write(planet.getName()+"\n");
						writer.write(String.format("%s %s %s\n", planet.getColor().getRed(), planet.getColor().getGreen(), planet.getColor().getBlue()));
						writer.write(Double.toString(planet.getRadius())+"\n");
						writer.write(Double.toString(planet.getMass())+"\n");
						writer.write(Double.toString(planet.getX())+"\n");
						writer.write(Double.toString(planet.getY())+"\n");
						writer.write(Double.toString(planet.getXvel())+"\n");
						writer.write(Double.toString(planet.getYvel())+"\n\n");
					}
					writer.close();
				} catch (IOException ex){
					ex.printStackTrace();
				}
			}
		});
		MenuItem openMenuItem = new MenuItem("Open");
		openMenuItem.setOnAction(e -> {
			FileChooser chooser = new FileChooser();
			File file = chooser.showOpenDialog(stage);
			if (file != null){
				try {
					BufferedReader reader = new BufferedReader(new FileReader(file));
					this.planets.clear();
					String line;
					String name = null;
					Color color = null;
					double r = 0, m = 0, x = 0, y = 0, xVel = 0, yVel = 0;
					int count = 0;
					while ((line = reader.readLine()) != null){
						if (line.isBlank()){
							Planet planet = new Planet(name, color, r, m, x, y, xVel, yVel);
							this.planets.add(planet);
							count = 0;
						} else {
							switch (count){
								case 0:
									name = line;
									break;
								case 1:
									color = Color.color(Double.parseDouble(line.split(" ")[0]), Double.parseDouble(line.split(" ")[1]), Double.parseDouble(line.split(" ")[2]));
									break;
								case 2:
									r = Double.parseDouble(line);
									break;
								case 3:
									m = Double.parseDouble(line);
									break;
								case 4:
									x = Double.parseDouble(line);
									break;
								case 5:
									y = Double.parseDouble(line);
									break;
								case 6:
									xVel = Double.parseDouble(line);
									break;
								case 7:
									yVel = Double.parseDouble(line);
									break;
							}
							count++;
						}
					}
				} catch (IOException ex){
					ex.printStackTrace();
				}
			}
		});

		menuBar.getMenus().add(fileMenu);
		fileMenu.getItems().addAll(saveMenuItem, openMenuItem);
		pane.add(menuBar, 0, 0, 2, 1);

		VBox settings = new VBox();
		settings.setSpacing(5);
		pane.add(settings, 1, 1);
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

		// Test
		reset();

		TextField gConstant = new TextField();
		gConstant.setPromptText("G");
		Button apply = new Button("Apply");
		apply.setOnAction(e -> Planet.G = Double.parseDouble(gConstant.getText()));

		Button pause = new Button("PAUSE");
		pause.setOnAction(e -> this.paused = true);
		Button resume = new Button("RESUME");
		resume.setOnAction(e -> this.paused = false);
		Button reset = new Button("RESET");
		reset.setOnAction(e -> reset());

		Button clearOrbit = new Button("Clear orbit");
		clearOrbit.setOnAction(e -> {
			for (Planet p : this.planets){
				p.orbit.clear();
			}
		});
		TextField planetIndex = new TextField();
		planetIndex.setMaxWidth(70);
		planetIndex.setPromptText("Index");
		TextField amount = new TextField("1000");
		amount.setMaxWidth(70);
		amount.setPromptText("Amount");
		Button simulate = new Button("Simulate");
		simulate.setOnAction(e -> simulateFrames(Integer.parseInt(amount.getText())));
		Button loadSliders = new Button("Load data");
		UiSlider radiusSlider = new UiSlider(0, 10, 0, r -> {
			this.planets.get(Integer.parseInt(planetIndex.getText())).setRadius(r);
			simulateFrames(Integer.parseInt(amount.getText()));
		});
		UiSlider massSlider = new UiSlider(0, 10, 0, m -> {
			this.planets.get(Integer.parseInt(planetIndex.getText())).setMass(m);
			simulateFrames(Integer.parseInt(amount.getText()));
		});
		UiSlider xVelSlider = new UiSlider(0, 10, 0, xv -> {
			this.planets.get(Integer.parseInt(planetIndex.getText())).setXvel(xv);
			simulateFrames(Integer.parseInt(amount.getText()));
		});
		UiSlider yVelSlider = new UiSlider(0, 10, 0, yv -> {
			this.planets.get(Integer.parseInt(planetIndex.getText())).setYvel(yv);
			simulateFrames(Integer.parseInt(amount.getText()));
		});
		loadSliders.setOnAction(e -> {
			Planet planet = this.planets.get(Integer.parseInt(planetIndex.getText()));
			radiusSlider.setValue(planet.getRadius());
			massSlider.setValue(planet.getMass());
			xVelSlider.setValue(planet.getXvel());
			yVelSlider.setValue(planet.getYvel());
		});

		settings.getChildren().addAll(new HBox(3, gConstant, apply));
		settings.getChildren().addAll(new Separator(), planetName, mass, color, radius, new HBox(3, xPos, yPos), new HBox(3, xVel, yVel), new HBox(3, addPlanet));
		settings.getChildren().addAll(new Separator(), new HBox(3, pause, resume, reset));
		settings.getChildren().addAll(new Separator(), new HBox(3, clearOrbit, simulate), new HBox(3, planetIndex, amount, loadSliders));
		settings.getChildren().addAll(radiusSlider.getNode(), massSlider.getNode(), xVelSlider.getNode(), yVelSlider.getNode());

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

	private void reset(){
		this.planets.clear();
		this.planets.add(new Planet("Earth", Color.CYAN, 20, 10, 300, 300, 0, 0));
		this.planets.add(new Planet("Moon", Color.GRAY, 10, 1, 300, 250, 0.4, 0));
		this.planets.add(new Planet("Sun", Color.YELLOW, 40, 1000, 600, 550, 0, 0));
		this.cameraX = 0;
		this.cameraY = 0;
	}

	private void simulateFrames(int amount){
		List<double[]> backup = new ArrayList<>();
		for (Planet planet : this.planets){
			planet.orbit.clear();
			backup.add(planet.backup());
		}

		for (int i = 0; i < amount; i++){
			for (Planet planet : this.planets){
				planet.updateVelocity(this.planets);
			}
			for (Planet planet : this.planets){
				planet.updatePosition();
				planet.orbit.add(new Point2D(planet.getX(), planet.getY()));
			}
		}

		for (int i = 0; i < this.planets.size(); i++){
			this.planets.get(i).restore(backup.get(i));
		}
	}

	private void update(GraphicsContext gc){
		gc.clearRect(0, 0, WIDTH, HEIGHT);
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, WIDTH, HEIGHT);

		gc.save();
		gc.scale(this.scale, this.scale);
		gc.translate(-this.cameraX, -this.cameraY);

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
			if (planet == this.selectedPlanet){
				gc.setStroke(Color.RED);
				gc.setLineWidth(1.5);
				gc.strokeRect(planet.getX()-planet.getRadius(), planet.getY()-planet.getRadius(), planet.getRadius()*2, planet.getRadius()*2);
				gc.setFill(Color.RED);
				gc.fillText(Integer.toString(i), planet.getX()-planet.getRadius(), planet.getY()-planet.getRadius()-7);
			}
		}

		gc.restore();

		gc.setFill(Color.WHITE);
		StringBuilder builder = new StringBuilder();
		builder.append("Debug info\n");
		builder.append("Mouse position: ").append(String.format("%.1f %.1f\n", this.mouseX, this.mouseY));
		builder.append("G: "+Planet.G);
		if (this.selectedPlanet != null){
			builder.append("\nSelected planet: "+this.selectedPlanet.getName()+"\n");
			builder.append(String.format("Planet radius: %.3f\n", this.selectedPlanet.getRadius()));
			builder.append(String.format("Planet mass: %.3f\n", this.selectedPlanet.getMass()));
			builder.append(String.format("Planet x: %.3f\n", this.selectedPlanet.getX()));
			builder.append(String.format("Planet y: %.3f\n", this.selectedPlanet.getY()));
			builder.append(String.format("Planet xVel: %.3f\n", this.selectedPlanet.getXvel()));
			builder.append(String.format("Planet yVel: %.3f", this.selectedPlanet.getYvel()));
		}
		gc.fillText(builder.toString(), 20, 20);
	}

	public static void main(String[] args){
		launch(args);
	}
}