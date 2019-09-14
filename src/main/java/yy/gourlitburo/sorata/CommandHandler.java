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
    String subcommand = args[0];
    if (subcommand.equalsIgnoreCase("tp-all")) {
      plugin.teleportAll((Player) sender, null);
      return true;
    } else if (subcommand.equalsIgnoreCase("tp-all-type") && args.length == 2) {
      plugin.teleportAll((Player) sender, args[1]);
      return true;
    }
    return false;
  }
}
