package cz.fanian.shaper;

import cz.fanian.shaper.shapes.Shape;

import static cz.fanian.shaper.util.MapFormatter.formatMap;

import javax.swing.*;
import java.io.IOException;

public class Shaper extends JDialog {
    private JPanel contentPane;
    private JTextPane originalPane;
    private JTextPane fixupPane;
    private JButton button;
    private ShapeDrawer shapeDrawer;
    private Shape currentShape;

    public Shaper(ShapeAPI api) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(button);
        button.addActionListener(e -> {
            try {
                currentShape = api.getShape();
                shapeDrawer.draw(currentShape);
                originalPane.setText(formatOriginalInfo(currentShape));
                fixupPane.setText(formatInfo(currentShape));
            } catch (IOException | ShapeException ex) {
                shapeDrawer.clear();
                originalPane.setText("");
                fixupPane.setText(ex.getMessage());
                ex.printStackTrace();
            }
        });
    }

    private String formatOriginalInfo(Shape shape) {
        return formatMap(shape.originalInfo());
    }

    private String formatInfo(Shape shape) {
        var info = shape.info();
        info.put("type", shape.getType().toString());
        return formatMap(info);
    }

}
