package cz.fanian.shaper;

import javax.swing.*;
import java.io.IOException;
import java.net.URI;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        String endpoint = System.getenv("SHAPER_ENDPOINT");
        ShapeAPI api;
        if (endpoint == null) {
            var message = "SHAPER_ENDPOINT environment variable not set: falling back to test case API";
            JOptionPane.showMessageDialog(null,
                    message,
                    "Warning", JOptionPane.WARNING_MESSAGE);
            System.out.println(message);

            api = NdJsonAPI.open("testData.ndjson");
        } else {
            api = new PostAPI(URI.create(endpoint));
        }

        var gui = new Shaper(api);
        gui.pack();
        gui.setVisible(true);

        api.close();
        System.exit(0);
    }
}