package eu.scrayos.proxytablist.listeners;

import eu.scrayos.proxytablist.ProxyTablist;
import eu.scrayos.proxytablist.objects.GlobalTablistView;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PostLoginListener implements Listener {

    @EventHandler
    public void onPostLogin(final PostLoginEvent e) {
        e.getPlayer().setTabList(ProxyTablist.getInstance().getTablist());
        GlobalTablistView.createPlayerTablistView(e.getPlayer());
    }
}
