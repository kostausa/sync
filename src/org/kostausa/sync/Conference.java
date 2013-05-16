package org.kostausa.sync;

/**
 * Represents each conference
 * 
 * @author eungyu
 *
 */
public enum Conference 
{
  CHICAGO(0),         // 25-And Up 을 위한 대학원 및 청년 컨퍼런스
  INDIANAPOLIS(1);    // 18-25세를 위한 학부생 컨퍼런스
  
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
