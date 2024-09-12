package main;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import engine.Vector2D;

public class SimulationSystem {

	private List<Wall> walls;
	private ArrayList<Particle> particles;
	private double kineticEnergySum;
	
	public boolean elasticCollisions = true; // true means don't use collision damping
	private final double WALL_COLLISION_DAMPING_FACTOR = 0.98;		// percentage of speed remaining for a particle after a wall collision
	private final double PARTICLE_COLLISION_DAMPING_FACTOR = 0.99; 	// percentage of speed remaining for both particles after a collision 
	
	private boolean running = false;
	
	public SimulationSystem() {
		walls = new ArrayList<Wall>();
		particles = new ArrayList<Particle>();
	}
	
	private void wallCollisions() {
		for (int i = 0; i < particles.size(); i++) {
			Particle particle = particles.get(i);
			for (int j = 0; j < walls.size(); j++) {
				Wall wall = walls.get(j);
				
				// figure out the closest point on the edge of the rectangle to the particle
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
				// check if particle intersect with wall (use squared distances to improve performance)
				if (distanceSq < Math.pow(radius, 2)) {		
					// change velocity and position of particle
					Vector2D velocity = particle.getVelocity();
					double newVelocityX = velocity.getX();
					double newVelocityY = velocity.getY();
					if (connection.getX() != 0) {
						newVelocityX *= -1;
					}
					if (connection.getY() != 0) {
						newVelocityY *= -1;
					}
					Vector2D newVelocity = new Vector2D(newVelocityX, newVelocityY);
					if (elasticCollisions) {
						particle.setVelocity(newVelocity);
					} else {
						particle.setVelocity(newVelocity.multiply(WALL_COLLISION_DAMPING_FACTOR)); // optimize handling of damping (TODO)
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
	
	private void particleCollisions() {
		for (int i = 0; i < particles.size(); i++) {
			Particle particleA = particles.get(i);
			for (int j = i + 1; j < particles.size(); j++) { // improve loops, maybe quadtree (TODO)
				Particle particleB = particles.get(j);
				
				// get both positions
				Vector2D positionA = particleA.getPosition();
				Vector2D positionB = particleB.getPosition();
				Vector2D impactVector = positionA.sub(positionB);
				double distanceSq = impactVector.lengthSq();
				double radiusSum = particleA.getRadius() + particleB.getRadius();
				
				// check if particles intersect (use squared distances to improve performance)
				if (distanceSq < Math.pow(radiusSum, 2)) {
					// variables for calculating the new velocities
					double massSum = particleA.getMass() + particleB.getMass();
					Vector2D velocityA = particleA.getVelocity();
					Vector2D velocityB = particleB.getVelocity();
					Vector2D velocityDifference = velocityA.sub(velocityB);
					double dotProduct = velocityDifference.dot(impactVector);
					
					// calculating new velocities for particle A and B
					Vector2D newVelocityA = velocityA.sub(impactVector.multiply((2 * particleB.getMass() * dotProduct) / (massSum * distanceSq)));
					Vector2D newVelocityB = velocityB.add(impactVector.multiply((2 * particleA.getMass() * dotProduct) / (massSum * distanceSq)));
					if (elasticCollisions) {
						particleA.setVelocity(newVelocityA);
						particleB.setVelocity(newVelocityB); 
					} else {
						particleA.setVelocity(newVelocityA.multiply(PARTICLE_COLLISION_DAMPING_FACTOR)); // optimize handling of damping (TODO)
						particleB.setVelocity(newVelocityB.multiply(PARTICLE_COLLISION_DAMPING_FACTOR)); 
					}
					
					// move particles apart (TODO)
					double distance = Math.sqrt(distanceSq);
					double overlap = distance - radiusSum;
					Vector2D offset = impactVector.multiply((0.5 * overlap) / distance);
					particleA.setPosition(positionA.sub(offset));
					particleB.setPosition(positionB.add(offset));
				}
			}
		}
	}
	
	// -------------------------- (TODO) overhaul generation and addtion of elements
	
	public void addWall(Wall wall) {
		walls.add(wall);
	}
	
	public void addParticle(Particle particle) {
		particles.add(particle);
	}
	
	// checks if the given circle intersects with any other particle or wall already present
	// this method is priorary for the initial placing of the walls
	// (TODO) find an alternative solution (maybe confusion with collision detection)
	public boolean particlePositionOccupied(double x, double y, double radius) {
		radius = radius + 5; // particles shouldn't be packed so tight together
		Vector2D position = new Vector2D(x, y);
		for (Particle p : particles) {
			if (p.getPosition().distance(position) <= p.getRadius() + radius) {
				return true;
			}
		}
		for (Wall w : walls) {
			if (w.getX() <= x + radius && x - radius <= w.getX() + w.getWidth() && w.getY() <= y + radius && y - radius <= w.getY() + w.getHeight()) {
				return true;
			}
		}
		return false;
	}
	
	// checks if the given rectangle intersects with any wall already present
	// this method is priorary for the initial placing of the walls
	// (TODO) find an alternative solution (maybe confusion with collision detection)
	public boolean wallPositionOccupied(double x, double y, double width, double height) {
		for (Wall w : walls) {
			if (w.getX() <= x + width && x <= w.getX() + w.getWidth() && w.getY() <= y + height && y <= w.getY() + w.getHeight()) {
				return true;
			}
		}
		return false;
	}
	
	// adds 4 walls around a given rectangle
	public void addBoundaryWalls(double x, double y, double width, double height, double wallWidth) {
		addWall(new Wall(x, y - wallWidth, width, wallWidth));
		addWall(new Wall(x + width, y, wallWidth, height));
		addWall(new Wall(x, y + height, width, wallWidth));
		addWall(new Wall(x - wallWidth, y, wallWidth, height));
	}
	
	// adds walls at random positions inside a given rectangular area
	public void spawnWalls(double xSpawn, double ySpawn, double widthSpawn, double heightSpawn, int numWalls) {
		for (int i = 0; i < numWalls; i++) {
			double xPosition, yPosition, width, height;
			do {
				xPosition = xSpawn + Math.random() * widthSpawn;
				yPosition = ySpawn + Math.random() * heightSpawn;
				width = 10 + Math.random() * 190;
				height = 10 + Math.random() * 190;
			} while (wallPositionOccupied(xPosition, yPosition, width, height));
			addWall(new Wall(xPosition, yPosition, width, height));
		}
	}
	
	// adds particles at with random properties and positions inside a given rectangular area
	public void spawnParticles(double xSpawn, double ySpawn, double widthSpawn, double heightSpawn, int numParticles) {
		for (int i = 0; i < numParticles; i++) {
			double xPosition, yPosition, mass, radius;
			do {
				xPosition = xSpawn + Math.random() * widthSpawn;
				yPosition = ySpawn + Math.random() * heightSpawn;
				mass = 0.01 * (1 + Math.random());
				radius = (float) (Math.sqrt(mass) * 200);
			} while (particlePositionOccupied(xPosition, yPosition, radius));
			Vector2D velocity = Vector2D.randomDirectionvector().multiply((Math.random() + 1) * 200);
			addParticle(new Particle(xPosition, yPosition, velocity.getX(), velocity.getY(), mass, radius)); // fix overlap in spawn (TODO)
		}
	}
	
	
	// -------------------------- update
	
	private double startTimer; // (TODO) experimental
	
	public void update(double tslf) {
		if (running == false) {
			startTimer += tslf;
			if (startTimer > 1) {
				running = true;
			}
			return;
		}
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
	
	// -------------------------- draw
	
	public void draw(Graphics graphics) {
		// draw the walls
		for (Wall wall : walls) {
			wall.draw(graphics);
		}
		
		// draw the particles
		for (Particle particle : particles) {
			particle.draw(graphics);
		}
	}
	
	public double getKineticEnergySum() {
		return kineticEnergySum;
	}
}
