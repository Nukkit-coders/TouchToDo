package cn.yescallop.touchtodo.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.TranslationContainer;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.touchtodo.TouchToDo;
import cn.yescallop.touchtodo.command.CommandBase;

import java.util.ArrayList;
import java.util.Arrays;

public class TouchToDoCommand extends CommandBase {

    public TouchToDoCommand(TouchToDo plugin) {
        super("touchtodo", plugin);
        this.setAliases(new String[]{"ttd", "t"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.onlyPlayer"));
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        ArrayList<String> list;
        String command;
        switch (args[0]) {
            case "set":
                if (plugin.isWaiting(player)) plugin.removeWaiting(player);
                list = new ArrayList<>(Arrays.asList(args));
                list.remove(0);
                command = plugin.implode(list.toArray(new String[]{}), " ");
                if (list.size() == 0) {
                    sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                    return false;
                }
                if (!command.startsWith("/")) {
                    sender.sendMessage(lang.translateString("commands.touchtodo.set.invalidprefix"));
                    return false;
                }
                plugin.addSetWaiting(player, command.substring(1));
                sender.sendMessage(lang.translateString("commands.touchtodo.set.waiting", command));
                break;
            case "opset":
                if (plugin.isWaiting(player)) plugin.removeWaiting(player);
                list = new ArrayList<>(Arrays.asList(args));
                list.remove(0);
                command = plugin.implode(list.toArray(new String[]{}), " ");
                if (list.size() == 0) {
                    sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                    return false;
                }
                if (!command.startsWith("/")) {
                    sender.sendMessage(lang.translateString("commands.touchtodo.set.invalidprefix"));
                    return false;
                }
                plugin.addOpSetWaiting(player, command.substring(1));
                sender.sendMessage(lang.translateString("commands.touchtodo.set.waiting", command));
                break;
            case "remove":
                if (plugin.isWaiting(player)) plugin.removeWaiting(player);
                plugin.addRemoveWaiting(player);
                sender.sendMessage(lang.translateString("commands.touchtodo.remove.waiting"));
                break;
            case "abort":
                if (plugin.removeWaiting(player)) {
                    sender.sendMessage(lang.translateString("commands.touchtodo.abort.success"));
                } else {
                    sender.sendMessage(lang.translateString("commands.touchtodo.abort.fail"));
                }
                break;
            default:
                sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
        }
        return false;
    }
}
