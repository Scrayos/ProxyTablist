package eu.scrayos.proxytablist.objects;

public class SlotContainer {
    private String text;
    private Short ping;

    public SlotContainer(String text, Short ping) {
        this.text = text;
        this.ping = ping;
    }

    public String getText() {
        return (text.length() > 16 ? text.substring(0, 16) : text);
    }

    public void setText(String text) {
        this.text = text;
    }

    public Short getPing() {
        return ping;
    }

    public void setPing(Short ping) {
        this.ping = ping;
    }
}
