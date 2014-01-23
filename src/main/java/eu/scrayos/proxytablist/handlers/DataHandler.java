package eu.scrayos.proxytablist.handlers;

import eu.scrayos.proxytablist.ProxyTablist;
import eu.scrayos.proxytablist.api.Variable;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;

public class DataHandler {
    private final HashSet<Variable> variables = new HashSet<>();
    private final HashMap<ProxiedPlayer, PlayerStringStorage> playerStorages = new HashMap<>();
    private int refreshID = 0;

    public DataHandler() {
        File[] files = new File(ProxyTablist.getInstance().getDataFolder() + "/variables").listFiles();
        if (files != null) {
            HashSet<URL> urls = new HashSet<>(files.length);
            for (File file : files) {
                try {
                    if (file.getName().endsWith(".jar")) {
                        urls.add(file.toURI().toURL());
                    }
                } catch (MalformedURLException ignored) {
                }
            }
            ClassLoader loader = null;
            try {
                loader = new URLClassLoader(urls.toArray(new URL[urls.size()]), Variable.class.getClassLoader());
            } catch (Exception ignored) {
            }
            for (File file : files) {
                try {
                    if (!file.getName().endsWith(".jar")) {
                        continue;
                    }
                    Class<?> aClass = loader.loadClass(file.getName().substring(0, file.getName().lastIndexOf(".")));
                    Object object = aClass.newInstance();


                    if (object instanceof Variable) {
                        Variable variable = (Variable) object;
                        variable.init(ProxyTablist.getInstance());

                        addVariable(variable);
                    } else {
                        ProxyTablist.getInstance().getLogger().log(Level.WARNING, "Error while loading " + file.getName() + " (No Variable)");
                        continue;
                    }
                } catch (Exception ex) {
                    ProxyTablist.getInstance().getLogger().log(Level.WARNING, "Error while loading " + file.getName() + " (Unspecified Error)");
                    ex.printStackTrace();
                }
            }
        }
    }

    private void addVariable(Variable variable) {
        variables.add(variable);
    }

    public int getRefreshID() {
        refreshID++;
        return refreshID - 1;
    }

    public PlayerStringStorage getStringStorage(ProxiedPlayer proxiedPlayer) {
        if(!playerStorages.containsKey(proxiedPlayer)) {
            playerStorages.put(proxiedPlayer, new PlayerStringStorage());
        }

        return playerStorages.get(proxiedPlayer);
    }

    public void removeStorage(ProxiedPlayer proxiedPlayer) {
        playerStorages.remove(proxiedPlayer);
    }

    public String verifyEntry(String arg) {
        return (arg.length() > 16 ? arg.substring(0, 16) : arg);
    }

    public HashSet<Variable> getVariables() {
        return variables;
    }
}
