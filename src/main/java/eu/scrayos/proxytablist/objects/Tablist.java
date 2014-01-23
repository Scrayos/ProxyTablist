package eu.scrayos.proxytablist.objects;

import eu.scrayos.proxytablist.ProxyTablist;
import eu.scrayos.proxytablist.api.BasisVariable;
import eu.scrayos.proxytablist.api.PlayerVariable;
import eu.scrayos.proxytablist.api.Variable;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.tab.CustomTabList;
import net.md_5.bungee.protocol.packet.PlayerListItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;

public class Tablist implements CustomTabList {

    public synchronized void refresh() {
        clear();
        int refreshID = ProxyTablist.getInstance().getDataHandler().getRefreshID();
        for (int r = 0; r < getRows(); r++) {
            for (int c = 0; c < getColumns(); c++) {
                if (ProxyTablist.getInstance().getConfig().getStringList("customcolumns." + (c + 1)).size() == 0) {
                    ProxyTablist.getInstance().getConfig().set("customcolumns." + (c + 1), new ArrayList<>(new HashSet<>(Arrays.asList(new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}))));
                    ProxyTablist.getInstance().saveConfig();
                }

                String columnvalue = ProxyTablist.getInstance().getConfig().getStringList("customcolumns." + (c + 1)).get(r);
                if (columnvalue != null) {
                    boolean placed = false;
                    if (columnvalue.startsWith("$")) {
                        for (Variable v : ProxyTablist.getInstance().getDataHandler().getVariables()) {
                            Matcher m = v.getPattern().matcher(columnvalue.substring(1));
                            if (m.find()) {
                                m.reset();

                                if(v instanceof BasisVariable) {
                                    Short shrt = 0;
                                    String text = ProxyTablist.getInstance().getDataHandler().verifyEntry(v.getText(columnvalue.substring(1), refreshID, shrt));
                                    if (!text.equals("")) {
                                        for (ProxiedPlayer pp : ProxyTablist.getInstance().getProxy().getPlayers()) {
                                            pp.unsafe().sendPacket(new PlayerListItem(text, true, shrt));
                                            ProxyTablist.getInstance().getDataHandler().getStringStorage(pp).addString(text);
                                        }
                                    }
                                }

                                if(v instanceof PlayerVariable) {
                                    for (ProxiedPlayer pp : ProxyTablist.getInstance().getProxy().getPlayers()) {
                                        Short shrt = 0;
                                        String text = ProxyTablist.getInstance().getDataHandler().verifyEntry(v.getText(columnvalue.substring(1), refreshID, shrt, pp));

                                        if (!text.equals("")) {
                                            pp.unsafe().sendPacket(new PlayerListItem(text, true, shrt));
                                            ProxyTablist.getInstance().getDataHandler().getStringStorage(pp).addString(text);
                                        }
                                    }
                                }

                                placed = true;
                            }
                        }
                    }

                    if (!placed) {
                        for (ProxiedPlayer pp : ProxyTablist.getInstance().getProxy().getPlayers()) {
                            pp.unsafe().sendPacket(new PlayerListItem(ProxyTablist.getInstance().getDataHandler().verifyEntry(columnvalue), true, (short) 0));
                            ProxyTablist.getInstance().getDataHandler().getStringStorage(pp).addString(ProxyTablist.getInstance().getDataHandler().verifyEntry(columnvalue));
                        }
                    }
                }
            }
        }
    }

    @Override
    public synchronized void clear() {
        for (ProxiedPlayer pp : ProxyTablist.getInstance().getProxy().getPlayers()) {
            for (String s : ProxyTablist.getInstance().getDataHandler().getStringStorage(pp).getStrings()) {
                pp.unsafe().sendPacket(new PlayerListItem(s, false, (short) 0));
            }

            ProxyTablist.getInstance().getDataHandler().getStringStorage(pp).clearStorage();
        }
    }

    @Override
    public int getColumns() {
        return (int) Math.floor(ProxyTablist.getInstance().getProxy().getConfigurationAdapter().getListeners().iterator().next().getTabListSize() / 20);
    }

    @Override
    public int getRows() {
        int size = ProxyTablist.getInstance().getProxy().getConfigurationAdapter().getListeners().iterator().next().getTabListSize();
        return (size > 20 ? 20 : size);
    }

    @Override
    public int getSize() {
        return ProxyTablist.getInstance().getProxy().getConfigurationAdapter().getListeners().iterator().next().getTabListSize();
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
        refresh();
    }

    @Override
    public void init(ProxiedPlayer proxiedPlayer) {
        //DO NOTHING
    }

    @Override
    public void onConnect() {
        //DO NOTHING
    }

    @Override
    public void onServerChange() {
        //DO NOTHING
    }

    @Override
    public void onPingChange(int i) {
        //DO NOTHING
    }

    @Override
    public void onDisconnect() {
        //DO NOTHING
    }

    @Override
    public boolean onListUpdate(String s, boolean b, int i) {
        return false;
    }
}
