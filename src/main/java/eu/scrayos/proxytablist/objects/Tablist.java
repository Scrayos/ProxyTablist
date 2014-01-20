package eu.scrayos.proxytablist.objects;

import eu.scrayos.proxytablist.ProxyTablist;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.tab.CustomTabList;

public class Tablist implements CustomTabList {

    public void refresh() {
        for (int c = 0; c < getColumns(); c++) {
            for (int r = 0; r < getRows(); r++) {
                String columnvalue = ((String) ProxyTablist.getInstance().getConfig().getMapList("customcolumns").get(c).get(r));
                if (columnvalue.startsWith("$")) {
                    for (Variable v : ProxyTablist.getInstance().getDataHandler().getVariables()) {
                        if (v.getPattern().equalsIgnoreCase(columnvalue.substring(1))) {
                            setSlot(r, c, v.getText());
                        }
                    }
                } else {
                    setSlot(r, c, columnvalue);
                }
            }
        }
    }

    @Override
    public void clear() {

    }

    @Override
    public int getColumns() {
        return 3;
    }

    @Override
    public int getRows() {
        return 20;
    }

    @Override
    public int getSize() {
        return getColumns() * getRows();
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

    }

    @Override
    public void init(ProxiedPlayer proxiedPlayer) {

    }

    @Override
    public void onConnect() {

    }

    @Override
    public void onServerChange() {

    }

    @Override
    public void onPingChange(int i) {
        //DO NOTHING
    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public boolean onListUpdate(String s, boolean b, int i) {
        return false;
    }
}