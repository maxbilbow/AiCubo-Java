package rmx.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.util.Properties;

public class Database implements SQLData {

//	protected String url = "mysql.maxbilbow.com";
	// init database constants
	private static final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DATABASE_URL = "jdbc:mysql://mysql.maxbilbow.com:80/test";
	private static final String USERNAME = "maxinnit";
	private static final String PASSWORD = "lskaadl";
	private static final String MAX_POOL = "250"; // set your own limit
	
	// init connection object
	private Connection connection;
	// init properties object
	private Properties properties;
	
	public Database() {
		this.properties = this.getProperties();
	}
	
	// create properties
	private Properties getProperties() {
	    if (properties == null) {
	        properties = new Properties();
	        properties.setProperty("user", USERNAME);
	        properties.setProperty("password", PASSWORD);
	        properties.setProperty("MaxPooledStatements", MAX_POOL);
	    }
	    return properties;
	}
	
	
	// connect database
	public Connection connect() {
	    if (connection == null) {
	        try {
	            Class.forName(DATABASE_DRIVER);
	            connection = DriverManager.getConnection(DATABASE_URL, getProperties());
	        } catch (ClassNotFoundException | SQLException e) {
	            // Java 7+
	            e.printStackTrace();
	        }
	    }
	    return connection;
	}
	
	// disconnect database
	public void disconnect() {
	    if (connection != null) {
	        try {
	            connection.close();
	            connection = null;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

	@Override
	public String getSQLTypeName() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void readSQL(SQLInput stream, String typeName) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeSQL(SQLOutput stream) throws SQLException {
		// TODO Auto-generated method stub

	}

	public static void test() {
		// !_ note _! this is just init
		// it will not create a connection
		Database mysqlConnect = new Database();
		String sql = "SELECT * FROM `test`";
		try {
		    PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
//		    ... go on ...
//		    ... go on ...
//		    ... DONE ....
		} catch (SQLException e) {
		    e.printStackTrace();
		} finally {
		    mysqlConnect.disconnect();
		}

	}
}
