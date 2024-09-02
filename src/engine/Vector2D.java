package engine;

public class Vector2D {

	private double x;
	private double y;
	
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
	
	public double distance(Vector2D other) {
		return Math.sqrt((x - other.getX()) * (x - other.getX()) + (y - other.getY()) * (y - other.getY()));
	}
	
	public double dot(Vector2D other) {
		return x * other.getX() + y * other.getY();
	}
	
	public Vector2D add(Vector2D other) {
		return add(other.getX(), other.getY());
	}
	
	public Vector2D add(double xIncrement, double yIncrement) {
		this.x += xIncrement;
		this.y += yIncrement;
		return this;
	}
	
	public Vector2D sub(Vector2D other) {
		return add(other.multiply(-1));
	}
	
	public Vector2D sub(double xIncrement, double yIncrement) {
		this.x -= xIncrement;
		this.y -= yIncrement;
		return this;
	}
	
	public Vector2D multiply(double scalar) {
		this.x *= scalar;
		this.y *= scalar;
		return this;
	}
	
	public double lengthSq() {
		return x * x + y * y;
	}
	
	public double length() {
		return Math.sqrt(lengthSq());
	}
	
	// overhaul getters and setters (TODO)
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
}
