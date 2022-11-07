package Machine;

import Machine.CTE.CTEReflect;
import SystemExceptions.doubleMappingReflector;
import SystemExceptions.invalidReflectorInputOutput;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Reflector implements Cloneable, Serializable {
    private Map<Integer, Integer> pairs;
    private Map<String, Integer> ABC;
    private String id;
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return this.id;
    }
    public Reflector() {
        this.pairs = new LinkedHashMap<>();
        this.ABC = new LinkedHashMap<>();
    }
    public Map<Integer, Integer> getPairs() {
        return pairs;
    }
    public Map<String, Integer> getABC() {
        return ABC;
    }
    public void setPairs(Map<Integer, Integer> pairs) {
        this.pairs = new LinkedHashMap<>(pairs);
    }
    public void setABC(Map<String, Integer> ABC) {
        this.ABC = new LinkedHashMap<>(ABC);
    }
    public void setPairs(List<CTEReflect> reflectList) throws invalidReflectorInputOutput,
            doubleMappingReflector {
        for (CTEReflect cteReflect : reflectList) {
            if (pairs.containsKey(cteReflect.getInput())) {
                throw new doubleMappingReflector(this.id, cteReflect.getInput());
            }
            if (pairs.containsKey(cteReflect.getOutput())) {
                throw new doubleMappingReflector(this.id, cteReflect.getOutput());
            }
            if (cteReflect.getInput() < 1 || cteReflect.getInput() > this.ABC.size()) {
                throw new invalidReflectorInputOutput(this.id, cteReflect.getInput(), this.ABC.size());
            }
            if (cteReflect.getOutput() < 1 || cteReflect.getOutput() > this.ABC.size()) {
                throw new invalidReflectorInputOutput(this.id, cteReflect.getOutput(), this.ABC.size());
            }
            pairs.put(cteReflect.getInput(), cteReflect.getOutput());
            pairs.put(cteReflect.getOutput(), cteReflect.getInput());
        }
    }
    public int getOutput(int input) {
        return pairs.get(input);
    }
    @Override
    public Reflector clone() throws CloneNotSupportedException {
        Reflector newReflector = new Reflector();
        newReflector.setPairs(this.getPairs());
        newReflector.setABC(this.getABC());
        newReflector.setId(this.id);
        return newReflector;
    }
}
