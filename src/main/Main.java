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
	
	private Wall[] walls;
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
			particles[i] = new Particle(xPosition, yPosition); // fix overlap in spawn (TODO)
		}
		
		// create some obstacles
		int numWalls = 10;
		walls = new Wall[numWalls];
		// hidden boundary walls
		walls[0] = new Wall(0, -10, SCREEN_WIDTH, 10);
		walls[1] = new Wall(SCREEN_WIDTH, 0, 10, SCREEN_HEIGHT);
		walls[2] = new Wall(0, SCREEN_HEIGHT, SCREEN_WIDTH, 10);
		walls[3] = new Wall(-10, 0, 10, SCREEN_HEIGHT);
		// random walls inside view
		for (int i = 4; i < numWalls; i++) {
			double xPosition = 0.05 * SCREEN_WIDTH + Math.random() * 0.9 * SCREEN_WIDTH;
			double yPosition = 0.05 * SCREEN_HEIGHT + Math.random() * 0.9 * SCREEN_HEIGHT;
			double width = 0.05 * SCREEN_WIDTH + Math.random() * 0.1 * SCREEN_WIDTH;
			double height = 0.05 * SCREEN_WIDTH + Math.random() * 0.1 * SCREEN_WIDTH;
			walls[i] = new Wall(xPosition, yPosition, width, height); // fix overlap in spawn (TODO)
		}
	}
	
	@Override
	public void update(double tslf) {
		// check for wall collisions
		wallCollisions();
		
		// check for particles collisions
		particleCollisions();
		
		// update the particles and kinetic energy of the system
		kineticEnergySum = 0;
		for (Particle particle : particles) {
			particle.update(tslf);
			kineticEnergySum += particle.getKineticEnergy();
		}
	}
	
	public void wallCollisions() {
		for (int i = 0; i < particles.length; i++) {
			Particle particle = particles[i];
			for (int j = 0; j < walls.length; j++) {
				Wall wall = walls[j];
				
				// figure out the closest point on the edge of the rectangle
				double testX = particle.getX(); 
				double testY = particle.getY();
				
				if (particle.getX() < wall.getX()) {
					testX = wall.getX();
				} else if (particle.getX() > wall.getX() + wall.getWidth()) {
					testX = wall.getX() + wall.getWidth();
				}
				
				if (particle.getY() < wall.getY()) {
					testY = wall.getY();
				} else if (particle.getY() > wall.getY() + wall.getHeight()) {
					testY = wall.getY() + wall.getHeight();
				}
				
				Vector2D closestPoint = new Vector2D(testX, testY);
				Vector2D connection = particle.getPosition().sub(closestPoint);
				double distanceSq = connection.lengthSq();
				double radius = particle.getRadius();
				if (distanceSq < Math.pow(radius, 2)) {		
					// change velocity and position of particle
					Vector2D velocity = particle.getVelocity();
					if (connection.getX() != 0) {
						particle.setVelocity(new Vector2D(-velocity.getX(), velocity.getY())); // add damping factor (TODO)
					}
					if (connection.getY() != 0) {
						particle.setVelocity(new Vector2D(velocity.getX(), -velocity.getY())); // add damping factor (TODO)
					}
					
					// move particle out of rectangle (TODO)
					double distance = Math.sqrt(distanceSq);
					if (distance > 0) { // fix case where particle inside rectangle (TODO)
						particle.setPosition(closestPoint.add(connection.multiply(radius / distance)));
					}
				}
			}
		}
	}
	
	public void particleCollisions() {
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
		
		// draw the walls
		for (Wall wall : walls) {
			wall.draw(graphics);
		}
		
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
