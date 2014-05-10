package eu.scrayos.proxytablist.handlers;

import eu.scrayos.proxytablist.ProxyTablist;
import eu.scrayos.proxytablist.api.Variable;
import eu.scrayos.proxytablist.objects.VariableContainer;
import lombok.Getter;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.regex.Matcher;

@Getter
public class DataHandler {
    private HashSet<Variable> loadedVariables = new HashSet<>();
    private VariableContainer[] variableContainers;

    public DataHandler() {
        loadVariables();
        loadConfig();
    }

    public void loadVariables() {
        File[] files = new File(ProxyTablist.getInstance().getDataFolder() + "/variables").listFiles();
        if (files != null) {
            HashSet<URL> urls = new HashSet<>(files.length);
            for (File file : files) {
                try {
                    if (file.getName().endsWith(".jar")) {
                        urls.add(file.toURI().toURL());
                    }
                } catch (Exception ignored) {
                }
            }
            ClassLoader loader = null;
            try {
                loader = new URLClassLoader(urls.toArray(new URL[urls.size()]), Variable.class.getClassLoader());
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
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

    public void loadConfig() {
        variableContainers = new VariableContainer[ProxyTablist.getInstance().getTablistHandler().getSize()];
        int slot = 0;
        for (int r = 0; r < ProxyTablist.getInstance().getTablistHandler().getRows(); r++) {
            for (int c = 0; c < ProxyTablist.getInstance().getTablistHandler().getColumns(); c++) {
                if (slot == ProxyTablist.getInstance().getTablistHandler().getSize()) {
                    break;
                }
                String columnValue = ProxyTablist.getInstance().getConfig().getCustomColumns().get(c).get(r);
                for (Variable v : loadedVariables) {
                    Matcher m = v.getPattern().matcher(columnValue);
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
}
