package yy.gourlitburo.sorata;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
  private final String STORE_FILENAME = "store.yml";
  private File storeLocation = new File(getDataFolder(), STORE_FILENAME);

  Logger logger = getLogger();

  private List<UnloadedTameable> unloadedTameables = new ArrayList<>();

  boolean addUnloadedTameable(Tameable tameable) {
    return unloadedTameables.add(new UnloadedTameable(tameable));
  }

  boolean addUnloadedTameable(UnloadedTameable unloadedTameable) {
    return unloadedTameables.add(unloadedTameable);
  }

  boolean removeUnloadedTameable(UUID tameableUUID) {
    for (UnloadedTameable unloadedTameable : unloadedTameables) {
      if (unloadedTameable.uuid.equals(tameableUUID)) {
        return unloadedTameables.remove(unloadedTameable);
      }
    }
    return false;
  }

  private boolean belongsToPlayer(Tameable tameable, Player player) {
    return tameable.isTamed() && tameable.getOwner().getUniqueId().equals(player.getUniqueId());
  }

  private boolean belongsToPlayer(UnloadedTameable unloadedTameable, Player player) {
    return unloadedTameable.ownerUUID.equals(player.getUniqueId());
  }

  private boolean nameMatches(String a, String b) {
    return a.equalsIgnoreCase(b) || a.equalsIgnoreCase("Craft" + b);
  }

  String getClassShortName(String className) {
    String[] parts = className.split("\\.");
    return parts[parts.length - 1];
  }

  List<Object> getPlayerTameables(Player player, String classShortNameRequirement) {
    World world = player.getWorld();
    Collection<Tameable> tameables = world.getEntitiesByClass(Tameable.class);
    List<Object> result = new ArrayList<>();
    // TODO: dry
    for (Tameable tameable : tameables) {
      String classShortName = getClassShortName(tameable.getClass().getName());
      if (
        (classShortNameRequirement == null || nameMatches(classShortName, classShortNameRequirement))
        && belongsToPlayer(tameable, player)
      ) {
        result.add(tameable);
      }
    }
    for (UnloadedTameable unloadedTameable : unloadedTameables) {
      String classShortName = getClassShortName(unloadedTameable.className);
      if (
        (classShortNameRequirement == null || nameMatches(classShortName, classShortNameRequirement))
        && belongsToPlayer(unloadedTameable, player)
      ) {
        result.add(unloadedTameable);
      }
    }
    return result;
  }

  void teleportAll(Player player, String classShortNameRequirement) {
    for (Object tameableObj : getPlayerTameables(player, classShortNameRequirement)) {
      Tameable tameable;
      if (tameableObj instanceof Tameable) tameable = (Tameable) tameableObj;
      else continue;
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
    YamlConfiguration store = YamlConfiguration.loadConfiguration(storeLocation);
    List<Map<?, ?>> unloadedTameablesStore = store.getMapList("unloaded_tameables");
    for (Map<?, ?> unloadedTameableStore : unloadedTameablesStore) {
      // TODO: check world exists
      addUnloadedTameable(new UnloadedTameable(unloadedTameableStore));
    }

    /* register command and events */
    getCommand("sorata").setExecutor(new CommandHandler(this));
    Bukkit.getPluginManager().registerEvents(new ChunkEventHandler(this), this);

    /* hajimeyou, niisan! */
    logger.info("Sorata ready.");
  }

  // TODO: save unloadedTameables list to file
}
