package org.kostausa.sync;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CustomElementCollection;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

/**
 * Sync Service between KOSTA Database and Google
 * 
 * @author eungyu
 *
 */
public class KostaSync
{ 
  private final static Logger LOG = Logger.getLogger(KostaSync.class);
  
  private final URL                _targetUrl;
  private final SpreadsheetService _service;  
  private final Conference         _conference;
  private final List<ColumnMapper> _mappers;
  /**
   * Constructor
   * 
   * @param targetUrl target spreadsheet feed to connect to 
   * @throws ServiceException
   * @throws IOException 
   * @throws FileNotFoundException 
   */
  public KostaSync(Conference conference, URL targetUrl, List<ColumnMapper> mappers)
    throws ServiceException
  {
    // the dirty work of authenticating is hidden to a factory
    _service = GoogleServiceFactory.createAuthenticatedService();        
    _targetUrl = targetUrl;
    _conference = conference;
    _mappers = mappers;
  }

  public void list() throws IOException, ServiceException
  {
    SpreadsheetFeed feed = _service.getFeed(new URL("https://spreadsheets.google.com/feeds/spreadsheets"), SpreadsheetFeed.class);
  
    for (SpreadsheetEntry entry : feed.getEntries())
    {
      LOG.info(entry.getTitle().getPlainText() + "/" + entry.getWorksheetFeedUrl());
    }
  }
  
  
  /**
   * Main loop of instantiating Kostan 
   * and syncing them with MySQL database
   * 
   * @throws IOException
   * @throws ServiceException
   * @throws SQLException 
   */
  public void run() throws IOException, ServiceException, SQLException
  {
    SpreadsheetEntry spreadsheet = _service.getEntry(_targetUrl, SpreadsheetEntry.class);
    
    WorksheetFeed feed = _service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
    List<WorksheetEntry> worksheets = feed.getEntries();    
    
    int worksheetIndex = 0;
    if (_conference == Conference.CHICAGO)
    {
      worksheetIndex = 1;
    }
    
    WorksheetEntry worksheet = worksheets.get(worksheetIndex); // get the first
    ListFeed records = _service.getFeed(worksheet.getListFeedUrl(), ListFeed.class);
    
    UserStore userStore = new UserStore(_conference);
    
    int count = 0;
    String trackKey = null;
    
    for (ListEntry entry : records.getEntries())
    {
      Kostan person = null;
      
      for (ColumnMapper mapper : _mappers)
      {
        try
        {
          CustomElementCollection columns = entry.getCustomElements();

          // lazily find out what the track column header is 
          if (mapper.getTrackColumn() != null &&
              trackKey == null)
          {
            for (String tag : columns.getTags())
            {
              if (tag.startsWith(mapper.getTrackColumn()))
              {
                trackKey = tag;
              }
            }          
          }
          
          String name   = columns.getValue(mapper.getNameColumn());
          String email  = columns.getValue(mapper.getEmailColumn());
          String gender = columns.getValue(mapper.getGenderColumn());
          String status = columns.getValue(mapper.getStatusColumn());
          
          String cancelKey = mapper.getCancelColumn();
          if (cancelKey != null)
          {
            String cancel = columns.getValue(cancelKey);
            if (cancel != null && cancel.equals("1"))
            {
              status = Kostan.Status.CANCELED.toString();
            }
          }

          Integer auxId = null;
          String auxKey = mapper.getAuxColumn();
          if (auxKey != null)
          {
            String aux = columns.getValue(auxKey);
            if (aux != null && !aux.equals(""))
            {
              auxId = Integer.valueOf(aux);
            }
          }
          
          String trackInfo = null;
          if (trackKey != null)
          {
            trackInfo = columns.getValue(trackKey);
          }
          person = new Kostan(_conference, name, gender, email, status, auxId, trackInfo);          
          
          LOG.debug(person.toString());
        }
        catch (Exception e)
        {
          LOG.debug("Skipping user: " + e.getMessage());
          continue;
        }        

        try
        {
          userStore.sync(person);
        }
        catch (SQLException e)
        {
          LOG.warn("Failed to sync record " + person.toString());
          continue;
        }

        count++;
      }
      
    }    
    
    LOG.info("Processed " + count + " new records");
  }

  /**
   * Main entry point
   * 
   * @param args
   * @throws IOException
   * @throws ServiceException
   */
  public static void main(String[] args) throws IOException, ServiceException
  {
    PropertyConfigurator.configure("resources/log4j.properties");

    LOG.info("=============");
    LOG.info("Starting Sync");
    
    // TODO: make this config driven
    
    URL indyUrl = 
        new URL("https://spreadsheets.google.com/feeds/spreadsheets/tyeT6YZgHyssi7AA9AAsQEw");
    
    URL chicagoUrl = 
        new URL("https://spreadsheets.google.com/feeds/spreadsheets/tQuRx5nxqYDoqwoeqBGzv-g");
    
    
    ColumnMapper indyMapper = new ColumnMapper("성명한글", "성별", "emailaddress", "paymentstatus");    
    ColumnMapper chicagoMapper = new ColumnMapper("이름", "성별", "이메일주소", "paymentstatus");    
    ColumnMapper chicagoSpouseMapper = new ColumnMapper("배우자이름", "배우자성별", "배우자이메일", "paymentstatus");

    chicagoMapper.setCancelColumn("등록취소");
    chicagoMapper.setAuxColumn("_cssly");
    chicagoMapper.setTrackColumn("트랙선택");
    
    chicagoSpouseMapper.setCancelColumn("등록취소");
    chicagoSpouseMapper.setAuxColumn("_cssly");
    chicagoSpouseMapper.setTrackColumn("트랙선택");
    
    List<ColumnMapper> indyMapperList = new ArrayList<ColumnMapper>(1);
    indyMapperList.add(indyMapper);
    
    List<ColumnMapper> chicagoMapperList = new ArrayList<ColumnMapper>(2);
    chicagoMapperList.add(chicagoMapper);
    chicagoMapperList.add(chicagoSpouseMapper);
    
    KostaSync indySync = null;
    KostaSync chicagoSync = null;
    try
    {
      indySync = new KostaSync(Conference.INDIANAPOLIS, indyUrl, indyMapperList);
      chicagoSync = new KostaSync(Conference.CHICAGO, chicagoUrl, chicagoMapperList);
    }
    catch (ServiceException e)
    {
      LOG.warn("Failed to initialize service");
      return;
    }
  
    try
    {
      indySync.run();
    }
    catch (Exception e)
    {
      LOG.error("Sync process stopped abnormally");
      LOG.error(e.getMessage());
      return;
    }    

    try
    {
      chicagoSync.run();
    }
    catch (Exception e)
    {
      LOG.error("Sync process stopped abnormally");
      LOG.error(e.getMessage());
      return;
    }    
    
    
  }
}