package me.abandoncaptian.TNTWars.Events;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitTask;

import me.abandoncaptian.TNTWars.CountDowns;
import me.abandoncaptian.TNTWars.GameFunc;
import me.abandoncaptian.TNTWars.InvAndExp;
import me.abandoncaptian.TNTWars.Main;
import net.minecraft.server.v1_12_R1.PacketPlayInClientCommand;
import net.minecraft.server.v1_12_R1.PacketPlayInClientCommand.EnumClientCommand;

public class PlayerDeath implements Listener {
	Main pl;
	CountDowns cd;
	InvAndExp IAE;
	GameFunc GF;
	BukkitTask deathTest;
	boolean isDead;

	public PlayerDeath(Main plugin) {
		pl = plugin;
		IAE = new InvAndExp(plugin);
		GF = new GameFunc(plugin);
	}

	@EventHandler
	public void gameDeath(PlayerDeathEvent e) {
		for(String map : pl.arenas.values()){
			if (pl.cd.active.get(map)) {
				if (pl.inGame.get(map).contains(e.getEntity().getName())) {
					Player p = (Player) e.getEntity();
					String name = p.getName();
					PacketPlayInClientCommand packet = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);
					CraftPlayer craftPlayer = (CraftPlayer) p;
					craftPlayer.getHandle().playerConnection.a(packet);
					Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
						@Override
						public void run() {
							p.sendTitle("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l]", "§bYou were eliminated", 0, 60, 0);
							p.getInventory().clear();
							pl.IAE.InventorySwitch(p);
							pl.IAE.ExpSwitch(name);
						}
					}, 5);
					e.getDrops().clear();
					pl.dead.get(map).add(name);
					pl.spec.get(map).add(name);
					e.setDeathMessage(null);
					p.setHealthScale(20);
					p.setCanPickupItems(true);
					pl.inGame.get(map).remove(name);
					pl.allInGame.remove(name);
					GF.UpdateBoard(true, name, map);
					int game = pl.inGame.get(map).size();
					e.getEntity().teleport(pl.spawnpoint.get(map));
					if (game == 1) {
						String winner = pl.inGame.get(map).get(0);
						Bukkit.getScheduler().runTaskLater(pl, new Runnable() {
							@Override
							public void run() {
								Bukkit.broadcastMessage("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l] §b§l" + winner + " §6has won!");
							}
						}, 2);
						for (String tname : pl.spec.get(map)) {
							Bukkit.getPlayer(tname).sendTitle("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l]",
									"§b" + pl.inGame.get(map).get(0) + " has won!", 0, 120, 0);
							Bukkit.getPlayer(tname).teleport(pl.tpBack.get(tname));
							pl.tpBack.remove(tname);
						}
						Bukkit.getPlayer(winner).setHealth(20);
						Bukkit.getPlayer(winner).setFoodLevel(20);
						Bukkit.getPlayer(winner).getInventory().clear();
						Bukkit.getPlayer(winner).closeInventory();
						GF.UpdateBoard(true, winner, map);
						pl.IAE.InventorySwitch(Bukkit.getPlayer(winner));
						pl.IAE.ExpSwitch(winner);
						Bukkit.getPlayer(winner).teleport(pl.tpBack.get(Bukkit.getPlayer(winner).getName()));
						pl.tpBack.remove(Bukkit.getPlayer(winner).getName());
						Bukkit.getPlayer(winner).setHealthScale(20);
						Bukkit.getPlayer(winner).setCanPickupItems(true);
						Bukkit.getPlayer(winner).sendTitle("§7§l[§c§lTNT Wars§7§l] [§6" + map + "§7§l]", "§bYou Won", 0, 120, 0);
						Bukkit.getPlayer(winner).getActivePotionEffects().clear();
						pl.inGame.get(map).clear();
						pl.allInGame.remove(winner);
						pl.cd.active.put(map, false);
						pl.cd.canKit.put(map, true);
						pl.cd.starting1.put(map, false);
						pl.cd.starting2.put(map, false);
						pl.spec.get(map).clear();
						pl.selectedKit.clear();
					}
				}
			}
		}
	}
}