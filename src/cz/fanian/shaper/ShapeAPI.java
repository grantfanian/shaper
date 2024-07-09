package cz.fanian.shaper;

import cz.fanian.shaper.shapes.Shape;

import java.io.Closeable;
import java.io.IOException;

public interface ShapeAPI extends Closeable {
    Shape getShape() throws IOException, ShapeException;
}
