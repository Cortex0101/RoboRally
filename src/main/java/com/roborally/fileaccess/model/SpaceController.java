package com.roborally.fileaccess.model;

import com.roborally.model.Heading;
import com.roborally.model.Space;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class SpaceController {
  private List<SpaceTemplate> spaces;

  SpaceController(List<SpaceTemplate> spaces) {
    this.spaces = spaces;
  }

  @GetMapping("/spaces")
  List<SpaceTemplate> all() {
    return spaces;
  }

  @GetMapping("/spaces/{x}/{y}")
  SpaceTemplate one(@PathVariable String x, @PathVariable String y) {
    return spaces.stream()
        .filter(space -> space.x == Integer.parseInt(x) && space.y == Integer.parseInt(y))
        .findFirst().orElse(null);
  }

  @PostMapping("/spaces")
  SpaceTemplate newSpace(@RequestBody SpaceTemplate newSpace) {
    spaces.add(newSpace);
    return newSpace;
  }

  @PutMapping("/spaces/{x}/{y}")
  SpaceTemplate replaceSpace(@RequestBody SpaceTemplate newSpace, @PathVariable String x, @PathVariable String y) {
    SpaceTemplate space = one(x, y);
    if (space != null) {
      space.walls.clear();
      space.walls.addAll(newSpace.walls);
      space.actions.clear();
      space.actions.addAll(newSpace.actions);
      space.player = newSpace.player;
    }
    return space;
  }

  @DeleteMapping("/players/{x}/{y}")
  void deletePlayer(@PathVariable String x, @PathVariable String y) {
    SpaceTemplate space = one(x, y);
    if (space != null) {
      space = null;
    }
  }

  public static void main(String[] args) {
    SpringApplication.run(SpaceController.class, args);
  }
}
