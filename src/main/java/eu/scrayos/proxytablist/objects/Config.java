package eu.scrayos.proxytablist.objects;

import eu.scrayos.proxytablist.ProxyTablist;
import lombok.Getter;
import lombok.Setter;
import net.cubespace.Yamler.Config.Comment;
import net.cubespace.Yamler.Config.ConfigMode;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Config extends net.cubespace.Yamler.Config.Config {

    public Config(Plugin plugin) {
        CONFIG_HEADER = new String[]{
                "#########################################################################################",
                "##__________                             ___________     ___.   .__  .__          __   ##",
                "##\\______   \\_______  _______  ______.__.\\__    ___/____ \\_ |__ |  | |__| _______/  |_ ##",
                "## |     ___/\\_  __ \\/  _ \\  \\/  <   |  |  |    |  \\__  \\ | __ \\|  | |  |/  ___/\\   __\\##",
                "## |    |     |  | \\(  <_> >    < \\___  |  |    |   / __ \\| \\_\\ \\  |_|  |\\___ \\  |  |  ##",
                "## |____|     |__|   \\____/__/\\_ \\/ ____|  |____|  (____  /___  /____/__/____  > |__|  ##",
                "##                              \\/\\/                    \\/    \\/             \\/        ##",
                "#########################################################################################",
                "                                                                                         ",
                "#|Thanks for choosing ProxyTablist as your solution for Tablist on BungeeCords GLOBAL  |#",
                "#|and GLOBAL_PING - mode. You definitely won't go back from the moment you use this.   |#",
                "                                                                                         ",
                "#|Please check your config with the YAML-Parser, if you're not sure about the rules you|#",
                "#|have to respect when editing YAML-Files. You can found one of those Parsers online at|#",
                "#|http://yaml-online-parser.appspot.com/ - Just copy and fix the Errors which are shown|#",
                "#|                                     Lots of Fun!                                    |#",
                "                                                                                         "
        };
        CONFIG_FILE = new File(plugin.getDataFolder() + "/config.yml");
        CONFIG_MODE = ConfigMode.DEFAULT;
        for (int ic = 1; ic <= (int) Math.ceil(ProxyTablist.getInstance().getSize() / 20); ic++) {
            customColumns.put(ic, new ArrayList<String>() {
            });
            for (int ir = 0; ir < (ProxyTablist.getInstance().getSize() < 20 ? ProxyTablist.getInstance().getSize() : 20); ir++) {
                customColumns.get(ic).add("{player}");
            }
        }
    }

    @Comment("This Setting does set the amount of Time (in Seconds) for the auto-refresh of ProxyTablist.")
    private int autoRefresh = 60;

    @Comment("This is the column-configuration of ProxyTablist.")
    private Map<Integer, List<String>> customColumns = new HashMap<>();
}
