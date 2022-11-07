package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class abcIsOdd extends Exception {
    public abcIsOdd() {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("ABC is odd! ABC should contain even number of letters!");
//        a.setHeaderText("ABC Is Odd");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
    }
    @Override
    public String getMessage() {
        return "ABC Is Odd\nABC is odd! ABC should contain even number of letters!";
    }
}
