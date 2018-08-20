import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FordFulkerson;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import edu.princeton.cs.algs4.SET;

public class BaseballElimination{
    private final String[] teams;
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;
    private final int[][] against;
    private FlowNetwork flowNetwork;
    // -- CORE DATA STRUCT -- BEGIN
    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename){
        In in = new In(filename);
        int numOfTeams = Integer.parseInt(in.readLine());
        teams = new String[numOfTeams];
        wins = new int[numOfTeams];
        losses = new int[numOfTeams];
        remaining = new int[numOfTeams];
        against = new int[numOfTeams][numOfTeams];
        int teamId = 0;
        while(in.hasNextLine()){
            String line = in.readLine();
            line = line.trim();
            String[] tokens = line.split(" +");
            teams[teamId] = tokens[0];
            wins[teamId] = Integer.parseInt(tokens[1]);
            losses[teamId] = Integer.parseInt(tokens[2]);
            remaining[teamId] = Integer.parseInt(tokens[3]);
            for(int idx = 0; idx < numOfTeams; idx++){
                against[teamId][idx] = Integer.parseInt(tokens[4+idx]);
            }
            teamId++;
        }
    }
    // number of teams
    public int numberOfTeams(){
        return teams.length;
    }
    // all teams
    public Iterable<String> teams(){
        ArrayList<String> res = new ArrayList<String>();
        for(int i = 0; i < teams.length; i++){
            res.add(teams[i]);
        }
        return res;
    }
    // number of wins for given team
    public int wins(String team){
        int teamId = getId(team);
        return wins[teamId];
    }
    // number of losses for given team
    public int losses(String team){
        int teamId = getId(team);
        return losses[teamId];
    }
    // number of remaining games for given team
    public int remaining(String team){
        int teamId = getId(team);
        return remaining[teamId];
    }
    // number of remaining games between team1 and team2
    public int against(String team1, String team2){
        int team1_id = getId(team1);
        int team2_id = getId(team2);
        return against[team1_id][team2_id];
    }
    // Private assistence function
    private int getId(String team){
        if(team == null) throw new java.lang.IllegalArgumentException();
        int id = -1;
        for(int i = 0; i < teams.length; i++){
            if(teams[i].equals(team)){
                id = i;
                break;
            }
        }
        if(id == -1) throw new java.lang.IllegalArgumentException();
        return id;
    }
    // -- CORE DATA STRUCT -- END
    // -- CORE ALGO -- BEGIN
    // is given team eliminated?
    public boolean isEliminated(String team){
        int teamId = getId(team);
        if(teams.length == 1) return false;
        if(teams.length == 2){
            if(teamId == 0 && wins[1]>wins[teamId]+remaining[teamId]) return true;
            if(teamId == 1 && wins[0]>wins[teamId]+remaining[teamId]) return true;
            return false;
        }
        // step_0_a trival justify
        for(int i = 0; i < teams.length; i++){
            if(i == teamId) continue;
            if(wins[i] > wins[teamId] + remaining[teamId]) return true;
        }
        // step_0_b nontrival justify construct flownetwork
        flowNetworkConstruct(team);
        // step_1 fordfulkerson algo
        FordFulkerson fordfulkerson = new FordFulkerson(flowNetwork, 0, flowNetwork.V()-1);
        // step_2 observe the incut to get the result of isEliminated
        for(int i = 1; i < (teams.length-1)*(teams.length-2)/2+1; i++){
            if(fordfulkerson.inCut(i) == true) return true;
        }
        return false;
    }
    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team){
        if(!isEliminated(team)) return null;
        int teamId = getId(team);
        SET<String> res = new SET<String>();
        // step_0_a trival situation
        for(int i = 0; i < teams.length; i++){
            if(i == teamId) continue;
            if(wins[i] > wins[teamId] + remaining[teamId]){
                res.add(teams[i]);
                //StdOut.printf("--%d",i);
            }
        }
        if(!res.isEmpty()) return res;
        //StdOut.printf("-- nontrival begin --\n");
        // step_0_b nontrival situation construct flownetwork
        flowNetworkConstruct(team);
        // step_1 fordfulkerson algo
        FordFulkerson fordfulkerson = new FordFulkerson(flowNetwork, 0, flowNetwork.V()-1);
        // step_2 observe the incut to get the result of isEliminated
        for(int i = 1; i < (teams.length-1)*(teams.length-2)/2+1; i++){
            if(fordfulkerson.inCut(i) == true){
                int rowR = teams.length - 3;
                int colR = teams.length - 3;
                int fullCeil = (teams.length-1)*(teams.length-2)/2;
                int fullFloor = fullCeil - 1;
                int cnt = 1;
                while(i<=fullFloor){
                    cnt++;
                    fullCeil = fullFloor;
                    fullFloor -= cnt;
                    rowR--;
                }
                colR = teams.length-3-(fullCeil - i);
                //StdOut.printf("\ni = %d; rowR = %d; colR = %d;",i,rowR,colR);
                //StdOut.printf("  cnt = %d; floor = %d; ceil = %d;",cnt,fullFloor,fullCeil);
                int row = rowR;
                int col = colR+1;
                if(row>=teamId) row++;
                if(col>=teamId) col++;
                //StdOut.printf("  row = %d; col = %d;",row,col);
                res.add(teams[row]);
                res.add(teams[col]);
            }
        }
        return res;
    }
    // support function
    private void flowNetworkConstruct(String team){
        if(teams.length < 3) return;
        // step_0 init flownetwork
        int numOfVertex = 2 + teams.length - 1 + (teams.length - 1)*(teams.length - 2)/2;
        this.flowNetwork = new FlowNetwork(numOfVertex);
        int teamId = getId(team);
        int baseGame = 1;
        int baseTeam = baseGame + (teams.length - 1)*(teams.length - 2)/2;
        // step_1 add head-out edge - head(0)
        for(int row = 0; row < teams.length - 1; row++){
            if(row == teamId) continue;
            for(int col = row + 1; col < teams.length; col++){
                if(col == teamId) continue;
                int rowR = row>teamId?row-1:row;
                int colR = col>teamId?col-1:col;
                // step_1 add head-out edge - head(0)
                int gameVertexIndex = (teams.length-2 + teams.length-2 - rowR)*(rowR+1)/2 - (teams.length-2 - colR) - 1;
                flowNetwork.addEdge(new FlowEdge(0, baseGame + gameVertexIndex, against[row][col]));
                // step_2 add finite middle edge - middleLeft(baseGame~baseTeam-1) ; middleRight(baseTeam~tail-1)
                flowNetwork.addEdge(new FlowEdge(baseGame + gameVertexIndex, baseTeam + rowR, Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(baseGame + gameVertexIndex, baseTeam + colR, Double.POSITIVE_INFINITY));
            }
        }
        // step_3 add tail-in edge - tail(numOfVertex-1)
        for(int i = 0, iFix = 0; i < teams.length - 1; i++,iFix++){
            if(i == teamId) iFix++;
            flowNetwork.addEdge(new FlowEdge(baseTeam + i, numOfVertex - 1, wins[teamId] + remaining[teamId] - wins[iFix]));
        }
    }
    // -- CORE ALGO -- END
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        StdOut.println(division.isEliminated("Turing"));
        /*
        String team = "Detroit";
        StdOut.printf("%s\n",team);
        division.certificateOfElimination(team);
        */
        // Data Read and Store Test -- begin --
        /*
        for (String team : division.teams()) {
            StdOut.printf("%s - win loss remain aginst\n",team);
            StdOut.printf("%d  %d  %d\n",division.wins(team), division.losses(team), division.remaining(team));
            for (String team2 : division.teams()){
                StdOut.printf("%d  ",division.against(team, team2));
            }
            StdOut.println();
        }*/
        // Data Read and Store Test -- end --
        // Algo Test -- begin --
        /*
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }*/
        // Algo Test -- end -- 
    }
}