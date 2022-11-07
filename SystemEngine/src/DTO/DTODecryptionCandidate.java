package DTO;

import Machine.Direction;
import Machine.Reflector;
import Machine.Rotor;
import javafx.scene.control.TextInputControl;

import java.util.List;

// == taken from agentPackage class
public class DTODecryptionCandidate {
    private String decryption;
    private String agentName;
    private String allyName;
    private DTOCodeConfigurationDescriptor currentConfiguration;
    private String configuration;

    public DTODecryptionCandidate(String allyName, String decryption, String agentName,
                                  DTOCodeConfigurationDescriptor currentConfiguration) {
        this.allyName = allyName;
        this.agentName = agentName;
        this.decryption = decryption;
        this.currentConfiguration = currentConfiguration;
        this.configuration = printCurrentCodeConfiguration(this.currentConfiguration);
    }

    public String getDecryption() {
        return decryption;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getConfiguration() {
        return configuration;
    }
    public DTOCodeConfigurationDescriptor getCurrentConfiguration() {
        return currentConfiguration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String getAllyName() {
        return allyName;
    }

    private String printCurrentCodeConfiguration(DTOCodeConfigurationDescriptor codeConfigurationDescriptor) {
        String str = "";
        str += printRotorsIDAndSCurrentWindowDistance(codeConfigurationDescriptor.getUsedRotors());
        str += printRotorsCurrentPositions(codeConfigurationDescriptor.getUsedRotors());
        str += printReflectorID(codeConfigurationDescriptor.getUsedReflector());
        return str;
    }

    private String printReflectorID(Reflector reflector) {
        String str = "";
        str += "<" + reflector.getId() + ">\n";
        return str;
    }

    private String printRotorsCurrentPositions(List<Rotor> rotorsList) {
        String str = "";
        str += "<";
        for (int i = rotorsList.size() - 1; i >= 1; i--) {
            str += rotorsList.get(i).convertIndexToKey(1, Direction.RIGHT_TO_LEFT); //RIGHT_TO_LEFT
        }
        str += ">";
        return str;
    }

    private String printRotorsIDAndSCurrentWindowDistance(List<Rotor> rotorsList) {
        String str = "";
        str += "<";
        for (int i = rotorsList.size() - 1; i >= 1; i--) {
            if (i != rotorsList.size() - 1) {
                str += ",";
            }
            str += rotorsList.get(i).id + "(" + calcWindowCurrentDistanceFromNotch(rotorsList.get(i)) + ")";
        }
        str += ">";
        return str;
    }

    private int calcWindowCurrentDistanceFromNotch(Rotor rotor) {
        int distance = (rotor.getNotch() - rotor.numOfRotations) % rotor.getRotorSize() - 1;
        if (distance < 0) {
            distance = rotor.getRotorSize() + distance;
        }
        return distance;
    }
}

