package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class pluginIsOdd extends Exception {
    public pluginIsOdd() {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("Plugin is odd! Plugin should contain even number of letters!");
//        a.setHeaderText("Plugin Is Odd");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
    }
    @Override
    public String getMessage() {
        return "Plugin Is Odd\nPlugin is odd! Plugin should contain even number of letters!";
    }
}
