package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Arco;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Player;
import it.polito.tdp.PremierLeague.model.Team;

public class PremierLeagueDAO {
	
	public Map<Integer, Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		Map<Integer, Player> result = new HashMap<>();
		Connection conn = DBConnect.getConnection();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				result.put(player.getPlayerID(), player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Map<Integer, Team> listAllTeams(){
		String sql = "SELECT * FROM Teams";
		Map<Integer, Team> result = new HashMap<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Team team = new Team(res.getInt("TeamID"), res.getString("Name"));
				result.put(team.getTeamID() ,team);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Match> listAllMatches(){
		String sql = "SELECT m.MatchID, m.TeamHomeID, m.TeamAwayID, m.teamHomeFormation, m.teamAwayFormation, m.resultOfTeamHome, m.date, t1.Name, t2.Name   "
				+ "FROM Matches m, Teams t1, Teams t2 "
				+ "WHERE m.TeamHomeID = t1.TeamID AND m.TeamAwayID = t2.TeamID";
		List<Match> result = new ArrayList<Match>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				Match match = new Match(res.getInt("m.MatchID"), res.getInt("m.TeamHomeID"), res.getInt("m.TeamAwayID"), res.getInt("m.teamHomeFormation"), 
							res.getInt("m.teamAwayFormation"),res.getInt("m.resultOfTeamHome"), res.getTimestamp("m.date").toLocalDateTime(), res.getString("t1.Name"),res.getString("t2.Name"));
				
				
				result.add(match);

			}
			conn.close();
			Collections.sort(result);
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Player> getVertex(int matchID, Map<Integer, Player> idMap){
		String sql = "SELECT DISTINCT `PlayerID`, `TeamID`, ((`TotalSuccessfulPassesAll`+ `Assists`)/`TimePlayed`) as eff "
				+ "FROM actions "
				+ "WHERE `MatchID` = ?";	
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, matchID);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				Player player = idMap.get(res.getInt("PlayerID"));
				player.setEfficienza(res.getDouble("eff"));
				result.add(player);

			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public List<Arco> getEdge(int matchID, Map<Integer, Player> idMap){
		String sql = "SELECT DISTINCT a1.`PlayerID` as p1, a2.`PlayerID` as p2, a1.`TotalSuccessfulPassesAll`as pass1, a1.`Assists` as ass1, a1.`TimePlayed` as t1, a2.`TotalSuccessfulPassesAll`as pass2, a2.`Assists` as ass2, a2.`TimePlayed` as t2  "
				+ "FROM actions a1, actions a2 "
				+ "WHERE a1.`MatchID` = ? AND a1.`MatchID` = a2.`MatchID` AND a1.`PlayerID` > a2.`PlayerID` AND a1.`TeamID` <> a2.`TeamID`";
		
		List<Arco> result = new ArrayList<Arco>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, matchID);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				Player p1 = idMap.get(res.getInt("p1"));
				Player p2 = idMap.get(res.getInt("p2"));
				double eff1 = (res.getInt("pass1") + res.getInt("ass1"))/res.getInt("t1");
				double eff2 = (res.getInt("pass2") + res.getInt("ass2"))/res.getInt("t2");
				double delta = 0;
				if(eff1 > eff2)
					delta = Math.abs(eff1 - eff2);
				else if(eff2 < eff1)
					delta = Math.abs(eff2 - eff1);
				
				Arco arco = new Arco(p1, p2, delta);
				result.add(arco);

			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
