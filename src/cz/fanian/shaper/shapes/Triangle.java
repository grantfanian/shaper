package cz.fanian.shaper.shapes;

import cz.fanian.shaper.InsufficientDataException;
import cz.fanian.shaper.InvalidShapeException;

import java.awt.geom.Path2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        if ((s.a == null && s.b == null)
                || (s.a == null && s.c == null)
                || (s.b == null && s.c == null))
            throw new InsufficientDataException("not enough sides info for a triangle");
        if (s.circumference != null) {
            if (s.a == null) s.a = s.circumference - s.b - s.c;
            else if (s.b == null) s.b = s.circumference - s.a - s.c;
            else if (s.c == null) s.c = s.circumference - s.a - s.b;
            else {
                if (compareWithTolerance(s.a + s.b + s.c, s.circumference) != 0)
                    throw new InvalidShapeException("given triangle perimeter doesn't match sides");
            }
        } else {
            if (s.a == null || s.b == null || s.c == null) {
                if (s.area == null)
                    throw new InsufficientDataException("can't restore one side without circumference or area");

                if (s.a == null)
                    s.a = getUnknownSide(s.b, s.c, s.area);
                else if (s.b == null)
                    s.b = getUnknownSide(s.a, s.c, s.area);
                else
                    s.c = getUnknownSide(s.a, s.b, s.area);
            }
            s.circumference = s.a + s.b + s.c;
        }

        // Heron's formula
        double myArea;
        double p = s.circumference / 2;
        myArea = sqrt(p * (p - s.a) * (p - s.b) * (p - s.c));

        if (s.area != null && compareWithTolerance(s.area, myArea) != 0)
            throw new InvalidShapeException("wrong triangle area");

        return new Triangle(s.a, s.b, s.c);
    }

    private static double getUnknownSide(double a, double b, double S) throws InvalidShapeException {
        // S = (ab * sin C) / 2
        // sin C = 2S / ab // if out of range, throw (invalid triangle)
        // c = ab sin C
        //
        // c^2 = a^2 + b^2 - 2ab cos C
        // cos^2 C + sin^2 C = 1
        //
        // cos C = (+-) sqrt((ab)^2 - 4S^2) / ab
        //
        // c = sqrt(a^2 + b^2 +- 2sqrt((ab)^2 - 4S^2))
        // ...I guess I'll just leave first valid solution and if all are invalid consider triangle invalid
        double cosC = sqrt(pow(a * b, 2) - 4 * S * S) / (a * b);

        double squares = a * a + b * b;

        double c = sqrt(squares + 2 * cosC);
        if (Double.isFinite(c)) return c;

        c = sqrt(squares - 2 * cosC);
        if (Double.isFinite(c)) return c;

        throw new InvalidShapeException("restored triangle is invalid");
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
        // ...
        // https://stackoverflow.com/a/4002584

        Path2D path = new Path2D.Double();
        path.moveTo(0, 0);
        path.lineTo(c, 0);
        double Cx = (a * a - b * b + c * c) / (2 * a);
        double Cy = sqrt(c * c - Cx * Cx);
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
