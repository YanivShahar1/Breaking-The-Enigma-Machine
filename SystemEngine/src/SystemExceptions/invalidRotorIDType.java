package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class invalidRotorIDType extends Exception {
    private String id;
    public invalidRotorIDType(String id) {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("id = " + id + " but rotor ID must be an integer!");
//        a.setHeaderText("Invalid Rotor ID Type");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
        this.id = id;
    }
    @Override
    public String getMessage() {
        return "Invalid Rotor ID Type\nid = " + id + " but rotor ID must be an integer!";
    }
}
