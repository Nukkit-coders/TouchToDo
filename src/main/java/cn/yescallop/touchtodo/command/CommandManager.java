package cn.yescallop.touchtodo.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandMap;
import cn.yescallop.touchtodo.TouchToDo;
import cn.yescallop.touchtodo.command.defaults.TouchToDoCommand;

import java.util.ArrayList;

public class CommandManager {

    public static void registerAll(TouchToDo plugin) {
        CommandMap map = plugin.getServer().getCommandMap();
        map.register("touchtodo", new TouchToDoCommand(plugin));
    }
}