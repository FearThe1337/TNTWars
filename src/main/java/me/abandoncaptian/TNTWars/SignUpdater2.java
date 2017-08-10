package me.abandoncaptian.TNTWars;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.Sign;

public class SignUpdater2 implements Runnable {
	Main pl;

	public SignUpdater2(Main plugin) {
		this.pl = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		if(!pl.signs.isEmpty()){
			for(String loc: pl.SU.mapStatus.keySet()){
				Sign s = (Sign) pl.ED.DecodeSignPos(loc).getBlock().getState();
				Block block = pl.ED.DecodeSignPos(loc).getBlock();
				Block b = block.getRelative(s.getAttachedFace());
				if(pl.SU.mapStatus.get(loc).contains("Playing")){
					b.setType(Material.CONCRETE);
					b.setData((byte) 14);
				}else{
					b.setType(Material.CONCRETE);
					b.setData((byte) 13);
				}
			}
		}
	}
}
