package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class invalidNotchPosition extends Exception {
    private int rotorID;
    private int notch;
    private int rotorSize;
    public invalidNotchPosition(int rotorID, int notch, int rotorSize) {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("Rotor #" + rotorID + " has notch = " + notch +
//                " while rotor has " + rotorSize + " inputs / outputs.\n"
//                + "Notch should be >= 1 and <= " + rotorSize);
//        a.setHeaderText("Invalid Notch Position");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
        this.notch = notch;
        this.rotorSize = rotorSize;
        this.rotorID = rotorID;
    }
    @Override
    public String getMessage() {
        return "Invalid Notch Position\nRotor #" + rotorID + " has notch = " + notch +
                " while rotor has " + rotorSize + " inputs / outputs.\n"
                + "Notch should be >= 1 and <= " + rotorSize;
    }
}