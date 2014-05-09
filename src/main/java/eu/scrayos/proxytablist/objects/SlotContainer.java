package eu.scrayos.proxytablist.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
