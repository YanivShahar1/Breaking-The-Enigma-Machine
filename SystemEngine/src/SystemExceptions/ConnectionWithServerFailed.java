package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class ConnectionWithServerFailed extends Exception {
    public ConnectionWithServerFailed(String errorDetails) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(errorDetails);
        a.setHeaderText("Connection With Server Failed");
        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        a.show();
    }
}
