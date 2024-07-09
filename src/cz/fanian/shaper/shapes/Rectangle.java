package cz.fanian.shaper.shapes;

import cz.fanian.shaper.InsufficientDataException;
import cz.fanian.shaper.InvalidShapeException;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

public class Rectangle extends Shape {
    public double width;
    public double height;

    public Rectangle(double width, double height) {
        setType(Type.RECTANGLE);
        this.width = width;
        this.height = height;
    }

    protected static Rectangle fromUninitialized(UninitializedShape scratchpad) throws InvalidShapeException, InsufficientDataException {
        var s = scratchpad;
        // this is kind of obvious

        if (s.height == null || s.width == null) {
            if (s.width != null) {
                if (s.area != null)
                    s.height = s.area / s.width;
                else if (s.circumference != null) s.height = (s.circumference / 2) - s.width;
                else throw new InsufficientDataException("not enough properties to get rectangle width");
            } else if (s.height != null) {
                if (s.area != null) s.width = s.area / s.height;
                else if (s.circumference != null) s.width = (s.circumference / 2) - s.height;
                else throw new InsufficientDataException("not enough properties to get rectangle height");
            } else {
                throw new InsufficientDataException("no sides given for rectangle");
            }
        }
        if (s.circumference == null)
            s.circumference = s.width * 2 + s.height * 2;
        else if (s.area == null) s.area = s.width * s.height;
        else if (compareWithTolerance(s.area, s.width * s.height) != 0
                || compareWithTolerance(s.circumference, 2 * (s.width + s.height)) != 0)
            throw new InvalidShapeException("invalid properties for rectangle");
        return new Rectangle(s.width, s.height);
    }

    public double getArea() {
        return width * height;
    }

    public double getPerimeter() {
        return 2 * (width + height);
    }

    @Override
    public java.awt.Shape to2D() {
        return new Rectangle2D.Double(0, 0, width, height);
    }

    @Override
    public Map<String, String> properties() {
        var map = new HashMap<String, String>();
        map.put("width", String.valueOf(width));
        map.put("height", String.valueOf(height));
        map.put("area", String.valueOf(getArea()));
        map.put("perimeter", String.valueOf(getPerimeter()));
        return map;
    }
}
