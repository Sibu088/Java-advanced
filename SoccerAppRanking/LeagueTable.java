package SoccerAppRanking;

import java.io.*;
import java.util.*;

// Class to manage soccer league data, including loading match results and ranking teams
public class LeagueTable {
    // HashMap to store teams, with team names as keys and SoccerAppRanking.Team objects as values
    private Map<String, Team> teams = new HashMap<>();

    // Method to load match data from a file
    public void loadMatches(String filePath) {
        // Use BufferedReader to read the file efficiently
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Read each line until the end of the file
            while ((line = br.readLine()) != null) {
                // Process each line as a match result
                processMatch(line);
            }
        } catch (IOException e) {
            // Handle any file reading errors and print an error message
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // Method to process a single match result from a line of text
    private void processMatch(String line) {
        // Split the line into two parts (team1 and team2 data) based on comma
        String[] parts = line.split(",");
        // Split team1 and team2 data into arrays, separating name and score
        String[] team1Data = parts[0].trim().split(" ");
        String[] team2Data = parts[1].trim().split(" ");

        // Extract team names from the arrays (excluding the score)
        String team1Name = getTeamName(team1Data);
        String team2Name = getTeamName(team2Data);

        // Parse the scores from the last element of each array
        int team1Score = Integer.parseInt(team1Data[team1Data.length - 1]);
        int team2Score = Integer.parseInt(team2Data[team2Data.length - 1]);

        // Add teams to the map if they don't already exist
        teams.putIfAbsent(team1Name, new Team(team1Name));
        teams.putIfAbsent(team2Name, new Team(team2Name));

        // Update points based on match result
        if (team1Score > team2Score) {
            // SoccerAppRanking.Team 1 wins: award 3 points to team1
            teams.get(team1Name).addPoints(3);
        } else if (team2Score > team1Score) {
            // SoccerAppRanking.Team 2 wins: award 3 points to team2
            teams.get(team2Name).addPoints(3);
        } else {
            // Draw: award 1 point to each team
            teams.get(team1Name).addPoints(1);
            teams.get(team2Name).addPoints(1);
        }
    }

    // Method to extract the team name from an array, excluding the score
    private String getTeamName(String[] arr) {
        // Use StringBuilder to efficiently build the team name
        StringBuilder sb = new StringBuilder();
        // Iterate through all elements except the last one (score)
        for (int i = 0; i < arr.length - 1; i++) {
            sb.append(arr[i]);
            // Add a space between words, but not after the last word
            if (i < arr.length - 2) sb.append(" ");
        }
        // Return the constructed team name as a string
        return sb.toString();
    }

    // Method to get a sorted list of teams based on their points
    public List<Team> getRankedTeams() {
        // Create a new ArrayList from the map's values (all SoccerAppRanking.Team objects)
        List<Team> sorted = new ArrayList<>(teams.values());
        // Sort the list using the SoccerAppRanking.Team class's natural ordering (assumed to be by points)
        Collections.sort(sorted);
        // Return the sorted list of teams
        return sorted;
    }
}