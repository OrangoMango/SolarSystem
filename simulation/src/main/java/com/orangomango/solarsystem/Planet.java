package com.orangomango.solarsystem;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Point2D;

import java.util.ArrayList;

public class Planet{
	private double x, y, tempX, tempY;
	private double mass, radius;
	private double xVelocity, yVelocity;
	private Color color;
	private ArrayList<Point2D> orbit = new ArrayList<>();
	private String name;

	public Planet(String name, Color color, double r, double m, double d, double v){
		this.name = name;
		this.color = color;
		this.radius = r;
		this.mass = m;
		this.x = -d;
		this.yVelocity = v;
	}

	private Point2D getAttractionForce(Planet planet){
		double distanceX = planet.x-this.x;
		double distanceY = planet.y-this.y;
		double distance = Math.sqrt(Math.pow(distanceX, 2)+Math.pow(distanceY, 2));

		double force = Util.G*this.mass*planet.mass/Math.pow(distance, 2);
		double angle = Math.atan2(distanceY, distanceX);
		double forceX = force * Math.cos(angle);
		double forceY = force * Math.sin(angle);

		return new Point2D(forceX, forceY);
	}

	public void updatePosition(ArrayList<Planet> planets){
		double totalForceX = 0;
		double totalForceY = 0;

		for (Planet planet : planets){
			if (planet == this) continue;
			Point2D force = getAttractionForce(planet);
			totalForceX += force.getX();
			totalForceY += force.getY();
		}

		this.xVelocity += totalForceX/this.mass*Util.TIMESTEP;
		this.yVelocity += totalForceY/this.mass*Util.TIMESTEP;

		this.tempX = this.x + this.xVelocity*Util.TIMESTEP;
		this.tempY = this.y + this.yVelocity*Util.TIMESTEP;
	}

	public void applyPosition(){
		this.x = this.tempX;
		this.y = this.tempY;
	}

	public Point2D convertPosition(){
		double x = this.x*Util.SCALE+Util.WIDTH/2;
		double y = this.y*Util.SCALE+Util.HEIGHT/2;
		return new Point2D(x, y);
	}

	public void render(GraphicsContext gc){
		Point2D pos = convertPosition();
			
		gc.setFill(this.color);
		final double radius = this.radius*Util.SCALE*30;
		gc.fillOval(pos.getX()-radius, pos.getY()-radius, radius*2, radius*2);
	}

	public void renderOrbit(GraphicsContext gc, double scale){
		Point2D pos = convertPosition();
		this.orbit.add(pos);
		if (this.orbit.size() == Util.MAX_ORBIT_SIZE){
			this.orbit.remove(0);
		}

		for (int i = 0; i < this.orbit.size()-1; i++){
			Point2D a = this.orbit.get(i);
			Point2D b = this.orbit.get(i+1);
			gc.setStroke(Color.WHITE);
			gc.setLineWidth(0.6/scale);
			gc.strokeLine(a.getX(), a.getY(), b.getX(), b.getY());
		}
	}

	@Override
	public String toString(){
		return String.format("Planet: %s\nx: %s\ny: %s\nspeed: %s m/s\nmass: %s kg\nradius: %s m", this.name, this.x, this.y, (new Point2D(this.xVelocity, this.yVelocity)).magnitude(), this.mass, this.radius);
	}
}