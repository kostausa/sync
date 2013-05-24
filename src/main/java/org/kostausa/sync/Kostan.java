package org.kostausa.sync;

/**
 * Class to represent a single person attending KOSTA
 * 
 * @author eungyu
 *
 */
public class Kostan 
{  
  public static final String NAME_COLUMN   = "name";
  public static final String EMAIL_COLUMN  = "gender";
  public static final String GENDER_COLUMN = "email";
  public static final String STATUS_COLUMN = "status";
  
  private final String _name;
  private final String _gender;
  private final String _email;
  private final Status _status;
  private final Integer _auxId;
  
  private final Conference _conference;
  private final Track _track;
  
  /**
   * Constructor 
   * 
   * @param conference enum type conference
   * @param entry a row containing all columns
   * @param keymap a map which maps key type to the actual column name
   * @throws IncompleteRecordException
   */
  public Kostan(Conference conference, String name, String gender, String email, 
                String status, Integer auxId, String trackInfo)
    throws IncompleteRecordException
  {        
    if (name == null ||
        email == null ||
        gender == null ||
        status == null || status.equals(""))
    {
      throw new IncompleteRecordException("Incomplete record");
    }
        
    _name = name.trim().replaceAll("\\s", "");
    if (_name.equals(""))
    {
      throw new IncompleteRecordException("empty name");      
    }
    
    gender = gender.trim();
    if (gender.equals("여") || gender.equals("F"))
    {
      _gender = "F";
    }
    else
    {
      _gender = "M";      
    }
    _email = email.trim().toLowerCase();
    
    if (!Validator.isValidEmail(_email))
    {
      throw new IncompleteRecordException("Invalid email format: " + _email);
    }
    
    try
    {
      _status = Status.valueOf(status.toUpperCase());
    }
    catch (Exception e)
    {
      throw new IncompleteRecordException("Invalid status");
    }

    _conference = conference;
    _auxId = auxId;
    
    if (trackInfo == null || trackInfo.equals("") ||
        trackInfo.startsWith("일반"))
    {
      _track = Track.NORMAL;
    }
    else if (trackInfo.startsWith("Journey"))
    {
      _track = Track.JOURNEY;
    }
    else if (trackInfo.startsWith("주제"))
    {
      _track = Track.THEME;
    }
    else if (trackInfo.startsWith("청년"))
    {
      _track = Track.MINISTRY;
    }
    else if (trackInfo.startsWith("기독교"))
    {
      _track = Track.WORLDVIEW;
    }
    else 
    {
      // all other case to default
      _track = Track.NORMAL;
    }
  }
  
  /**
   * Accessor for name
   * 
   * @return name
   */
  public String getName()
  {
    return _name;
  }
  
  /**
   * Accessor for gender
   * 
   * @return gender
   */
  public String getGender()
  {
    return _gender.toUpperCase();
  }
  
  /**
   * Accessor for email
   * 
   * @return email address
   */
  public String getEmail()
  {
    return _email;
  }
  
  /**
   * Accessor for auxId
   * 
   * @return auxilary Id
   */
  public Integer getAuxId()
  {
    return _auxId;
  }
  
  /**
   * Accessor for conference
   * 
   * @return conference
   */
  public Conference getConference()
  {
    return _conference;
  }
  
  public Status getStatus()
  {
    return _status;
  }
  
  public Track getTrack()
  {
    return _track;
  }
  
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append(_conference).append(" ")
      .append(_name)
      .append("(").append(_gender).append(") ")      
      .append(_email);
    
    if (_auxId != null)
    {
      sb.append(" - " + _auxId);
    }

    sb.append(" (" + _track.getShorthand() + ")");    
    return sb.toString();
  }
  
  public static enum Status 
  {
    COMPLETED(0),
    UNPAID(1),
    PENDING(2),
    CANCELED(3), 
    REFUNDED(4);
    
    private int _num;
    
    private Status(int num)
    {
      _num = num;
    }
    
    public int getNum()
    {
      return _num;
    }
    
  }
  
  public static enum Track
  {
    NORMAL('N'),
    JOURNEY('J'),
    THEME('T'),
    WORLDVIEW('W'),
    MINISTRY('M');
    
    private Character _shorthand;
    
    private Track(char ch)
    {
      _shorthand = ch;
    }
    
    public Character getShorthand()
    {
      return _shorthand;
    }
  }
}
