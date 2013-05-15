package org.kostausa.sync;

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


public class UserStore 
{
  private final static Logger LOG = Logger.getLogger(UserStore.class);
  
  private static final String dbClass = "com.mysql.jdbc.Driver";
  private static final String jdbcURI = "jdbc:mysql://localhost/kosta";
    
  private final Properties _props;
  private final Conference _conference;
  
  private final Set<String> _keys;
  
  public UserStore(Conference conference) throws SQLException
  {
    _keys = new HashSet<String>();
    _props = new Properties();
    _props.put("user", "eungyu");
    _props.put("password", "rladmsrb");
    _conference = conference;
    
    loadRecords();
  }
    
  public boolean exists(Kostan person)  
  {
    return _keys.contains(person.getEmail());
  }
  
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
    }
    finally
    {
      cleanup(conn, statement, null);
    }
  }
  
  private Connection getConnection() throws ClassNotFoundException, SQLException
  {
    Class.forName(dbClass);  
    return DriverManager.getConnection(jdbcURI, _props);
  }
  
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
      while (records.next())
      {
        String key = records.getString("email");
        String sanitizedKey = key.trim().toLowerCase();
        
        if (_keys.contains(sanitizedKey))
        {
          continue;
        }        
        _keys.add(sanitizedKey);
      }
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
