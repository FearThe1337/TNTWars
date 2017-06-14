package me.abandoncaptian.TNTWars;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;

public class SignUpdater implements Runnable {
	Main pl;

	public SignUpdater(Main plugin) {
		this.pl = plugin;
	}

	@Override
	public void run() {
		if(!pl.signs.isEmpty()){
			if(pl.signs.size() == 1){
				Location loc = pl.ED.DecodeSignPos(pl.signs.get(0));
				Sign s = (Sign) loc.getBlock().getState();
				String map = ChatColor.stripColor(s.getLine(1));
				if(pl.cd.active.get(map)){
					s.setLine(2, "§1Current§7: §2" + pl.inGame.get(map).size() +"§7/§2" + pl.gameMax);
				}else{
					s.setLine(2, "§1Current§7: §2" + pl.gameQueue.get(map).size() +"§7/§2" + pl.gameMax);
				}
				if(pl.cd.active.get(map))s.setLine(3, "§1Status§7: §2Playing");
				else s.setLine(3, "§1Status§7: §2Waiting");
				s.update();
			}else{
				for(String loc: pl.signs){
					Sign s = (Sign) pl.ED.DecodeSignPos(loc).getBlock().getState();
					String map = ChatColor.stripColor(s.getLine(1));
					if(pl.cd.active.get(map)){
						s.setLine(2, "§1Current§7: §2" + pl.inGame.get(map).size() +"§7/§2" + pl.gameMax);
					}else{
						s.setLine(2, "§1Current§7: §2" + pl.gameQueue.get(map).size() +"§7/§2" + pl.gameMax);
					}
					if(pl.cd.active.get(map))s.setLine(3, "§1Status§7: §2Playing");
					else s.setLine(3, "§1Status§7: §2Waiting");
					s.update();
				}
			}
		}
	}
}
