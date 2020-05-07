package me.jedimastersoda.oden;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import lombok.Setter;

public class Oden extends JavaPlugin implements Listener {

  @Getter private OdenThread odenThread;
  @Getter private OdenDatabase odenDatabase;
  @Getter private OdenRandomForest odenRandomForest;
  @Getter @Setter private boolean kickCheaters = false;

  @Getter private HashMap<UUID, Long> playerStartTime = new HashMap<>();

  public void onEnable() {
    this.odenDatabase = new OdenDatabase(this);
    this.odenThread = new OdenThread(this);

    try {
      ArrayList<OdenCollectionData> dataArrayList = this.odenDatabase.loadAllData();
      this.odenRandomForest = new OdenRandomForest(dataArrayList);
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.getServer().getPluginManager().registerEvents(this, this);

    this.getCommand("oden").setExecutor(new OdenCommand(this));
  }

  public void kickPlayer(Player player) {
    new BukkitRunnable() {
      @Override
      public void run() {
        if(player.isOnline()) {
          getLogger().info(player.getName() + " was kicked for cheating.");
          player.kickPlayer(ChatColor.RED + "You were kicked for cheating.");
        }
      }
    }.runTaskLater(this, 1L);
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Location location = event.getPlayer().getLocation().clone();
    OdenData odenData = new OdenData(location.getX(), location.getY(), location.getZ());

    this.playerStartTime.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
    this.odenThread.playerDataMap.put(event.getPlayer(), odenData);
  }

  @EventHandler
  public void onPlayerTeleport(PlayerTeleportEvent event) {
    double xDistance = Math.abs(event.getTo().getX() - event.getFrom().getX());
    double yDistance = Math.abs(event.getTo().getY() - event.getFrom().getY());
    double zDistance = Math.abs(event.getTo().getZ() - event.getFrom().getZ());

    OdenData data = this.odenThread.playerDataMap.get(event.getPlayer());

    data.setXTeleported(data.getXTeleported() + xDistance);
    data.setYTeleported(data.getYTeleported() + yDistance);
    data.setZTeleported(data.getZTeleported() + zDistance);
  }

  @EventHandler
  public void onBlockPlace(BlockPlaceEvent event) {
    OdenData data = this.odenThread.playerDataMap.get(event.getPlayer());

    data.setBlocksPlaced(data.getBlocksPlaced() + 1);
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    OdenData data = this.odenThread.playerDataMap.get(event.getPlayer());

    data.setBlocksBreaked(data.getBlocksBreaked() + 1);
  }

  @EventHandler
  public void onBlockBreak(EntityDamageByEntityEvent event) {
    if(event.getDamager() instanceof Player) {
      Player player = (Player) event.getDamager();

      OdenData data = this.odenThread.playerDataMap.get(player);

      data.setSwordHits(data.getSwordHits() + 1);
    }
  }

  @EventHandler
  public void onFlightToggle(PlayerToggleFlightEvent event) {
    if(event.isFlying()) {
      OdenData data = this.odenThread.playerDataMap.get(event.getPlayer());

      data.setHasFlyed(true);
    }
  }

  @EventHandler
  public void onSprintToggle(PlayerToggleSprintEvent event) {
    OdenData data = this.odenThread.playerDataMap.get(event.getPlayer());

    if(event.isSprinting()) {
      data.startSprint();
    } else {
      data.stopSprint();
    }
  }

  @EventHandler
  public void onSneakToggle(PlayerToggleSneakEvent event) {
    OdenData data = this.odenThread.playerDataMap.get(event.getPlayer());

    if(event.isSneaking()) {
      data.startSneak();
    } else {
      data.stopSneak();
    }
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    this.odenThread.playerDataMap.remove(event.getPlayer());
    this.playerStartTime.remove(event.getPlayer().getUniqueId());
  }
}