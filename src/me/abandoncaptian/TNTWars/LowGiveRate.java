package me.abandoncaptian.TNTWars;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LowGiveRate implements Runnable {
	Main pl;
	CountDowns cd;
	LoadFunctions LF;
	boolean had = false;
	boolean canGive = true;
	int max;
	boolean correctKit = true;

	public LowGiveRate(Main plugin) {
		this.pl = plugin;
		cd = new CountDowns(plugin);
		LF = new LoadFunctions(plugin);
	}

	@Override
	public void run() {
		for(String map : pl.arenas.values()){
			if (pl.cd.active.get(map)) {
				for (String name : pl.inGame.get(map)) {
					max = 2;
					had = false;
					if (pl.selectedKit.containsKey(name)) {
						for (String kit : LF.kitsListHighRate) {
							if (pl.selectedKit.get(name) == kit) {
								correctKit = false;
								break;
							} else
								correctKit = true;
						}
						if (correctKit) {
							if (pl.selectedKit.get(name) == "Heavy Loader")
								max = 5;
							if (pl.selectedKit.get(name) == "Sniper")
								max = 1;
							if (pl.selectedKit.get(name) == "Potion Worker")
								max = 2;
							if (pl.selectedKit.get(name) == "Doctor Who")
								max = 2;
							if (pl.selectedKit.get(name) == "Tank")
								max = 1;
							if (pl.selectedKit.get(name) == "Hail Mary")
								max = 1;
							if (pl.selectedKit.get(name) == "Storm")
								max = 2;
							Player p = Bukkit.getPlayer(name);
							ItemStack tnt = new ItemStack(Material.TNT, 1);
							ItemMeta meta = tnt.getItemMeta();
							List<String> lore = new ArrayList<String>();
							meta.setDisplayName("§c§lThrowable TNT");
							lore.add("§bLeft click to throw");
							lore.add("§bMade By: abandoncaptian");
							meta.setLore(lore);
							tnt.setItemMeta(meta);
							for (int index = 0; index < p.getInventory().getSize(); index++) {
								ItemStack item = p.getInventory().getItem(index);
								if (item != null) {
									if(item.getType().equals(Material.TNT)){
										if (item.getItemMeta().getDisplayName() == tnt.getItemMeta().getDisplayName()) {
											had = true;
											if (item.getAmount() < max) {
												p.getInventory().addItem(tnt);
												break;
											}
										}
									}
								}
							}
							if (!had) {
								p.getInventory().addItem(tnt);
							}
						}else {
							continue;
						}
					}
				}
			}
		}
	}
}