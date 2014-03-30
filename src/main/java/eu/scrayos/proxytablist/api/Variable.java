package eu.scrayos.proxytablist.api;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * That is the interface for custom Variables. The variables should be in the <b>"/plugins/ProxyTablist/variables/"</b> -
 * Directory.
 * <p/>
 * A variable is able to refuse to update. But every Variable should at least update once into the Global Tablist per refresh Cycle.
 * If it does not it can destroy the View
 *
 * @author Scrayos
 */
public interface Variable {
    /**
     * The Pattern or Template which is used to determine where to insert the Variable. The Pattern gets checked with
     * RegEx, you're free to use special RegEx-Chars.
     *
     * @return The Pattern or Template which is used to determine where to insert the Variable.
     */
    public Pattern getPattern();

    /**
     * Sets the new Refresh ID for this call
     *
     * @param refreshId The refresh-id of this call. Every refresh has its own id. It's the Xth time refreshing since start.
     */
    public void setRefreshId(int refreshId);

    /**
     * Checks if the Variable has an update for this slot and player
     *
     * @param slot          to check for an update
     * @param proxiedPlayer to check for an update
     * @return true when there is an update, false when not
     */
    public boolean hasUpdate(int slot, ProxiedPlayer proxiedPlayer);

    /**
     * Sets the current MatchResult for the next getText()
     *
     * @param matchResult for the match for the pattern which has matched
     */
    public void setMatchResult(MatchResult matchResult);

    /**
     * Checks if the next Text is for the Global Tablist
     *
     * @return true for the global Tablist, false for the PlayerView
     */
    public boolean isForGlobalTablist();

    /**
     * The Text or Value this Variable contains. The found matches will get replaced by this. The Text or Value is
     * queried everytime the Tablist refreshes.
     *
     * @param pingRef The reference of the ping that this Column will got. Change it to modify the ping.
     * @return The Text or Value this Variable contains.
     */
    public String getText(Short pingRef);
}
