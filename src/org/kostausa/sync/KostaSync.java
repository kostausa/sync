package org.kostausa.sync;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

public class KostaSync 
{ 
  private final static Logger LOG = Logger.getLogger(KostaSync.class.getName());
  
  private final URL                _targetUrl;
  private final SpreadsheetService _service;
  private final Map<String, String> _keymap;
  
  public KostaSync(URL targetUrl) throws ServiceException
  {
    String username = "eungyu.kim@gmail.com";
    String password = "yumndfajucxhcssl";
  
    _service = new SpreadsheetService("eungyu-spreadsheet");
    _service.setUserCredentials(username, password);
    _service.setProtocolVersion(SpreadsheetService.Versions.V3);  

    _keymap = new HashMap<String, String>();
    _keymap.put(Kostan.NAME_COLUMN, "성명한글");
    _keymap.put(Kostan.GENDER_COLUMN, "성별");
    _keymap.put(Kostan.EMAIL_COLUMN, "emailaddress");
    
    _targetUrl = targetUrl;
  }

  public void run() throws IOException, ServiceException
  {
    SpreadsheetEntry spreadsheet = _service.getEntry(_targetUrl, SpreadsheetEntry.class);
    
    WorksheetFeed feed = _service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
    List<WorksheetEntry> worksheets = feed.getEntries();
    
    WorksheetEntry worksheet = worksheets.get(0); // get the first
    ListFeed records = _service.getFeed(worksheet.getListFeedUrl(), ListFeed.class);
    
    for (ListEntry entry : records.getEntries())
    {
      Kostan person = null;
      try
      {
        person = new Kostan(Conference.INDIANAPOLIS, entry, _keymap);
      }
      catch (IncompleteRecordException e)
      {
        LOG.warning("Skipping user");
      }
      
      LOG.info(person.toString());
    }    
  }

  public static void main(String[] args) throws IOException, ServiceException
  {
    LOG.setLevel(Level.INFO);

    URL indyUrl = 
        new URL("https://spreadsheets.google.com/feeds/spreadsheets/tyeT6YZgHyssi7AA9AAsQEw");
    
    KostaSync sync = null;
    try
    {
      sync = new KostaSync(indyUrl);
    }
    catch (ServiceException e)
    {
      LOG.warning("Failed to initialize service");
      return;
    }
  
    try
    {
      sync.run();
    }
    catch (Exception e)
    {
      LOG.severe("Sync process stopped abnormally");
      LOG.severe(e.getMessage());
      return;
    }    
    
  }
}
