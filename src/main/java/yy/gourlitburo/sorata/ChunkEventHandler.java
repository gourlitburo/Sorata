package yy.gourlitburo.sorata;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

class ChunkEventHandler implements Listener {
  private static Main plugin;

  ChunkEventHandler(Main instance) { plugin = instance; }

  private void handle(Entity[] entities, boolean isLoad) {
    for (Entity entity : entities) {
      if (entity instanceof Tameable && ((Tameable) entity).isTamed()) {
        if (isLoad) plugin.removeUnloadedTameable(entity.getUniqueId());
        else plugin.addUnloadedTameable((Tameable) entity);
      }
    }
  }

  @EventHandler
  public void onChunkUnload(ChunkUnloadEvent event) {
    handle(event.getChunk().getEntities(), false);
  }

  @EventHandler
  public void onChunkLoad(ChunkLoadEvent event) {
    handle(event.getChunk().getEntities(), true);
  }
}
