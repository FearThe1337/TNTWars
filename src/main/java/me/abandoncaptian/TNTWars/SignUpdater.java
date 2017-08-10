package me.abandoncaptian.TNTWars;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;

public class SignUpdater implements Runnable {
	Main pl;
	public HashMap<String, String> mapStatus = new HashMap<String, String>();

	public SignUpdater(Main plugin) {
		this.pl = plugin;
	}

	@Override
	public void run() {
		if(!pl.signs.isEmpty()){
			for(String loc: pl.signs){
				Sign s = (Sign) pl.ED.DecodeSignPos(loc).getBlock().getState();
				String map = ChatColor.stripColor(s.getLine(1));
				if(pl.cd.active.get(map)){
					s.setLine(2, "§1Current§7: §2" + pl.inGame.get(map).size() +"§7/§2" + pl.gameMax.get(map));
				}else{
					s.setLine(2, "§1Current§7: §2" + pl.gameQueue.get(map).size() +"§7/§2" + pl.gameMax.get(map));
				}
				if(pl.cd.active.get(map)){
					s.setLine(3, "§1Status§7: §2Playing");
					mapStatus.put(map, "Playing");
				}else{
					s.setLine(3, "§1Status§7: §2Waiting");
					mapStatus.put(map, "Waiting");
				}
				s.update();
			}
		}
	}
}
