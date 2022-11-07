package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class invalidReflectorID extends Exception {
    private String id;
    private int reflectorAmount;
    public invalidReflectorID(String id, int reflectorAmount) {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("Reflector id = " + id + " while reflector "
//                + "id should be >= 1 and <= " + reflectorAmount + "\n"
//                + "It can be entered as integer either as roman.\n"
//        + "For example: in case of 3 reflectors exist, you can only choose reflector id "
//        + "between 1 to 3 or I to III");
//        a.setHeaderText("Invalid Reflector ID");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
        this.reflectorAmount = reflectorAmount;
        this.id = id;
    }
    @Override
    public String getMessage() {
        return "Invalid Reflector ID\nReflector id = " + id + " while reflector "
                + "id should be >= 1 and <= " + reflectorAmount + "\n"
                + "It can be entered as integer either as roman.\n"
                + "For example: in case of 3 reflectors exist, you can only choose reflector id "
                + "between 1 to 3 or I to III";
    }
}