package SystemExceptions;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class fileNotExist extends Exception {
    private String fileName;

    public fileNotExist(String fileName) {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setContentText("File " + fileName + " not exist!");
//        a.setHeaderText("File Not Exist");
//        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        a.show();
        this.fileName = fileName;
    }
    @Override
    public String getMessage() {
        return "File Not Exist\nFile " + fileName + " not exist!";
    }
}