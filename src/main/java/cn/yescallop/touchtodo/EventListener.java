package cn.yescallop.touchtodo;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.math.Vector2;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.touchtodo.lang.BaseLang;

public class EventListener implements Listener {

    TouchToDo plugin;
    BaseLang lang;

    public EventListener(TouchToDo plugin) {
        this.plugin = plugin;
        lang = plugin.getLanguage();
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (plugin.isWaiting(player)) {
            event.setCancelled();
            if (plugin.isRemoveWaiting(player)) {
                if (plugin.removeBlockCommand(block)) {
                    player.sendMessage(lang.translateString("commands.touchtodo.remove.success"));
                } else {
                    player.sendMessage(lang.translateString("commands.touchtodo.remove.fail"));
                }
            } else {
                String command = plugin.getSetWaiting(player);
                if (command == null) {
                    command = plugin.getOpSetWaiting(player);
                    plugin.setBlockCommand(block, command, true);
                } else {
                    plugin.setBlockCommand(block, command, false);
                }
                player.sendMessage(lang.translateString("commands.touchtodo.set.success", "/" + command));
            }
            plugin.removeWaiting(player);
        } else {
            String command = plugin.getBlockCommand(block);
            if (command != null) {
                event.setCancelled();
                command = command.replaceAll("@p", player.getName());
                if (plugin.isBlockCommandOp(block) && !player.isOp()) {
                    player.setOp(true);
                    Server.getInstance().dispatchCommand(player, command);
                    player.setOp(false);
                } else {
                    Server.getInstance().dispatchCommand(player, command);
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (player == null || plugin.getBlockCommand(block) == null) return;
        if (!player.hasPermission("touchtodo.break.commandblock")) {
            event.setCancelled();
            player.sendMessage(TextFormat.RED + lang.translateString("touchtodo.break.nopermission"));
        } else if (!event.isCancelled()) {
            plugin.removeBlockCommand(block);
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.removeWaiting(event.getPlayer());
    }
}