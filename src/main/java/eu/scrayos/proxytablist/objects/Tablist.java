package eu.scrayos.proxytablist.objects;

import eu.scrayos.proxytablist.ProxyTablist;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.tab.CustomTabList;

public class Tablist implements CustomTabList {
    @Override
    public synchronized void clear() {
        //NOTHING
    }

    @Override
    public int getColumns() {
        return (int) Math.floor(getSize() / 20);
    }

    @Override
    public int getRows() {
        return (getSize() > 20 ? 20 : getSize());
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
        //NOTHING
    }

    @Override
    public void init(ProxiedPlayer proxiedPlayer) {
        //NOTHING
    }

    @Override
    public void onConnect() {
        //NOTHING
    }

    @Override
    public void onServerChange() {
        //NOTHING
    }

    @Override
    public void onPingChange(int i) {
        //NOTHING
    }

    @Override
    public void onDisconnect() {
        //NOTHING
    }

    @Override
    public boolean onListUpdate(String s, boolean b, int i) {
        return false;
    }
}
