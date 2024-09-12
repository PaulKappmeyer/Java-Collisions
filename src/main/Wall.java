package main;

import java.awt.Color;
import java.awt.Graphics;

import engine.Vector2D;

public class Wall {

	private final Vector2D position; // top left corner
	
	private final double width;
	private final double height;
	
	public Wall(double x, double y, double width, double height) {
		this.position = new Vector2D(x, y);
		this.width = width;
		this.height = height;
	}
	
	public void draw(Graphics graphics) {
		// draw the shape
		graphics.setColor(Color.DARK_GRAY);
		graphics.fillRect((int) position.getX(), (int) position.getY(), (int) width, (int) height);
		
		// draw the outline
		graphics.setColor(Color.BLACK);
		graphics.drawRect((int) position.getX(), (int) position.getY(), (int) width, (int) height);
	}
	
	public Vector2D getPosition() {
		return new Vector2D(position);
	}
	
	public double getX() {
		return position.getX();
	}
	
	public double getY() {
		return position.getY();
	}
	
	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}
}
