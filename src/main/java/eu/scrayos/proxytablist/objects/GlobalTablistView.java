package eu.scrayos.proxytablist.objects;

import com.google.common.base.Preconditions;
import eu.scrayos.proxytablist.ProxyTablist;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.Map;

public class GlobalTablistView {
    //The global Views
    private static SlotContainer[] currentView;
    private static SlotContainer[] newView;

    //Hold all PlayerTablistViews
    private static HashMap<ProxiedPlayer, PlayerTablistView> playerTablistViews = new HashMap<>();

    /**
     * Create some new Instances for the PlayerTablistViews and recreate the view arrays with the new Size
     */
    public static void init() {
        newView = new SlotContainer[ProxyTablist.getInstance().getTablist().getSize()];

        //Be sure to clear out all Tablists
        for (Map.Entry<ProxiedPlayer, PlayerTablistView> playerTablistView : new HashMap<>(playerTablistViews).entrySet()) {
            playerTablistView.getValue().completeClear();
            createPlayerTablistView(playerTablistView.getKey());
        }

        currentView = new SlotContainer[ProxyTablist.getInstance().getTablist().getSize()];
    }

    /**
     * Return the slot out of the currentView
     *
     * @param slot
     * @return
     */
    public static SlotContainer getSlot(int slot) {
        return currentView[slot];
    }

    /**
     * Return the slot out of the newView
     *
     * @param slot
     * @return
     */
    public static SlotContainer getSlotFromNewView(int slot) {
        return newView[slot];
    }

    /**
     * Create a new PlayerTablistView for the Player. If there was an old one it gets deleted
     *
     * @param proxiedPlayer
     */
    public static void createPlayerTablistView(ProxiedPlayer proxiedPlayer) {
        if (playerTablistViews.containsKey(proxiedPlayer)) {
            playerTablistViews.remove(proxiedPlayer);
        }

        playerTablistViews.put(proxiedPlayer, new PlayerTablistView(proxiedPlayer));
    }

    /**
     * Sets a specific slot inside the array
     *
     * @param slotNumber number of which slot to set
     * @param text       to set into the slot
     * @param ping       to set into the slot
     */
    public static void setSlot(int slotNumber, String text, Short ping) {
        Preconditions.checkArgument(slotNumber > -1, "Slot number is under 0");
        Preconditions.checkNotNull(text, "Text can not be null");

        text = (text.length() > 16) ? text.substring(0, 16) : text;

        //Tell all PlayerTablistViews we got a update
        for (PlayerTablistView playerTablistView : playerTablistViews.values()) {
            playerTablistView.setSlot(slotNumber, "", (short) 0);
        }

        newView[slotNumber - 1] = (text.equals("")) ? null : new SlotContainer(text, ping);
    }

    public static void fireUpdate() {
        //Tell all PlayerTablistViews to fire an update
        for (PlayerTablistView playerTablistView : playerTablistViews.values()) {
            playerTablistView.fireUpdate();
        }

        //Copy over the newView into the currentView
        System.arraycopy(newView, 0, currentView, 0, newView.length);
    }

    /**
     * Delete a PlayerTablistView for the Player
     *
     * @param proxiedPlayer
     */
    public static void removePlayerTablistView(ProxiedPlayer proxiedPlayer) {
        if (playerTablistViews.containsKey(proxiedPlayer)) {
            playerTablistViews.remove(proxiedPlayer);
        }
    }

    /**
     * Return the stored PlayerTablistView
     *
     * @param proxiedPlayer
     * @return
     */
    public static PlayerTablistView getPlayerTablistView(ProxiedPlayer proxiedPlayer) {
        if (playerTablistViews.containsKey(proxiedPlayer)) {
            return playerTablistViews.get(proxiedPlayer);
        }

        return null;
    }
}
