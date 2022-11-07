package Machine;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class Keyboard implements Serializable, Cloneable {
    private Map<String, String> plugins;
    public Keyboard(Map<String, String> plugins) {
        this.plugins = new LinkedHashMap<>(plugins);
    }
    public String getPlugin(String letter) {
        if (plugins.get(letter) == null)
            return letter;
        else
            return plugins.get(letter);
    }

    public Map<String, String> getPlugins() {
        return this.plugins;
    }

    @Override
    public Keyboard clone() throws CloneNotSupportedException {
        Map<String, String> tempPlugin = new LinkedHashMap<>(this.plugins);
        return new Keyboard(tempPlugin);
    }
}
