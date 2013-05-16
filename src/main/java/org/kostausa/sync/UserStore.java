package org.kostausa.sync;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Model for user store
 * 
 * @author eungyu
 *
 */
public class UserStore 
{
  private static Logger LOG = Logger.getLogger(UserStore.class);
  
  private static final String dbClass = "com.mysql.jdbc.Driver";
  private static final String jdbcURI = "jdbc:mysql://localhost/";
    
  private final Properties _props;
  private final Conference _conference;
  
  private final Set<String> _keys;
  private final String      _connectString;
  
  /**
   * Constructor 
   * 
   * @param conference which conference the store is modelling
   * @throws SQLException
   */
  public UserStore(Conference conference) throws SQLException
  {
    try
    {
      Properties props = new Properties();
      props.load(new FileInputStream("resources/db.properties"));
        
      String db       = props.getProperty("mysql.db");
      String username = props.getProperty("mysql.user");
      String password = props.getProperty("mysql.pass");
    
      if (db == null || username == null || password == null)
      {
        throw new SQLException("Failed to get user credentials");
      }

      _connectString = jdbcURI + db;
      
      _keys = new HashSet<String>();
      _props = new Properties();
      _props.put("user", username);
      _props.put("password", password);
      _conference = conference;

    } 
    catch (Exception e)
    {
      throw new SQLException(e.getMessage());
    }
    
    loadRecords();
  }
  
  /**
   * See if a person exist in the db already
   * 
   * @param person person represented by Kostan object
   * @return true if person exists already, false otherwise.
   *   the lookup is done by email as the key
   */
  public boolean exists(Kostan person)  
  {
    return _keys.contains(person.getEmail());
  }
  
  /**
   * Persist the person in the database
   * 
   * @param kostan single person
   * @throws SQLException
   */  
  public void save(Kostan kostan) throws SQLException
  {
    Connection conn = null;
    PreparedStatement statement = null;
    
    try
    {
      conn = getConnection();
      String query = "INSERT INTO user (kname, email, gender, conf) VALUES (?, ?, ?, ?)";      
      statement = conn.prepareStatement(query);
      statement.setString(1, kostan.getName());
      statement.setString(2, kostan.getEmail());
      statement.setString(3, kostan.getGender());
      statement.setInt(4, kostan.getConference().getConfNum());
      statement.execute();
    }
    catch (Exception e)
    {
      LOG.error(e.getMessage());
      throw new SQLException("Failed to insert record");
    }
    finally
    {
      cleanup(conn, statement, null);
    }
  }
  
  /** 
   * Create connection
   * 
   * @return a connection object
   * 
   * @throws ClassNotFoundException
   * @throws SQLException
   */
  private Connection getConnection() throws ClassNotFoundException, SQLException
  {
    Class.forName(dbClass);  
    return DriverManager.getConnection(_connectString, _props);
  }
  
  /**
   * Cleans up resources
   * 
   * @param conn connection
   * @param statement statement
   * @param results result set
   */
  private void cleanup(Connection conn, Statement statement, ResultSet results)
  {
    try
    {
      if (conn != null)
      {
        conn.close();
      }
      if (statement != null)
      {
        statement.close();
      }
      if (results != null)
      {
        results.close();
      }
    } 
    catch (Exception e)
    {
      LOG.warn("Failed to close some resource, but that's ok");
    }    
  }
  
  /**
   * Load configuration
   * 
   * @throws SQLException
   */
  private void loadRecords() throws SQLException
  {
    LOG.info("Initializing records");
    
    Connection conn = null;
    PreparedStatement statement = null;
    ResultSet records = null;
    try
    {
      conn = getConnection();
      statement = conn.prepareStatement("SELECT email FROM user WHERE conf = ?");
      statement.setInt(1, _conference.getConfNum());
      
      records = statement.executeQuery();

      int count = 0;
      
      while (records.next())
      {
        String key = records.getString("email");
        String sanitizedKey = key.trim().toLowerCase();
        
        if (_keys.contains(sanitizedKey))
        {
          continue;
        }        
        count++;
        _keys.add(sanitizedKey);
      }
      
      StringBuilder stat = new StringBuilder();
      stat.append("Loaded ").append(count).append(" records");
      
      LOG.info(stat.toString());
    }
    catch (Exception e)
    {
      LOG.error("Failed to initialize records: " + e.getMessage());
    }
    finally
    {
      cleanup(conn, statement, records);
    }
  }
    
}
