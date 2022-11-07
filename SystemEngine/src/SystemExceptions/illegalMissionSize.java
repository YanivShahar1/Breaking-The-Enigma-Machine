package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class illegalMissionSize extends Exception {
    private long numOfOptions;
    public illegalMissionSize(long numOfOptions) {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("Mission size should be an integer bigger than 0 and smaller than " + numOfOptions);
//        a.setHeaderText("Illegal Mission Size");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
        this.numOfOptions = numOfOptions;
    }
    @Override
    public String getMessage() {
        return "Illegal Mission Size\nMission size should be an integer bigger than 0 and smaller than "
                + numOfOptions;
    }
}
