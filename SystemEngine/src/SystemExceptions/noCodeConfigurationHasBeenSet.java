package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class noCodeConfigurationHasBeenSet extends Exception {
    public noCodeConfigurationHasBeenSet() {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("You must set code configuration first!");
//        a.setHeaderText("No Code Configuration Has Been Set");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
    }
    @Override
    public String getMessage() {
        return "No Code Configuration Has Been Set\nYou must set code configuration first!";
    }
}
