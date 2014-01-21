package eu.scrayos.proxytablist.objects;

import java.util.regex.Pattern;

/**
 * Thats the interface for custom Variables. The variables should be in the <b>"/plugins/ProxyTablist/variables/"</b> -
 * Directory.
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
     * The Text or Value this Variable contains. The found matches will get replaced by this. The Text or Value is
     * queried everytime the Tablist refreshes.
     *
     * @param arg The pattern which was found for this Variable.
     * @param id The refresh-id of this call. Every refresh has its own id. It's the Xth time refreshing since start.
     *
     * @return The Text or Value this Variable contains.
     */
    public String getText(String arg, int id);
}
