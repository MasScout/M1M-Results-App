import java.util.*;

public class PlayerData {
    public String playerName;

    public int wins;
    public int losses;

    public Map<String, ArrayList<Integer>> levelTimes; // Level Name, [Time, Time, Time, Time...]

    public int calcAverageTime(String levelName) {
        ArrayList<Integer> times = levelTimes.get(levelName);
        return (int)times.stream().mapToInt(val -> val).average().orElse(0);
    }

    public int calcBestTime(String levelName) {
        ArrayList<Integer> times = levelTimes.get(levelName);
        if(!times.isEmpty()) {
            return Collections.min(times);
        }
        else {
            return 0;
        }
    }
}
