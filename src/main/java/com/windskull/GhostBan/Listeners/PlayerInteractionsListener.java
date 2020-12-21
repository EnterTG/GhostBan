package com.windskull.GhostBan.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.windskull.GhostBan.GhostBanCore;

@SuppressWarnings("deprecation")
public class PlayerInteractionsListener implements Listener
{

	private GhostBanCore core;

	public PlayerInteractionsListener(GhostBanCore core)
	{
		super();
		this.core = core;
	}
	
	@EventHandler
	public void onPlayerItemDrop(PlayerDropItemEvent e)
	{
		if(core.banedPlayer.containsKey(e.getPlayer()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerItemDrop(PlayerPickupItemEvent e)
	{
		if(core.banedPlayer.containsKey(e.getPlayer()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		if(core.banedPlayer.containsKey(e.getPlayer()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerDMG(EntityDamageByBlockEvent e)
	{
		if(core.banedPlayer.containsKey(e.getEntity()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerDMGbyEnt(EntityDamageByEntityEvent e)
	{
		if(core.banedPlayer.containsKey(e.getEntity()) || core.banedPlayer.containsKey(e.getDamager()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerBlockHarvest(PlayerHarvestBlockEvent e)
	{
		if(core.banedPlayer.containsKey(e.getPlayer()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void disableBlockPlace(BlockPlaceEvent e)
	{
		if(core.banedPlayer.containsKey(e.getPlayer()))
			e.setCancelled(true);
		
	}
	@EventHandler
	public void disableBlockBreak(BlockBreakEvent e)
	{
		if(core.banedPlayer.containsKey(e.getPlayer()))
			e.setCancelled(true);
	}
}
