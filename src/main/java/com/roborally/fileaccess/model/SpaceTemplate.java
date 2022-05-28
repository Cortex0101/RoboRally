package com.roborally.fileaccess.model;

import com.roborally.controller.FieldAction;
import com.roborally.model.Heading;

import java.util.ArrayList;
import java.util.List;

public class SpaceTemplate {
    public List<Heading> walls = new ArrayList<>();
    public List<FieldAction> actions = new ArrayList<>();

    public int x;
    public int y;

    public PlayerTemplate player;
}
