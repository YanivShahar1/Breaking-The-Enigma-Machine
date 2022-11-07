package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class doubleRotorID extends Exception {
    private int id;
    public doubleRotorID(int id) {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("Rotor id = " + id + " already exists!");
//        a.setHeaderText("Double Rotor ID");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
        this.id = id;
    }
    @Override
    public String getMessage() {
        return "Double Rotor ID\nRotor id = " + id + " already exists!";
    }
}