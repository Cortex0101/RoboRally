package com.roborally.fileaccess.model;

import com.roborally.fileaccess.LoadBoard;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class BoardController {
  private Map<String, String> boards;

  /**
   * Constructor.
   * Loads the contents of each board in the boards folder into the boards list.
   */
  BoardController() {
    this.boards = new HashMap<>();
    try {
      Files.walk(Paths.get(LoadBoard.SAVED_BOARDS_PATH)).forEach(path -> {
        if (Files.isRegularFile(path) && !Files.isDirectory(path)) {
          try {
            String boardName = path.toString().substring(path.toString().indexOf("boards") + "boards".length(), path.toString().length() - 5);
            this.boards.put(boardName, LoadBoard.getBoardContent(boardName));
            System.out.println(path);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @GetMapping("/boards")
  List<String> all() {
    return boards.values().stream().toList();
  }

  @GetMapping("/boards/{name}")
  String one(@PathVariable String name) {
    return boards.getOrDefault("\\" + name, "404, board not found");
  }

  @PutMapping("/boards/{name}")
  String update(@PathVariable String name, @RequestBody String board) {
    boards.put(name, board);
    return "200, board updated";
  }

  @PostMapping("/boards")
  String newBoard(@RequestBody String newBoard, @RequestBody String newBoardName) {
    boards.put(newBoardName, newBoard);
    return newBoard;
  }

  @DeleteMapping("/boards/{name}")
  void deleteBoard(@PathVariable String name) {
    boards.remove(name);
  }

  enum Action {
    POST,
    PUT,
    DELETE,
    GET
  }

  public static void sendRequest(Action action, String url, String body) throws IOException, InterruptedException {
    HttpClient client = HttpClient.newBuilder()
        .version(Version.HTTP_1_1)
        .connectTimeout(Duration.ofSeconds(10))
        .build();

    HttpRequest request = null;

    switch (action) {
      case POST:
        request = HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .uri(URI.create(url))
            .build();
        break;
      case PUT:
        request = HttpRequest.newBuilder()
            .PUT(HttpRequest.BodyPublishers.ofString(body))
            .uri(URI.create(url))
            .build();
        System.out.println(request);
        break;
      case DELETE:
        request = HttpRequest.newBuilder()
            .DELETE()
            .uri(URI.create(url))
            .build();
        System.out.println(request);
        break;
      case GET:
        request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(url))
            .setHeader("Content-Type", "application/json")
            .build();
        System.out.println(request);
        break;
    }

    client.sendAsync(request, BodyHandlers.ofString()).thenApply(HttpResponse::body).join();//.thenAccept(System.out::println).join();
  }

  public static void main(String[] args) {
    SpringApplication.run(BoardController.class, args);

    String URI = "http://localhost:8080/boards";

    try {
      //sendRequest(URI, "");
      sendRequest(Action.GET, URI + "/x", "");
      sendRequest(Action.PUT, URI + "/x", "{ width: 10 }");
      sendRequest(Action.GET, URI + "/x", "");
      sendRequest(Action.GET, URI + "/x", "");
      sendRequest(Action.DELETE, URI + "/x", "");
      Thread.sleep(1000);
      sendRequest(Action.GET, URI + "/x", "");
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }
}
