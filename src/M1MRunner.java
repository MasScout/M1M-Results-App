package src;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class M1MRunner extends Frame implements ActionListener {
    public static String levels[] = {"BT-7274", "Blood and Rust", "Into the Abyss 1", "Into the Abyss 3", "Effect and Cause 2", "Effect and Cause 3", "The Beacon 2", "The Beacon 3", "Trial by Fire", "The Ark", "The Fold Weapon"};
    public static java.util.List<String> ncsLevels = Arrays.asList("bt-7274", "the beacon 3", "trial by fire", "the ark", "the fold weapon");
    public static String groupingCSVLocation;
    public static String groupsResultsCSVLocation;
    public static String bracketResultsCSVLocation;
    public static String groupsResultsURL;
    public static String bracketResultsURL;
    public static boolean runBracketBool;
    public static boolean outputRemainingGroupMatchesBool;
    public static String remainingGroupsFile;
    public static ArrayList<String> exemptRunners = new ArrayList<>();
    // TODO: Add seed map
    
    JFrame MasBot = new JFrame("MasBot");
    JLabel runner1Label = new JLabel("Runner 1 Name: ");
    JLabel runner2Label = new JLabel("Runner 2 Name: ");
    JTextField runner1Name = new JTextField();
    JTextField runner2Name = new JTextField();
    JLabel levelLabel = new JLabel("Level Name: ");
    JComboBox<String> levelNameField = new JComboBox<String>(levels);
    JCheckBox ncsCheckBox = new JCheckBox("NCS?");
    JButton trigger = new JButton("Get Stats!");
    TextArea runner1Info = new TextArea("");
    TextArea runner2Info = new TextArea("");
    JScrollPane runner1InfoPane = new JScrollPane(runner1Info, JScrollPane.VERTICAL_SCROLLBAR_NEVER ,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    JScrollPane runner2InfoPane = new JScrollPane(runner2Info, JScrollPane.VERTICAL_SCROLLBAR_NEVER ,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    M1MRunner() {
        // TODO: Rework UI to look better and show all information
        runner1Label.setBounds(50, 50, 100, 30);
        runner2Label.setBounds(250, 50, 100, 30);
        runner1Name.setBounds(50, 75, 100, 30);
        runner2Name.setBounds(250, 75, 100, 30);
        levelLabel.setBounds(150, 125, 100, 30);
        levelNameField.setBounds(150, 150, 100, 30);
        ncsCheckBox.setBounds(250, 145, 100, 50);
        trigger.setBounds(150, 200, 75, 30);
        trigger.addActionListener(this);
        runner1InfoPane.setBounds(50, 250, 105, 105);
        runner2InfoPane.setBounds(250, 250, 105, 105);
        MasBot.add(runner1Label);
        MasBot.add(runner2Label);
        MasBot.add(runner1Name);
        MasBot.add(runner2Name);
        MasBot.add(levelLabel);
        MasBot.add(levelNameField);
        MasBot.add(ncsCheckBox);
        MasBot.add(trigger);
        MasBot.add(runner1InfoPane);
        MasBot.add(runner2InfoPane);
        MasBot.setSize(400, 400);
        MasBot.setLayout(null);
        MasBot.setVisible(true);
        MasBot.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {

        Optional<PlayerData> selectedRunner1 = ResultsAnalyzer.playerDatas.stream().filter(player -> player.playerName.toLowerCase().equals(runner1Name.getText().toLowerCase())).findFirst();
        Optional<PlayerData> selectedRunner2 = ResultsAnalyzer.playerDatas.stream().filter(player -> player.playerName.toLowerCase().equals(runner2Name.getText().toLowerCase())).findFirst();

        // // Declare File variables
        // try{
        //     FileWriter runner1Name = new FileWriter("Runner1Name.txt");
        //     FileWriter runner1Seed = new FileWriter("Runner1Seed.txt");
        //     FileWriter runner1WL = new FileWriter("Runner1WL.txt");
        //     FileWriter runner1BestTime = new FileWriter("Runner1Best.txt");
        //     FileWriter runner1AvgTime = new FileWriter("Runner1Avg.txt");

        //     FileWriter runner2Name = new FileWriter("Runner2Name.txt");
        //     FileWriter runner2Seed = new FileWriter("Runner2Seed.txt");
        //     FileWriter runner2WL = new FileWriter("Runner2WL.txt");
        //     FileWriter runner2BestTime = new FileWriter("Runner2Best.txt");
        //     FileWriter runner2AvgTime = new FileWriter("Runner2Avg.txt");

        // } catch (IOException writerException) {
        //     writerException.printStackTrace();
        // }

        // TODO: Write this data to respective files
        if (selectedRunner1.isPresent()) {
            PlayerData runner1 = selectedRunner1.get();
            String levelName = levelNameField.getItemAt(levelNameField.getSelectedIndex()).toLowerCase();
            if (ncsLevels.contains(levelName)) {
                boolean ncsBool = ncsCheckBox.isSelected();
                if (ncsBool) {
                    levelName = levelName + " - ncs";
                }
            }
            String runner1Data = "Name: " + runner1.playerName;
            runner1Data += "\nW/L: " + runner1.wins + "-" + runner1.losses;
            runner1Data += "\nAvg Time: " + runner1.calcAverageTime(levelName);
            runner1Data += "\nBest Time: " + runner1.calcBestTime(levelName);
            runner1Info.setText(runner1Data);
        }
        else {
            String runner1Data = "Runner not found.\nPlease try again.";
            runner1Info.setText(runner1Data);
        }

        if (selectedRunner2.isPresent()) {
            PlayerData runner2 = selectedRunner2.get();
            String levelName = levelNameField.getItemAt(levelNameField.getSelectedIndex()).toLowerCase();
            if (ncsLevels.contains(levelName)) {
                boolean ncsBool = ncsCheckBox.isSelected();
                if (ncsBool) {
                    levelName = levelName + " - ncs";
                }
            }
            String runner2Data = "Name: " + runner2.playerName;
            runner2Data += "\nW/L: " + runner2.wins + "-" + runner2.losses;
            runner2Data += "\nAvg Time: " + runner2.calcAverageTime(levelName);
            runner2Data += "\nBest Time: " + runner2.calcBestTime(levelName);
            runner2Info.setText(runner2Data);
        }
        else {
            String runner2Data = "Runner not found.\nPlease try again.";
            runner2Info.setText(runner2Data);
        }
    }

    public static boolean readConfiguration(String filePath) {
        try {
            File file = new File(filePath);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("#")) {
                    if (line.contains("GroupingCSVLocation")) {
                        groupingCSVLocation = line.split("=")[1];
                    } else if (line.contains("GroupsResultsCSVLocation")) {
                        groupsResultsCSVLocation = line.split("=")[1];
                    } else if (line.contains("BracketResultsCSVLocation")) {
                        bracketResultsCSVLocation = line.split("=")[1];
                    } else if (line.contains("GroupsResultsURL")) {
                        groupsResultsURL = line.split("~")[1];
                    } else if (line.contains("BracketResultsURL")) {
                        bracketResultsURL = line.split("~")[1];
                    } else if (line.contains("RunBracketBool")) {
                        runBracketBool = (line.split("=")[1].equals("true")) ? true : false;
                    } else if (line.contains("OutputRemainingGroupMatchesBool")) {
                        outputRemainingGroupMatchesBool = (line.split("=")[1].equals("true")) ? true : false;
                    } else if (line.contains("RemainingGroupsFile")) {
                        remainingGroupsFile = (line.split("="))[1];
                    } else if (line.contains("ExemptRunners")) {
                        for (String runnerName : Arrays.asList(line.split("=")[1].split(","))) {
                            exemptRunners.add(runnerName);
                        }
                    }
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("================================\nCOULD NOT FIND CONFIG!!!\n================================");
            return false;
        } catch (IOException e) {
            System.out.println("================================\nERROR READING CONFIG!!!\n================================");
            return false;
        }
        return true;
    }
    public static void main(String[] args) {

        String directory = System.getProperty("user.dir");
        if (!readConfiguration(directory + "\\config.cfg")) {
            System.out.println("Exiting Program");
            return;
        }

        // Create player objects from seeding
        try {
            ResultsAnalyzer.readGroupingCSV(directory + groupingCSVLocation);
        } catch (FileNotFoundException e) {
            System.out.println("================================\nCOULD NOT FIND SEEDING INFO!!!\n================================");
        }

        // Download the CSVs from Google Sheets
        try {
            // Download Groups CSV
            InputStream groupIn = new URL(groupsResultsURL).openStream();
            Files.copy(groupIn, Paths.get(directory + groupsResultsCSVLocation), StandardCopyOption.REPLACE_EXISTING);
            // Download Bracket CSV
            if (runBracketBool) {
                InputStream bracketIn = new URL(bracketResultsURL).openStream();
                Files.copy(bracketIn, Paths.get(directory + bracketResultsCSVLocation), StandardCopyOption.REPLACE_EXISTING);    
            }
        } catch (IOException e) {
            e. printStackTrace();
            System.exit(1);
        }
        
        // Read results for Groups and Finals (if they exist)
        try {
            ResultsAnalyzer.readResultsCSV(directory + groupsResultsCSVLocation);
        } catch (FileNotFoundException e) {
            System.out.println("================================\nCOULD NOT FIND GROUPS RESULTS!!!\n================================");
        }
        if (runBracketBool) {
            try {
                ResultsAnalyzer.readResultsCSV(directory + bracketResultsCSVLocation);
            } catch (FileNotFoundException e) {
                System.out.println("=================================\nCOULD NOT FIND BRACKET RESULTS!!!\n=================================");
            }
        }

        System.out.println("Done reading results.");

        // Debugging info
        // System.out.println(outputRemainingGroupMatchesBool);
        if (outputRemainingGroupMatchesBool) {
            // String[] exemptRunners = {"skeleton_jelly", "lemuura", "randombanana9", "fuchsiano"};
            String remainingRunners = "Runner,Group,Sets Played\n";
            for (PlayerData runner : ResultsAnalyzer.playerDatas) {
                // if (!Arrays.asList(exemptRunners).contains(runner.playerName) && runner.wins + runner.losses < 9) {
                if (!exemptRunners.contains(runner.playerName) && runner.wins + runner.losses < 9) {                   
                    int gamesPlayed = runner.wins + runner.losses;
                    String runnerInfo = runner.playerName + "," + runner.group + "," + gamesPlayed / 3 + "\n";
                    remainingRunners += runnerInfo;
                    // System.out.println(remainingRunners);
                }
            }

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(directory + remainingGroupsFile));
                writer.write(remainingRunners);
                writer.close();
            } catch (IOException e) {
                System.out.println("=================================\nCOULD NOT WRITE TO REMAINING GROUPS FILE!!!\n=================================");
            }
        }

        new M1MRunner();
    }
}
