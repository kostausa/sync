package org.kostausa.sync;

/**
 * Represents each conference
 * 
 * @author eungyu
 *
 */
public enum Conference 
{
  CHICAGO(0),         // 25-And Up 대학원생과 청년들을 위한 집회
  INDIANAPOLIS(1);    // 18-25세의 학부생들 중심 집회
  
  private final int _num;
  
  Conference(int num)
  {
    _num = num;
  }
  
  public int getConfNum()
  {
    return _num;
  }
  
  @Override
  public String toString()
  {
    String name = super.toString().toLowerCase();
    String firstLetterCap = name.substring(0, 1).toUpperCase() + 
                            name.substring(1);
    
    return firstLetterCap;
  }  
}
