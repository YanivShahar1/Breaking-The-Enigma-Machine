package Machine;

import Machine.CTE.CTEPositioning;
import SystemExceptions.doubleMappingRotor;
import SystemExceptions.keyIsNotInABC;
import SystemExceptions.noMatchBetweenMachineABCAndRotorABC;
import java.io.Serializable;
import java.util.*;

public class Rotor implements Cloneable, Serializable {
    private int indexRight = 1, indexLeft = 1;
    private Map<String, Integer> left, right;
    private List<String> leftStartingArray, rightStartingArray;
    private int rotorSize;
    private int notch;
    private Map<String, Integer> ABC;
    public int numOfRotations = 0;
    private int startingNumOfRotations = 0;
    private String startingPosition;
    public int id;
    public Rotor() {
        left = new LinkedHashMap<>();
        right = new LinkedHashMap<>();
        leftStartingArray = new ArrayList<>();
        rightStartingArray = new ArrayList<>();
        leftStartingArray.add("dummy");
        rightStartingArray.add("dummy");
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStartingNumOfRotations() {
        return this.startingNumOfRotations;
    }

    public void setPosition(List<CTEPositioning> positionList) throws doubleMappingRotor,
            keyIsNotInABC, noMatchBetweenMachineABCAndRotorABC {
        for (CTEPositioning ctePosition : positionList) {
            String rightPosition = ctePosition.getRight().toUpperCase();
            String leftPosition = ctePosition.getLeft().toUpperCase();
            if (right.containsKey(rightPosition)) {
                throw new doubleMappingRotor(id, rightPosition);
            }
            if (!ABC.containsKey(rightPosition)) {
                throw new keyIsNotInABC(rightPosition);
            }
            right.put(rightPosition, indexRight++);

            if (left.containsKey(leftPosition)) {
                throw new doubleMappingRotor(id, leftPosition);
            }
            if (!ABC.containsKey(leftPosition)) {
                throw new keyIsNotInABC(leftPosition);
            }
            left.put(leftPosition, indexLeft++);

            leftStartingArray.add(leftPosition);
            rightStartingArray.add(rightPosition);
        }
        this.rotorSize = left.size();
        if (rotorSize != this.ABC.size()) {
            throw new noMatchBetweenMachineABCAndRotorABC(rotorSize, this.ABC.size());
        }
    }
    public void setABC(Map<String, Integer> ABC) {
        this.ABC = new LinkedHashMap<>(ABC);
    }
    public String convertIndexToKey(int index, Direction direction) {
        int res = (index + numOfRotations) % rotorSize;
        if (res == 0) {
            res = rotorSize;
        }
        if (direction == Direction.LEFT_TO_RIGHT) { // LEFT_TO_RIGHT
            return leftStartingArray.get(res);
        }
        else { // RIGHT_TO_LEFT
            return rightStartingArray.get(res);
        }
    }
    public int getIndexOutputOfRotor(String key, Direction direction) {
        int res;
        if (direction == Direction.LEFT_TO_RIGHT) {// LEFT_TO_RIGHT
            res = (right.get(key) - numOfRotations) % rotorSize;
        }
        else { // RIGHT_TO_LEFT
            res = (left.get(key) - numOfRotations) % rotorSize;
        }
        while (res <= 0) {
            res += rotorSize;
        }
        return  res;
    }
    public void setStartingPosition(String start) {
        numOfRotations = right.get(start) - 1;
        startingNumOfRotations = numOfRotations;
        startingPosition = start;
    }
    public void rotateRotor() {
        this.numOfRotations++;
    }
    public boolean isNextRotorShouldMove() {
        if ((numOfRotations + 1) % rotorSize == 0) {
            return notch == rotorSize;
        }
        else {
            return notch == (numOfRotations + 1) % rotorSize;
        }
    }
    public String getStartingPosition() {
        return this.startingPosition;
    }
    public void setNotch(int notch) {
            this.notch = notch;
    }
    public int getNotch() {
        return this.notch;
    }
    public int getRotorSize() {
        return this.rotorSize;
    }

    public List<String> getLeftStartingArray() {
        return leftStartingArray;
    }

    public List<String> getRightStartingArray() {
        return rightStartingArray;
    }

    public Map<String, Integer> getABC() {
        return ABC;
    }
    public Map<String, Integer> getLeft() {
        return left;
    }
    public Map<String, Integer> getRight() {
        return right;
    }
    @Override
    public Rotor clone() {
        Rotor newRotor = new Rotor();
        newRotor.rotorSize = this.rotorSize;
        newRotor.notch = this.notch;
        newRotor.id = this.id;
        newRotor.ABC = new LinkedHashMap<>(this.ABC);
        newRotor.numOfRotations = this.numOfRotations;
        newRotor.startingNumOfRotations= this.startingNumOfRotations;

        newRotor.startingPosition = this.startingPosition;

        newRotor.right = new LinkedHashMap<>(this.getRight());
        newRotor.left= new LinkedHashMap<>(this.getLeft());

        newRotor.rightStartingArray = new ArrayList<>(this.rightStartingArray);
        newRotor.leftStartingArray = new ArrayList<>(this.leftStartingArray);

        return newRotor;
    }
}
