package eu.scrayos.proxytablist.handlers;

import eu.scrayos.proxytablist.ProxyTablist;
import eu.scrayos.proxytablist.api.Variable;
import eu.scrayos.proxytablist.objects.GlobalTablistView;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Matcher;

public class DataHandler {
    private HashSet<Variable> loadedVariables = new HashSet<>();
    private VariableContainer[] variableContainers;
    private List<String> placeholders = new ArrayList<>(Arrays.asList(new String[]{
            "§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8", "§9", "§a", "§b", "§c", "§d", "§e", "§f", "§l", "§m", "§n", "§o", "§k", "§r",
            "§0§0", "§1§0", "§2§0", "§3§0", "§4§0", "§5§0", "§6§0", "§7§0", "§8§0", "§9§0", "§a§0", "§b§0", "§c§0", "§d§0", "§e§0", "§f§0", "§l§0", "§m§0", "§n§0", "§o§0", "§k§0", "§r§0",
            "§0§1", "§1§1", "§2§1", "§3§1", "§4§1", "§5§1", "§6§1", "§7§1", "§8§1", "§9§1", "§a§1", "§b§1", "§c§1", "§d§1", "§e§1", "§f§1", "§l§1", "§m§1", "§n§1", "§o§1", "§k§1", "§r§1",
            "§0§2", "§1§2", "§2§2", "§3§2", "§4§2", "§5§2", "§6§2", "§7§2", "§8§2", "§9§2", "§a§2", "§b§2", "§c§2", "§d§2", "§e§2", "§f§2", "§l§2", "§m§2", "§n§2", "§o§2", "§k§2", "§r§2",
            "§0§3", "§1§3", "§2§3", "§3§3", "§4§3", "§5§3", "§6§3", "§7§3", "§8§3", "§9§3", "§a§3", "§b§3", "§c§3", "§d§3", "§e§3", "§f§3", "§l§3", "§m§3", "§n§3", "§o§3", "§k§3", "§r§3"}));
    private Iterator it;
    private int lastRefreshID = -1;
    private int refreshID = -1;

    public DataHandler() {
        //Load variables
        loadVariables();

        //Read in the config
        loadConfigTablist();
    }

    public void loadVariables() {
        File[] files = new File(ProxyTablist.getInstance().getDataFolder() + "/variables").listFiles();
        if (files != null) {
            //Check for Files who end with a .jar and add them to the be loaded list
            HashSet<URL> urls = new HashSet<>(files.length);
            for (File file : files) {
                try {
                    if (file.getName().endsWith(".jar")) {
                        urls.add(file.toURI().toURL());
                    }
                } catch (MalformedURLException ignored) {
                }
            }

            //Try to build the Classloader with which the JARs should be loaded
            ClassLoader loader = null;
            try {
                loader = new URLClassLoader(urls.toArray(new URL[urls.size()]), Variable.class.getClassLoader());
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }

            //If there is a valid Classloader load the jars
            if (loader != null) {
                for (File file : files) {
                    try {
                        if (!file.getName().endsWith(".jar")) {
                            continue;
                        }

                        Class<?> aClass = loader.loadClass(file.getName().substring(0, file.getName().lastIndexOf(".")));
                        Object object = aClass.newInstance();
                        if (!(object instanceof Variable)) {
                            ProxyTablist.getInstance().getLogger().log(Level.WARNING, "Error while loading " + file.getName() + " (No Variable)");
                            continue;
                        }

                        loadedVariables.add((Variable) object);
                    } catch (Exception ex) {
                        ProxyTablist.getInstance().getLogger().log(Level.WARNING, "Error while loading " + file.getName() + " (Unspecified Error)");
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * This reads in the current custom columns and parses them into the correct Variable
     */
    public void loadConfigTablist() {
        variableContainers = new VariableContainer[ProxyTablist.getInstance().getTablist().getSize()];

        //Keep track of the Array slot :)
        int slot = 0;

        //Check each Row first so we check them as they get delivered
        for (int r = 0; r < ProxyTablist.getInstance().getTablist().getRows(); r++) {
            for (int c = 0; c < ProxyTablist.getInstance().getTablist().getColumns(); c++) {
                //If there is a Column which is not in the Config, create a new empty column for it
                if (ProxyTablist.getInstance().getConfig().getStringList("customcolumns." + (c + 1)).size() == 0) {
                    ProxyTablist.getInstance().getConfig().set("customcolumns." + (c + 1), new ArrayList<>(new HashSet<>(Arrays.asList(new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}))));
                    ProxyTablist.getInstance().saveConfig();
                }

                //Check which Column handles this slot
                String columnvalue = ProxyTablist.getInstance().getConfig().getStringList("customcolumns." + (c + 1)).get(r);
                for (Variable v : loadedVariables) {
                    Matcher m = v.getPattern().matcher(columnvalue);

                    while (m.find()) {
                        if (variableContainers[slot] == null) {
                            variableContainers[slot] = new VariableContainer();
                        }

                        variableContainers[slot].addVariableMatch(v, m.toMatchResult());
                        String text = m.replaceFirst("");
                        m = v.getPattern().matcher(text);
                    }
                }

                slot++;
            }
        }
    }

    public void update() {
        int refreshId = getRefreshID();
        int slot = 0;
        for (int r = 0; r < ProxyTablist.getInstance().getTablist().getRows(); r++) {
            for (int c = 0; c < ProxyTablist.getInstance().getTablist().getColumns(); c++) {
                String columnvalue = ProxyTablist.getInstance().getConfig().getStringList("customcolumns." + (c + 1)).get(r);
                if (variableContainers[slot] == null) {
                    if (columnvalue.isEmpty()) {
                        GlobalTablistView.setSlot(slot + 1, getPlaceholder(refreshId), (short) 0);
                    } else {
                        GlobalTablistView.setSlot(slot + 1, ChatColor.translateAlternateColorCodes('&', columnvalue), (short) 0);
                    }
                } else {
                    VariableContainer currentVariable = variableContainers[slot];
                    for (ProxiedPlayer pp : ProxyTablist.getInstance().getProxy().getPlayers()) {
                        Short ping = 0;
                        Boolean global = true;
                        Boolean updated = false;
                        String text = ChatColor.translateAlternateColorCodes('&', columnvalue);

                        for (int i = 0; i < currentVariable.getVariable().size(); i++) {
                            Variable variable = currentVariable.getVariable().get(i);
                            variable.setMatchResult(currentVariable.getFoundStr().get(i));
                            variable.setRefreshId(refreshId);
                            if (variable.hasUpdate(slot, pp)) {
                                text = text.replace(currentVariable.getFoundStr().get(i).group(), variable.getText(ping));
                                global = global && variable.isForGlobalTablist();
                                updated = true;
                            }
                        }
                        if (updated) {
                            if (text.isEmpty()) {
                                text = getPlaceholder(refreshId);
                            }
                            if (global) {
                                GlobalTablistView.setSlot(slot + 1, text, ping);
                            } else {
                                GlobalTablistView.getPlayerTablistView(pp).setSlot(slot + 1, text, ping);
                            }
                        }
                    }
                }
                slot++;
            }
        }
        GlobalTablistView.fireUpdate();
    }

    public String getPlaceholder(int refreshID) {
        if (refreshID != lastRefreshID) {
            lastRefreshID = refreshID;
            it = placeholders.iterator();
        }
        return it.hasNext() ? (String) it.next() : null;
    }

    public int getRefreshID() {
        refreshID++;
        return refreshID;
    }
}
