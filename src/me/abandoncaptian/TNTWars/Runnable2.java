package me.abandoncaptian.TNTWars;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Runnable2 implements Runnable{
	Main pl;
	public Runnable2(Main plugin){
		this.pl = plugin;
	}

	@Override
	public void run() {
		if(pl.active){
			List<Entity> tnts = new ArrayList<Entity>();
			tnts.addAll(pl.tntActive.keySet());
			for(Entity ent : tnts){
				if(pl.selectedKit.get(pl.tntActive.get(ent)) == "Glue Factory Worker"){
					Player p = Bukkit.getPlayer(pl.primeTnt.getCustomName());
					if(ent.isOnGround()){
						Vector vec = p.getEyeLocation().getDirection().normalize().multiply(0);
						ent.setVelocity(vec);
					}
				}
			}
			tnts.clear();
		}
	}
}
