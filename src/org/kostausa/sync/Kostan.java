package org.kostausa.sync;

import java.util.Map;

import com.google.gdata.data.spreadsheet.CustomElementCollection;
import com.google.gdata.data.spreadsheet.ListEntry;

/**
 * Class to represent a single person attending KOSTA
 * 
 * @author Eun-Gyu Kim <eungyu.kim@gmail.com>
 *
 */
public class Kostan 
{  
  public static final String NAME_COLUMN   = "name";
  public static final String EMAIL_COLUMN  = "gender";
  public static final String GENDER_COLUMN = "email";
  
  private final String _name;
  private final String _gender;
  private final String _email;
  private final Conference _conference;
  
  /**
   * Constructor 
   * 
   * @param conference enum type conference
   * @param entry a row containing all columns
   * @param keymap a map which maps key type to the actual column name
   * @throws IncompleteRecordException
   */
  public Kostan(Conference conference, ListEntry entry, Map<String, String> keymap)
    throws IncompleteRecordException
  {
    String nameKey = keymap.get(NAME_COLUMN);
    String emailKey = keymap.get(EMAIL_COLUMN);
    String genderKey = keymap.get(GENDER_COLUMN);
    
    if (nameKey == null ||
        emailKey == null ||
        genderKey == null)
    {
      throw new IncompleteRecordException("Keymap does not contain required fields");
    }
    
    CustomElementCollection columns = entry.getCustomElements();
    String name = columns.getValue(nameKey);
    String email = columns.getValue(emailKey);
    String gender = columns.getValue(genderKey);
    
    if (name == null ||
        email == null ||
        gender == null)
    {
      throw new IncompleteRecordException("Incomplete record");
    }
    
    _name = name.trim();
    if (gender.equals("여"))
    {
      _gender = "F";
    }
    else
    {
      _gender = "M";      
    }
    _email = email.toLowerCase().trim();
    _conference = conference;
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
   * Accessor for conference
   * 
   * @return conference
   */
  public Conference getConference()
  {
    return _conference;
  }
  
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append(_conference).append(" ")
      .append(_name)
      .append("(").append(_gender).append(") ")      
      .append(_email);
    
    return sb.toString();
  }
}
