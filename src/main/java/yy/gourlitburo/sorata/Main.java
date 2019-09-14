package yy.gourlitburo.sorata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
  Logger logger = getLogger();

  private boolean belongsToPlayer(Tameable tameable, Player player) {
    return tameable.isTamed() && tameable.getOwner().getUniqueId().equals(player.getUniqueId());
  }

  private boolean nameMatches(String a, String b) {
    return a.equalsIgnoreCase(b) || a.equalsIgnoreCase("Craft" + b);
  }

  String getClassShortName(String className) {
    String[] parts = className.split("\\.");
    return parts[parts.length - 1];
  }

  List<Tameable> getPlayerTameables(Player player, String classShortNameRequirement) {
    World world = player.getWorld();
    Collection<Tameable> tameables = world.getEntitiesByClass(Tameable.class);
    List<Tameable> result = new ArrayList<>();
    for (Tameable tameable : tameables) {
      String classShortName = getClassShortName(tameable.getClass().getName());
      if (
        (classShortNameRequirement == null || nameMatches(classShortName, classShortNameRequirement))
        && belongsToPlayer(tameable, player)
      ) {
        result.add(tameable);
      }
    }
    return result;
  }

  void teleportAll(Player player, String classShortNameRequirement) {
    for (Tameable tameable : getPlayerTameables(player, classShortNameRequirement)) {
      String classShortName = getClassShortName(tameable.getClass().getName());
      boolean teleported = tameable.teleport(player);
      if (teleported) {
        logger.info(String.format("Teleported to %s their %s.", player.getName(), classShortName));
      } else {
        logger.info(String.format("Failed to teleport to %s their %s.", player.getName(), classShortName));
      }
    }
  }

  @Override
  public void onEnable() {
    getCommand("sorata").setExecutor(new CommandHandler(this));
    logger.info("Sorata ready.");
  }
}
