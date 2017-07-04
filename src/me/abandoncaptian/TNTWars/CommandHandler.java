package me.abandoncaptian.TNTWars;

import org.bukkit.Bukkit;
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
		if(commandLabel.equalsIgnoreCase("tw")) {
			if (theSender instanceof Player) {
				Player p = (Player) theSender;
				if (args.length == 0) {
					pl.mh.openMainMenu(p);
				}else if(p.isOp() || p.getName().contains("abandoncaptian")){
					if(args[0].equalsIgnoreCase("reload")){
						pl.reload();
						pl.reloadTwo();
					}
				}else{
					p.sendMessage("§cInvalid Arguments, Usage: §b/tw");
				}
			}
		}
		if(commandLabel.equalsIgnoreCase("mod") || commandLabel.equalsIgnoreCase("mods")) {
			if (theSender instanceof Player) {
				Player p = (Player) theSender;
				if(p.hasPermission("tntwars.mod") || p.hasPermission("tntwars.admin") || p.hasPermission("tntwars.owner")){
					if(args.length == 0){
						p.sendMessage("§cYou cannot send a blank message to the Mod's staff chat");
						return true;
					}else{
						String message = "§c";
						for(String text: args){
							message += (text + " ");
						}
						for(Player player: Bukkit.getOnlinePlayers()){
							if(player.hasPermission("tntwars.mod")){
								player.sendMessage("§7[§6Mods§7] " + p.getName() + "§7: " + message);
							}else continue;
						}
						return true;
					}
				}else{
					p.sendMessage("§fUnkown command");
				}
			}
		}
		if(commandLabel.equalsIgnoreCase("admin") || commandLabel.equalsIgnoreCase("admins")) {
			if (theSender instanceof Player) {
				Player p = (Player) theSender;
				if(p.hasPermission("tntwars.admin") || p.hasPermission("tntwars.owner")){
					if(args.length == 0){
						p.sendMessage("§cYou cannot send a blank message to the Admin's staff chat");
						return true;
					}else{
						String message = "§c";
						for(String text: args){
							message += (text + " ");
						}
						for(Player player: Bukkit.getOnlinePlayers()){
							if(player.hasPermission("tntwars.admin")){
								player.sendMessage("§7[§6Admins§7] " + p.getName() + "§7: " + message);
							}else continue;
						}
						return true;
					}
				}else{
					p.sendMessage("§fUnkown command");
				}
			}
		}
		if(commandLabel.equalsIgnoreCase("owner") || commandLabel.equalsIgnoreCase("owners")) {
			if (theSender instanceof Player) {
				Player p = (Player) theSender;
				if(p.hasPermission("tntwars.owner")){
					if(args.length == 0){
						p.sendMessage("§cYou cannot send a blank message to the Owner's staff chat");
						return true;
					}else{
						String message = "§c";
						for(String text: args){
							message += (text + " ");
						}
						for(Player player: Bukkit.getOnlinePlayers()){
							if(player.hasPermission("tntwars.owner") || player.hasPermission("tntwars.RightHand")){
								player.sendMessage("§7[§6Owners§7] " + p.getName() + "§7: " + message);
							}else continue;
						}
						return true;
					}
				}else{
					p.sendMessage("§fUnkown command");
				}
			}
		}
		return true;
	}
}
