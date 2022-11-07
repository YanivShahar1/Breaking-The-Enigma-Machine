package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

import java.util.List;

public class keyIsNotInABC extends Exception {
    private String key;
    public keyIsNotInABC(String key) {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("ABC supplied in XML file does not contain the character -> " + key);
//        a.setHeaderText("Key Is Not In ABC");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
        this.key = key;
    }
    @Override
    public String getMessage() {
        return "Key Is Not In ABC\nABC supplied in XML file does not contain the character -> " + key;
    }
}

