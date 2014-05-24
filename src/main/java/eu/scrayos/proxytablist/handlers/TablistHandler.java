package eu.scrayos.proxytablist.handlers;

import eu.scrayos.proxytablist.ProxyTablist;
import eu.scrayos.proxytablist.api.Variable;
import eu.scrayos.proxytablist.objects.GlobalTablistView;
import eu.scrayos.proxytablist.objects.VariableContainer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.tab.CustomTabList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TablistHandler implements CustomTabList {

    private List<String> placeholders = new ArrayList<>(Arrays.asList(new String[]{
            "§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8", "§9", "§a", "§b", "§c", "§d", "§e", "§f", "§l", "§m", "§n", "§o", "§k", "§r",
            "§0§0", "§1§0", "§2§0", "§3§0", "§4§0", "§5§0", "§6§0", "§7§0", "§8§0", "§9§0", "§a§0", "§b§0", "§c§0", "§d§0", "§e§0", "§f§0", "§l§0", "§m§0", "§n§0", "§o§0", "§k§0", "§r§0",
            "§0§1", "§1§1", "§2§1", "§3§1", "§4§1", "§5§1", "§6§1", "§7§1", "§8§1", "§9§1", "§a§1", "§b§1", "§c§1", "§d§1", "§e§1", "§f§1", "§l§1", "§m§1", "§n§1", "§o§1", "§k§1", "§r§1",
            "§0§2", "§1§2", "§2§2", "§3§2", "§4§2", "§5§2", "§6§2", "§7§2", "§8§2", "§9§2", "§a§2", "§b§2", "§c§2", "§d§2", "§e§2", "§f§2", "§l§2", "§m§2", "§n§2", "§o§2", "§k§2", "§r§2",
            "§0§3", "§1§3", "§2§3", "§3§3", "§4§3", "§5§3", "§6§3", "§7§3", "§8§3", "§9§3", "§a§3", "§b§3", "§c§3", "§d§3", "§e§3", "§f§3", "§l§3", "§m§3", "§n§3", "§o§3", "§k§3", "§r§3"}));
    private int refreshID = 0;

    @Override
    public synchronized void clear() {
    }

    @Override
    public int getColumns() {
        int columns = 1;
        while ((getSize() / columns) > 20) {
            columns++;
        }
        return columns;
    }

    @Override
    public int getRows() {
        return (getSize() / getColumns());
    }

    @Override
    public int getSize() {
        return ProxyTablist.getInstance().getSize();
    }

    @Override
    public String setSlot(int i, int i2, String s) {
        return setSlot(i, i2, s, true);
    }

    @Override
    public String setSlot(int i, int i2, String s, boolean b) {
        return null;
    }

    @Override
    public void update() {
        int refreshId = getRefreshID();
        Iterator it = placeholders.iterator();
        int slot = 0;
        for (int r = 0; r < ProxyTablist.getInstance().getTablistHandler().getRows(); r++) {
            for (int c = 0; c < ProxyTablist.getInstance().getTablistHandler().getColumns(); c++) {
                if (slot == ProxyTablist.getInstance().getTablistHandler().getSize()) {
                    break;
                }
                String possiblePlaceholder = (String) it.next();
                String columnValue = ProxyTablist.getInstance().getConfig().getCustomColumns().get(c).get(r);
                if (ProxyTablist.getInstance().getDataHandler().getVariableContainers()[slot] == null) {
                    GlobalTablistView.setSlot(slot + 1, (columnValue.isEmpty() ? possiblePlaceholder : ChatColor.translateAlternateColorCodes('&', columnValue)), (short) 0);
                } else {
                    VariableContainer currentVariable = ProxyTablist.getInstance().getDataHandler().getVariableContainers()[slot];
                    for (ProxiedPlayer pp : ProxyTablist.getInstance().getProxy().getPlayers()) {
                        Boolean global = true;
                        Boolean updated = false;
                        short ping = 0;
                        String text = ChatColor.translateAlternateColorCodes('&', columnValue);

                        for (int i = 0; i < currentVariable.getVariable().size(); i++) {
                            Variable variable = currentVariable.getVariable().get(i);
                            variable.setMatchResult(currentVariable.getFoundStr().get(i));
                            variable.setRefreshId(refreshId);
                            if (variable.hasUpdate(slot, pp)) {
                                text = text.replace(currentVariable.getFoundStr().get(i).group(), variable.getText());
                                ping = variable.getPing();
                                global = global && variable.isGlobal();
                                updated = true;
                            }
                        }
                        if (updated) {
                            if (text.isEmpty()) {
                                text = possiblePlaceholder;
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

    @Override
    public void init(ProxiedPlayer proxiedPlayer) {
        GlobalTablistView.createPlayerTablistView(proxiedPlayer);
    }

    @Override
    public void onConnect() {
    }

    @Override
    public void onServerChange() {
    }

    @Override
    public void onPingChange(int i) {
    }

    @Override
    public void onDisconnect() {
    }

    @Override
    public boolean onListUpdate(String s, boolean b, int i) {
        return false;
    }

    public int getRefreshID() {
        return refreshID++;
    }
}
