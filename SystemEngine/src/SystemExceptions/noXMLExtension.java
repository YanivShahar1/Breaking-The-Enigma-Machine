package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class noXMLExtension extends Exception {
    public noXMLExtension() {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("File should be an XML!");
//        a.setHeaderText("No XML Extension");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
    }
    @Override
    public String getMessage() {
        return "No XML Extension\nFile should be an XML!";
    }
}