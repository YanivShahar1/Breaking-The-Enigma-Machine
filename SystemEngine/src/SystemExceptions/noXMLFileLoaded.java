package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class noXMLFileLoaded extends Exception {
    public noXMLFileLoaded() {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("You must load an XML File first!");
//        a.setHeaderText("No XML File Loaded");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
    }
    @Override
    public String getMessage() {
        return "No XML File Loaded\nYou must load an XML File first!";
    }
}
