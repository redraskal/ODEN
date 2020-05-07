package me.jedimastersoda.oden;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OdenCommand implements CommandExecutor {

  private final Oden oden;

  public OdenCommand(Oden oden) {
    this.oden = oden;
  }

  @Override
  public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
    if(!arg0.isOp()) {
      arg0.sendMessage("no.");

      return false;
    }

    if(arg3.length > 0) {
      if(arg3[0].toLowerCase().equals("save")) {
        arg0.sendMessage("Saving data...");
        oden.getOdenThread().saveData();
        arg0.sendMessage("Data saved.");
      } else if(arg3[0].toLowerCase().equals("collect")) {
        oden.getOdenThread().collectingData = !oden.getOdenThread().collectingData;

        if(oden.getOdenThread().collectingData) {
          arg0.sendMessage("Now collecting data.");
        } else {
          oden.getOdenThread().saveQueue.clear();
          arg0.sendMessage("Stopped collecting data.");
        }
      } else if(arg3[0].toLowerCase().equals("kick")) {
        oden.setKickCheaters(!oden.isKickCheaters());

        if(oden.isKickCheaters()) {
          arg0.sendMessage("Now kicking cheaters.");
        } else {
          arg0.sendMessage("Stopped kicking cheaters.");
        }
      } else {
        Player player = Bukkit.getPlayer(arg3[0]);
        
        if(player != null) {
          oden.getOdenThread().setCheater(player, true);
          arg0.sendMessage("Player flagged as a cheater.");
        } else {
          arg0.sendMessage("Player not online.");
        }
      }
    } else {
      arg0.sendMessage("/oden <<username>|save|collect|kick>");
    }

    return true;
  }
}