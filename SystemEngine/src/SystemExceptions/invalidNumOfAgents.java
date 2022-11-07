package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class invalidNumOfAgents extends Exception {
    public invalidNumOfAgents() {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("Num of agents must be between 2 and 50!");
//        a.setHeaderText("Invalid Num Of Agents");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
    }
    @Override
    public String getMessage() {
        return "Invalid Num Of Agents\nNum of agents must be between 2 and 50!";
    }
}
