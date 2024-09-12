package main;

import java.awt.Color;
import java.awt.Graphics;

import engine.Vector2D;

public class Particle {

	private Vector2D position; // center of the particle
	private Vector2D velocity; 
	private Vector2D acceleration;

	private double mass;
	private double radius;
	private double speed; // magnitude of the velocity vector
	private double kineticEnergy; // kinetic energy, i.e. 1/2 * mass * speed^2
	
	// overhaul constructor (TODO)
	public Particle(double x, double y) {
		this.position = new Vector2D(x, y);
		this.velocity = Vector2D.randomDirectionvector().multiply((Math.random() + 1) * 200);
		this.acceleration = new Vector2D(0, 0);
		
		this.mass = 0.01 * (1 + Math.random());
		this.radius = (float) (Math.sqrt(mass) * 200);
	}
	
	public void update(double tslf) {
		// update cycle:
		velocity = velocity.add(acceleration.multiply(tslf)); 		// v = a * dt
		position = position.add(velocity.multiply(tslf));			// pos += v * dt
		
		// update attributes
		speed = velocity.length();									// speed = |v|
		kineticEnergy = 0.5 * mass * Math.pow(speed, 2);			// E_kin = 1/2 * m * |v|^2
		
		// check collisions with walls:
		boundaryCollisions();
	}
	
	// make boundaries into objects (TODO)
	public void boundaryCollisions() {
		if (position.getX() < radius) {
			position = new Vector2D(radius, position.getY());
			velocity = new Vector2D(-velocity.getX(), velocity.getY()); // add damping factor (TODO)
		} else if (position.getX() > Main.SCREEN_WIDTH - radius) {
			position = new Vector2D(Main.SCREEN_WIDTH - radius, position.getY());
			velocity = new Vector2D(-velocity.getX(), velocity.getY()); // add damping factor (TODO)
		}
		
		if (position.getY() < radius) {
			position = new Vector2D(position.getX(), radius);
			velocity = new Vector2D(velocity.getX(), -velocity.getY()); // add damping factor (TODO)
		} else if (position.getY() > Main.SCREEN_HEIGHT - radius) {
			position = new Vector2D(position.getX(), Main.SCREEN_HEIGHT - radius);
			velocity = new Vector2D(velocity.getX(), -velocity.getY()); // add damping factor (TODO)
		}
	}
	
	public void draw(Graphics graphics) {
		// draw the shape
		graphics.setColor(Color.GRAY); // color scheme depending on speed etc. (TODO)
		graphics.fillOval((int) (position.getX() - radius), (int) (position.getY() - radius), (int) (2 * radius), (int) (2 * radius));
		
		// draw the outline
		graphics.setColor(Color.BLACK);
		graphics.drawOval((int) (position.getX() - radius), (int) (position.getY() - radius), (int) (2 * radius), (int) (2 * radius));
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
