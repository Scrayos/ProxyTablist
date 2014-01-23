package eu.scrayos.proxytablist.api;

import eu.scrayos.proxytablist.ProxyTablist;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public abstract class PlayerVariable implements Variable {
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
     * The normal getText Method is capped in this implementation (it does return ""). This implementation gets scanned
     * and detected. It is made sure that only the Player Implementation of getText is called.
     *
     * @param arg The pattern which was found for this Variable.
     * @param id The refresh-id of this call. Every refresh has its own id. It's the Xth time refreshing since start.
     * @param pingRef The reference of the ping that this Column will got. Change it to modify the ping.
     *
     * @return
     */
    public String getText(String arg, int id, Short pingRef) {
        return "";
    }
}
