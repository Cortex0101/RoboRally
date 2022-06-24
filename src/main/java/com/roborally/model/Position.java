package com.roborally.model;

import java.util.Objects;

public class Position {

  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public boolean is(int x, int y) {
    return this.x == x && this.y == y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Position position = (Position) o;
    return x == position.x && y == position.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public String toString() {
    return "Util.Position{" +
        "x=" + x +
        ", y=" + y +
        '}';
  }

  public int x;
  public int y;
}