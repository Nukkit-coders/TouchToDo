package cn.yescallop.touchtodo.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.PluginIdentifiableCommand;
import cn.yescallop.touchtodo.TouchToDo;
import cn.yescallop.touchtodo.lang.BaseLang;

public abstract class CommandBase extends Command implements PluginIdentifiableCommand {

    protected TouchToDo plugin;
    protected BaseLang lang;

    public CommandBase(String name, TouchToDo plugin) {
        super(name);
        this.lang = plugin.getLanguage();
        this.description = lang.translateString("commands." + name + ".description");
        String usageMessage = lang.translateString("commands." + name + ".usage");
        this.usageMessage = usageMessage.equals("commands." + name + ".usage") ? "/" + name : usageMessage;
        this.setPermission("touchtodo.command." + name);
        this.plugin = plugin;
    }

    @Override
    public TouchToDo getPlugin() {
        return plugin;
    }
}