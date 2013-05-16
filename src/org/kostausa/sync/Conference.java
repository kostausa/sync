package org.kostausa.sync;

/**
 * Two conferences
 * 
 * @author eungyu
 *
 */
public enum Conference 
{
  CHICAGO(0),
  INDIANAPOLIS(1);
  
  private final int _num;
  
  Conference(int num)
  {
    _num = num;
  }
  
  public int getConfNum()
  {
    return _num;
  }
}
