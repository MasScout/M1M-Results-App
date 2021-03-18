import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ResultsAnalyzer {
    
    public Map<List<String>, Map<String, ArrayList<Integer>>> playerInfo; //<[Name, Wins, Losses], <Level, [Time, Time, Time, Time]>>
    public static List<PlayerData> playerDatas = new LinkedList<PlayerData>();
    private static int[][] langConversions = {
        {7, 1},     // BT
        {2, 0},     // BnR
        {-2, 0},    // EnC2
        {11, 3},    // B3
        {2, 1},     // Ark
        {3, 0}      // FW
    }; // E, E-NCS, G, G-NCS 


    
    public static void readResultsCSV(String filePath) {
        try {
            File file = new File(filePath);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            String[] tempArr;
            while((line = br.readLine()) != null) {
                tempArr = line.split(",");
                // for(String tempStr : tempArr) {
                //     System.out.print(tempStr + " ");
                // }
                if (!line.contains("Timestamp")) {
                    if (filePath.contains("GroupResults.csv")) {
                        analyzeGroupLines(tempArr);
                    }
                    else if (filePath.contains("FinalResults.csv")) {
                        analyzeFinalLines(tempArr);
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void analyzeGroupLines(String[] line) {
        // Create level names, with NCS as necessary
        String level1Name = ((!line[4].contains("1")) ? line[1] : line[1] + " - NCS").toLowerCase();
        String level2Name = ((!line[4].contains("2")) ? line[2] : line[2] + " - NCS").toLowerCase();
        String level3Name = ((!line[4].contains("3")) ? line[3] : line[3] + " - NCS").toLowerCase();

        // Create player names
        String player1 = line[5].toLowerCase();
        String player2 = line[10].toLowerCase();

        // Create and convert player times to seconds w/ correct language adjustments
        int[] player1Times = {convertTime(level1Name, line[6], line[7]), convertTime(level2Name, line[6], line[8]), convertTime(level3Name, line[6], line[9])};
        int[] player2Times = {convertTime(level1Name, line[11], line[12]), convertTime(level2Name, line[11], line[13]), convertTime(level3Name, line[11], line[14])};

        // Check if player object already exists
        Optional<PlayerData> player1Data = playerDatas.stream().filter(player -> player.playerName.equals(player1)).findFirst();
        Optional<PlayerData> player2Data = playerDatas.stream().filter(player -> player.playerName.equals(player2)).findFirst();

        PlayerData player1Obj;
        PlayerData player2Obj;
        // Construct new player object if it doesn't exist
        if (!player1Data.isPresent()) {
            player1Obj = new PlayerData(player1);
            // System.out.println("Creating new object for: " + player1Obj.playerName);
        }
        else {
            player1Obj = player1Data.get();
            playerDatas.remove(player1Data.get());
        }
        if (!player2Data.isPresent()) {
            player2Obj = new PlayerData(player2);
            // System.out.println("Creating new object for: " + player2Obj.playerName);
        }
        else {
            player2Obj = player2Data.get();
            playerDatas.remove(player2Data.get());
        }

        // add wins and losses
        for (int i = 0; i < player1Times.length; i++) {
            if (player1Times[i] == 0) {
                player1Obj.losses++;
                player2Obj.wins++;
            }
            else if (player2Times[i] == 0) {
                player1Obj.wins++;
                player2Obj.losses++;
            }
            else if (player1Times[i] < player2Times[i]) {
                player1Obj.wins++;
                player2Obj.losses++;
            }
            else {
                player1Obj.losses++;
                player2Obj.wins++;
            }
        }

        // Add player times to object
        player1Obj.addTime(level1Name, player1Times[0]);
        player1Obj.addTime(level2Name, player1Times[1]);
        player1Obj.addTime(level3Name, player1Times[2]);     
        player2Obj.addTime(level1Name, player2Times[0]);
        player2Obj.addTime(level2Name, player2Times[1]);
        player2Obj.addTime(level3Name, player2Times[2]);

        // player1Obj.printAll();
        // player2Obj.printAll();

        playerDatas.add(player1Obj);
        playerDatas.add(player2Obj);
    }

    public static void analyzeFinalLines(String[] line) {
        // Create level names, with NCS as necessary
        String level1Name = ((!line[4].contains("1")) ? line[1] : line[1] + " - NCS").toLowerCase();
        String level2Name = ((!line[4].contains("2")) ? line[2] : line[2] + " - NCS").toLowerCase();
        String level3Name = ((!line[4].contains("3")) ? line[3] : line[3] + " - NCS").toLowerCase();
        String level4Name = ((!line[4].contains("4")) ? line[3] : line[3] + " - NCS").toLowerCase();
        String level5Name = ((!line[4].contains("5")) ? line[3] : line[3] + " - NCS").toLowerCase();

        // Create player names
        String player1 = line[7].toLowerCase();
        String player2 = line[13].toLowerCase();

        // Create and convert player times to seconds
        int[] player1Times = {timeToSeconds(line[8]), timeToSeconds(line[9]), timeToSeconds(line[10]), timeToSeconds(line[11]), timeToSeconds(line[12])};
        int[] player2Times = {timeToSeconds(line[14]), timeToSeconds(line[15]), timeToSeconds(line[16]), timeToSeconds(line[17]), timeToSeconds(line[18])};

        // Check if player object already exists
        Optional<PlayerData> player1Data = playerDatas.stream().filter(player -> player.playerName.equals(player1)).findFirst();
        Optional<PlayerData> player2Data = playerDatas.stream().filter(player -> player.playerName.equals(player2)).findFirst();

        PlayerData player1Obj;
        PlayerData player2Obj;
        // Construct new player object if it doesn't exist
        if (!player1Data.isPresent()) {
            player1Obj = new PlayerData(player1);
            System.out.println("Creating new object for: " + player1Obj.playerName + ". Check to make sure the submitted name is correct.");
        }
        else {
            player1Obj = player1Data.get();
            playerDatas.remove(player1Data.get());
        }
        if (!player2Data.isPresent()) {
            player2Obj = new PlayerData(player2);
            System.out.println("Creating new object for: " + player2Obj.playerName + ". Check to make sure the submitted name is correct.");
        }
        else {
            player2Obj = player2Data.get();
            playerDatas.remove(player2Data.get());
        }

        // add wins and losses
        for (int i = 0; i < player1Times.length; i++) {
            if (player1Times[i] == 0 && player2Times[i] == 0) {
                // This match wasn't actually played
            }
            else if (player1Times[i] == 0) {
                player1Obj.losses++;
                player2Obj.wins++;
            }
            else if (player2Times[i] == 0) {
                player1Obj.wins++;
                player2Obj.losses++;
            }
            else if (player1Times[i] < player2Times[i]) {
                player1Obj.wins++;
                player2Obj.losses++;
            }
            else {
                player1Obj.losses++;
                player2Obj.wins++;
            }
        }

        // Add player times to object
        player1Obj.addTime(level1Name, player1Times[0]);
        player1Obj.addTime(level2Name, player1Times[1]);
        player1Obj.addTime(level3Name, player1Times[2]);     
        player1Obj.addTime(level4Name, player1Times[3]);
        player1Obj.addTime(level5Name, player1Times[4]);
        player2Obj.addTime(level1Name, player2Times[0]);
        player2Obj.addTime(level2Name, player2Times[1]);
        player2Obj.addTime(level3Name, player2Times[2]);
        player2Obj.addTime(level4Name, player2Times[3]);
        player2Obj.addTime(level5Name, player2Times[4]);

        // player1Obj.printAll();
        // player2Obj.printAll();

        playerDatas.add(player1Obj);
        playerDatas.add(player2Obj);
    }

    public static int convertTime(String level, String lang, String time) {
        String timeArray[] = time.split(":");
        int timeSeconds = 3600*(Integer.parseInt(timeArray[0])) + 60*(Integer.parseInt(timeArray[1])) + Integer.parseInt(timeArray[2]);
        if (lang.equalsIgnoreCase("mandarin")) {
            return timeSeconds;
        }
        else if (lang.equalsIgnoreCase("english")) {
            if (level.equalsIgnoreCase("BT-7274") || level.equalsIgnoreCase("BT-7274 - NCS")) {
                timeSeconds -= langConversions[0][0];
            }
            else if (level.equalsIgnoreCase("Blood and Rust")) {
                timeSeconds -= langConversions[1][0];
            }
            else if (level.equalsIgnoreCase("Effect and Cause 2")) {
                timeSeconds -= langConversions[2][0];
            }
            else if (level.equalsIgnoreCase("The Beacon 3")) {
                timeSeconds -= langConversions[3][0];
            }
            else if (level.equalsIgnoreCase("The Ark") || level.equalsIgnoreCase("The Ark - NCS")) {
                timeSeconds -= langConversions[4][0];
            }
            else if (level.equalsIgnoreCase("The Fold Weapon")) {
                timeSeconds -= langConversions[5][0];
            }
        }
        else if (lang.equalsIgnoreCase("german")) {
            if (level.equalsIgnoreCase("BT-7274") || level.equalsIgnoreCase("BT-7274 - NCS")) {
                timeSeconds -= langConversions[0][1];
            }
            else if (level.equalsIgnoreCase("The Beacon 3")) {
                timeSeconds -= langConversions[3][1];
            }
            else if (level.equalsIgnoreCase("The Ark") || level.equalsIgnoreCase("The Ark - NCS")) {
                timeSeconds -= langConversions[4][1];
            }
        }
        
        return timeSeconds;
    }

    public static int timeToSeconds(String time) {
        String timeArray[] = time.split(":");
        int timeSeconds = 3600*(Integer.parseInt(timeArray[0])) + 60*(Integer.parseInt(timeArray[1])) + Integer.parseInt(timeArray[2]);
        return timeSeconds;
    }
}
