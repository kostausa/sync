package org.kostausa.sync;

public class ColumnMapper 
{
  private String _nameColumn;
  private String _genderColumn;
  private String _emailColumn;
  private String _statusColumn;
  
  private String _cancelColumn;
  private String _auxColumn;
  private String _trackColumn;
  
  public ColumnMapper(String name, String gender, String email, String status)
  {
    _nameColumn = name;
    _genderColumn = gender;
    _emailColumn = email;
    _statusColumn = status;
    _cancelColumn = null;
    _auxColumn = null;
    _trackColumn = null;
  }
  
  public String getNameColumn()
  {
    return _nameColumn;
  }
  
  public String getGenderColumn()
  {
    return _genderColumn;
  }
  
  public String getEmailColumn()
  {
    return _emailColumn;
  }
  
  public String getStatusColumn()
  {
    return _statusColumn;
  }
  
  public void setCancelColumn(String cancelColumn)
  {
    _cancelColumn = cancelColumn;
  }
  
  public void setAuxColumn(String auxColumn)
  {
    _auxColumn = auxColumn;
  }
  
  public void setTrackColumn(String trackColumn)
  {
    _trackColumn = trackColumn;
  }
  
  public String getAuxColumn()
  {
    return _auxColumn;
  }
  
  public String getCancelColumn()
  {
    return _cancelColumn;
  }
  
  public String getTrackColumn()
  {
    return _trackColumn;
  }
  
}
