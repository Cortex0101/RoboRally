package com.roborally.fileaccess.model;

import java.util.ArrayList;
import java.util.List;

public class BoardTemplate {

  public int width;
  public int height;
  public String name = "default";

  public final List<SpaceTemplate> spaces = new ArrayList<>();
}
