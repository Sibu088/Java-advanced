package SoccerAppRanking;

public class Team implements Comparable<Team> {
    private String name;
    private int points;

    public Team(String name) {
        this.name = name;
        this.points = 0;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int p) {
        this.points += p;
    }

    @Override
    public int compareTo(Team other) {
        if (this.points != other.points)
            return Integer.compare(other.points, this.points);
        return this.name.compareToIgnoreCase(other.name);
    }

    @Override
    public String toString() {
        return name + " - " + points + " pts";
    }
}
