package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class EmptyBattleFieldName extends Exception {
    public EmptyBattleFieldName() {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("BattleField name should not be empty !");
//        a.setHeaderText("Empty Battle Field Name");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
    }
    public String getMessage() {
        return "Empty Battle Field Name\nBattleField name should not be empty !";
    }
}
