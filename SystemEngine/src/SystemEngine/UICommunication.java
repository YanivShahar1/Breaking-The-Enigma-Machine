package SystemEngine;

import DTO.DTOHistoryAndStatistics;
import DTO.DTOMachineComponents;
import SystemExceptions.invalidABCLetter;

import java.util.List;

public interface UICommunication {
    DTOMachineComponents getMachineComponents() throws CloneNotSupportedException;
    String inputProcessing(String input, boolean isManual) throws invalidABCLetter, CloneNotSupportedException;
    void resetCodeConfiguration() throws CloneNotSupportedException;
    List<DTOHistoryAndStatistics> getHistoryAndStatistics();
}
