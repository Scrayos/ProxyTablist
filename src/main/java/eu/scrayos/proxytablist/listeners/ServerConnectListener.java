package eu.scrayos.proxytablist.listeners;

import eu.scrayos.proxytablist.ProxyTablist;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerConnectListener implements Listener {

    @EventHandler
    public void onServerConnect(ServerConnectEvent e) {
        ProxyTablist.getInstance().getTablist().refresh();
    }
}
