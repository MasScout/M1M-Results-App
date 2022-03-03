import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class M1MRunner extends Frame implements ActionListener {
    public static String levels[] = {"BT-7274", "Blood and Rust", "Into the Abyss 1", "Into the Abyss 3", "Effect and Cause 2", "Effect and Cause 3", "The Beacon 2", "The Beacon 3", "Trial by Fire", "The Ark", "The Fold Weapon"};
    public static java.util.List<String> ncsLevels = Arrays.asList("bt-7274", "the beacon 3", "trial by fire", "the ark", "the fold weapon");
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
        
        // TODO: Write this data to respective files
        if (selectedRunner1.isPresent()) {
            PlayerData runner1 = selectedRunner1.get();
            String levelName = levelNameField.getItemAt(levelNameField.getSelectedIndex()).toLowerCase();
            if (ncsLevels.contains(levelName)) {
                boolean ncsBool = ncsCheckBox.isSelected();//inputScanner.nextLine();
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
                boolean ncsBool = ncsCheckBox.isSelected();//inputScanner.nextLine();
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
    public static void main(String[] args) {

        // Commented out for data accuracy testing
        // // Download the CSV from Google Sheets
        // try {
        //     // Download Groups CSV
        //     InputStream in = new URL("https://docs.google.com/spreadsheets/d/11j00ZUrU4htj7MJjPymSzN1Raz5LxP8O7sUy0EkT7A8/export?format=csv&1369435889").openStream();
        //     Files.copy(in, Paths.get("M2MGroupResults.csv"), StandardCopyOption.REPLACE_EXISTING);
        //     // TODO: Import Bracket Spreadsheet
        // } catch (IOException e) {
        //     e. printStackTrace();
        //     System.exit(1);
        // }

        // Read results for Groups and Finals (if they exist)
        String directory = System.getProperty("user.dir");
        
        try {
            ResultsAnalyzer.readResultsCSV(directory + "\\M2MGroupResults.csv");
        } catch (FileNotFoundException e) {
            System.out.println("================================\nCOULD NOT FIND GROUPS RESULTS!!!\n================================");
        }
        try {
            ResultsAnalyzer.readResultsCSV(directory + "\\M2MBracketResults.csv");
        } catch (FileNotFoundException e) {
            System.out.println("=================================\nCOULD NOT FIND BRACKET RESULTS!!!\n=================================");
        }

        System.out.println("Done reading results.");

        // // Debugging info
        // ResultsAnalyzer.playerDatas.stream().forEach(runner -> {
        //     runner.printAll();
        //     // runner.removeTimes();
        // });

        new M1MRunner();
    }
}
