package me.abandoncaptian.TNTWars.Events;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import me.abandoncaptian.TNTWars.CountDowns;
import me.abandoncaptian.TNTWars.Main;

public class PlayerInteract  implements Listener{
	Main pl;
	CountDowns cd;
	ItemStack tnt = new ItemStack(Material.TNT);
	ItemMeta meta = tnt.getItemMeta();
	List<String> lore = new ArrayList<String>();
	public TNTPrimed primeTnt;
	public PlayerInteract(Main plugin) {
		pl = plugin;
		meta.setDisplayName("�6�lThrowable �c�lTNT");
		lore.add("�bLeft click to throw");
		lore.add("�bMade By: abandoncaptian");
		meta.setLore(lore);
		tnt.setItemMeta(meta);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void tntClick(PlayerInteractEvent e){
		if(cd.active){
			if((e.getAction() == Action.LEFT_CLICK_BLOCK) || (e.getAction() == Action.LEFT_CLICK_AIR)){
				Player p = e.getPlayer();
				if(pl.selectedKit.get(p.getName()) == "Miner"){
					ItemStack item = p.getItemInHand();
					if(item.getType() == Material.TNT){
						if(item.getItemMeta().getDisplayName().equals("�6�lThrowable �c�lTNT")){
							if(pl.inGame.contains(p.getName())){
								this.primeTnt = (TNTPrimed) p.getWorld().spawn(p.getLocation(), TNTPrimed.class);
								pl.tntActive.put(this.primeTnt, p.getName());
								this.primeTnt.setFuseTicks(20*5);
								this.primeTnt.setCustomName(p.getName());
								this.primeTnt.setCustomNameVisible(false);
								e.setCancelled(true);
							}
						}
						if((p.getItemInHand().getAmount()-1)>=1){
							p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
						}else{
							p.getItemInHand().setAmount(0);
						}
					}
				}
				return;
			}
			if((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)){
				Player p = e.getPlayer();
				ItemStack item = p.getItemInHand();
				if(item.getType() == Material.TNT){
					if(item.getItemMeta().getDisplayName().equals("�6�lThrowable �c�lTNT")){
						if(pl.inGame.contains(p.getName())){
							double power = 0;
							int fuse;
							if(pl.selectedKit.containsKey(p.getName())){
								if(pl.selectedKit.get(p.getName()) == "Sniper") power = 4;
								else if((pl.selectedKit.get(p.getName()) == "Suicide Bomber"))power = 0;
								else if((pl.selectedKit.get(p.getName()) == "Boomerang"))power = 3;
								else if((pl.selectedKit.get(p.getName()) == "Miner"))power = 0;
								else power = 1.5;
							}else power =  1.5;
							if(pl.selectedKit.containsKey(p.getName())){
								if(pl.selectedKit.get(p.getName()) == "Short Fuse")fuse = 10;
								else if((pl.selectedKit.get(p.getName()) == "Ender"))fuse = 60;
								else if((pl.selectedKit.get(p.getName()) == "Suicide Bomber"))fuse = 0;
								else if((pl.selectedKit.get(p.getName()) == "Boomerang"))fuse = 30;
								else if((pl.selectedKit.get(p.getName()) == "Miner"))fuse = 40;
								else fuse = 20;
							}else fuse = 20;
							Location eye = p.getEyeLocation();
							Vector vec = eye.getDirection().normalize().multiply(power);
							eye.setY(eye.getY() + 0.4);
							this.primeTnt = (TNTPrimed) p.getWorld().spawn(eye, TNTPrimed.class);
							this.primeTnt.setFuseTicks(fuse);
							this.primeTnt.setCustomName(p.getName());
							this.primeTnt.setCustomNameVisible(false);
							pl.tntActive.put(this.primeTnt, p.getName());
							if(pl.selectedKit.get(p.getName()) == "Ender"){
								Block block = p.getTargetBlock((HashSet<Byte>)null, 30);
								Location bLoc = block.getLocation();
								bLoc.setY(bLoc.getY() + 2);
								this.primeTnt.teleport(bLoc);
							}else{
								this.primeTnt.setVelocity(vec);
							}
							if(pl.selectedKit.containsKey(p.getName())){
								if((pl.selectedKit.get(p.getName()) == "Boomerang")){
									Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
										@Override
										public void run() {
											Vector vec = eye.getDirection().normalize().multiply(-1);
											primeTnt.setVelocity(vec);
										}
									}, 10);
								}
							}
							if((p.getItemInHand().getAmount()-1)>=1){
								p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
							}else{
								p.getItemInHand().setAmount(0);
							}
						}else{
							p.sendMessage("�7�l[�c�lTNT Wars�7�l] �cYou are not in-game");
							return;
						}
					}else{
						return;
					}
				}else{
					return;
				}
			}
		}
	}
}