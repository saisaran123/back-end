import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connectdb {
	private static Connection con=null;
	private static Connectdb db=null;
	
	
	
	private Connectdb() throws SQLException{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

		}
		
		catch(Exception e) {
			e.printStackTrace();
		}
		
		con=DriverManager.getConnection("jdbc:mysql://localhost:3306/jan23","sai","erenxnaruto");
		


	}
	public static Connectdb getInstance() throws SQLException {
		if(con==null) {
			db=new Connectdb();
		}
		return db;
	}
	
	public Connection getConnection() {
		return con;
	}
}
