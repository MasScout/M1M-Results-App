import java.util.*;
import java.util.Scanner;

public class M1MRunner {
    public static List<String> ncsLevels = Arrays.asList("bt-7274", "the beacon 3", "trial by fire", "the ark", "the fold weapon");
    
    public static void main(String[] args) {
        String directory = System.getProperty("user.dir");
        ResultsAnalyzer.readResultsCSV(directory + "\\GroupResults.csv");
        ResultsAnalyzer.readResultsCSV(directory + "\\FinalResults.csv");

        System.out.println("Done reading results.");

        ResultsAnalyzer.playerDatas.stream().forEach(runner -> {
            //System.out.println(runner.playerName + "\t\t" + runner.wins + "-" + runner.losses);
            runner.removeTimes();
        });

        Scanner inputScanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter runner name: ");
            String runnerName = inputScanner.nextLine().toLowerCase();
            
            if (runnerName.equals("quit") || runnerName.equals("exit")) {
                break;
            }

            Optional<PlayerData> selectedRunner = ResultsAnalyzer.playerDatas.stream().filter(player -> player.playerName.toLowerCase().equals(runnerName.toLowerCase())).findFirst();

            if (selectedRunner.isPresent()) {
                PlayerData runner = selectedRunner.get();
                System.out.println(runner.playerName);
                System.out.println("Wins-Losses: " + runner.wins + "-" + runner.losses);
                
                System.out.print("Enter level name: ");
                String levelName = inputScanner.nextLine().toLowerCase();
                if (ncsLevels.contains(levelName)) {
                    System.out.print("NCS? (y/n) ");
                    String ncsBool = inputScanner.nextLine();
                    if (ncsBool.toLowerCase().equals("y")) {
                        levelName = levelName + " - ncs";
                    }
                }
                System.out.println(levelName);
                System.out.println("Average Time: " + runner.calcAverageTime(levelName));
                System.out.println("Best Time: " + runner.calcBestTime(levelName));
            }
            else {
                System.out.println("Runner not found. Please try again.");
            }



        }

        inputScanner.close();
        
    }
}
