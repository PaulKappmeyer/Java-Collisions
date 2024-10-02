package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;

import engine.GameBase;

public class Main extends GameBase implements KeyListener {

	// size of the window (in pixel)
	public static final int SCREEN_WIDTH = 1200;
	public static final int SCREEN_HEIGHT = 900;
	
	public static final boolean SHOW_FPS = true;
	public static final boolean SHOW_TOTAL_ENERGY = true;
	
	private SimulationSystem system;
	
	public static void main(String[] args) {
		Main main = new Main();
		main.start("Main: testing and experimenting", SCREEN_WIDTH, SCREEN_HEIGHT);
	}
	
	@Override
	public void init() {
		window.addKeyListener(this);
		
		system = new SimulationSystem();
		system.elasticCollisions = true;
		Particle.SHOW_DIRECTION = false;
		
		// add hidden boundary walls
		system.addBoundaryWalls(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, 1);
		
		// add random walls
		system.spawnWalls(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, 10);
		
		// add some particles with random positions and random properties
		system.spawnParticles(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, 50);
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
		if (SHOW_FPS) {
			graphics.setColor(Color.BLACK);
			graphics.drawString("FPS: " + getFPS(), 10, 10);
		}
		if (SHOW_TOTAL_ENERGY) {
			DecimalFormat df = new DecimalFormat("#.#####");
			graphics.drawString("Energy: " + df.format(system.getKineticEnergySum()), 100, 10);
		}
	}

	
	// -------------------------- keyboard input
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			system.toggleRunning();
		}
	}
}
