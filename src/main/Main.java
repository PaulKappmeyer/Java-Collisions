package main;

import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;

import engine.GameBase;
import engine.Vector2D;

public class Main extends GameBase {

	// size of the window (in pixel)
	public static final int SCREEN_WIDTH = 1200;
	public static final int SCREEN_HEIGHT = 900;
	
	private Particle[] particles;
	private double kineticEnergySum;
	
	public static void main(String[] args) {
		Main main = new Main();
		main.start("Elastic Collisions", SCREEN_WIDTH, SCREEN_HEIGHT);
	}
	
	@Override
	public void init() {
		// create some particles with random positions and random properties
		int numParticles = 50;
		particles = new Particle[numParticles];
		for (int i = 0; i < numParticles; i++) {
			double xPosition = 0.05 * SCREEN_WIDTH + Math.random() * 0.9 * SCREEN_WIDTH;
			double yPosition = 0.05 * SCREEN_HEIGHT + Math.random() * 0.9 * SCREEN_HEIGHT;
			particles[i] = new Particle(xPosition, yPosition);
		}
	}

	@Override
	public void update(double tslf) {
		// update the particles and kinetic energy of the system
		kineticEnergySum = 0;
		for (Particle particle : particles) {
			particle.update(tslf);
			kineticEnergySum += particle.getKineticEnergy();
		}
		
		// check for particles collisions
		for (int i = 0; i < particles.length; i++) {
			Particle particleA = particles[i];
			for (int j = i + 1; j < particles.length; j++) {
				Particle particleB = particles[j];
				
				Vector2D impactVector = particleA.getPosition().sub(particleB.getPosition());
				double distanceSq = impactVector.lengthSq();
				double radiusSum = particleA.getRadius() + particleB.getRadius();
				if (distanceSq < Math.pow(radiusSum, 2)) {
					// variables for calculating the new velocities
					double massSum = particleA.getMass() + particleB.getMass();
					Vector2D velocityA = particleA.getVelocity();
					Vector2D velocityB = particleB.getVelocity();
					Vector2D velocityDifference = velocityA.sub(velocityB);
					double dotProduct = velocityDifference.dot(impactVector);
					
					// calculating new velocity for particle A
					Vector2D newVelocityA = velocityA.sub(impactVector.multiply((2 * particleB.getMass() * dotProduct) / (massSum * distanceSq)));
					particleA.setVelocity(newVelocityA); // add damping factor (TODO)
					
					// calculating new velocity for particle B
					Vector2D newVelocityB = velocityB.add(impactVector.multiply((2 * particleA.getMass() * dotProduct) / (massSum * distanceSq)));
					particleB.setVelocity(newVelocityB); // add damping factor (TODO)
					
					// move particles apart (TODO)
					double distance = Math.sqrt(distanceSq);
					double overlap = distance - radiusSum;
					Vector2D offset = impactVector.multiply((0.5 * overlap) / distance);
					particleA.setPosition(particleA.getPosition().sub(offset));
					particleB.setPosition(particleB.getPosition().add(offset));
				}
			}
		}
	}

	@Override
	public void draw(Graphics graphics) {
		// refresh the background
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		// draw the particles
		for (Particle particle : particles) {
			particle.draw(graphics);
		}
		
		// draw text (fps and kinetic energy)
		graphics.setColor(Color.BLACK);
		graphics.drawString("FPS: " + getFPS(), 10, 10);
		DecimalFormat df = new DecimalFormat("#.#####");
		graphics.drawString("Energy: " + df.format(kineticEnergySum), 100, 10);
	}
}
