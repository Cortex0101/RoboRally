package com.roborally.fileaccess.model;

import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class BoardController {
  private List<BoardTemplate> boards;

  BoardController(List<BoardTemplate> boards) {
    this.boards = boards;
  }

  @GetMapping("/boards")
  List<BoardTemplate> all() {
    return boards;
  }

  @GetMapping("/boards/{name}")
  BoardTemplate one(@PathVariable String name) {
    return boards.stream()
        .filter(board -> board.name.equals(name))
        .findFirst().orElse(null);
  }

  @PostMapping("/boards")
  BoardTemplate newBoard(@RequestBody BoardTemplate newBoard) {
    boards.add(newBoard);
    return newBoard;
  }

  @PutMapping("/spaces/{name}")
  BoardTemplate replaceBoard(@RequestBody BoardTemplate newBoard, @PathVariable String name) {
    BoardTemplate board = one(name);
    if (board != null) {
      board.width = newBoard.width;
      board.height = newBoard.height;
      board.name = newBoard.name;
      board.spaces.clear();
      board.spaces.addAll(newBoard.spaces);
    }
    return board;
  }

  @DeleteMapping("/boards/{name}")
  void deleteBoard(@PathVariable String name) {
    BoardTemplate board = one(name);
    if (board != null) {
      boards.remove(board);
    }
  }

  public static void main(String[] args) {
    SpringApplication.run(BoardController.class, args);
  }
}
