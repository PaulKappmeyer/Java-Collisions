package main;

import java.awt.Color;
import java.awt.Graphics;

import engine.MyMath;
import engine.Vector2D;

public class Line {

	private final Vector2D point1;
	private final Vector2D point2;
	private final Vector2D normal;

	public Line(double x1, double y1, double x2, double y2) {
		this.point1 = new Vector2D(x1, y1);
		this.point2 = new Vector2D(x2, y2);
		double angle = Math.atan2(y2 - y1, x2 - x1) + Math.PI / 2;
		this.normal = new Vector2D(Math.cos(angle), Math.sin(angle));
	}

	public void draw(Graphics graphics) {
		graphics.setColor(Color.BLACK);
		graphics.drawLine((int) point1.getX(), (int) point1.getY(), (int) point2.getX(), (int) point2.getY());
	}

	// -------------------------- collision helper function
	// returns the closest point on a line to a given point
	public static Vector2D getClosestPoint(Line line, Vector2D point, boolean infiniteLines) {
		double x0 = point.getX();
		double y0 = point.getY();

		double lx1 = line.getX1();
		double ly1 = line.getY1();
		double lx2 = line.getX2();
		double ly2 = line.getY2();

		// calculate help variables
		double a = ly2 - ly1;
		double b = lx1 - lx2;
		double c1 = a * lx1 + b * ly1;
		double c2 = -b * x0 + a * y0;

		// if det == 0 the point is on the line, and thus the closest point on the line is the point itself
		double det = Math.pow(a, 2) + Math.pow(b, 2);
		if (det == 0) {
			return point;
		}

		double cx = (a * c1 - b * c2) / det;
		double cy = (a * c2 + b * c1) / det;

		// if lines extend infinitely return the point
		if (infiniteLines == true) {
			return new Vector2D(cx, cy);
		} 

		// otherwise clamp values to the line
		cx = MyMath.clamp(cx, Math.min(lx1, lx2), Math.max(lx1, lx2));
		cy = MyMath.clamp(cy, Math.min(ly1, ly2), Math.max(ly1, ly2));
		return new Vector2D(cx, cy);
	}

	// -------------------------- getters
	public Vector2D getPoint1() {
		return new Vector2D(point1);
	}

	public double getX1() {
		return point1.getX();
	}

	public double getY1() {
		return point1.getY();
	}

	public Vector2D getPoint2() {
		return new Vector2D(point2);
	}

	public double getX2() {
		return point2.getX();
	}

	public double getY2() {
		return point2.getY();
	}

	public Vector2D getNormal() {
		return new Vector2D(normal);
	}
}
