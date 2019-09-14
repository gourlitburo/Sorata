package yy.gourlitburo.sorata;

import java.util.Collection;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
  Logger logger = getLogger();

  void teleportAll(Player player) {
    World world = player.getWorld();
    Collection<Tameable> tameables = world.getEntitiesByClass(Tameable.class);
    for (Tameable tameable : tameables) {
      if (tameable.getOwner().getUniqueId().equals(player.getUniqueId())) {
        boolean teleported = tameable.teleport(player);
        if (teleported) {
          logger.info(String.format("Teleported %s's %s to player.", player.getName(), tameable.getClass().getName()));
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
