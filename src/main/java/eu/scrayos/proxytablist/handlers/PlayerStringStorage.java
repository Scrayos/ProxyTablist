package eu.scrayos.proxytablist.handlers;

import java.util.HashSet;
import java.util.Set;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PlayerStringStorage {
    private final HashSet<String> strings = new HashSet<>();

    /**
     * Stores a String in the Storage. This is needed to clean out the Tablist (since you need to send every entry twice)
     *
     * @param string
     */
    public void addString(String string) {
        strings.add(string);
    }

    /**
     * Get the Set of Strings stored in this Storage
     *
     * @return
     */
    public Set<String> getStrings() {
        return strings;
    }

    /**
     * Clear out the Storage
     */
    public void clearStorage() {
        strings.clear();
    }
}
