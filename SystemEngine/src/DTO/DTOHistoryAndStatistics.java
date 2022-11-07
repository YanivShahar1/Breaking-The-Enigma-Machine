package DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DTOHistoryAndStatistics implements Serializable {
    private DTOCodeConfigurationDescriptor codeConfigurationDescriptor;
    private List<StringPair> enigmaInputOutputList;

    public DTOHistoryAndStatistics(DTOCodeConfigurationDescriptor codeConfigurationDescriptor) {
        this.codeConfigurationDescriptor = codeConfigurationDescriptor;
        enigmaInputOutputList = new ArrayList<>();
    }
    public DTOCodeConfigurationDescriptor getCodeConfigurationDescriptor() {
        return this.codeConfigurationDescriptor;
    }
    public List<StringPair> getEnigmaInputOutputList() {
        return this.enigmaInputOutputList;
    }
}
