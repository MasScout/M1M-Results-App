import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class M1MRunner extends Frame implements ActionListener {
    public static String levels[] = {"BT-7274", "Blood and Rust", "Into the Abyss 1", "Into the Abyss 3", "Effect and Cause 2", "Effect and Cause 3", "The Beacon 2", "The Beacon 3", "Trial by Fire", "The Ark", "The Fold Weapon"};
    public static java.util.List<String> ncsLevels = Arrays.asList("bt-7274", "the beacon 3", "trial by fire", "the ark", "the fold weapon");
    
    JFrame MasBot = new JFrame("MasBot");
    JLabel runner1Label = new JLabel("Runner 1 Name: ");
    JLabel runner2Label = new JLabel("Runner 2 Name: ");
    JTextField runner1Name = new JTextField();
    JTextField runner2Name = new JTextField();
    JLabel levelLabel = new JLabel("Level Name: ");
    JComboBox<String> levelNameField = new JComboBox<String>(levels);
    JCheckBox ncsCheckBox = new JCheckBox("NCS?");
    JButton trigger = new JButton("Get Stats!");
    JLabel runner1Info = new JLabel();
    JLabel runner2Info = new JLabel();

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
        runner1Info.setBounds(50, 250, 100, 100);
        runner2Info.setBounds(250, 250, 500, 100);
        MasBot.add(runner1Label);
        MasBot.add(runner2Label);
        MasBot.add(runner1Name);
        MasBot.add(runner2Name);
        MasBot.add(levelLabel);
        MasBot.add(levelNameField);
        MasBot.add(ncsCheckBox);
        MasBot.add(trigger);
        MasBot.add(runner1Info);
        MasBot.add(runner2Info);
        MasBot.setSize(400, 400);
        MasBot.setLayout(null);
        MasBot.setVisible(true);
        MasBot.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {

        Optional<PlayerData> selectedRunner1 = ResultsAnalyzer.playerDatas.stream().filter(player -> player.playerName.toLowerCase().equals(runner1Name.getText().toLowerCase())).findFirst();
        Optional<PlayerData> selectedRunner2 = ResultsAnalyzer.playerDatas.stream().filter(player -> player.playerName.toLowerCase().equals(runner2Name.getText().toLowerCase())).findFirst();

        if (selectedRunner1.isPresent()) {
            PlayerData runner1 = selectedRunner1.get();
            String levelName = levelNameField.getItemAt(levelNameField.getSelectedIndex()).toLowerCase();
            if (ncsLevels.contains(levelName)) {
                boolean ncsBool = ncsCheckBox.isSelected();//inputScanner.nextLine();
                if (ncsBool) {
                    levelName = levelName + " - ncs";
                }
            }
            String runner1Data = "<html>Name: " + runner1.playerName;
            runner1Data += "<br>W/L: " + runner1.wins + "-" + runner1.losses;
            runner1Data += "<br>Avg Time: " + runner1.calcAverageTime(levelName);
            runner1Data += "<br>Best Time: " + runner1.calcBestTime(levelName) + "</html>";
            runner1Info.setText(runner1Data);
        }
        else {
            String runner1Data = "<html>Runner not found.<br>Please try again.</html>";
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
            String runner2Data = "<html>Name: " + runner2.playerName;
            runner2Data += "<br>W/L: " + runner2.wins + "-" + runner2.losses;
            runner2Data += "<br>Avg Time: " + runner2.calcAverageTime(levelName);
            runner2Data += "<br>Best Time: " + runner2.calcBestTime(levelName) + "</html>";
            runner2Info.setText(runner2Data);
        }
        else {
            String runner2Data = "<html>Runner not found.<br>Please try again.</html>";
            runner2Info.setText(runner2Data);
        }
    }
    public static void main(String[] args) {

        String directory = System.getProperty("user.dir");
        ResultsAnalyzer.readResultsCSV(directory + "\\GroupResults.csv");
        ResultsAnalyzer.readResultsCSV(directory + "\\FinalResults.csv");

        System.out.println("Done reading results.");

        ResultsAnalyzer.playerDatas.stream().forEach(runner -> {
            //System.out.println(runner.playerName + "\t\t" + runner.wins + "-" + runner.losses);
            runner.removeTimes();
        });

        new M1MRunner();        
    }
}
