package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class InvalidNumOfAllies extends Exception {
    public InvalidNumOfAllies() {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("Num of allies should be >= 1 !");
//        a.setHeaderText("Invalid Num Of Allies");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
    }
    @Override
    public String getMessage() {
        return "Invalid Num Of Allies\nNum of allies should be >= 1 !";
    }
}
