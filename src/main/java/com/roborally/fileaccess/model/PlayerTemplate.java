package com.roborally.fileaccess.model;

import com.roborally.model.Command;
import com.roborally.model.CommandCard;
import com.roborally.model.Heading;

import java.util.ArrayList;
import java.util.List;

public class PlayerTemplate {
    public String name;
    public String color;
    public Heading heading;
    public boolean AI;
    public List<Command> commandCards = new ArrayList<>(8);
    public List<Command> commandCardsInRegisters = new ArrayList<>(5);
}
