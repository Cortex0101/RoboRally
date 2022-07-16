package com.roborally.fileaccess.model;

import com.roborally.RESTapi.KmkmkmApplication;
import com.roborally.fileaccess.LoadBoard;
import com.roborally.model.Board;
import com.roborally.model.Heading;
import java.util.List;
import java.util.Objects;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class PlayerController {
  private List<PlayerTemplate> players;

  PlayerController(List<PlayerTemplate> players) {
    PlayerTemplate playerTemplate1 = new PlayerTemplate();
    playerTemplate1.name = "player1";
    playerTemplate1.color = "red";
    playerTemplate1.heading = Heading.EAST;
    playerTemplate1.AI = false;

    PlayerTemplate playerTemplate2 = new PlayerTemplate();
    playerTemplate2.name = "player2";
    playerTemplate2.color = "blue";
    playerTemplate2.heading = Heading.EAST;
    playerTemplate2.AI = false;

    PlayerTemplate playerTemplate3 = new PlayerTemplate();
    playerTemplate3.name = "player3";
    playerTemplate3.color = "green";
    playerTemplate3.heading = Heading.EAST;
    playerTemplate3.AI = false;
    players.addAll(List.of(playerTemplate1, playerTemplate2, playerTemplate3));
    this.players = players;
  }

  @GetMapping("/players")
  List<PlayerTemplate> all() {
    return players;
  }

  @GetMapping("/players/{name}")
  PlayerTemplate one(@PathVariable String name) {
    return players.stream()
        .filter(player -> player.name.equals(name))
        .findFirst().orElse(null);
  }

  @PostMapping("/players")
  PlayerTemplate newPlayer(@RequestBody PlayerTemplate newPlayer) {
    players.add(newPlayer);
    return newPlayer;
  }

  @PutMapping("/players/{name}")
  PlayerTemplate replacePlayer(@RequestBody PlayerTemplate newPlayer, @PathVariable String name) {
    PlayerTemplate player = players.stream()
        .filter(player1 -> player1.name.equals(name))
        .findFirst().orElse(null);
    player = newPlayer;
    return player;
  }

  @DeleteMapping("/players/{name}")
  void deletePlayer(@PathVariable String name) {
    players.removeIf(player -> player.name.equals(name));
  }

  public static void main(String[] args) {
    PlayerTemplate playerTemplate1 = new PlayerTemplate();
    playerTemplate1.name = "player1";
    playerTemplate1.color = "red";
    playerTemplate1.heading = Heading.EAST;
    playerTemplate1.AI = false;

    PlayerTemplate playerTemplate2 = new PlayerTemplate();
    playerTemplate2.name = "player2";
    playerTemplate2.color = "blue";
    playerTemplate2.heading = Heading.EAST;
    playerTemplate2.AI = false;

    PlayerTemplate playerTemplate3 = new PlayerTemplate();
    playerTemplate3.name = "player3";
    playerTemplate3.color = "green";
    playerTemplate3.heading = Heading.EAST;
    playerTemplate3.AI = false;

    SpringApplication.run(PlayerController.class, args);
  }
}
