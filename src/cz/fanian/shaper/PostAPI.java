package cz.fanian.shaper;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import cz.fanian.shaper.shapes.Shape;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PostAPI implements ShapeAPI, Closeable {
    private HttpClient client = HttpClient.newHttpClient();
    private URI endpointUri;

    public PostAPI(URI endpointUri) {
        this.endpointUri = endpointUri;
    }

    public Shape getShape() throws IOException, ShapeException {
        try {
            HttpResponse<String> stream =
                    client.send(HttpRequest
                                    .newBuilder(endpointUri)
                                    .POST(HttpRequest.BodyPublishers.noBody())
                                    .build(),
                            HttpResponse.BodyHandlers.ofString());
            // probably more efficient for short json than InputStream
            return Shape.shapeFromJson(new JsonReader(new StringReader(stream.body())));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        client.close();
    }
}
