package org.kostausa.sync;

import java.io.FileInputStream;
import java.util.Properties;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.util.ServiceException;

public class GoogleServiceFactory 
{
  private GoogleServiceFactory() {}
  
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
