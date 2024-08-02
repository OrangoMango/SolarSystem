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

	public Planet(Color color, double r, double m, double d, double v){
		this.color = color;
		this.radius = Math.random()*8; //r;
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

	public void render(GraphicsContext gc){
		final double x = this.x*Util.SCALE+Util.WIDTH/2;
		final double y = this.y*Util.SCALE+Util.HEIGHT/2;
			
		gc.setFill(this.color);
		final double radius = this.radius; //*Util.SCALE*10;
		gc.fillOval(x-radius, y-radius, radius*2, radius*2);
	}
}