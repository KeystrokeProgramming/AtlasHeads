package me.AtlasHeads.com;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class Methods implements CommandExecutor, Listener {


	public static List<ItemStack> items = new ArrayList<ItemStack>();
	@SuppressWarnings("unchecked")
	public static List<ItemStack> getItems = (List<ItemStack>) Primal.configsConfig.getList("ItemMarket");
	public static boolean isInt(String price) {
		try {
			Integer.parseInt(price);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		ItemStack item = e.getCurrentItem();
		if(e.getInventory().getTitle().contains(HeadGUI.Prefix)) {
			if(item.getType() == Material.SKULL_ITEM) {
				e.setCancelled(true);
				Bukkit.broadcastMessage("Successfully Sold");
				for(String s : item.getItemMeta().getLore()) {
					String[] sp = s.split(":");
					String part = sp[1];
					if(s.contains(":")) {
						Primal.economy.depositPlayer(p, Integer.valueOf(part));		
						getItems.remove(item);
						Bukkit.broadcastMessage("Bang");
						Primal.saveConfigs();
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player) sender;
		if(commandLabel.equalsIgnoreCase("head")) {
			if(args.length == 0) {
				Primal.loadConfigs();
				p.updateInventory();
				if(Primal.configsConfig.get("ItemMarket") == null) {
					// todo put player message no heads in config
					return true;
				}
				p.updateInventory();
				HeadGUI.openGUI(p);
				p.sendMessage(HeadGUI.Prefix + ChatColor.GREEN + " Accessed Atlas Market!");
			}
			if(args.length == 1) {
				p.sendMessage(HeadGUI.Prefix + ChatColor.RED + " Arguments Incorrect! >> /head sell <price>");
				return true;
			}
			if(args.length == 2) {
				if(args[0].equalsIgnoreCase("sell")) {
					if(isInt(args[1])) {
						if(p.getItemInHand().getType().equals(Material.SKULL_ITEM)) {
							ItemStack skull = p.getItemInHand();
							SkullMeta meta = (SkullMeta) skull.getItemMeta();
							meta.setDisplayName(ChatColor.GRAY + p.getName());
							meta.setOwner(p.getName());
							meta.setLore(Arrays.asList(new String[] { ChatColor.YELLOW + " Player Balance:" + ChatColor.GREEN + " " + Primal.economy.getBalance(p.getName().trim()),
									ChatColor.YELLOW + " Player Kills:" + ChatColor.GREEN,
									ChatColor.GOLD.toString() + ChatColor.BOLD + "Sell Price: " + args[1],
									ChatColor.GOLD.toString() + ChatColor.BOLD + "Valued At:"}));
							skull.setItemMeta(meta);
							if(Primal.configsConfig.get("ItemMarket") == null) {
								Bukkit.broadcastMessage("ERROR");
							}
							items.add(skull);
							Primal.configsConfig.set("ItemMarket", items);
							Primal.saveConfigs();
							Bukkit.broadcastMessage("Success");
							p.sendMessage(HeadGUI.Prefix + ChatColor.GREEN + " Player head has been added to the Market!");
							p.updateInventory();
							p.getInventory().removeItem(skull);

						}else {
							p.sendMessage(HeadGUI.Prefix + ChatColor.RED + " You don't have a Player head in your hand!");
							return true;
						}
					}else{
						p.sendMessage(HeadGUI.Prefix + ChatColor.RED + " Must be a Integer.");
						return true;
					}
				}else{
					p.sendMessage(HeadGUI.Prefix + ChatColor.RED + " Arguments Incorrect! >> /head sell <price>");
					return true;
				}
			}
		}
		return false;
	}
}