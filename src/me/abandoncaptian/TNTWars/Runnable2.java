package me.abandoncaptian.TNTWars;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.abandoncaptian.TNTWars.Events.PlayerInteract;

public class Runnable2 implements Runnable{
	Main pl;
	CountDowns cd;
	PlayerInteract PI;
	public Runnable2(Main plugin){
		this.pl = plugin;
		cd = new CountDowns(plugin);
	}
	@Override
	public void run() {
		if(pl.cd.active){
			List<Entity> tnts = new ArrayList<Entity>();
			tnts.addAll(pl.tntActive.keySet());
			for(Entity ent : tnts){
				if(pl.selectedKit.get(pl.tntActive.get(ent)) == "Glue Factory Worker"){
					Player p = Bukkit.getPlayer(PI.primeTnt.getCustomName());
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
