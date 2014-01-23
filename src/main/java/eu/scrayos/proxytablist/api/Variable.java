package eu.scrayos.proxytablist.api;

import eu.scrayos.proxytablist.ProxyTablist;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.regex.Pattern;

/**
 * That is the interface for custom Variables. The variables should be in the <b>"/plugins/ProxyTablist/variables/"</b> -
 * Directory.
 *
 * @author Scrayos
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public interface Variable {
    /**
     * This gets called by the loader and sets the Plugin into the Variable to access things like config
     *
     * @param proxyTablist current instance of the ProxyTablist Plugin
     */
    public void init(ProxyTablist proxyTablist);

    /**
     * The Pattern or Template which is used to determine where to insert the Variable. The Pattern gets checked with
     * RegEx, you're free to use special RegEx-Chars.
     *
     * @return The Pattern or Template which is used to determine where to insert the Variable.
     */
    public Pattern getPattern();

    /**
     * The Text or Value this Variable contains. The found matches will get replaced by this. The Text or Value is
     * queried everytime the Tablist refreshes.
     *
     * @param arg The pattern which was found for this Variable.
     * @param id The refresh-id of this call. Every refresh has its own id. It's the Xth time refreshing since start.
     * @param pingRef The reference of the ping that this Column will got. Change it to modify the ping.
     *
     * @return The Text or Value this Variable contains.
     */
    public String getText(String arg, int id, Short pingRef);

    /**
     * The Text or Value this Variable contains. The found matches will get replaced by this. The Value is queried
     * everytime the Tablist refreshes. It gets called for each Player in the Bungeecord !
     *
     * @param arg The pattern which was found for this Variable.
     * @param id The refresh-id of this call. Every refresh has its own id. It's the Xth time refreshing since start.
     * @param pingRef The reference of the ping that this Column will got. Change it to modify the ping.
     * @param player The Player for which this Variable should be resolved
     *
     * @return The Text or Value this Variable contains.
     */
    public String getText(String arg, int id, Short pingRef, ProxiedPlayer player);
}
