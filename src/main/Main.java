package main;

import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;

import engine.GameBase;

public class Main extends GameBase {

	// size of the window (in pixel)
	public static final int SCREEN_WIDTH = 1200;
	public static final int SCREEN_HEIGHT = 900;
	
	private SimulationSystem system;
	
	public static void main(String[] args) {
		Main main = new Main();
		main.start("Elastic Collisions", SCREEN_WIDTH, SCREEN_HEIGHT);
	}
	
	@Override
	public void init() {
		system = new SimulationSystem(true);
		
		// add hidden boundary walls
		system.addWall(new Wall(0, -10, SCREEN_WIDTH, 10));
		system.addWall(new Wall(SCREEN_WIDTH, 0, 10, SCREEN_HEIGHT));
		system.addWall(new Wall(0, SCREEN_HEIGHT, SCREEN_WIDTH, 10));
		system.addWall(new Wall(-10, 0, 10, SCREEN_HEIGHT));
		// add random walls
		int numWalls = 10;
		for (int i = 0; i < numWalls; i++) {
			double xPosition, yPosition, width, height;
			do {
				xPosition = Math.random() * SCREEN_WIDTH;
				yPosition = Math.random() * SCREEN_HEIGHT;
				width = 0.005 * SCREEN_WIDTH + Math.random() * 0.25 * SCREEN_WIDTH;
				height = 0.005 * SCREEN_WIDTH + Math.random() * 0.25 * SCREEN_WIDTH;
			} while (system.wallPositionOccupied(xPosition, yPosition, width, height));
			system.addWall(new Wall(xPosition, yPosition, width, height));
		}
		
		// add some particles with random positions and random properties
		int numParticles = 50;
		for (int i = 0; i < numParticles; i++) {
			double xPosition, yPosition, mass, radius;
			do {
				xPosition = 0.005 * SCREEN_WIDTH + Math.random() * 0.95 * SCREEN_WIDTH;
				yPosition = 0.005 * SCREEN_HEIGHT + Math.random() * 0.95 * SCREEN_HEIGHT;
				mass = 0.01 * (1 + Math.random());
				radius = (float) (Math.sqrt(mass) * 200);
			} while (system.particlePositionOccupied(xPosition, yPosition, radius));
			system.addParticle(new Particle(xPosition, yPosition, mass, radius)); // fix overlap in spawn (TODO)
		}
	}
	
	@Override
	public void update(double tslf) {
		system.update(tslf);
	}

	@Override
	public void draw(Graphics graphics) {
		// refresh the background
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		// draw the particles and walls
		system.draw(graphics);
		
		// draw text (fps and kinetic energy)
		graphics.setColor(Color.BLACK);
		graphics.drawString("FPS: " + getFPS(), 10, 10);
		DecimalFormat df = new DecimalFormat("#.#####");
		graphics.drawString("Energy: " + df.format(system.getKineticEnergySum()), 100, 10);
	}
}
