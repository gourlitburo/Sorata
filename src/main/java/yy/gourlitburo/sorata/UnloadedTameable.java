package yy.gourlitburo.sorata;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Tameable;

class UnloadedTameable {
  public UUID uuid;
  public AnimalTamer owner;
  public Location location;
  public String className;
  public String name;

  UnloadedTameable(Tameable tameable) {
    this.uuid = tameable.getUniqueId();
    this.owner = tameable.getOwner();
    this.location = tameable.getLocation();
    this.className = tameable.getClass().getName();
    this.name = tameable.getCustomName();
  }
}
