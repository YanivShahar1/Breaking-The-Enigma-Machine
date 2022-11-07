package DTO;

import Machine.Enigma;
import Machine.Reflector;
import Machine.Rotor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DTOCodeConfigurationDescriptor implements Serializable {
    private List<Rotor> usedRotors;
    private Reflector usedReflector;
    private Map<String, String> plugins;

    public DTOCodeConfigurationDescriptor(List<Rotor> usedRotors,
                                          Reflector usedReflector,
                                          Map<String, String> plugins) throws CloneNotSupportedException {
        this.usedRotors = new ArrayList<>(usedRotors);
        this.usedReflector = usedReflector.clone();
        if (plugins == null) {
            this.plugins = null;
        }
        else {
            this.plugins = new LinkedHashMap<>(plugins);
        }
    }

    public List<Rotor> getUsedRotors() {
        return this.usedRotors;
    }

    public Reflector getUsedReflector() {
        return this.usedReflector;
    }

    public Map<String, String> getPlugins() {
        return this.plugins;
    }
}
