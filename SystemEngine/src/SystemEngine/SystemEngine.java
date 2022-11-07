package SystemEngine;

import DTO.*;
import DTO.ContestStatus;
import DecryptionManager.Level;
import Machine.CTE.*;
import Machine.Enigma;
import Machine.Reflector;
import Machine.Rotor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.*;

import java.lang.Exception;
import SystemExceptions.*;

public class SystemEngine extends Exception implements Engine, UICommunication, Serializable {
    private Enigma enigma;
    //private DM dm;
    private int rotorsCount = 0;
    private int numOfProcessedMessages;
    private int numOfCodeConfigurationChanges = 0;
    private List<Rotor> rotorsList;
    private List<Rotor> usedRotorsList;
    private Reflector usedReflector;
    private List<Reflector> reflectorsList;
    private Map<String, Integer> ABC;
    private Map<String, String> plugins;
    private List<Rotor> containerRotorsList;
    private List<Reflector> containerReflectorsList;
    private Map<String, Integer> containerABC;
    private List<DTOHistoryAndStatistics> DTOHistoryList;
    private Boolean[] allRotorID;
    private Boolean[] allReflectorID;
    private List<String> abcList;
    private Set<String> dictionary;
    private Set<String> excludeChars;

    private int numOfAlliesNeeded;
    private int numOfRegisteredAliies;
    private String battleFieldName;
    private String uboatName;
    private Level contestLevel;
    private ContestStatus status;


    public SystemEngine() {
        this.usedRotorsList = new ArrayList<>();
        this.usedReflector = new Reflector();
        this.plugins = new LinkedHashMap<>();
        this.ABC = new LinkedHashMap<>();
        this.abcList = new ArrayList<>();
        this.dictionary = new HashSet<>();
        this.excludeChars = new HashSet<>();
        status = ContestStatus.WAITING;
    }
    private int convertRomanLettersToInt(String roman) {
        int integer = -1;
        switch (roman) {
            case "I":
                integer = 1;
                break;
            case "II":
                integer = 2;
                break;
            case "III":
                integer = 3;
                break;
            case "IV":
                integer = 4;
                break;
            case "V":
                integer = 5;
                break;
        }
        return integer;
    }
    private String convertIntLettersToRoman(String integer) {
        String roman = integer;
        switch (integer) {
            case "1":
                roman = "I";
                break;
            case "2":
                roman = "II";
                break;
            case "3":
                roman = "III";
                break;
            case "4":
                roman = "IV";
                break;
            case "5":
                roman = "V";
                break;
        }
        return roman;
    }

    public List<String> getAbcList() {
        return abcList;
    }

    public void updateRotorsList(List<CTERotor> rotorsList) throws tooFewRotors,
            rotorsCountBiggerThanExist, rotorIDNotValid, doubleMappingRotor, keyIsNotInABC,
            noMatchBetweenMachineABCAndRotorABC, invalidNotchPosition, doubleRotorID {
        if (rotorsList.size() < 2) {
            throw new tooFewRotors(rotorsList.size());
        }
        if (this.rotorsCount > rotorsList.size()) {
            throw new rotorsCountBiggerThanExist(this.rotorsCount, rotorsList.size());
        }
        this.allRotorID = new Boolean[rotorsList.size() + 1];
        Arrays.fill(this.allRotorID, false);
        List<Rotor> tempRotorList = new ArrayList<>();
        tempRotorList.add(null); // dummy
        for (CTERotor rotor : rotorsList) {
            Rotor tempRotor = new Rotor();
            if (isRotorIDValid(rotor.getId(), rotorsList.size())) {
                tempRotor.setId(rotor.getId());
            }
            tempRotor.setABC(this.containerABC);
            tempRotor.setPosition(rotor.getCTEPositioning());
            if (rotor.getNotch() < 1 || rotor.getNotch() > tempRotor.getRotorSize()) {
                throw new invalidNotchPosition(rotor.getId(), rotor.getNotch(), tempRotor.getRotorSize());
            }
            tempRotor.setNotch(rotor.getNotch());
            tempRotorList.add(tempRotor);
        }
        this.containerRotorsList = new ArrayList<>(tempRotorList);
    }
    public void updateReflectorsList(List<CTEReflector> reflectorsList) throws invalidNumOfReflectors,
            invalidReflectorID, doubleReflectorID, invalidReflectorInputOutput,
            doubleMappingReflector {
        if (reflectorsList.size() < 1 || reflectorsList.size() > 5) {
            throw new invalidNumOfReflectors(reflectorsList.size());
        }
        List<Reflector> tempReflectorList = new ArrayList<>();
        this.allReflectorID = new Boolean[reflectorsList.size() + 1];
        Arrays.fill(this.allReflectorID, false);
        tempReflectorList.add(null); // dummy
        for (CTEReflector reflector : reflectorsList) {
            Reflector tempReflector = new Reflector();
            tempReflector.setABC(this.containerABC);
            if (isReflectorIDValid(reflector.getId())) {
                tempReflector.setId(reflector.getId());
            }
            tempReflector.setPairs(reflector.getCTEReflect());
            tempReflectorList.add(tempReflector);
        }
        this.containerReflectorsList = new ArrayList<>(tempReflectorList);
    }
    public void updateABC(String ABC) throws emptyABC, abcIsOdd, doubleLetterInABC {
        ABC = ABC.toUpperCase();
        int len = ABC.length();
        // checking if ABC contains characters
        if (len == 0) {
            throw new emptyABC();
        }
        // checking if ABC has an even number of characters
        if (len % 2 != 0) {
            throw new abcIsOdd();
        }
        Map<String, Integer> tempABC = new LinkedHashMap<>();
        for (int i = 0; i < len; i++) {
            char letter = ABC.charAt(i);
            // checking if ABC has one-time-appearance character
            if (tempABC.containsKey(Character.toString(letter))) {
                throw new doubleLetterInABC(letter);
            }
            tempABC.put(Character.toString(letter), i + 1);
            this.abcList.add(Character.toString(letter));
        }
        this.containerABC = new LinkedHashMap<>(tempABC);
    }
    private boolean isRotorIDValid(int id, int rotorsSize) throws rotorIDNotValid, doubleRotorID {
        if (id > rotorsSize || id <= 0) {
            throw new rotorIDNotValid(id, rotorsSize);
        }
        if (allRotorID[id]) {
            throw new doubleRotorID(id);
        }
        else {
            allRotorID[id] = true;
        }
        return true;
    }
    private boolean isReflectorIDValid(String id) throws doubleReflectorID, invalidReflectorID {
        int indexID = convertRomanLettersToInt(id.toUpperCase());
        if (indexID == -1 || indexID > allReflectorID.length - 1) {
            throw new invalidReflectorID(id, allReflectorID.length - 1);
        }
        else {
            allReflectorID[indexID] = true;
        }
        return true;
    }
    private String getFileExtension(String fileName) {
        String name = new String(fileName);
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) { // empty extension
            return "";
        }
        return name.substring(lastIndexOf);
    }
    @Override
    public void parseXML(InputStream fileInputStream, Set<String> allBattleFieldNames) throws JAXBException, emptyABC, doubleLetterInABC,
            abcIsOdd, noXMLExtension, fileNotExist, tooFewRotors, tooFewRotorsCount,
            rotorsCountBiggerThanExist, rotorIDNotValid, doubleMappingRotor, keyIsNotInABC,
            noMatchBetweenMachineABCAndRotorABC, invalidNotchPosition, invalidNumOfReflectors,
            doubleRotorID, invalidReflectorID, doubleReflectorID, invalidReflectorInputOutput,
            doubleMappingReflector, InterruptedException, CloneNotSupportedException, invalidNumOfAgents, InvalidNumOfAllies, InvalidContestLevel, EmptyBattleFieldName, BattleFieldNameIsAlreadyExists {
//        if (!getFileExtension(XMLFilePath).equals(".xml")) {
//            throw new noXMLExtension();
//        }
//        File file = new File(XMLFilePath);
//        if (!file.isFile()) {
//            throw new fileNotExist(XMLFilePath);
//        }
        JAXBContext jaxbContext = JAXBContext.newInstance(CTEEnigma.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

       // InputStream in = new ByteArrayInputStream(fileInputStream.getBytes(StandardCharsets.UTF_8));
        CTEEnigma en = (CTEEnigma) jaxbUnmarshaller.unmarshal(fileInputStream);


        //CTEEnigma en = (CTEEnigma) jaxbUnmarshaller.unmarshal(file);
        this.rotorsCount = en.getCTEMachine().getRotorsCount();
        if (this.rotorsCount < 2) {
            throw new tooFewRotorsCount(this.rotorsCount);
        }
        updateABC(en.getCTEMachine().getABC().trim());
        updateReflectorsList(en.getCTEMachine().getCTEReflectors().getCTEReflector());
        updateRotorsList(en.getCTEMachine().getCTERotors().getCTERotor());
        updateBattleField(en.getCTEBattlefield(), allBattleFieldNames);

        setDictionary(en.getCTEDecipher().getCTEDictionary());
       // dm.setNumOfMaxAgents(en.getCTEDecipher().getAgents());

        this.rotorsList = new ArrayList<>(this.containerRotorsList);
        this.reflectorsList = new ArrayList<>(this.containerReflectorsList);
        this.ABC = new LinkedHashMap<>(this.containerABC);
        numOfProcessedMessages = 0;
        numOfCodeConfigurationChanges = 0;
        Arrays.fill(allRotorID, false);
        Arrays.fill(allReflectorID, false);
        DTOHistoryList = new ArrayList<>();
        this.usedRotorsList = new ArrayList<>();
        this.usedReflector = new Reflector();
        this.plugins = new LinkedHashMap<>();
    }
    public void updateBattleField(CTEBattlefield cteBattlefield, Set<String> allBattleFieldNames) throws InvalidNumOfAllies, InvalidContestLevel, EmptyBattleFieldName, BattleFieldNameIsAlreadyExists {
        int tempNumOfAllies = cteBattlefield.getAllies();
        if (tempNumOfAllies <= 0) {
            throw new InvalidNumOfAllies();
        }
        String tempBattleFieldName = cteBattlefield.getBattleName();
        if (tempBattleFieldName.isEmpty()) {
            throw new EmptyBattleFieldName();
        }
        if (allBattleFieldNames.contains(tempBattleFieldName)) {
            throw new BattleFieldNameIsAlreadyExists();
        }
        Level tempContestLevel = convertStringToLevel(cteBattlefield.getLevel().toUpperCase());
        if (tempContestLevel == null) {
            throw new InvalidContestLevel(cteBattlefield.getLevel());
        }
        this.numOfAlliesNeeded = tempNumOfAllies;
        this.numOfRegisteredAliies = 0;
        this.status = ContestStatus.WAITING;
        this.battleFieldName = tempBattleFieldName;
        this.contestLevel = tempContestLevel;
    }

    public String getBattleFieldName() {
        return battleFieldName;
    }

    private Level convertStringToLevel(String strLevel) {
        Level level = null;
        switch (strLevel) {
            case "EASY":
                level = Level.EASY;
                break;
            case "MEDIUM":
                level = Level.MEDIUM;
                break;
            case "HARD":
                level = Level.HARD;
                break;
            case "IMPOSSIBLE":
                level = Level.IMPOSSIBLE;
                break;
        }
        return level;
    }
//    public DM getDm() {
//        return this.dm;
//    }
    public void setDictionary(CTEDictionary cteDictionary) {
        for (int i = 0; i < cteDictionary.getExcludeChars().length(); i++) {
            this.excludeChars.add(Character.toString(cteDictionary.getExcludeChars().toUpperCase().charAt(i)));
        }
        List<String> words = new ArrayList<String>(Arrays.asList(cteDictionary.getWords().trim().split(" ")));
        for (String word : words) {
            this.dictionary.add(word.toUpperCase().replaceAll("[" + cteDictionary.getExcludeChars() + "]", ""));
        }
    }

    public Set<String> getDictionary() {
        return dictionary;
    }

    @Override
    public DTOMachineComponents getMachineComponents() throws CloneNotSupportedException {
        DTOCodeConfigurationDescriptor current = new DTOCodeConfigurationDescriptor(usedRotorsList, usedReflector, plugins);
        return new DTOMachineComponents(rotorsList.size() - 1, rotorsCount,
                reflectorsList.size() - 1, numOfProcessedMessages, this.abcList, current);
    }
    public List<Integer> setUsedRotors(String rotorsID) throws doubleRotorID, rotorIDNotValid,
            invalidAmountOfRotors, invalidRotorIDType {
        Arrays.fill(allRotorID, false);
        List<String> listUsedRotors = new ArrayList<String>(Arrays.asList(rotorsID.split(",")));
        List<Integer> rotorsIDList = new ArrayList<>();
        if (listUsedRotors.size() != rotorsCount) {
            throw new invalidAmountOfRotors(listUsedRotors.size(), rotorsCount);
        }
        for (String id : listUsedRotors) {
            if (isInteger(id)) {
                int integerID = Integer.parseInt(id);
                if (isRotorIDValid(integerID, rotorsList.size() - 1)) {
                    rotorsIDList.add(integerID);
                }
            }
            else {
                throw new invalidRotorIDType(id);
            }
        }
        return rotorsIDList;
    }
    public boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        }catch(NumberFormatException e) {
            return false;
        }
    }
    public Reflector setUsedReflector(String reflectorID) throws CloneNotSupportedException,
            invalidReflectorID, doubleReflectorID {
        reflectorID = reflectorID.toUpperCase();
        if (isInteger(reflectorID)) {
            reflectorID = convertIntLettersToRoman(reflectorID);
        }
        if (!isReflectorIDValid(reflectorID)) {
            throw new invalidReflectorID(reflectorID, reflectorsList.size() - 1);
        }

        for (Reflector reflector : reflectorsList) {
            if (reflector == null) {
                continue;
            }
            if (reflector.getId().equals(reflectorID)) {
                return reflector.clone();
            }
        }
        return null;
    }
    public List<Rotor> setStartingUsedRotorsPositions(String startingPositions, List<Rotor> tempUsedRotorsList)
            throws keyIsNotInABC, invalidRotorsStartingPositions, CloneNotSupportedException {
        startingPositions = startingPositions.toUpperCase();

        int len = startingPositions.length();
        if (len != rotorsCount) {
            throw new invalidRotorsStartingPositions(len, rotorsCount);
        }
        Enigma tempEnigma = new Enigma();
        List<Rotor> tempUsedRotorsList2 = tempEnigma.cloneRotorList(tempUsedRotorsList);

        for (int i = 0; i < len; i++) {
            Character letter = startingPositions.charAt(len - i - 1); // flipping positions
            if (ABC.containsKey(Character.toString(letter))) {
                tempUsedRotorsList2.get(i + 1).setStartingPosition(Character.toString(letter));
            } else {
                throw new keyIsNotInABC(Character.toString(letter));
            }
        }
        return tempUsedRotorsList2;
    }
    public Map<String, String> setPlugins(String plugins) throws pluginIsOdd, doubleLetterInPlugin, CloneNotSupportedException, keyIsNotInABC {
        plugins = plugins.toUpperCase();
        if (plugins.length() % 2 != 0) {
            throw new pluginIsOdd();
        }
        Map<String, String> tempPlugin = new LinkedHashMap<>();
        for (int i = 0; i < plugins.length(); i+=2) {
            String firstInPair = Character.toString(plugins.charAt(i));
            String secondInPair = Character.toString(plugins.charAt(i + 1));
            if (!ABC.containsKey(firstInPair)) {
                throw new keyIsNotInABC(firstInPair);
            }
            if (!ABC.containsKey(secondInPair)) {
                throw new keyIsNotInABC(secondInPair);
            }
            if (!tempPlugin.containsKey(firstInPair) && !tempPlugin.containsKey(secondInPair)) {
                tempPlugin.put(firstInPair, secondInPair);
                tempPlugin.put(secondInPair, firstInPair);
            }
            else {
                throw new doubleLetterInPlugin(plugins.charAt(i));
            }
        }
        numOfCodeConfigurationChanges++;
        return tempPlugin;
    }
    public void setCodeConfiguration(List<Rotor> usedRotorsList, Reflector usedReflector, Map<String, String> usedPlugins) throws CloneNotSupportedException {
        // static cloneRotorList???????????
        Enigma tempEnigma = new Enigma();
        this.usedRotorsList = tempEnigma.cloneRotorList(usedRotorsList);
        this.usedReflector = usedReflector.clone();
        this.plugins = new LinkedHashMap<>(usedPlugins);
        numOfCodeConfigurationChanges++;
        DTOHistoryList.add(new DTOHistoryAndStatistics(new DTOCodeConfigurationDescriptor(this.usedRotorsList, this.usedReflector, this.plugins)));
    }
    public void randomRotors() {
        Random random = new Random();
        List<Integer> randomRotorsIDList = new ArrayList<>();
        boolean[] randomIDs = new boolean[rotorsList.size()];
        Arrays.fill(randomIDs, false);
        int id;
        while (randomRotorsIDList.size() != rotorsCount) {
            id = random.nextInt(rotorsList.size() - 1) + 1;
            if (!randomIDs[id]) {
                randomRotorsIDList.add(id);
                randomIDs[id] = true;
            }
        }
        this.usedRotorsList = createUsedRotorsList(randomRotorsIDList);
    }
    public void randomReflector() throws CloneNotSupportedException {
        Random random = new Random();
        int id = random.nextInt(reflectorsList.size() - 1) + 1;
        String romanID = convertIntLettersToRoman(String.valueOf(id));

        for (Reflector reflector : reflectorsList) {
            if (reflector == null) {
                continue;
            }
            if (reflector.getId().equals(romanID)) {
                this.usedReflector = reflector.clone();
            }
        }
    }
    public void randomPlugins() {
        Random random = new Random();
        int numOfPlugins = random.nextInt(ABC.size() / 2 + 1);
        Map<String, String> tempPlugin = new LinkedHashMap<>();
        List<String> ABCList = new ArrayList<>();
        for (String key : this.ABC.keySet()) {
            ABCList.add(key);
        }
        while (tempPlugin.size() != numOfPlugins * 2) {
            int plugIndex1 = random.nextInt(ABCList.size());
            String plugLetter1 = ABCList.get(plugIndex1);
            ABCList.remove(plugIndex1);
            int plugIndex2 = random.nextInt(ABCList.size());
            String plugLetter2 = ABCList.get(plugIndex2);
            ABCList.remove(plugIndex2);
            tempPlugin.put(plugLetter1, plugLetter2);
            tempPlugin.put(plugLetter2, plugLetter1);
        }
        this.plugins = new LinkedHashMap<>(tempPlugin);
    }
    public void randomStartingRotorsPositions() {
        List<String> ABCList = new ArrayList<>();
        for (String key : this.ABC.keySet()) {
            ABCList.add(key);
        }
        Random random = new Random();

        for (Rotor rotor : this.usedRotorsList) {
            if (rotor == null) {
                continue;
            }
            rotor.setStartingPosition(ABCList.get(random.nextInt(ABCList.size())));
        }
    }
    public void randomMachineComponents(boolean isPlugin) throws CloneNotSupportedException {
        randomRotors();
        randomStartingRotorsPositions();
        randomReflector();
        if (isPlugin) {
            randomPlugins();
        }
        setEnigmaMachine();
        numOfCodeConfigurationChanges++;
        DTOHistoryList.add(new DTOHistoryAndStatistics(new DTOCodeConfigurationDescriptor(usedRotorsList, usedReflector, plugins)));
    }
    public List<Rotor> createUsedRotorsList(List<Integer> rotorsIDList) {
        //usedRotorsList = new ArrayList<>();
       // usedRotorsList.add(null); // dummy
        List<Rotor> tempUsedRotorList = new ArrayList<>();
        tempUsedRotorList.add(null); // dummy
        for (Integer ID : rotorsIDList) {

            for (Rotor rotor : rotorsList) {
                if (rotor == null) {

                    continue;
                }
                if (rotor.id == ID) {
                    //usedRotorsList.add(1, rotor);
                    tempUsedRotorList.add(1, rotor);
                }
            }
        }
        return tempUsedRotorList;
    }
    public void checkValidityOfInput(String input) throws invalidABCLetter {
        List<String> badCharacters = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            if (!this.ABC.containsKey(Character.toString(input.charAt(i)))) {
                if(!badCharacters.contains(Character.toString(input.charAt(i)))){
                    badCharacters.add(Character.toString(input.charAt(i)));
                }
            }
        }
        if (badCharacters.size() != 0) {
            throw new invalidABCLetter(badCharacters);
        }
    }
    @Override
    public String inputProcessing(String input, boolean isManual) throws invalidABCLetter, CloneNotSupportedException {
        String output = "";
        input = input.toUpperCase();
        checkValidityOfInput(input);
        if (!isManual)
        {
            TimeMeasure timer = new TimeMeasure();
            timer.startTime();
            for (int i = 0; i < input.length(); i++) {
                output += enigma.encodeDecode(Character.toString(input.charAt(i)));
            }
            timer.stopTime();

            this.usedRotorsList = enigma.cloneRotorList(enigma.getRotors());

            DTOHistoryList.get(numOfCodeConfigurationChanges - 1).getEnigmaInputOutputList().add(new StringPair(input, output, timer.getElapsedTime()));
            numOfProcessedMessages++;
            return output;
        }
        else {
            String manualOutput = enigma.encodeDecode(Character.toString(input.charAt(0)));
            this.usedRotorsList = enigma.cloneRotorList(enigma.getRotors());
            return manualOutput;
        }
    }
    public void setDTOHistoryListManualMode(StringPair stringPair) {
        DTOHistoryList.get(numOfCodeConfigurationChanges - 1).getEnigmaInputOutputList().add(stringPair);
        numOfProcessedMessages++;
    }
    @Override
    public void resetCodeConfiguration() throws CloneNotSupportedException {
        for (Rotor rotor : usedRotorsList) {
            if (rotor == null) {
                continue;
            }
            rotor.numOfRotations = rotor.getStartingNumOfRotations();
        }
        setEnigmaMachine();
    }
    @Override
    public List<DTOHistoryAndStatistics> getHistoryAndStatistics() {
        return this.DTOHistoryList;
    }
    public void setEnigmaMachine() throws CloneNotSupportedException {
        this.enigma = new Enigma(this.usedRotorsList, this.usedReflector, this.ABC, this.plugins);
    }

    public int getRotorsCount() {
        return this.rotorsCount;
    }

    public List<Rotor> getRotorsList() {
        return rotorsList;
    }

    public List<Reflector> getReflectorsList() {
        return reflectorsList;
    }

    public Enigma getEnigma() {
        return this.enigma;
    }

    public DTOContestData getContestData(){

        return new DTOContestData(this.battleFieldName, this.uboatName, this.status, this.contestLevel,
                this.numOfRegisteredAliies, this.numOfAlliesNeeded);

    }

    public void setNumOfRegisteredAliies(int numOfRegisteredAliies) {
        this.numOfRegisteredAliies = numOfRegisteredAliies;
    }

    public int getNumOfRegisteredAliies() {
        return numOfRegisteredAliies;
    }
    public void increaseNumOfRegisteredAllies(){
        this.numOfRegisteredAliies += 1;
    }
    public void decreaseNumOfRegisteredAllies(){
        this.numOfRegisteredAliies -= 1;
    }

    public void setStatus(ContestStatus status) {
        this.status = status;
    }

    public ContestStatus getStatus() {
        return status;
    }

    public void setUboatName(String uboatName) {
        this.uboatName = uboatName;
    }

    public int getNumOfAlliesNeeded() {
        return numOfAlliesNeeded;
    }

    public Level getContestLevel() {
        return contestLevel;
    }
}
