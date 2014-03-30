package eu.scrayos.proxytablist.commands;

import eu.scrayos.proxytablist.ProxyTablist;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class MainCommand extends Command {

    public MainCommand() {
        super("proxytablist", "proxy.tablist.command.main", "pt");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(new TextComponent("§6ProxyTablist by Scrayos"));
        } else {
            switch (strings[0]) {
                case "reload":
                    ProxyTablist.getInstance().getDataHandler().loadVariables();
            }
        }
    }
}
