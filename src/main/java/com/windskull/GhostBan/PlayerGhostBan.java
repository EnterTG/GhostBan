package com.windskull.GhostBan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import com.windskull.GhostBan.Events.GhostBanPlayerCollectVisiblePlayersEvent;
import com.windskull.GhostBan.Events.PlayerGhostBanEvent;
import com.windskull.GhostBan.Events.PlayerGhostUnbanEvent;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * @author WindSkull
 *
 */
@SuppressWarnings("deprecation")
public class PlayerGhostBan extends BukkitRunnable implements Listener
{

	public static int BAN_DURATION = 5*60;
	public static GameMode GAME_MODE = GameMode.SURVIVAL;
	public static String TIME_TO_UNBAN = "Time to unban: %t";
	public static boolean BANED_SEE_BANED = true;
	
	private Player player;
	
	private final Team team;
	
	private int banTime = 0;
	
	private List<Player> onlineVisiblePlayers;
	private List<OfflinePlayer> offlineVisiblePlayers;
	
	
	private ItemStack[] playerArmor;
	private ItemStack[] playerInventory;
	
	
	public void run()
	{
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(TIME_TO_UNBAN.replaceAll("%t", (BAN_DURATION - banTime) + "")));
		if(banTime++ >= BAN_DURATION)
			unbanPlayer();
	}
	
	
	public PlayerGhostBan(Player player)
	{
		super();
		this.player = player;
		Bukkit.getPluginManager().registerEvents(this, GhostBanCore.getInstance());
		
		team = Bukkit.getScoreboardManager().getNewScoreboard().registerNewTeam(player.getName());
		team.setCanSeeFriendlyInvisibles(true);
		team.addPlayer(player);
		collectPlayers();
		banPlayer();
		
	}

	public void hideFromAll()
	{
		List<Player> players = new ArrayList<Player>(Bukkit.getOnlinePlayers());
		if(BANED_SEE_BANED)
		{
			List<Player> banned = Collections.list(GhostBanCore.getInstance().banedPlayer.keys());
			players.removeAll(banned);
			if(!GhostBanCore.USE_SPECTATOR_MODE)
				banned.forEach(p -> team.addPlayer(p));
			banned.forEach(p -> p.showPlayer(GhostBanCore.getInstance(), player));
			banned.forEach(p -> player.showPlayer(GhostBanCore.getInstance(), p));
		}
		
		players.forEach(p -> p.hidePlayer(GhostBanCore.getInstance(), player));
		players.forEach(p -> player.hidePlayer(GhostBanCore.getInstance(), p));
		
	}
	
	public void showAll()
	{
		List<Player> players = new ArrayList<Player>(Bukkit.getOnlinePlayers());
		List<Player> banned = Collections.list(GhostBanCore.getInstance().banedPlayer.keys());
		players.removeAll(banned);
		players.forEach(p -> p.showPlayer(GhostBanCore.getInstance(), player));
		players.forEach(p -> player.showPlayer(GhostBanCore.getInstance(), p));
	}
	public void collectPlayers()
	{
		GhostBanPlayerCollectVisiblePlayersEvent kde = new GhostBanPlayerCollectVisiblePlayersEvent(player);
		Bukkit.getServer().getPluginManager().callEvent(kde);
		onlineVisiblePlayers = kde.getOnlineVisiblePlayers();
		offlineVisiblePlayers = kde.getOfflineVisiblePlayers();
		
		onlineVisiblePlayers.forEach(p -> team.addPlayer(p));
		offlineVisiblePlayers.forEach(p -> team.addPlayer(p));
	}
	

	
	public void banPlayer()
	{
		PlayerGhostBanEvent kde = new PlayerGhostBanEvent(player);
		Bukkit.getServer().getPluginManager().callEvent(kde);
		if (!kde.isCancelled())
		{
			hideFromAll();
			if(GhostBanCore.USE_SPECTATOR_MODE)
				player.setGameMode(GameMode.SPECTATOR);
			else
				noSpectatorModeBan();
		}
	}
	
	private void noSpectatorModeBan()
	{
		player.setAllowFlight(true);
		player.setFlying(true);
		player.closeInventory();
		PlayerInventory inv = player.getInventory();
		playerInventory = inv.getContents();
		playerArmor = inv.getArmorContents();
		player.getInventory().clear();
		setBanItems();
	}
	

	
	
	public void setBanItems()
	{
		
	}
	
	
	public void unbanPlayer()
	{
		PlayerGhostUnbanEvent kde = new PlayerGhostUnbanEvent(this);
		Bukkit.getServer().getPluginManager().callEvent(kde);
		if (!kde.isCancelled())
		{
			this.cancel();
			if(GhostBanCore.USE_SPECTATOR_MODE)
				player.setGameMode(GAME_MODE);
			else
				noSpectatorModeUnban();
			
			team.unregister();
			PlayerQuitEvent.getHandlerList().unregister(this);
			PlayerJoinEvent.getHandlerList().unregister(this);
			PlayerGhostUnbanEvent.getHandlerList().unregister(this);
			GhostBanCore.getInstance().banedPlayer.remove(player);
		}
	}

	private void noSpectatorModeUnban()
	{
		player.setFlying(false);
		player.setAllowFlight(false);
		player.closeInventory();
		PlayerInventory inv = player.getInventory();
		inv.clear();
		inv.setArmorContents(playerArmor);
		inv.setContents(playerInventory);
	}
	
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		if(onlineVisiblePlayers.contains(e.getPlayer()))
		{
			offlineVisiblePlayers.add(e.getPlayer());
			onlineVisiblePlayers.remove(e.getPlayer());
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerGhostUnban(PlayerGhostUnbanEvent e)
	{
		if(e.getPlayerGhostBan() != this)
			if(BANED_SEE_BANED)
			{
				if(!GhostBanCore.USE_SPECTATOR_MODE)
				{
					Player bannedPlayer = e.getPlayerGhostBan().getPlayer();
					if(!onlineVisiblePlayers.contains(bannedPlayer))
						if(team.hasPlayer(bannedPlayer))
							team.removePlayer(bannedPlayer);
				}
			}
	}
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		if(offlineVisiblePlayers.contains(e.getPlayer()))
		{
			onlineVisiblePlayers.add(e.getPlayer());
			offlineVisiblePlayers.remove(e.getPlayer());
			player.showPlayer(GhostBanCore.getInstance(), e.getPlayer());
			e.getPlayer().hidePlayer(GhostBanCore.getInstance(), player);
		}
		else
		{
			player.hidePlayer(GhostBanCore.getInstance(), e.getPlayer());
			e.getPlayer().hidePlayer(GhostBanCore.getInstance(), player);
		}
	}

	public Player getPlayer()
	{
		return player;
	}

	public void setPlayer(Player player)
	{
		this.player = player;
	}

	public int getBanTime()
	{
		return banTime;
	}

	public void setBanTime(int banTime)
	{
		this.banTime = banTime;
	}

	public List<Player> getOnlineVisiblePlayers()
	{
		return onlineVisiblePlayers;
	}

	public void setOnlineVisiblePlayers(List<Player> onlineVisiblePlayers)
	{
		this.onlineVisiblePlayers = onlineVisiblePlayers;
	}

	public List<OfflinePlayer> getOfflineVisiblePlayers()
	{
		return offlineVisiblePlayers;
	}

	public void setOfflineVisiblePlayers(List<OfflinePlayer> offlineVisiblePlayers)
	{
		this.offlineVisiblePlayers = offlineVisiblePlayers;
	}

	public ItemStack[] getPlayerArmor()
	{
		return playerArmor;
	}

	public void setPlayerArmor(ItemStack[] playerArmor)
	{
		this.playerArmor = playerArmor;
	}

	public ItemStack[] getPlayerInventory()
	{
		return playerInventory;
	}

	public void setPlayerInventory(ItemStack[] playerInventory)
	{
		this.playerInventory = playerInventory;
	}

	public Team getTeam()
	{
		return team;
	}
	
}
