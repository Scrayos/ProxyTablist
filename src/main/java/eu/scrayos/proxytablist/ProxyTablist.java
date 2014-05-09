package eu.scrayos.proxytablist;

import eu.scrayos.proxytablist.commands.MainCommand;
import eu.scrayos.proxytablist.handlers.DataHandler;
import eu.scrayos.proxytablist.handlers.TablistHandler;
import eu.scrayos.proxytablist.listeners.PlayerDisconnectListener;
import eu.scrayos.proxytablist.listeners.PostLoginListener;
import eu.scrayos.proxytablist.listeners.ServerSwitchListener;
import eu.scrayos.proxytablist.objects.Config;
import eu.scrayos.proxytablist.objects.GlobalTablistView;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import org.mcstats.Metrics;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

@SuppressWarnings("ResultOfMethodCallIgnored")
@Getter
public class ProxyTablist extends Plugin {

    private static ProxyTablist instance;
    private TablistHandler tablistHandler;
    private DataHandler dataHandler;
    private int size;
    private Config config;

    public static ProxyTablist getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        size = getProxy().getConfigurationAdapter().getListeners().iterator().next().getTabListSize();
        new File(getDataFolder() + "/variables").mkdirs();
        try {
            config = new Config(this);
            config.init();
            config.save();
        } catch (Exception ex) {
            System.out.println("Your Configuration-File for ProxyTablist doesn't match the standards for YAML-Files. Please revisit it.");
        }
        instance = this;
        tablistHandler = new TablistHandler();
        instance = this;
        dataHandler = new DataHandler();
        instance = this;
        GlobalTablistView.init();

        getProxy().getPluginManager().registerListener(this, new ServerSwitchListener());
        getProxy().getPluginManager().registerListener(this, new PostLoginListener());
        getProxy().getPluginManager().registerListener(this, new PlayerDisconnectListener());
        getProxy().getPluginManager().registerCommand(this, new MainCommand());

        getProxy().getScheduler().schedule(this, new Runnable() {
            @Override
            public void run() {
                tablistHandler.update();
            }
        }, getConfig().getAutoRefresh(), getConfig().getAutoRefresh(), TimeUnit.SECONDS);


        try {
            new Metrics(this).start();
        } catch (Exception ex) {
            getLogger().log(Level.WARNING, "Failed to initialize Metrics!");
        }
    }
}
