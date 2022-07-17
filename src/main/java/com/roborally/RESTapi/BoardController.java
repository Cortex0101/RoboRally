package com.roborally.RESTapi;

import java.util.List;

import com.roborally.model.Board;
import com.roborally.model.Space;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("boards")
@RestController
public class BoardController {
    private final BoardService boardService;
    @Autowired
    BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    //GET for boards
    @GetMapping
    public List<String> getBoards() {
        return boardService.getBoards();
    }

    //POST for boards
    //TODO - fix
    //@PostMapping
    //void newBoard(@RequestBody Integer playerNumber, @RequestBody Integer AINumber, @RequestBody LoadBoard.DefaultBoard board, @RequestBody String boardName) {
        //boardService.addBoard(playerNumber, AINumber, board, boardName);
    {

    }

    //GET for boards/{id}
    @GetMapping(path = "{boardName}")
    Board getBoard(@PathVariable("boardName") String boardName) {
        return boardService.getBoard(boardName);
        }


    //PUT for boards/{id}
    //TODO - fix
    @PutMapping(path = "{boardName}")
    void updateBoard(Board board) {}

    //DELETE for boards/{id}
    @DeleteMapping(path = "{boardName}")
    boolean deleteBoard(@PathVariable("boardName") String boardName) {
        return boardService.deleteBoard(boardName);
    }
}
