package DTO;

import java.io.Serializable;
import java.util.List;

public class DTOMachineComponents implements Serializable {
    private int numOfOptionalRotors;
    private int numOfUsedRotors;
    private int numOfOptionalReflectors;
    private int numOfProcessedMessages;
    List<String> abcList;
    private DTOCodeConfigurationDescriptor current;

    public DTOMachineComponents(int numOfOptionalRotors, int numOfUsedRotors, int numOfOptionalReflectors,
                                int numOfProcessedMessages, List<String> abcList, DTOCodeConfigurationDescriptor current) {
        this.numOfOptionalRotors = numOfOptionalRotors;
        this.numOfUsedRotors = numOfUsedRotors;
        this.numOfOptionalReflectors = numOfOptionalReflectors;
        this.numOfProcessedMessages = numOfProcessedMessages;
        this.abcList = abcList;
        this.current = current;
    }
    public int getNumOfOptionalRotors() {
        return this.numOfOptionalRotors;
    }

    public int getNumOfUsedRotors() {
        return this.numOfUsedRotors;
    }

    public int getNumOfOptionalReflectors() {
        return this.numOfOptionalReflectors;
    }

    public int getNumOfProcessedMessages() {
        return this.numOfProcessedMessages;
    }

    public DTOCodeConfigurationDescriptor getCurrent() {
        return this.current;
    }

    public List<String> getAbcList() {
        return abcList;
    }
}