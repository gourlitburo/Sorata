package yy.gourlitburo.sorata;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandHandler implements CommandExecutor {
  private static Main plugin;

  CommandHandler(Main instance) { plugin = instance; }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 0) return false;
    if (args[0].equalsIgnoreCase("tp-all")) {
      plugin.teleportAll((Player) sender);
      return true;
    }
    return false;
  }
}
