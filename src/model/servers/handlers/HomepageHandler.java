package model.servers.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Random;
import java.io.File;

public class HomepageHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка стартовой страницы.");
        exchange.getResponseHeaders().set("Content-Type", "image/gif");
        ArrayList<File> homepages = new ArrayList<>();
        homepages.add(new File("src/model/servers/images/homepage1.png"));
        FileInputStream fileInputStream = new FileInputStream(homepages.get(0));

        try (fileInputStream; OutputStream os = exchange.getResponseBody()) {
            var response = fileInputStream.readAllBytes();
            exchange.sendResponseHeaders(200, 0);
            os.write(response);
        }
    }
}