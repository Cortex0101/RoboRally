package com.roborally.RESTapi;

import com.roborally.fileaccess.LoadBoard;
import com.roborally.model.Board;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BoardService {

    public List<String>getBoards() {
        return getFileNames(System.getProperty("user.dir") + "\\src\\main\\resources\\com\\roborally\\boards\\");
    }

    public Board getBoard(String boardName) {
        Board board = LoadBoard.loadBoardFromFile(boardName);
        return board;
    }

    //TODO might need restructuring
    private List<String> getFileNames(String directory) {
        File[] fileArray = new File(directory).listFiles(File::isFile);
        List<String> fileNames = new ArrayList<>();
        for (File file : Objects.requireNonNull(fileArray)) {
            fileNames.add(file.getName().substring(0, file.getName().length() - 5));
        }
        return fileNames;
    }

    public void addBoard(int playerNumber, int AINumber, LoadBoard.DefaultBoard board, String boardName) {
        LoadBoard.BoardConfig config = new LoadBoard.BoardConfig(playerNumber, AINumber);
        LoadBoard.saveBoard(LoadBoard.loadDefaultBoard(config, board), boardName);
    }
    
    public boolean deleteBoard(String boardName){
        List<String> list = getFileNames(System.getProperty("user.dir") + "\\src\\main\\resources\\com\\roborally\\boards\\");
        for (String var : list) {
            if (var.equals(boardName)){
                File f = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\com\\roborally\\boards\\"+boardName+".json");
                f.delete();
                return true;
            }
        }
        return false;
    }
}
