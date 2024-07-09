package cz.fanian.shaper;

import com.google.gson.Strictness;
import com.google.gson.stream.JsonToken;
import cz.fanian.shaper.shapes.Shape;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.stream.Collectors;

/**
 * an NDJSON-based ShapeAPI, reading from a file or a string source.
 */
public class NdJsonAPI implements ShapeAPI {
    private String source;
    private JsonReader jsonReader;

    /**
     * Reads NDJSON from the given reader
     *
     * @param reader reader providing the NDJSON stream
     */
    public NdJsonAPI(Reader reader) {
        try {
            // I hope there's a better way to read a file
            // otherwise it's _most elegant j*va code_
            try (BufferedReader file = new BufferedReader(reader)) {
                source = file.lines().collect(Collectors.joining("\n"));
            }

            reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads from the given path.
     *
     * @param path Path to file with NDJSON text.
     */
    public static NdJsonAPI open(String path) {
        try {
            return new NdJsonAPI(new FileReader(path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("shapes source file `%s` not found. cannot continue", path));
        }
    }

    private void reset() {
        jsonReader = new JsonReader(new StringReader(source));
        jsonReader.setStrictness(Strictness.LENIENT);
    }

    @Override
    public Shape getShape() throws ShapeException, IOException {
        if (jsonReader.peek() == JsonToken.END_DOCUMENT)
            reset();

        return Shape.shapeFromJson(jsonReader);
    }

    @Override
    public void close() {
    }
}
