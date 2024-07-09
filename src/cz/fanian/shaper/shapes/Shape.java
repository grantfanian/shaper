package cz.fanian.shaper.shapes;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import cz.fanian.shaper.InsufficientDataException;
import cz.fanian.shaper.InvalidShapeException;
import lombok.Getter;

import java.util.Map;

public abstract class Shape {
    private final static double epsilon = 1e-7; // tolerance

    private UninitializedShape originalShape;

    private Type type = Type.UNSPECIFIED;

    protected static int compareWithTolerance(double a, double b) {
        return Math.abs(a - b) > epsilon ? Double.compare(a, b) : 0;
    }

    public static Shape shapeFromJson(JsonReader reader) throws InvalidShapeException, InsufficientDataException {
        UninitializedShape source = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
                .fromJson(reader, UninitializedShape.class);

        var type = identify(source);

        Shape result;

        var originalShape = source.clone();
        switch (type) {
            case CIRCLE -> result = Circle.fromUninitialized(source);
            case TRIANGLE -> result = Triangle.fromUninitialized(source);
            case RECTANGLE -> result = Rectangle.fromUninitialized(source);
            default -> throw new InvalidShapeException("unknown shape");
        }
        result.originalShape = originalShape;
        return result;
    }

    private static Type identify(UninitializedShape shape) {
        var type = shape.type;
        if (shape.type == Type.UNSPECIFIED) {
            if (shape.a != null || shape.b != null || shape.c != null) {
                type = Type.TRIANGLE;
            } else if (shape.width != null || shape.height != null) {
                type = Type.RECTANGLE;
            } else {
                // could try all known shapes by area too
                type = Type.CIRCLE;
            }
        }
        return type;
    }


    public Type getType() {
        return this.type;
    }

    protected void setType(Type type) {
        this.type = type;
    }

    public abstract java.awt.Shape to2D();

    public abstract Map<String, String> properties();

    public Map<String, String> info() {
        var properties = properties();
        properties.put("type", getType().toString());
        return properties;
    }

    public Map<String, String> originalInfo() {
        return originalShape.properties();
    }
}

