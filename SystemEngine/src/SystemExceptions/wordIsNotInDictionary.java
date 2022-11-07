package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class wordIsNotInDictionary extends Exception {
    public wordIsNotInDictionary() {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("One or more of the words are not in the dictionary");
//        a.setHeaderText("Word Is Not In Dictionary");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
    }
    @Override
    public String getMessage() {
        return "Word Is Not In Dictionary\nOne or more of the words are not in the dictionary";
    }
}
