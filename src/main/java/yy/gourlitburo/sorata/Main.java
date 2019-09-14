package yy.gourlitburo.sorata;

import java.util.Collection;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
  Logger logger = getLogger();

  String getClassShortName(String className) {
    logger.info(className);
    String[] parts = className.split("\\.");
    return parts[parts.length - 1];
  }

  boolean belongsToPlayer(Tameable tameable, Player player) {
    return tameable.isTamed() && tameable.getOwner().getUniqueId().equals(player.getUniqueId());
  }

  void teleportAll(Player player, String classShortNameRequirement) {
    World world = player.getWorld();
    Collection<Tameable> tameables = world.getEntitiesByClass(Tameable.class);
    for (Tameable tameable : tameables) {
      String classShortName = getClassShortName(tameable.getClass().getName());
      if (
        (classShortNameRequirement == null || classShortNameRequirement.equalsIgnoreCase(classShortName))
        && belongsToPlayer(tameable, player)
      ) {
        boolean teleported = tameable.teleport(player);
        if (teleported) {
          logger.info(String.format("Teleported %s's %s to player.", player.getName(), classShortName));
        }
      }
    }
  }

  @Override
  public void onEnable() {
    getCommand("sorata").setExecutor(new CommandHandler(this));
    logger.info("Sorata ready.");
  }
}
