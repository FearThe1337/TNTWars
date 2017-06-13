package me.abandoncaptian.TNTWars;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {
	Main pl;

	public CommandHandler(Main plugin) {
		pl = plugin;
	}

	@Override
	public boolean onCommand(CommandSender theSender, Command cmd, String commandLabel, String[] args) {
		if (commandLabel.equalsIgnoreCase("tw")) {
			if (theSender instanceof Player) {
				Player p = (Player) theSender;
				if (args.length == 0) {
					pl.mh.openMainMenu(p);
				}else{
					p.sendMessage("§cInvalid Arguments, Usage: §b/tw");
				}
			}
		}
		return true;
	}
}
