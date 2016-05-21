package cn.yescallop.touchtodo;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.yescallop.touchtodo.command.CommandManager;
import cn.yescallop.touchtodo.lang.BaseLang;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class TouchToDo extends PluginBase {

    private File worldsFolder;
    private HashMap<String, String> setWaiting = new HashMap<>();
    private HashMap<String, String> opSetWaiting = new HashMap<>();
    private ArrayList<String> removeWaiting = new ArrayList<>();
    private BaseLang lang;

    @Override
    public void onEnable() {
        this.getDataFolder().mkdirs();
        worldsFolder = new File(this.getDataFolder(), "worlds");
        worldsFolder.mkdirs();
        lang = new BaseLang(this.getServer().getLanguage().getLang());
        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
        CommandManager.registerAll(this);
        this.getLogger().info(lang.translateString("touchtodo.loaded"));
    }

    public BaseLang getLanguage() {
        return lang;
    }
    
    public void addSetWaiting(Player player, String command) {
        setWaiting.put(player.getName().toLowerCase(), command);
    }
    
    public void addOpSetWaiting(Player player, String command) {
        opSetWaiting.put(player.getName().toLowerCase(), command);
    }
    
    public void addRemoveWaiting(Player player) {
        removeWaiting.add(player.getName().toLowerCase());
    }
    
    public boolean removeWaiting(Player player) {
        return setWaiting.remove(player.getName().toLowerCase()) != null || opSetWaiting.remove(player.getName().toLowerCase()) != null || removeWaiting.remove(player.getName().toLowerCase());
    }
    
    public boolean isWaiting(Player player) {
        return getSetWaiting(player) != null || getOpSetWaiting(player) != null || isRemoveWaiting(player);
    }
    
    public String getSetWaiting(Player player) {
        return setWaiting.get(player.getName().toLowerCase());
    }
    
    public String getOpSetWaiting(Player player) {
        return opSetWaiting.get(player.getName().toLowerCase());
    }
    
    public boolean isRemoveWaiting(Player player) {
        return removeWaiting.contains(player.getName().toLowerCase());
    }
    
    public void setBlockCommand(Block block, String command, boolean op) {
        Config config = getLevelConfig(block.level);
        config.set(implode(getVector3FlooredArray(block), ","), new Object[]{command, op});
        config.save();
    }
    
    public String getBlockCommand(Block block) {
        Config config = getLevelConfig(block.level);
        ArrayList<Object> command = (ArrayList<Object>) config.get(implode(getVector3FlooredArray(block), ","));
        return command == null ? null : (String) command.get(0);
    }
    
    public boolean isBlockCommandOp(Block block) {
        Config config = getLevelConfig(block.level);
        ArrayList<Object> command = (ArrayList<Object>) config.get(implode(getVector3FlooredArray(block), ","));
        return command == null ? false : (boolean) command.get(1);
    }
    
    public boolean removeBlockCommand(Block block) {
        Config config = getLevelConfig(block.level);
        String key = implode(getVector3FlooredArray(block), ",");
        boolean exists = config.exists(key);
        config.remove(key);
        config.save();
        return exists;
    }
    
    private String[] getVector3FlooredArray(Vector3 pos) {
        return new String[]{String.valueOf((int) pos.x), String.valueOf((int) pos.y), String.valueOf((int) pos.z)};
    }
    
    public String implode(String[] arr, String sp) {
        String output = "";
        for (int i = 0; i < arr.length; i++) {
            output += arr[i];
            if (arr.length - i != 1) output += sp;
        }
        return output;
    }
    
    public Config getLevelConfig(Level level) {
        return new Config(new File(worldsFolder, level.getName() + ".yml"), Config.YAML);
    }
    
    public File getLevelFolder(Level level) {
        File folder = new File(worldsFolder, level.getName());
        folder.mkdirs();
        return folder;
    }
}