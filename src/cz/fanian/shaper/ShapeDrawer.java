package cz.fanian.shaper;

import cz.fanian.shaper.shapes.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * A custom component for drawing AWT shapes in Swing.
 */
public class ShapeDrawer extends JComponent {
    java.awt.Shape currentShape;

    public ShapeDrawer() {
        super();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        if (currentShape != null) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.black);
            g2d.setBackground(Color.white);

            Rectangle2D bounds = currentShape.getBounds2D();

            double scale = this.getWidth() / Double.max(Double.max(bounds.getWidth(), bounds.getHeight()), 1);
            // scale to 1, or scale to fit if shape is larger (it still shouldn't be)

            g2d.scale(scale, scale);

            g2d.fill(currentShape);
        } else {
            g2d.clearRect(0, 0, getWidth(), getHeight());
        }
    }

    /**
     * Draws the given shape.
     *
     * @param shape Shape to draw.
     */
    public void draw(Shape shape) {
        currentShape = shape.to2D();
        this.repaint();
    }

    public void clear() {
        this.currentShape = null;
        this.repaint();
    }
}
