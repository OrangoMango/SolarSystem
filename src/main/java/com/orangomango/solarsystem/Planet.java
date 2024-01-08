package com.orangomango.solarsystem;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Point2D;

import java.util.*;

public class Planet{
	private String name;
	private double x, y;
	private double xVelocity, yVelocity;
	private double mass, radius;
	private Color color;
	public List<Point2D> orbit = new ArrayList<>();

	public Planet(String name, Color color, double radius, double mass, double x, double y, double xVelocity, double yVelocity){
		this.name = name;
		this.color = color;
		this.radius = radius;
		this.mass = mass;
		this.x = x;
		this.y = y;
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
	}

	private Point2D getAttractionForce(Planet planet){
		double distanceX = planet.x-this.x;
		double distanceY = planet.y-this.y;
		double distance = Math.sqrt(Math.pow(distanceX, 2)+Math.pow(distanceY, 2));

		double force = this.mass*planet.mass/Math.pow(distance, 2);
		double angle = Math.atan2(distanceY, distanceX);
		double forceX = force * Math.cos(angle);
		double forceY = force * Math.sin(angle);

		return new Point2D(forceX, forceY);
	}

	public void updateVelocity(List<Planet> planets){
		double totalForceX = 0;
		double totalForceY = 0;

		for (Planet planet : planets){
			if (planet == this) continue;
			Point2D force = getAttractionForce(planet);
			totalForceX += force.getX();
			totalForceY += force.getY();
		}

		this.xVelocity += totalForceX/this.mass;
		this.yVelocity += totalForceY/this.mass;
	}

	public void updatePosition(){
		this.x += this.xVelocity;
		this.y += this.yVelocity;
	}

	public double[] backup(){
		return new double[]{this.x, this.y, this.xVelocity, this.yVelocity};
	}

	public void restore(double[] backup){
		this.x = backup[0];
		this.y = backup[1];
		this.xVelocity = backup[2];
		this.yVelocity = backup[3];
	}

	public void render(GraphicsContext gc){
		gc.setFill(this.color);
		gc.fillOval(this.x-this.radius, this.y-this.radius, this.radius*2, this.radius*2);

		gc.setStroke(Color.WHITE);
		gc.setLineWidth(0.8);
		for (int i = 0; i < this.orbit.size()-1; i++){
			gc.strokeLine(this.orbit.get(i).getX(), this.orbit.get(i).getY(), this.orbit.get(i+1).getX(), this.orbit.get(i+1).getY());
		}
	}

	public String getName(){
		return this.name;
	}

	public double getMass(){
		return this.mass;
	}

	public void setMass(double value){
		this.mass = value;
	}

	public double getRadius(){
		return this.radius;
	}

	public void setRadius(double value){
		this.radius = value;
	}

	public double getX(){
		return this.x;
	}

	/*public void setX(double value){
		this.x = value;
	}*/

	public double getY(){
		return this.y;
	}

	/*public void setY(double value){
		this.y = value;
	}*/

	public double getXvel(){
		return this.xVelocity;
	}

	public void setXvel(double value){
		this.xVelocity = value;
	}

	public double getYvel(){
		return this.yVelocity;
	}

	public void setYvel(double value){
		this.yVelocity = value;
	}
}