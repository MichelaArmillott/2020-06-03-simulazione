package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public void listAllPlayers(Map<Integer, Player> idMap){
		String sql = "SELECT * FROM Players";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getInt("PlayerID"))) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				idMap.put(player.getPlayerID(), player);
			}}
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			
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

	public List< Player> getVertici(Map<Integer, Player> idMap,double media) {
		String sql="SELECT DISTINCT a.PlayerID,AVG(a.Goals) AS media "
				+ "FROM actions a "
				+ "GROUP BY a.PlayerID "
				+ "HAVING AVG(a.Goals)>? ";
		List<Player>stampa=new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, media);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				stampa.add(idMap.get(res.getInt("PlayerID")));

			}
			conn.close();
			return stampa;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Adiacenza> getArchi(Map<Integer, Player> idMap, double media) {
		List<Adiacenza>stampa=new ArrayList<>();
		String sql="SELECT  a.PlayerID AS id1,a2.PlayerID AS id2,SUM(a.TimePlayed)-SUM(a2.TimePlayed) AS diff "
				+ "FROM actions a,actions a2 "
				+ "WHERE a.PlayerID>a2.PlayerID AND a.MatchID=a2.MatchID AND a.TeamID<>a2.TeamID AND a.`Starts`=1 AND a2.`Starts`=a.`Starts`  "
				+ "GROUP BY a.PlayerID,a2.PlayerID "
				+ "HAVING SUM(a.TimePlayed)-SUM(a2.TimePlayed)<>0 ";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
			stampa.add(new Adiacenza(idMap.get(res.getInt("id1")),idMap.get(res.getInt("id2")),res.getDouble("diff")));

			}
			conn.close();
			return stampa;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
}