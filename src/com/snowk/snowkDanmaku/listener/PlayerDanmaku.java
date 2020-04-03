package com.snowk.snowkDanmaku.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.snowk.snowkDanmaku.Message;
import com.snowk.snowkDanmaku.config.ConfigBuffer;
import com.snowk.snowkDanmaku.util.SensitivewordFilter;


public class PlayerDanmaku implements Listener {
	
	/** playerMatcher (ArrayList)
	 *  k: Player Name (String)
	 *  function: save PlayerName when they use /fdm command,
	 *  		  then for matching them with the next chat.
	 */
	public static ArrayList<String> playerMatcher = new ArrayList<String>();
	/** chatCooldown (HashMap)
	 *  k: Player Name (String)
	 *  v: the time that a player can chat (long) 
	 */
	public static HashMap<String, Long> chatCooldown = new HashMap<String, Long>();

					
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		String p = e.getPlayer().getName();
		
		if (playerMatcher.contains(p) && !e.getMessage().equals("cancel")) {
			if (ConfigBuffer.enableCooldown) {
				// condition that a player want to send Danmaku
				if (!chatCooldown.containsKey(p)) {
					// the player didn't send any Danmaku before

					chatCooldown.put(p, Long.valueOf((System.currentTimeMillis() / 1000) + ((long) ConfigBuffer.Delay)));
					sendDanmaku(e);
				} else if (chatCooldown.get(p).longValue() > System.currentTimeMillis() / 1000) {
					// the player want to send the next Danmaku, but it's too quickly
					long cdLeft = chatCooldown.get(p).longValue() - (System.currentTimeMillis() / 1000);
					e.getPlayer().sendMessage(Message.msg_Cd.replace("{s}", cdLeft+""));
					playerMatcher.remove(p);
					e.setCancelled(true);
				} else {
					// the player want to send the next Danmaku, after the cooldown
					chatCooldown.put(p, Long.valueOf((System.currentTimeMillis() / 1000) + ((long) ConfigBuffer.Delay))); //renew HashMap
					sendDanmaku(e);
				}
			} else {
				sendDanmaku(e);
			}
		} else if (playerMatcher.contains(p) && e.getMessage().equals("cancel")){
			// cancel Danmaku
			playerMatcher.remove(p);
			e.setCancelled(true);
			e.getPlayer().sendMessage(Message.msg_Cancel);
		}
	}
	
	
	public void onQuit(PlayerQuitEvent e) {
		String p = e.getPlayer().getName();
		if (playerMatcher.contains(p)) {
			playerMatcher.remove(p);
		}
	}
	
	
	/** Name: sendDanmaku
	 *  
	 *  Function: Send a Danmaku with SensitiveWord Check
	 */
	private void sendDanmaku(AsyncPlayerChatEvent e) {
		String p = e.getPlayer().getName();
		
		// available heights initialization
		ArrayList<Integer> height = new ArrayList<Integer>();
		height.add(12);
		height.add(15);
		height.add(18);
		height.add(21);
		height.add(24);
		height.add(27);
		height.add(30);
		height.add(33);
		height.add(36);
		int rdmHeight = height.get(new Random().nextInt(height.size()));
		
		String chat = e.getMessage().replace(" ", "_/").replace("&", "").replace("¡ì", "") + "_/[-" + e.getPlayer().getName().toString() + "]";
		
		// sensitive words test
		SensitivewordFilter filter = new SensitivewordFilter();
		Set<String> set = filter.getSensitiveWord(chat, 1);
		if (!set.isEmpty()) {
			e.getPlayer().sendMessage(Message.msg_Reject);
			playerMatcher.remove(p);
			e.setCancelled(true);
			return;
		}
		
		// send command to console
		String cmd = "flowview @all " + rdmHeight + " " + chat + " false";
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
		playerMatcher.remove(p);
		e.setCancelled(true);
		e.getPlayer().sendMessage(Message.msg_Sent);
	}
}
