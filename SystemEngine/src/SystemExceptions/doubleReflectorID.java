package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class doubleReflectorID extends Exception {
    private String id;
    public doubleReflectorID(String id) {
//        System.out.println();
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("Reflector id = " + id + " already exists!");
//        a.setHeaderText("Double Reflector ID");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
        this.id = id;
    }
    @Override
    public String getMessage() {
        return "Double Reflector ID\nReflector id = " + id + " already exists!";
    }
}
