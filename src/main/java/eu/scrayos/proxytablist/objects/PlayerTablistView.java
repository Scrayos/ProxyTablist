package eu.scrayos.proxytablist.objects;

import com.google.common.base.Preconditions;
import eu.scrayos.proxytablist.ProxyTablist;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.packet.PlayerListItem;

public class PlayerTablistView {
    private SlotContainer[] currentView;
    private SlotContainer[] newView;
    private int changedSlot = -1;
    private ProxiedPlayer proxiedPlayer;

    /**
     * Generate a new empty array with the size which is given through the Argument
     *
     * @param proxiedPlayer for which this PlayerTablistView should send
     */
    public PlayerTablistView(ProxiedPlayer proxiedPlayer) {
        currentView = new SlotContainer[ProxyTablist.getInstance().getTablist().getSize()];
        newView = new SlotContainer[ProxyTablist.getInstance().getTablist().getSize()];

        this.proxiedPlayer = proxiedPlayer;

        fireUpdate();
    }

    /**
     * Sets a specific slot inside the array
     *
     * @param slotNumber number of which slot to set
     * @param text       to set into the slot
     * @param ping       to set into the slot
     */
    public void setSlot(int slotNumber, String text, Short ping) {
        Preconditions.checkArgument(slotNumber > -1, "Slot number is under 0");
        Preconditions.checkNotNull(text, "Text can not be null");

        //Care that only the lowest number gets marked
        if (slotNumber < changedSlot) {
            changedSlot = slotNumber;
        }

        newView[slotNumber - 1] = (text.equals("")) ? null : new SlotContainer(text, ping);
    }

    /**
     * Gets the Slot from the current view (first checks the global then the player views)
     *
     * @param slot
     * @return
     */
    private SlotContainer getSlot(int slot) {
        SlotContainer fromGlobal = GlobalTablistView.getSlot(slot);

        if (fromGlobal != null) return fromGlobal;
        return currentView[slot];
    }

    /**
     * Gets the Slot from the new view (first checks the global then the player views)
     *
     * @param slot
     * @return
     */
    private SlotContainer getSlotFromNewView(int slot) {
        SlotContainer fromGlobal = GlobalTablistView.getSlotFromNewView(slot);

        if (fromGlobal != null) return fromGlobal;
        return newView[slot];
    }

    /**
     * Empties the Players Tablist
     */
    public void completeClear() {
        for (int i = 0; i < currentView.length; i++) {
            SlotContainer slotValue = getSlot(i);
            if (slotValue != null)
                proxiedPlayer.unsafe().sendPacket(new PlayerListItem(slotValue.getText(), false, slotValue.getPing()));
        }
    }

    /**
     * Send out an Update to the player
     */
    public void fireUpdate() {
        //No slot has been changed
        if (changedSlot == -1) {
            //Send the new Entries
            for (int i = 0; i < newView.length - 1; i++) {
                SlotContainer send = getSlotFromNewView(i);

                if (send != null)
                    proxiedPlayer.unsafe().sendPacket(new PlayerListItem(send.getText(), true, send.getPing()));
            }

            changedSlot = 999;
        }

        if (changedSlot == 999) return;

        //Send offline messages to clear out the list
        for (int i = currentView.length - 1; i > changedSlot - 2; i--) {
            SlotContainer send = getSlot(i);

            if (send != null)
                proxiedPlayer.unsafe().sendPacket(new PlayerListItem(send.getText(), false, send.getPing()));
        }

        //Send the new Entries
        for (int i = changedSlot - 1; i < newView.length; i++) {
            SlotContainer send = getSlotFromNewView(i);

            if (send != null)
                proxiedPlayer.unsafe().sendPacket(new PlayerListItem(send.getText(), true, send.getPing()));
        }

        //Copy over the newView into the currentView
        System.arraycopy(newView, 0, currentView, 0, newView.length);

        changedSlot = 999;
    }
}
