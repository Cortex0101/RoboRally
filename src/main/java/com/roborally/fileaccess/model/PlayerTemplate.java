package com.roborally.fileaccess.model;

import com.roborally.model.Command;
import com.roborally.model.Heading;
import java.util.ArrayList;
import java.util.List;

public class PlayerTemplate {

  public String name;
  public String color;
  public Heading heading;
  public boolean AI;
  public final List<Command> commandCards = new ArrayList<>(8);
  public final List<Command> commandCardsInRegisters = new ArrayList<>(5);
}
