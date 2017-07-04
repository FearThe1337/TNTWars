package me.abandoncaptian.TNTWars.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.abandoncaptian.TNTWars.Main;

public class PlayerChat implements Listener {
	Main pl;

	public PlayerChat(Main plugin) {
		pl = plugin;

	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void playerChat(AsyncPlayerChatEvent e){
		Player p = e.getPlayer();
		String message = e.getMessage();
		String chat = "";
		String prefix = pl.chat.getPlayerPrefix(p);
		String seperator = "§7: §f";
		if(prefix.equals("") || prefix.equals(" ")){
			chat += "§r";
		}else{
			chat += "§7[";
			chat += prefix;
			chat += "§7]";
			chat += " §b";
		}
		chat += p.getName();
		chat += seperator;
		if(pl.chat.getPrimaryGroup(p).contains("Owner")||pl.chat.getPrimaryGroup(p).contains("Admin")||pl.chat.getPrimaryGroup(p).contains("Mod")||pl.chat.getPrimaryGroup(p).contains("RightHand"))chat += "§b";
		chat += message;
		if(p.hasPermission("tntwars.color")){
			chat = chat.replaceAll("&", "§");
		}

		e.setCancelled(true);
		for(Player player: Bukkit.getOnlinePlayers()){
			player.sendMessage(chat);
		}
	}
}
