package me.AtlasHeads.com;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class HeadGUI {

	static String Prefix = ChatColor.translateAlternateColorCodes('&', "&8[&dAtlas&7Heads&8]");
	public static void openGUI(Player p) {
		Inventory inv = Bukkit.createInventory(p, 18, Prefix);

		Primal.loadConfigs();
		inv.addItem(Methods.getItems.toArray(new ItemStack[Methods.getItems.size()]));
		if(Methods.getItems.size() == 0) {
			p.closeInventory();
			p.sendMessage(Prefix + ChatColor.RED + "There are no heads being sold at this time!");
		}
		p.updateInventory();
		p.openInventory(inv);
	}
	
}
