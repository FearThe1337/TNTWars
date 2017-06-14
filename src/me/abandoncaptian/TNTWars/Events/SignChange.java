package me.abandoncaptian.TNTWars.Events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

import me.abandoncaptian.TNTWars.Main;

public class SignChange implements Listener {
	Main pl;
	public SignChange(Main plugin) {
		pl = plugin;
	}
	
	@EventHandler
	public void signEdit(SignChangeEvent e){
		Player p = e.getPlayer();
		String titleLine = e.getLine(0);
		String mapLine = e.getLine(1);
		if(p.hasPermission("tntwars.Sign")){
			if(titleLine.contains("[tw]")){
				if(pl.arenas.values().contains(mapLine)){
					e.setLine(0, "§7§l[§c§lTNT Wars§7§l]");
					e.setLine(1, "§1" + mapLine);
					e.setLine(2, "§1Current§7: §2" + pl.gameQueue.get(mapLine).size() +"§7/§2" + pl.gameMax);
					if(pl.cd.active.get(mapLine))e.setLine(3, "§1Status§7: §2Playing");
					else e.setLine(3, "§1Status§7: §2Waiting");
					pl.signs.add(pl.ED.EncodeSignPos(e.getBlock().getLocation()));
				}else{
					p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cInvalid Map");
					e.getBlock().breakNaturally();
				}
			}
		}else{
			p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cYou can't create those signs");
		}
	}
	
	@EventHandler
	public void signBreak(BlockBreakEvent e){
		Player p = e.getPlayer();
		Block b = e.getBlock();
		if(p.hasPermission("tntwars.Sign")){
			if(pl.signs.contains(pl.ED.EncodeSignPos(b.getLocation()))){
				pl.signs.remove(pl.ED.EncodeSignPos(b.getLocation()));
			}
		}
	}
}
