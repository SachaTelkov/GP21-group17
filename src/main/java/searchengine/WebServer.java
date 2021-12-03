package searchengine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;


public class WebServer {
  static final int PORT = 8080;
  static final int BACKLOG = 0;
  static final Charset CHARSET = StandardCharsets.UTF_8;

  List<Page> allPages = new ArrayList<>();
  HttpServer server;

  public WebServer(int port) throws IOException {
    server = HttpServer.create(new InetSocketAddress(port), BACKLOG);
    server.createContext("/", io -> respond(io, 200, "text/html", getFile("web/index.html")));
    server.createContext("/search", io -> takesSearchTermFromServer(io));
    server.createContext(
        "/favicon.ico", io -> respond(io, 200, "image/x-icon", getFile("web/favicon.ico")));
    server.createContext(
        "/code.js", io -> respond(io, 200, "application/javascript", getFile("web/code.js")));
    server.createContext(
        "/style.css", io -> respond(io, 200, "text/css", getFile("web/style.css")));
    server.start();
    String msg = " WebServer running on http://localhost:" + port + " ";
    System.out.println("╭"+"─".repeat(msg.length())+"╮");
    System.out.println("│"+msg+"│");
    System.out.println("╰"+"─".repeat(msg.length())+"╯");
  }
  
  // This method gets the searchterm, from which a user has typed in as a request: 
 public void takesSearchTermFromServer(HttpExchange io) { 
    var searchTerm = io.getRequestURI().getRawQuery().split("=")[1];
    var response = new ArrayList<String>();
    // Vi skal få Search_term-klassen til at tale sammen med severen her: 
    for (var page : search(searchTerm)) {
      response.add(String.format("{\"url\": \"%s\", \"title\": \"%s\"}",
      // Vi skal bruge get-metoderne fra vores Page class i næste linje: 
        //page.get(0).substring(6), page.get(1)));
        allPages.get(io.getUrl());
    }
    var bytes = response.toString().getBytes(CHARSET);
    respond(io, 200, "application/json", bytes);
  }


  // This method gets the file out from the Paths written in our constructor: 
 public byte[] getFile(String filename) {
    try {
      return Files.readAllBytes(Paths.get(filename));
    } catch (IOException e) {
      e.printStackTrace();
      return new byte[0];
    }
  }

  //This method gives the user a respond to the search request:  
  public void respond(HttpExchange io, int code, String mime, byte[] response) { //example of 'bad' code naming: mime
    try {
      io.getResponseHeaders()
          .set("Content-Type", String.format("%s; charset=%s", mime, CHARSET.name()));
      io.sendResponseHeaders(200, response.length);
      io.getResponseBody().write(response);
    } catch (Exception e) {
    } finally {
      io.close();
    }
  }

  public static void main(final String... args) throws IOException {
    Files.readString(Paths.get("config.txt")).strip();
    new WebServer(PORT);
  }
}