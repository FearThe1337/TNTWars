package me.abandoncaptian.TNTWars.Events;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import me.abandoncaptian.TNTWars.CountDowns;
import me.abandoncaptian.TNTWars.GameFunc;
import me.abandoncaptian.TNTWars.Main;
import me.abandoncaptian.TNTWars.MenuHandler;

public class PlayerInteract implements Listener {
	Main pl;
	CountDowns cd;
	GameFunc GF;
	MenuHandler MH;
	ItemStack tnt = new ItemStack(Material.TNT);
	ItemMeta meta = tnt.getItemMeta();
	List<String> lore = new ArrayList<String>();
	public TNTPrimed primeTnt;

	public PlayerInteract(Main plugin) {
		pl = plugin;
		cd = new CountDowns(plugin);
		GF = new GameFunc(plugin);
		MH = new MenuHandler(plugin);
		meta.setDisplayName("§6§lThrowable §c§lTNT");
		lore.add("§bLeft click to throw");
		lore.add("§bMade By: abandoncaptian");
		meta.setLore(lore);
		tnt.setItemMeta(meta);
	}

	@EventHandler
	public void playerMove(PlayerMoveEvent e) {
		if(e.getTo().getY()<=0)e.getPlayer().teleport(pl.hub);
		return;
	}
	
	@EventHandler
	public void playerNoHunger(FoodLevelChangeEvent e) {
		if(!pl.allInGame.contains(e.getEntity().getName()))e.setFoodLevel(20);
		return;
	}
	
	@EventHandler
	public void clickWithInvItem(PlayerInteractEvent e) {
		if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) || (e.getAction() == Action.RIGHT_CLICK_AIR)) {
			Player p = e.getPlayer();
			ItemStack hand = e.getItem();
			if(hand == null)return;
			if(!hand.hasItemMeta())return;
			if(!hand.getItemMeta().hasDisplayName())return;
			if(hand.getItemMeta().getDisplayName().contains("§cLeave TNT Wars")){
				GF.gameLeave(p.getName());
			}
			if(hand.getItemMeta().getDisplayName().contains("§aKit Shop")){
				MH.openKitShop(p);
			}
			if(hand.getItemMeta().getDisplayName().contains("§aTNT Wars Kits")){
				MH.openKitMenu(p);
			}
			if(hand.getItemMeta().getDisplayName().contains("§cTNT Wars Menu")){
				MH.openMainMenu(p);
			}
		}
	}

	@EventHandler
	public void signClick(PlayerInteractEvent e) {
		if ((e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			if(e.getClickedBlock().getState() instanceof Sign){
				Sign s = (Sign) e.getClickedBlock().getState();
				Player p = e.getPlayer();
				if(s.getLine(0).equals("§7§l[§c§lTNT Wars§7§l]"));
				String map = ChatColor.stripColor(s.getLine(1));
				MH.openArenaTeams(p, map);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void tntClick(PlayerInteractEvent e) {
		for(String map : pl.arenas.values()){
			if (pl.cd.active.get(map)){
				if ((e.getAction() == Action.LEFT_CLICK_BLOCK) || (e.getAction() == Action.LEFT_CLICK_AIR)) {
					Player p = e.getPlayer();
					if (pl.selectedKit.get(p.getName()) == "Miner") {
						ItemStack item = p.getItemInHand();
						if (item.getType() == Material.TNT) {
							if (item.getItemMeta().getDisplayName().contains("§c§lTNT")) {
								if (pl.inGame.get(map).contains(p.getName())) {
									primeTnt = (TNTPrimed) p.getWorld().spawn(p.getLocation(), TNTPrimed.class);
									pl.tntActive.put(this.primeTnt, p.getName());
									primeTnt.setFuseTicks(20 * 5);
									primeTnt.setCustomName(p.getName());
									primeTnt.setCustomNameVisible(false);
									if (pl.Perks.containsKey(p.getName())) {
										if (pl.Perks.get(p.getName()).get("Outline")) {
											primeTnt.setGlowing(true);
										}
									}
									e.setCancelled(true);
								}
							}
							if ((p.getItemInHand().getAmount() - 1) >= 1) {
								p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
							} else {
								p.getItemInHand().setAmount(0);
							}
						}
					}
					return;
				}
				if ((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
					Player p = e.getPlayer();
					ItemStack item = p.getItemInHand();
					if (item.getType() == Material.TNT) {
						if (item.getItemMeta().getDisplayName().equals("§c§lThrowable TNT")) {
							if (pl.inGame.get(map).contains(p.getName())) {
								double power = 0;
								int fuse;
								if (pl.selectedKit.containsKey(p.getName())) {
									if (pl.selectedKit.get(p.getName()) == "Sniper")
										power = 4;
									else if ((pl.selectedKit.get(p.getName()) == "Suicide Bomber"))
										power = 0;
									else if ((pl.selectedKit.get(p.getName()) == "Boomerang"))
										power = 3;
									else if ((pl.selectedKit.get(p.getName()) == "Miner"))
										power = 0;
									else if ((pl.selectedKit.get(p.getName()) == "Space Man"))
										power = 2;
									else if ((pl.selectedKit.get(p.getName()) == "Storm"))
										power = 0;
									else if ((pl.selectedKit.get(p.getName()) == "Shotgun"))
										power = 3;
									else if ((pl.selectedKit.get(p.getName()) == "Virg Special"))
										power = 20;
									else
										power = 1.5;
								} else
									power = 1.5;
								if (pl.selectedKit.containsKey(p.getName())) {
									if (pl.selectedKit.get(p.getName()) == "Short Fuse")
										fuse = 10;
									else if ((pl.selectedKit.get(p.getName()) == "Ender"))
										fuse = 60;
									else if ((pl.selectedKit.get(p.getName()) == "Suicide Bomber"))
										fuse = 0;
									else if ((pl.selectedKit.get(p.getName()) == "Boomerang"))
										fuse = 30;
									else if ((pl.selectedKit.get(p.getName()) == "Miner"))
										fuse = 40;
									else if ((pl.selectedKit.get(p.getName()) == "Storm"))
										fuse = 60;
									else if ((pl.selectedKit.get(p.getName()) == "Shutgun"))
										fuse = 20;
									else
										fuse = 20;
								} else
									fuse = 20;
								Location eye = p.getEyeLocation();
								eye.setY(eye.getY() + 0.4);
								if (pl.selectedKit.get(p.getName()) == "Storm") {
									Block block = p.getTargetBlock((HashSet<Byte>) null, 30);
									Location bLoc = block.getLocation();
									bLoc.setY(bLoc.getY() + 30);
									primeTnt = (TNTPrimed) p.getWorld().spawn(bLoc, TNTPrimed.class);

								} else if (pl.selectedKit.get(p.getName()) == "Virg Special") {
									Vector vec = eye.getDirection().normalize().multiply(power);
									eye.getBlock().setType(Material.WATER);
									LivingEntity squid = (LivingEntity) eye.getWorld().spawnEntity(eye, EntityType.SQUID);
									squid.setFallDistance(0);
									squid.setVelocity(vec);
									eye.getBlock().setType(Material.AIR);
									if ((p.getItemInHand().getAmount() - 1) >= 1) {
										p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
									} else {
										p.getItemInHand().setAmount(0);
									}
									Bukkit.getScheduler().runTaskLater(pl, new Runnable() {

										@Override
										public void run() {
											Location squidLoc = squid.getLocation();
											squid.setInvulnerable(true);
											squid.remove();
											squidLoc.getWorld().createExplosion(squidLoc.getX(), squidLoc.getY(),
													squidLoc.getZ(), 3, false, false);
										}
									}, 40);
									return;
								} else {
									primeTnt = (TNTPrimed) p.getWorld().spawn(eye, TNTPrimed.class);
								}
								Vector vec = eye.getDirection().normalize().multiply(power);
								primeTnt.setFuseTicks(fuse);
								if(pl.cName.containsKey(p.getName())){
									if(pl.Perks.containsKey(p.getName())){
										if(pl.Perks.get(p.getName()).containsKey("CName")){
											if(pl.Perks.get(p.getName()).get("CName")){
												if(!pl.cNameColor.containsKey(p.getName()))pl.cNameColor.put(p.getName(), ChatColor.WHITE);
												primeTnt.setCustomName(pl.cNameColor.get(p.getName()) + pl.cName.get(p.getName()));
												primeTnt.setCustomNameVisible(true);
											}else{
												primeTnt.setCustomName(p.getName());
												primeTnt.setCustomNameVisible(false);
											}
										}else{
											primeTnt.setCustomName(p.getName());
											primeTnt.setCustomNameVisible(false);
										}
									}
								}else{
									primeTnt.setCustomName(p.getName());
									primeTnt.setCustomNameVisible(false);
								}
								if (pl.Perks.containsKey(p.getName())) {
									if (pl.Perks.get(p.getName()).get("Outline")) {
										primeTnt.setGlowing(true);
									}
								}
								if (pl.selectedKit.get(p.getName()) == "Space Man") {
									primeTnt.setGravity(false);
								}
								pl.tntActive.put(primeTnt, p.getName());
								if (pl.selectedKit.get(p.getName()) == "Ender") {
									Block block = p.getTargetBlock((HashSet<Byte>) null, 30);
									Location bLoc = block.getLocation();
									bLoc.setY(bLoc.getY() + 2);
									primeTnt.teleport(bLoc);
								} else {
									primeTnt.setVelocity(vec);
								}
								if (pl.selectedKit.containsKey(p.getName())) {
									if ((pl.selectedKit.get(p.getName()) == "Boomerang")) {
										Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
											@Override
											public void run() {
												Vector vec = eye.getDirection().normalize().multiply(-1);
												primeTnt.setVelocity(vec);
											}
										}, 10);
									}
								}
								if ((p.getItemInHand().getAmount() - 1) >= 1) {
									p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
								} else {
									p.getItemInHand().setAmount(0);
								}
							} else {
								p.sendMessage("§7§l[§c§lTNT Wars§7§l] §cYou are not in-game");
								return;
							}
						} else {
							return;
						}
					} else {
						return;
					}
				}
			}
		}
	}
}