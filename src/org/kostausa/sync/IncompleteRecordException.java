package org.kostausa.sync;

/**
 * Exception to represent incomplete record
 * 
 * @author Eun-Gyu Kim <eungyu.kim@gmail.com>
 *
 */
public class IncompleteRecordException extends Exception 
{
  /**
   * Generated
   */
  private static final long serialVersionUID = 1L;

  public IncompleteRecordException(String msg)
  {
    super(msg);
  }
  
  public IncompleteRecordException(String msg, Throwable e)
  {
    super(msg, e);
  }
}
