package engine;

/**
 * The {@code Vector2D} class represents an immutable vector/point in 2D space.
 * 
 * @author Paul
 */
public class Vector2D {
	// consider using java.awt.geom.Point2D.Double (TODO)
	
	private final double x;
	private final double y;
	
	// -------------------------- constructors
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2D(Vector2D other) {
		this(other.getX(), other.getY());
	}
	
	public static Vector2D randomDirectionvector() {
		double angle = Math.random() * 2 * Math.PI;
		return new Vector2D(Math.cos(angle), Math.sin(angle));
	}
	
	// -------------------------- addition
	public Vector2D add(Vector2D other) {
		return add(other.getX(), other.getY());
	}
	
	public Vector2D add(double xIncrement, double yIncrement) {
		return new Vector2D(x + xIncrement, y + yIncrement);
	}
	
	// -------------------------- subtraction
	public Vector2D sub(Vector2D other) {
		return sub(other.getX(), other.getY());
	}
	
	public Vector2D sub(double xDecrement, double yDecrement) {
		return new Vector2D(x - xDecrement, y - yDecrement);
	}
	
	// -------------------------- scalar multiplication
	public Vector2D multiply(double scalar) {
		return new Vector2D(x * scalar, y * scalar);
	}
	
	// -------------------------- length and distance
	public double lengthSq() {
		return x * x + y * y;
	}
	
	public double length() {
		return Math.sqrt(lengthSq());
	}
	
	public double distance(Vector2D other) {
		return Math.sqrt((x - other.getX()) * (x - other.getX()) + (y - other.getY()) * (y - other.getY()));
	}
	
	// -------------------------- dot product
	public double dot(Vector2D other) {
		return x * other.getX() + y * other.getY();
	}
	
	// -------------------------- getters
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
}
