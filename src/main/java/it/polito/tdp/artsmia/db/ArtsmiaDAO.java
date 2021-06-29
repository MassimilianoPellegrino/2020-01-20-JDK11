package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.Arco;
import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Author;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Author> getVertici(String role, Map<Integer, Author> idMap){
		String sql = "SELECT DISTINCT(ar.artist_id) AS id, ar.name AS nome "
				+ "FROM artists ar, authorship au "
				+ "WHERE ar.artist_id = au.artist_id "
				+ "AND au.role = ?";
		
		List<Author> result = new ArrayList<>();
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, role);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Author author = new Author(res.getInt("id"), res.getString("nome"));
				
				if(!idMap.containsValue(author))
					idMap.put(author.getId(), author);
				
				result.add(author);
			}
			conn.close();
			
			//System.out.println(result.size()+"\n"+idMap.size());
			
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Arco> getArchi(String role, Map<Integer, Author> idMap){
		String sql = "SELECT a1.artist_id as a1, a2.artist_id as a2, COUNT(distinct e1.exhibition_id) as w "
				+ "FROM authorship a1, authorship a2, exhibition_objects e1, exhibition_objects e2 "
				+ "WHERE a1.role = ? "
				+ "AND a2.role = a1.role "
				+ "AND a1.artist_id>a2.artist_id "
				+ "AND a1.object_id = e1.object_id "
				+ "AND a2.object_id = e2.object_id "
				+ "AND e1.exhibition_id = e2.exhibition_id "
				+ "GROUP BY a1.artist_id, a2.artist_id";
		
		List<Arco> result = new ArrayList<>();
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, role);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Arco arco = new Arco(idMap.get(res.getInt("a1")), idMap.get(res.getInt("a2")), res.getInt("w"));
				
				result.add(arco);
			}
			conn.close();
			
			//System.out.println(result.size());
			
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getRuoli(){
		String sql = "SELECT DISTINCT role AS role "
				+ "FROM authorship";
		
		List<String> result = new ArrayList<>();
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				result.add(res.getString("role"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
	
}
