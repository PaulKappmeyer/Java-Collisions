package main;

import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;

import engine.Vector2D;

public class Particle {
	// consider using java.awt.geom.Ellipse2D.Double (TODO)

	public static boolean SHOW_DIRECTION = true;
	public static boolean SHOW_MASS = true;
	
	private Vector2D position; // center of the particle
	private Vector2D velocity; 
	private Vector2D acceleration;

	private double mass;
	private double radius;
	private double speed; // magnitude of the velocity vector
	private double kineticEnergy; // kinetic energy, i.e. 1/2 * mass * speed^2
	
	// overhaul constructor (TODO)
	public Particle(double x, double y, double velocityX, double velocityY, double mass, double radius) {
		this.position = new Vector2D(x, y);
		this.velocity = new Vector2D(velocityX, velocityY);
		this.acceleration = new Vector2D(0, 0);
		
		this.mass = mass;
		this.radius = radius;
		this.speed = velocity.length();
		this.kineticEnergy = 0.5 * mass * Math.pow(speed, 2);
	}
	
	public void update(double tslf) {
		// update cycle:
		velocity = velocity.add(acceleration.multiply(tslf)); 		// v = a * dt
		position = position.add(velocity.multiply(tslf));			// pos += v * dt
		
		// update attributes
		speed = velocity.length();									// speed = |v|
		kineticEnergy = 0.5 * mass * Math.pow(speed, 2);			// E_kin = 1/2 * m * |v|^2
	}
	
	public void draw(Graphics graphics) {
		// draw the shape
		graphics.setColor(Color.GRAY); // color scheme depending on speed etc. (TODO)
		graphics.fillOval((int) (position.getX() - radius), (int) (position.getY() - radius), (int) (2 * radius), (int) (2 * radius));
		
		// draw the outline
		graphics.setColor(Color.BLACK);
		graphics.drawOval((int) (position.getX() - radius), (int) (position.getY() - radius), (int) (2 * radius), (int) (2 * radius));
		
		if (SHOW_DIRECTION && speed > 0) {
			graphics.setColor(Color.BLACK);
			Vector2D endPoint = position.add(velocity.multiply(radius/speed));
			graphics.drawLine((int) position.getX(), (int) position.getY(), (int) endPoint.getX(), (int) endPoint.getY());
		}
		
		if (SHOW_MASS) {
			DecimalFormat df = new DecimalFormat("#.##");
			graphics.drawString(df.format(mass) + " kg", (int) (position.getX() - 20), (int) (position.getY() + 2.5));
		}
	}
	
	// overhaul getters and setters (TODO)
	public Vector2D getPosition() {
		return new Vector2D(position);
	}
	
	public void setPosition(Vector2D position) {
		this.position = position;
	}
	
	public double getX() {
		return position.getX();
	}
	
	public double getY() {
		return position.getY();
	}
	
	public Vector2D getVelocity() {
		return new Vector2D(velocity);
	}
	
	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public double getMass() {
		return mass;
	}
	
	public double getKineticEnergy() {
		return kineticEnergy;
	}
}
