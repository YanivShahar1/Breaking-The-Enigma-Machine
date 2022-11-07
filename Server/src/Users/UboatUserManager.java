package Users;

import java.util.*;

import SystemEngine.SystemEngine;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UboatUserManager {

    private final Set<String> usersSet;
    private Map<String, SystemEngine> uboatToSystemEngine;
    private Map<String, Boolean> readyUboats;
    private Map<String,String> uboatInputs;
    private Map<String,String> uboatToHisCorrectEncryption;
    private Map<String, String> uboatToAllyNameWinner;
    private Set<String> allBattleFieldNames;

    public UboatUserManager() {
        usersSet = new HashSet<>();
        uboatToSystemEngine = new LinkedHashMap<>();
        readyUboats = new HashMap<>();
        uboatInputs = new HashMap<>();
        uboatToAllyNameWinner = new HashMap<>();
        uboatToHisCorrectEncryption = new HashMap<>();
        allBattleFieldNames = new HashSet<>();
    }

    public Set<String> getAllBattleFieldNames() {
        return allBattleFieldNames;
    }

    public synchronized void setUboatReady(String uboatName){
        readyUboats.put(uboatName, true);
    }
    public synchronized Boolean isUboatReady(String uboatName){
        return readyUboats.get(uboatName);
    }
    public synchronized void addUserName(String username) {
        usersSet.add(username);

    }

    public Map<String, String> getUboatToHisCorrectEncryption() {
        return uboatToHisCorrectEncryption;
    }

    public synchronized void addUboat(String username, SystemEngine systemEngine) {
        uboatToSystemEngine.put(username,systemEngine);

    }
    public synchronized void addUboatInput(String uboatName, String input) {
        uboatInputs.put(uboatName,input);

    }

    public Map<String, String> getUboatToAllyNameWinner() {
        return uboatToAllyNameWinner;
    }

    public Map<String, String> getUboatInputs() {
        return uboatInputs;
    }

    public synchronized void removeUser(String username) {
        usersSet.remove(username);
        uboatToSystemEngine.remove(username);
        readyUboats.remove(username);
        uboatInputs.remove(username);
        uboatToHisCorrectEncryption.remove(username);
        uboatToAllyNameWinner.remove(username);
    }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public synchronized Map<String,SystemEngine> getAllUboats() {
        return (uboatToSystemEngine);
    }

    public boolean isUserExists(String username) {
        return usersSet.contains(username);
    }

    public Map<String, Boolean> getReadyUboats() {
        return readyUboats;
    }
}