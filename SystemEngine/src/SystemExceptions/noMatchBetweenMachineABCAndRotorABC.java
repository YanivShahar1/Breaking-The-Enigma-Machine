package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class noMatchBetweenMachineABCAndRotorABC extends Exception {
    private int rotorABCSize;
    private int MachineABCSize;
    public noMatchBetweenMachineABCAndRotorABC(int rotorABCSize, int MachineABCSize) {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("Rotor ABC size = " + rotorABCSize + "\n"
//        + "Machine ABC size = " + MachineABCSize + "\n"
//        + "Machine ABC size must equal rotor ABC size!");
//        a.setHeaderText("No Match Between Machine ABC And Rotor ABC");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
        this.MachineABCSize = MachineABCSize;
        this.rotorABCSize = rotorABCSize;
    }
    @Override
    public String getMessage() {
        return "No Match Between Machine ABC And Rotor ABC\nRotor ABC size = " + rotorABCSize + "\n"
                + "Machine ABC size = " + MachineABCSize + "\n"
                + "Machine ABC size must equal rotor ABC size!";
    }
}
