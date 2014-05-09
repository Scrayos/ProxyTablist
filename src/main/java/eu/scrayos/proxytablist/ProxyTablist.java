package eu.scrayos.proxytablist;

import eu.scrayos.proxytablist.commands.MainCommand;
import eu.scrayos.proxytablist.handlers.DataHandler;
import eu.scrayos.proxytablist.include.Metrics;
import eu.scrayos.proxytablist.listeners.PlayerDisconnectListener;
import eu.scrayos.proxytablist.listeners.PostLoginListener;
import eu.scrayos.proxytablist.listeners.ServerSwitchListener;
import eu.scrayos.proxytablist.objects.GlobalTablistView;
import eu.scrayos.proxytablist.objects.Tablist;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class ProxyTablist extends Configurablelugin {

    private static ProxyTablist is;
    private Tablist tl;
    private DataHandler dh;

    @Override
    public void onEnable() {
        is = this;

        new File(getDataFolder() + "/variables").mkdirs();
        saveDefaultConfig();
        tl = new Tablist();
        is = this;
        dh = new DataHandler();
        is = this;
        //Init the GlobalView
        GlobalTablistView.init();

        getProxy().getPluginManager().registerListener(this, new ServerSwitchListener());
        getProxy().getPluginManager().registerListener(this, new PostLoginListener());
        getProxy().getPluginManager().registerListener(this, new PlayerDisconnectListener());
        getProxy().getPluginManager().registerCommand(this, new MainCommand());

        getProxy().getScheduler().schedule(this, new Runnable() {
            @Override
            public void run() {
                dh.update();
            }
        }, getConfig().getInt("autorefresh"), getConfig().getInt("autorefresh"), TimeUnit.SECONDS);


        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (Exception ex) {
            getLogger().log(Level.WARNING, "Failed to initialize Metrics!");
            ex.printStackTrace();
        }
    }

    public static ProxyTablist getInstance() {
        return is;
    }

    public DataHandler getDataHandler() {
        return dh;
    }

    public Tablist getTablist() {
        return tl;
    }
}
