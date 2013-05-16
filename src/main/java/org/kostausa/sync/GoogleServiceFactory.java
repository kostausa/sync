package org.kostausa.sync;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.util.ServiceException;

/**
 * Static factory method to create
 * an authenticated google service
 * (does all the dirty work)
 * 
 * @author eungyu
 *
 */
public class GoogleServiceFactory 
{
  private static Logger LOG = Logger.getLogger(GoogleServiceFactory.class);

  private GoogleServiceFactory() {}
  
  /**
   * Creates a SpreadsheetService and authenticates it
   * The authentication property file (login.properties)
   * should be located in /resources directory
   * 
   * @return authenticated spreadsheet
   * @throws ServiceException
   */
  public static SpreadsheetService createAuthenticatedService() 
    throws ServiceException
  {
    SpreadsheetService service = new SpreadsheetService("eungyu-spreadsheet");
    try
    {
      Properties props = new Properties();
      props.load(new FileInputStream("resources/login.properties"));
        
      String username = props.getProperty("google.user");
      String password = props.getProperty("google.pass");
    
      if (username == null || password == null)
      {
        throw new ServiceException("Failed to get user credentials");
      }
      
      LOG.info("Logging in as " + username);
      service.setUserCredentials(username, password);
      service.setProtocolVersion(SpreadsheetService.Versions.V3);  
    }
    catch (Exception e)
    {
       throw new ServiceException(e.getMessage());
    }
    
    return service;
  }
}
