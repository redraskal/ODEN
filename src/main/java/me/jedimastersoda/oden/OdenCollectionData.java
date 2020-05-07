package me.jedimastersoda.oden;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

public class OdenCollectionData {

  @Getter private final UUID uuid;
  @Getter @Setter private boolean cheaterData = false;
  
  @Getter private final double xTravelled;
  @Getter private final double yTravelled;
  @Getter private final double zTravelled;
  
  @Getter private final boolean hasFlyed;
  @Getter private final double timeSneakedMs;
  @Getter private final double timeSprintedMs;

  @Getter private final int blocksPlaced;
  @Getter private final int blocksBreaked;
  @Getter private final int swordHits;

  public OdenCollectionData(UUID uuid, boolean cheaterData, 
      double xTravelled, double yTravelled, double zTravelled, 
      boolean hasFlyed, double timeSneakedMs, double timeSprintedMs, 
      int blocksPlaced, int blocksBreaked, int swordHits) {
    this.uuid = uuid;
    this.cheaterData = cheaterData;
    this.xTravelled = xTravelled;
    this.yTravelled = yTravelled;
    this.zTravelled = zTravelled;
    this.hasFlyed = hasFlyed;
    this.timeSneakedMs = timeSneakedMs;
    this.timeSprintedMs = timeSprintedMs;
    this.blocksPlaced = blocksPlaced;
    this.blocksBreaked = blocksBreaked;
    this.swordHits = swordHits;
  }

  public boolean isEmpty() {
    return this.xTravelled == 0 && this.yTravelled == 0 && this.zTravelled == 0 
      && this.timeSneakedMs == 0 && this.timeSprintedMs == 0 
      && this.blocksPlaced == 0 && this.blocksBreaked == 0 && this.swordHits == 0;
  }

  @Override
  public String toString() {
    return this.uuid + "," + this.cheaterData + "," 
      + this.xTravelled + "," + this.yTravelled + "," + this.zTravelled 
      + "," + this.hasFlyed + "," + this.timeSneakedMs + "," + this.timeSprintedMs + "," 
      + this.blocksPlaced + "," + this.blocksBreaked + "," + this.swordHits;
  }

  public double[] conv() {
    return new double[]{
      this.xTravelled, this.yTravelled, this.zTravelled,
      this.hasFlyed ? 1 : 0, this.timeSneakedMs, this.timeSprintedMs,
      this.blocksPlaced, this.blocksBreaked, this.swordHits
    };
  }

  public static OdenCollectionData fromString(String string) {
    String[] parts = string.split(",");

    UUID uuid = UUID.fromString(parts[0]);
    boolean cheaterData = Boolean.valueOf(parts[1]);

    double xTravelled = Double.valueOf(parts[2]);
    double yTravelled = Double.valueOf(parts[3]);
    double zTravelled = Double.valueOf(parts[4]);

    boolean hasFlyed = Boolean.valueOf(parts[5]);
    double timeSneakedMs = Double.valueOf(parts[6]);
    double timeSprintedMs = Double.valueOf(parts[7]);
    
    int blocksPlaced = Integer.valueOf(parts[8]);
    int blocksBreaked = Integer.valueOf(parts[9]);
    int swordHits = Integer.valueOf(parts[10]);

    return new OdenCollectionData(uuid, cheaterData, 
      xTravelled, yTravelled, zTravelled, 
      hasFlyed, timeSneakedMs, timeSprintedMs, 
      blocksPlaced, blocksBreaked, swordHits);
  }
}