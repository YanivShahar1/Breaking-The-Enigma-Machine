package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class noProcessingModeHasBeenSet extends Exception {
    public noProcessingModeHasBeenSet() {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("You have to set Manual or Automatic processing mode first!");
//        a.setHeaderText("No Processing Mode Has Been Set");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
    }
    @Override
    public String getMessage() {
        return "No Processing Mode Has Been Set\nYou have to set Manual or Automatic processing mode first!";
    }
}
