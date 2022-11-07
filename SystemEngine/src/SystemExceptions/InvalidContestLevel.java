package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class InvalidContestLevel extends Exception {
    private String level;
    public InvalidContestLevel(String level) {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("Level " + level + " is incorrect!");
//        a.setHeaderText("Invalid Contest Level");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
        this.level = level;
    }
    @Override
    public String getMessage() {
        return "Invalid Contest Level\nLevel " + level + " is incorrect!";
    }
}
