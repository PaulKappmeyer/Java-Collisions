package main;

import java.awt.Color;
import java.awt.Graphics;

import engine.GameBase;

public class Main extends GameBase {

	// size of the window (in pixel)
	public static final int SCREEN_WIDTH = 1200;
	public static final int SCREEN_HEIGHT = 900;
	
	Particle[] particles;
	
	public static void main(String[] args) {
		Main main = new Main();
		main.start("Collisions", SCREEN_WIDTH, SCREEN_HEIGHT);
	}
	
	@Override
	public void init() {
		particles = new Particle[1];
		for (int i = 0; i < 1; i++) {
			particles[i] = new Particle(SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
		}
	}

	@Override
	public void update(double tslf) {
		for(Particle particle : particles) {
			particle.update(tslf);
		}
	}

	@Override
	public void draw(Graphics graphics) {
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		graphics.setColor(Color.BLACK);
		graphics.drawString("FPS: " + getFPS(), 10, 10);
		
		for(Particle particle : particles) {
			particle.draw(graphics);
		}
	}
}
