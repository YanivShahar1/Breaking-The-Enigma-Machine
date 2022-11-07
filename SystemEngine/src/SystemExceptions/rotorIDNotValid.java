package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class rotorIDNotValid extends Exception {
    private int id;
    private int rotorsSize;
    public rotorIDNotValid(int id, int rotorsSize) {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText();
//        a.setHeaderText("Rotor ID Not Valid");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
        this.id = id;
        this.rotorsSize = rotorsSize;
    }
    @Override
    public String getMessage() {
        return "id = " + id + ". But id should be <= " + rotorsSize + " and >= 1";
    }
}

