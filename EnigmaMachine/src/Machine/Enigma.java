package Machine;

import SystemEngine.SystemEngine;

import java.io.Serializable;
import java.sql.Ref;
import java.util.*;
import java.util.function.Function;

public class Enigma implements Serializable, Cloneable {
    private List<Rotor> rotors;
    private Reflector reflector;
    private Keyboard plugins;
    private Map<String, Integer> ABC;
    private List<String> ABCList;

    @Override
    public Enigma clone() throws CloneNotSupportedException {
        //return new Enigma(rotors, reflector, ABC, plugins.getPlugins());
        return new Enigma(cloneRotorList(rotors), reflector.clone(),
                new LinkedHashMap<>(ABC), new LinkedHashMap<>(plugins.getPlugins()));
    }
    public Enigma() {}
    public Enigma(List<Rotor> rotorsList, Reflector reflector, Map<String, Integer> ABC,
                  Map<String, String> plugins) throws CloneNotSupportedException {
        this.rotors = cloneRotorList(rotorsList);
        this.reflector = reflector.clone();
        this.ABC = new LinkedHashMap<>(ABC);
        mapToArr();
        this.plugins = new Keyboard(plugins);
    }
    public Map<String, String> getPlugins() {
        return this.plugins.getPlugins();
    }
    public String encodeDecode(String letter) {

        //plugin
        String pluginOfLetter = plugins.getPlugin(letter);
        int index = ABC.get(pluginOfLetter);
        updateRotors();

        // rotors right to left
        boolean toContinue = true;
        for (Rotor rotor : rotors) {
            if (toContinue)
            {
                toContinue = false;
                continue;
            }
            String rightKeyRotor = rotor.convertIndexToKey(index, Direction.RIGHT_TO_LEFT);
            index = rotor.getIndexOutputOfRotor(rightKeyRotor, Direction.RIGHT_TO_LEFT);
        }
        // reflector
        index = reflector.getOutput(index);

        // rotors left to right
        for (int i = rotors.size() - 1; i > 0; i--) {
            String leftKeyRotor = rotors.get(i).convertIndexToKey(index, Direction.LEFT_TO_RIGHT);
            index = rotors.get(i).getIndexOutputOfRotor(leftKeyRotor, Direction.LEFT_TO_RIGHT);
        }
        String rotorResultLetter = ABCList.get(index);

        // plugin
        return plugins.getPlugin(rotorResultLetter);
    }
    private void mapToArr() {
        ABCList = new ArrayList<>(ABC.size());
        ABCList.add(null); //dummy
        for (String s : ABC.keySet()) {
            ABCList.add(s);
        }
    }
    public void updateRotors() {
        rotors.get(1).rotateRotor();
        for (int i = 1; i < rotors.size() - 1; i++) {
            if (rotors.get(i).isNextRotorShouldMove()) {
                rotors.get(i + 1).rotateRotor();
            }
            else {
                break;
            }
        }
    }

    public Map<String, Integer> getABC() {
        return this.rotors.get(1).getABC();
    }

    public List<String> getABCList() {
        return this.ABCList;
    }

    public List<Rotor> getRotors() {
        return this.rotors;
    }
    public Reflector getReflector() {
        return this.reflector;
    }
    public List<Rotor> cloneRotorList(List<Rotor> rotorList) throws CloneNotSupportedException {
        if (rotorList.size() == 0) {
            return null;
        }

        List<Rotor> clonedList = new ArrayList<>();
        clonedList.add(null); //dummy
        boolean toContinue = true;

        for (Rotor temp: rotorList){
            if (toContinue){
                toContinue = false;
                continue;
            }
            clonedList.add(temp.clone());
        }
        return clonedList;
    }
    public List<Reflector> cloneReflectorList(List<Reflector> reflectorList) throws CloneNotSupportedException {
        if (reflectorList.size() == 0) {
            return null;
        }
        List<Reflector> clonedList = new ArrayList<>();
        clonedList.add(null); //dummy
        boolean toContinue = true;

        for (Reflector temp: reflectorList){
            if (toContinue){
                toContinue = false;
                continue;
            }
            clonedList.add(temp.clone());
        }
        return clonedList;
    }
    public boolean updateAllNextPositions() {
        boolean finished = false;
        int i = 1;
        int counter = 0;

        for (Rotor rotor : rotors) {
            if(rotor == null) {
                continue;
            }
            if (ABC.get(rotor.convertIndexToKey(1, Direction.RIGHT_TO_LEFT)) == ABC.size()){
                counter++;
            }
        }
        if (counter == rotors.size() - 1) {
            return false;
        }
        while (!finished && i < rotors.size()) {
            int currentIndex = ABC.get(rotors.get(i).convertIndexToKey(1, Direction.RIGHT_TO_LEFT));
            if (currentIndex < ABC.size()) {
                rotors.get(i).setStartingPosition(ABCList.get(currentIndex + 1));
                finished = true;
            }
            else {
                rotors.get(i).setStartingPosition(ABCList.get(1));
                i++;
            }
        }
        return true;
    }
    public void setReflector(Reflector reflector) throws CloneNotSupportedException {
        this.reflector = reflector.clone();
    }
    public void setRotors(List<Rotor> rotorList) throws CloneNotSupportedException {
        this.rotors = cloneRotorList(rotorList);
    }
}

