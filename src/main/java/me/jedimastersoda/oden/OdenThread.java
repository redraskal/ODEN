package me.jedimastersoda.oden;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class OdenThread extends BukkitRunnable {

  private final Oden oden;
  public boolean collectingData = false;
  public HashMap<Player, OdenData> playerDataMap = new HashMap<>();
  public HashMap<UUID, ArrayList<OdenCollectionData>> saveQueue = new HashMap<>();

  public OdenThread(Oden oden) {
    this.oden = oden;

    this.runTaskTimer(oden, 0, 60L);
  }

  @Override
  public void run() {
    playerDataMap.forEach((player, data) -> {
      long timeOnline = System.currentTimeMillis() - oden.getPlayerStartTime().get(player.getUniqueId());

      if(timeOnline > 3000L) {
        check(player, data);
      }
    });
  }

  public void setCheater(Player player, boolean cheating) {
    if (saveQueue.containsKey(player.getUniqueId())) {
      saveQueue.get(player.getUniqueId()).forEach(data -> data.setCheaterData(cheating));
    }
  }

  public void addToSaveQueue(OdenCollectionData data) {
    if(data.isEmpty()) {
      return;
    }

    if(saveQueue.containsKey(data.getUuid())) {
      saveQueue.get(data.getUuid()).add(data);
    } else {
      saveQueue.put(data.getUuid(), new ArrayList<>(Arrays.asList(data)));
    }
  }

  public void saveData() {
    ArrayList<OdenCollectionData> data = new ArrayList<>();

    saveQueue.values().forEach(dataArrayList -> data.addAll(dataArrayList));
    saveQueue.clear();

    try {
      oden.getOdenDatabase().createSave(data, "TESTSRV");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void check(Player player, OdenData data) {
    Location location = player.getLocation().clone();

    data.setNextLocation(location.getX(), location.getY(), location.getZ());

    OdenCollectionData collectionData = new OdenCollectionData(player.getUniqueId(), false, 
      data.getXTravelled(), data.getYTravelled(), data.getZTravelled(), 
      data.isHasFlyed(), data.getTimeSneakedMs(), data.getTimeSprintedMs(), 
      data.getBlocksPlaced(), data.getBlocksBreaked(), data.getSwordHits());
    
    // Save results
    if(collectingData) {
      addToSaveQueue(collectionData);
    } else if(!collectionData.isEmpty()) {
      // TODO: Test Player

      if(oden.getOdenRandomForest() != null) {
        boolean cheating = oden.getOdenRandomForest().test(collectionData);

        player.sendMessage("ODEN thinks: " + cheating);

        if(cheating && oden.isKickCheaters()) {
          oden.kickPlayer(player);
        }
      }
    }

    data.setLocation(location.getX(), location.getY(), location.getZ());

    if(player.isFlying()) {
      data.setHasFlyed(true);
    }
  }
}