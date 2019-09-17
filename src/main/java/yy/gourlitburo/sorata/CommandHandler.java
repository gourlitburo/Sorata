package yy.gourlitburo.sorata;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

class CommandHandler implements CommandExecutor {
  private static Main plugin;

  CommandHandler(Main instance) { plugin = instance; }

  private String getDisplayedClassName(String fullClassName) {
    return plugin.getClassShortName(fullClassName).replaceAll("^Craft", "");
  }

  private String formatOwnedTameablesList(Player player, List<Object> tameableObjs) {
    List<String> lines = new ArrayList<>();
    for (Object tameableObj : tameableObjs) {
      String typeName;
      Location location;
      String name;
      String worldName;
      String unloadedComponent;
      if (tameableObj instanceof Tameable) {
        Tameable tameable = (Tameable) tameableObj;
        typeName = getDisplayedClassName(tameable.getClass().getName());
        location = tameable.getLocation();
        name = tameable.getCustomName();
        worldName = tameable.getWorld().getName();
        unloadedComponent = "";
      } else { // tameableObj instanceof UnloadedTameable
        UnloadedTameable unloadedTameable = (UnloadedTameable) tameableObj;
        typeName = getDisplayedClassName(unloadedTameable.className);
        location = unloadedTameable.location;
        name = unloadedTameable.name;
        worldName = Bukkit.getWorld(unloadedTameable.worldUUID).getName();
        unloadedComponent = " [unloaded]";
      }
      long x = Math.round(location.getX());
      long y = Math.round(location.getY());
      long z = Math.round(location.getZ());
      long distance = Math.round(location.distance(player.getLocation()));
      String nameComponent = name == null ? "" : String.format(" (%s)", name);
      lines.add(String.format("%s%s at %d, %d, %d in world '%s' (distance %d blocks)%s", typeName, nameComponent, x, y, z, worldName, distance, unloadedComponent));
    }
    if (lines.isEmpty()) return "No owned tameables.";
    else return String.join("\n", lines);
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 0) return false;
    if (!(sender instanceof Player)) {
      sender.sendMessage("Only players can use Sorata.");
      return true;
    }
    String subcommand = args[0];
    Player player = (Player) sender;
    if (subcommand.equalsIgnoreCase("tp-all")) {
      plugin.teleportAll(player, null);
      return true;
    } else if (subcommand.equalsIgnoreCase("tp-type") && args.length == 2) {
      plugin.teleportAll(player, args[1]);
      return true;
    } else if (subcommand.equalsIgnoreCase("list-all") || (subcommand.equalsIgnoreCase("list-type") && args.length == 2)) {
      String typeRequirement = subcommand.equalsIgnoreCase("list-all") ? null : args[1];
      List<Object> list = plugin.getPlayerTameables(player, typeRequirement);
      player.sendMessage(formatOwnedTameablesList(player, list));
      return true;
    }
    return false;
  }
}
