package me.abandoncaptian.TNTWars;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InvAndExp {
	Main pl;
	public InvAndExp(Main plugin) {
		pl = plugin;
	}
	
	public void ExpSwitch(String pName) {
		boolean found = false;
		if(pl.savedXPL.size() > 0){
			for(String name : pl.savedXPL.keySet()){
				if(name.equals(pName)){
					found = true;
					Player p = Bukkit.getPlayer(name);
					p.setLevel(pl.savedXPL.get(name));
					pl.savedXPL.remove(name);
					break;
				}
			}
		}
		if(pl.savedXP.size() > 0){
			for(String name : pl.savedXP.keySet()){
				if(name.equals(pName)){
					found = true;
					Player p = Bukkit.getPlayer(name);
					p.setExp(pl.savedXP.get(name));
					pl.savedXP.remove(name);
					break;
				}
			}
		}
		if(!found){
			Player p = Bukkit.getPlayer(pName);
			int expL = p.getLevel();
			Float exp = p.getExp();
			if(expL > 0){
				pl.savedXPL.put(pName, expL);
			}
			if(exp > 0){
				pl.savedXP.put(pName, exp);
			}
			p.setLevel(0);
			p.setExp(0);
		}
	}

	public void InventorySwitch(Player player) {
		boolean found = false;
		if(pl.extraInv.size() > 0){
			for (String uuid : pl.extraInv.keySet()) {
				if (uuid.equals(player.getUniqueId().toString())) {
					found = true;
					ItemStack[] items = pl.extraInv.get(uuid);
					pl.extraInv.remove(player.getUniqueId().toString());
					player.getInventory().clear();
					player.getInventory().setArmorContents((ItemStack[]) ArrayUtils.subarray(items, 0, 4));
					player.getInventory().setContents((ItemStack[]) ArrayUtils.subarray(items, 4, items.length));
					break;
				}
			}
		}
		if (!found) {
			ItemStack[] curr = (ItemStack[]) ArrayUtils.addAll(
					player.getInventory().getArmorContents(),
					player.getInventory().getContents()
					);
			pl.extraInv.put(player.getUniqueId().toString(), curr);
			player.getInventory().clear();
		}
	}

	public boolean hasSwitchedInv(Player p) {
		return pl.extraInv.keySet().contains(p.getUniqueId().toString());
	}

	public boolean hasSwitchedExp(Player p) {
		if(pl.savedXP.keySet().contains(p.getName()) || pl.savedXPL.keySet().contains(p.getName())){
			return true;
		}else return false;
	}
	
}
