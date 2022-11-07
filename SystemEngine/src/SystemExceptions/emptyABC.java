package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class emptyABC extends Exception {
    public emptyABC() {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("ABC should not be empty!");
//        a.setHeaderText("Empty ABC");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
    }
    @Override
    public String getMessage() {
        return "Empty ABC\nABC should not be empty!";
    }
}