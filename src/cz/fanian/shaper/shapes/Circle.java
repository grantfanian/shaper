package cz.fanian.shaper.shapes;

import cz.fanian.shaper.InsufficientDataException;
import cz.fanian.shaper.InvalidShapeException;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.PI;

public class Circle extends Shape {
    public double radius;

    public Circle(double radius) {
        setType(Type.CIRCLE);
        this.radius = radius;
    }

    protected static Circle fromUninitialized(UninitializedShape scratchpad) throws InvalidShapeException, InsufficientDataException {
        var s = scratchpad;
        // r = circumference/2/pi
        // circumference = 2*pi*r
        // area = pi * r**2

        if (s.radius == null) {
            if (s.circumference != null) {
                s.radius = s.circumference / PI / 2;
            } else if (s.area != null) {
                s.radius = Math.sqrt(s.area / PI);
            } else {
                throw new InsufficientDataException("not enough data to get circle's radius");
            }
        }
        if (s.circumference == null) {
            s.circumference = s.radius * PI * 2;
        }
        if (s.area == null) {
            s.area = s.radius * s.radius * PI;
        }
        if (compareWithTolerance(2 * PI * s.radius, s.circumference) != 0
                || compareWithTolerance(PI * s.radius * s.radius, s.area) != 0) {
            throw new InvalidShapeException("invalid values for circle");
        }
        return new Circle(s.radius);
    }

    public double getCircumference() {
        return 2 * PI * radius;
    }

    public double getArea() {
        return PI * radius * radius;
    }

    @Override
    public java.awt.Shape to2D() {
        return new java.awt.geom.Ellipse2D.Double(0, 0, radius, radius);
    }

    @Override
    public Map<String, String> properties() {
        var map = new HashMap<String, String>();
        map.put("radius", String.valueOf(radius));
        map.put("area", String.valueOf(getArea()));
        map.put("circumference", String.valueOf(getCircumference()));
        return map;
    }
}
