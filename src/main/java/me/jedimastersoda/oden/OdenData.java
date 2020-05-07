package me.jedimastersoda.oden;

import lombok.Getter;
import lombok.Setter;

public class OdenData {

  private double xInitial;
  private double yInitial;
  private double zInitial;

  @Getter @Setter private int blocksPlaced = 0;
  @Getter @Setter private int blocksBreaked = 0;
  @Getter @Setter private int swordHits = 0;

  @Getter private double xTravelled;
  @Getter private double yTravelled;
  @Getter private double zTravelled;

  @Getter @Setter private double xTeleported;
  @Getter @Setter private double yTeleported;
  @Getter @Setter private double zTeleported;

  @Getter @Setter private boolean hasFlyed = false;

  @Getter private double timeSneakedMs = 0;
  @Getter private double timeSprintedMs = 0;

  private long sneakStart = 0;
  private long sprintStart = 0;

  public OdenData(double x, double y, double z) {
    this.setLocation(x, y, z);
  }

  public void setLocation(double x, double y, double z) {
    this.xInitial = x;
    this.yInitial = y;
    this.zInitial = z;

    this.blocksPlaced = 0;
    this.blocksBreaked = 0;
    this.swordHits = 0;

    this.xTeleported = 0;
    this.yTeleported = 0;
    this.zTeleported = 0;

    this.hasFlyed = false;

    this.timeSneakedMs = 0;
    this.timeSprintedMs = 0;

    if(this.sneakStart > 0) {
      this.startSneak();
    }

    if(this.sprintStart > 0) {
      this.startSprint();
    }
  }

  public void startSneak() {
    this.sneakStart = System.currentTimeMillis();
  }

  public void stopSneak() {
    if(this.sneakStart <= 0) {
      return;
    }

    this.timeSneakedMs += (System.currentTimeMillis() - this.sneakStart);

    this.sneakStart = 0;
  }

  public void startSprint() {
    this.sprintStart = System.currentTimeMillis();
  }

  public void stopSprint() {
    if(this.sprintStart <= 0) {
      return;
    }

    this.timeSprintedMs += (System.currentTimeMillis() - this.sprintStart);

    this.sprintStart = 0;
  }

  public void setNextLocation(double x, double y, double z) {
    this.xTravelled = Math.abs(x - this.xInitial) - this.xTeleported;
    this.yTravelled = Math.abs(y - this.yInitial) - this.yTeleported;
    this.zTravelled = Math.abs(z - this.zInitial) - this.zTeleported;

    if(this.sneakStart > 0) {
      this.timeSneakedMs += (System.currentTimeMillis() - this.sneakStart);
    }

    if(this.sprintStart > 0) {
      this.timeSprintedMs += (System.currentTimeMillis() - this.sprintStart);
    }
  }
}