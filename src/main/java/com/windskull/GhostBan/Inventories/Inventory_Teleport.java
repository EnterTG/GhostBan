package com.windskull.GhostBan.Inventories;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.windskull.GhostBan.PlayerGhostBan;

public class Inventory_Teleport extends InventoryGui
{
	public static String headName = "&7%p";
	public static String headLore = "&7Click to teleport";
	private PlayerGhostBan player;
	
	public Inventory_Teleport(PlayerGhostBan player)
	{
		super();
		this.player = player;
		initInventory();
	}

	private void initInventory()
	{
		player.getOnlineVisiblePlayers().forEach(p -> this.setItem(getPlayerHead(p),e -> teleportTo(p)));
	}
	
	
	private ItemStack getPlayerHead(Player p)
	{
		ItemStack item = SkullCreator.itemFromUuid(p.getUniqueId());
		ItemMeta itemM = item.getItemMeta();
		itemM.setDisplayName(headName.replaceAll("%p", p.getName()));
		List<String> ls = new ArrayList<>();
		ls.add(headLore.replaceAll("%p", p.getName()));
		itemM.setLore(ls);
		item.setItemMeta(itemM);
		return item;
	}
	
	public void teleportTo(Player p)
	{
		player.getPlayer().teleport(p);
	}
	
	@Override
	public boolean onInventoryGuiClick(Player var1, int var2, ItemStack var3)
	{
		return true;
	}

	@Override
	public boolean onInventoryOpen(Player var1)
	{
		return false;
	}

	@Override
	public boolean onInventoryClose(Player var1)
	{
		return false;
	}

	@Override
	public boolean blockPlayerInventoryClick()
	{
		return true;
	}

}
