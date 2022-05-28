package com.roborally.fileaccess.model;

import java.util.ArrayList;
import java.util.List;

public class BoardTemplate {

  public int width;
  public int height;

  public final List<SpaceTemplate> spaces = new ArrayList<>();
}
