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
	
	public Particle(float x, float y) {
		this.position = new Vector2D(x, y);
		this.velocity = Vector2D.randomDirectionvector().multiply((Math.random() + 1) * 200);
		this.acceleration = new Vector2D(0, 0);
		
		this.mass = Math.random() * 6 + 1;
		this.radius = (float) (Math.sqrt(mass) * 20);
	}
	
	public void update(double tslf) {
		velocity.add(new Vector2D(acceleration).multiply(tslf));
		position.add(new Vector2D(velocity).multiply(tslf));
		
		boundaryCollisions();
	}
	
	public void boundaryCollisions() {
		if (position.getX() < radius) {
			position.setX(radius);
			velocity.setX(velocity.getX() * -1);
		} else if (position.getX() > Main.SCREEN_WIDTH - radius) {
			position.setX(Main.SCREEN_WIDTH - radius);
			velocity.setX(velocity.getX() * -1);
		}
		
		if (position.getY() < radius) {
			position.setY(radius);
			velocity.setY(velocity.getY() * -1);
		} else if (position.getY() > Main.SCREEN_HEIGHT - radius) {
			position.setY(Main.SCREEN_HEIGHT - radius);
			velocity.setY(velocity.getY() * -1);
		}
	}
	
	public void draw(Graphics graphics) {
		graphics.setColor(Color.GRAY);
		graphics.fillOval((int) (position.getX() - radius), (int) (position.getY() - radius), (int) (2 * radius), (int) (2 * radius));
		
		graphics.setColor(Color.BLACK);
		graphics.drawOval((int) (position.getX() - radius), (int) (position.getY() - radius), (int) (2 * radius), (int) (2 * radius));
	}
}
