package DTO;

import java.io.Serializable;

public class StringPair implements Serializable {
    private String input;
    private String output;
    private long elapsedEncryptionTime;
    public StringPair(String input, String output, long elapsedEncryptionTime) {
        this.input = input;
        this.output = output;
        this.elapsedEncryptionTime = elapsedEncryptionTime;
    }

    public String getInput() {
        return this.input;
    }

    public String getOutput() {
        return this.output;
    }

    public long getTimer() {
        return this.elapsedEncryptionTime;
    }
}
