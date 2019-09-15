package yy.gourlitburo.sorata;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Tameable;

class UnloadedTameable {
  public UUID uuid;
  public UUID ownerUUID;
  public UUID worldUUID;
  public Location location;
  public String className;
  public String name;

  UnloadedTameable(Map<?, ?> map) {
    this.uuid = UUID.fromString((String) map.get("uuid"));
    this.ownerUUID = UUID.fromString((String) map.get("owner_uuid"));
    this.worldUUID = UUID.fromString((String) map.get("world_uuid"));
    this.className = (String) map.get("class_name");
    this.name = (String) map.get("name");

    World world = Bukkit.getWorld(this.worldUUID);
    double x = (double) map.get("location_x");
    double y = (double) map.get("location_y");
    double z = (double) map.get("location_z");
    this.location = new Location(world, x, y, z);
  }

  UnloadedTameable(Tameable tameable) {
    this.uuid = tameable.getUniqueId();
    this.ownerUUID = tameable.getOwner().getUniqueId();
    this.worldUUID = tameable.getWorld().getUID();
    this.location = tameable.getLocation();
    this.className = tameable.getClass().getName();
    this.name = tameable.getCustomName();
  }

  public Map<String, Object> toMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("uuid", uuid.toString());
    map.put("owner_uuid", ownerUUID.toString());
    map.put("world_uuid", worldUUID.toString());
    map.put("location_x", location.getX());
    map.put("location_y", location.getY());
    map.put("location_z", location.getZ());
    map.put("class_name", className);
    map.put("name", name);
    return map;
  }
}
