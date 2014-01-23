package eu.scrayos.proxytablist.api;

import eu.scrayos.proxytablist.ProxyTablist;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public abstract class BasisVariable implements Variable {
    protected ProxyTablist proxyTablist;

    /**
     * This gets called from the loader and should not be used from Variables. You can access the plugin via the
     * proxyTablist Variable.
     *
     * @param proxyTablist current instance of the ProxyTablist Plugin
     */
    public void init(ProxyTablist proxyTablist) {
        this.proxyTablist = proxyTablist;
    }

    /**
     * This implementation should be taken if a normal Replace Variable should be written. This ignores the getText Method which
     * needs a Player and it is sure that it only calls without it.
     *
     * @param arg The pattern which was found for this Variable.
     * @param id The refresh-id of this call. Every refresh has its own id. It's the Xth time refreshing since start.
     * @param pingRef The reference of the ping that this Column will got. Change it to modify the ping.
     * @param player The Player for which this Variable should be resolved
     *
     * @return
     */
    public String getText(String arg, int id, Short pingRef, ProxiedPlayer player) {
        return "";
    }
}
