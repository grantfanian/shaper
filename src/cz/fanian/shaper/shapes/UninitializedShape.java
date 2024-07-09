package cz.fanian.shaper.shapes;

import lombok.Data;

import java.util.HashMap;

@Data
public class UninitializedShape {
    public Double circumference;
    public Double radius;
    public Double area;
    public Double a;
    public Double b;
    public Double c;
    public Double width;
    public Double height;
    public Type type = Type.UNSPECIFIED;

    public HashMap<String, String> properties() {
        var map = new HashMap<String, String>();
        for (var field : UninitializedShape.class.getDeclaredFields()) {
            try {
                var value = field.get(this);
                if (value instanceof Double d) {
                    map.put(field.getName(), d.toString());
                } else if (value instanceof Type t) {
                    map.put(field.getName(), t.toString());
                }
            } catch (IllegalAccessException e) {
                // can't happen
                throw new RuntimeException(e);
            }
        }
        return map;
    }

    @Override
    public UninitializedShape clone() {
        UninitializedShape cloned = new UninitializedShape();

        for (var field : UninitializedShape.class.getDeclaredFields()) {
            try {
                var value = field.get(this);
                field.set(cloned, value);
            } catch (IllegalAccessException e) {
                // can't happen
                throw new RuntimeException(e);
            }
        }
        return cloned;
    }


}
