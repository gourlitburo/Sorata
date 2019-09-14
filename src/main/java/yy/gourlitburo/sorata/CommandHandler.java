package yy.gourlitburo.sorata;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

class CommandHandler implements CommandExecutor {
  private static Main plugin;

  CommandHandler(Main instance) { plugin = instance; }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 0) return false;
    String subcommand = args[0];
    Player player = (Player) sender;
    if (subcommand.equalsIgnoreCase("tp-all")) {
      plugin.teleportAll(player, null);
      return true;
    } else if (subcommand.equalsIgnoreCase("tp-all-type") && args.length == 2) {
      plugin.teleportAll(player, args[1]);
      return true;
    } else if (subcommand.equalsIgnoreCase("list-all") || (subcommand.equalsIgnoreCase("list-all-type") && args.length == 2)) {
      String typeRequirement = subcommand.equalsIgnoreCase("list-all") ? null : args[1];
      List<Tameable> list = plugin.getPlayerTameables(player, typeRequirement);
      StringBuilder sb = new StringBuilder();
      for (Tameable tameable : list) {
        String typeName = plugin.getClassShortName(tameable.getClass().getName()).replaceAll("^Craft", "");
        Location location = tameable.getLocation();
        long x = Math.round(location.getX());
        long y = Math.round(location.getY());
        long z = Math.round(location.getZ());
        long distance = Math.round(location.distance(player.getLocation()));
        sb.append(String.format("%s at %d, %d, %d (distance %d blocks)", typeName, x, y, z, distance));
        return true;
      }
      player.sendMessage(sb.toString());
    }
    return false;
  }
}
