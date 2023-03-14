import java.util.*;

public class PlayerData {

    public String playerName;
    public char group;

    public int wins;
    public int losses;

    public Map<String, ArrayList<Integer>> levelTimes; // Level Name, [Time, Time, Time, Time...]

    public PlayerData(String name, char group) {
        this.playerName = name;
        this.group = group;
        this.levelTimes = new HashMap<String, ArrayList<Integer>>();
    }

    public String calcAverageTime(String levelName) {
        ArrayList<Integer> times = new ArrayList<Integer>();
        if (levelTimes.containsKey(levelName)) {
            times = levelTimes.get(levelName);
            return convertTime((int)times.stream().mapToInt(val -> val).average().orElse(0));
        }
        else {
            return "-:--";
        }
       
    }

    public String calcBestTime(String levelName) {
        ArrayList<Integer> times = new ArrayList<Integer>();
        if (levelTimes.containsKey(levelName)) {
            times = levelTimes.get(levelName);
            return convertTime(Collections.min(times));
        }
        else {
            return "-:--";
        }
    }

    public void addTime(String level, int time) {
        if (levelTimes.containsKey(level)) {
            ArrayList<Integer> times = new ArrayList<Integer>();
            times = levelTimes.get(level);
            times.add(time);
            levelTimes.replace(level, times);
        }
        else {
            ArrayList<Integer> times = new ArrayList<Integer>();
            times.add(time);
            levelTimes.put(level, times);
        }
    }

    public void removeTimes() {
        levelTimes.entrySet().stream().forEach(level -> {
            ArrayList<Integer> timesList = new ArrayList<Integer>();
            timesList = level.getValue();
            Iterator<Integer> itr = timesList.iterator();
            while (itr.hasNext()) {
                int time = itr.next();
                if (time == 0) {
                    itr.remove();
                }
            }
        });
    }

    public void printAll() {
        System.out.println(this.playerName + ": " + this.wins + "-" + this.losses);
        levelTimes.entrySet().stream().forEach(level -> {
            System.out.println(level);
        });
    }

    public String convertTime(int time) {
        if (time == 0) {
            return "-:--";
        }
        String minutes = Integer.toString(time/60);
        String seconds = Integer.toString(time%60);
        if (time%60 < 10) {
            seconds = "0" + seconds;
        }
        return minutes + ":" + seconds;
    }
}
