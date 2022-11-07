package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class doubleMappingRotor extends Exception {
    private int rotorID;
    private String key;
    public doubleMappingRotor(int rotorID, String key) {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("Rotor #" + rotorID + " has double mapping issue: the key "
//                + key + " appears more than once!");
//        a.setHeaderText("Double Mapping Rotor");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
        this.key = key;
        this.rotorID = rotorID;
    }
    @Override
    public String getMessage() {
        return "Double Mapping Rotor\nRotor #" + rotorID + " has double mapping issue: the key "
                + key + " appears more than once!";
    }
}

