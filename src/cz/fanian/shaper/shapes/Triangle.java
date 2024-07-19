package cz.fanian.shaper.shapes;

import cz.fanian.shaper.InsufficientDataException;
import cz.fanian.shaper.InvalidShapeException;

import java.awt.geom.Path2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.*;

public class Triangle extends Shape {
    public double a;
    public double b;
    public double c;

    public Triangle(double a, double b, double c) throws InvalidShapeException {
        setType(Type.TRIANGLE);
        if ((b + c) <= a || (a + c) <= b || (a + b) <= c)
            throw new InvalidShapeException("invalid sides for triangle");
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * @param scratchpad - the source data that may be overwritten
     * @return A valid triangle
     * @throws InsufficientDataException if data is insufficient
     * @throws InvalidShapeException     if triangle is invalid
     */
    protected static Triangle fromUninitialized(UninitializedShape scratchpad) throws InvalidShapeException, InsufficientDataException {
        var s = scratchpad;
        if (s.circumference != null) {
            if ((s.a == null && s.b == null)
                    || (s.a == null && s.c == null)
                    || (s.b == null && s.c == null))
                throw new InsufficientDataException("not enough sides info for a triangle");

            if (s.a == null) s.a = s.circumference - s.b - s.c;
            else if (s.b == null) s.b = s.circumference - s.a - s.c;
            else if (s.c == null) s.c = s.circumference - s.a - s.b;
            else {
                if (compareWithTolerance(s.a + s.b + s.c, s.circumference) != 0)
                    throw new InvalidShapeException("given triangle perimeter doesn't match sides");
            }
        } else if (s.a == null || s.b == null || s.c == null) {
            throw new InvalidShapeException("can't restore sides without circumference");
        } else {
            s.circumference = s.a + s.b + s.c;
        }

        // Heron's formula
        double myArea;
        double p = s.circumference / 2;
        myArea = sqrt(p * (p - s.a) * (p - s.b) * (p - s.c));

        if (s.area == null) s.area = myArea;
        else if (compareWithTolerance(s.area, myArea) != 0)
            throw new InvalidShapeException("wrong triangle area");

        return new Triangle(s.a, s.b, s.c);
    }

    public double getPerimeter() {
        return a + b + c;
    }

    public double getArea() {
        double p = getPerimeter() / 2;
        return sqrt(p * (p - a) * (p - c) * (p - b));
    }

    @Override
    public java.awt.Shape to2D() {
        // SoE using circles (for valid triangles has two solutions for (x, y) coords of point C)
        // A, B - end points of the longest side c (base)
        // assume x>0, y>0 to isolate solutions
        // {
        //  x^2 + y^2 = b^2
        //  (x + c)^2 + y^2 = a^2
        // }
        // x^2 + a^2 - (x - c)^2 = b^2
        // => y = a^2 - (x - c)^2
        // => x = (b^2 + c^2 - a^2) / 2c
        Path2D path = new Path2D.Double();
        path.moveTo(0, 0);
        path.lineTo(c, 0);
        double Cx = (pow(b, 2) + pow(c, 2) - pow(a, 2)) / (2*c);
        double Cy = (pow(a, 2) - pow((Cx - c), 2)) * 2;
        path.lineTo(Cx, Cy);
        path.lineTo(0, 0);
        return path;
    }

    @Override
    public Map<String, String> properties() {
        var map = new HashMap<String, String>();
        map.put("a", String.valueOf(a));
        map.put("b", String.valueOf(b));
        map.put("c", String.valueOf(c));
        map.put("perimeter", String.valueOf(getPerimeter()));
        map.put("area", String.valueOf(getArea()));
        return map;
    }
}
