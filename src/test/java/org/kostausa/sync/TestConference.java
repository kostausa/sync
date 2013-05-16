package org.kostausa.sync;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestConference 
{

  @Test
  public void testValueOf()
  {
    Conference c1 = Conference.valueOf("CHICAGO");
    assertEquals(c1, Conference.CHICAGO);

    Conference c2 = Conference.valueOf("INDIANAPOLIS");
    assertEquals(c2, Conference.INDIANAPOLIS);
  }

  @Test
  public void testGetNum()
  {
    assertEquals(Conference.CHICAGO.getConfNum(), 0);
    assertEquals(Conference.INDIANAPOLIS.getConfNum(), 1);    
  }
  
  @Test
  public void testToString()
  {
    assertEquals(Conference.CHICAGO.toString(), "Chicago");
    assertEquals(Conference.INDIANAPOLIS.toString(), "Indianapolis");   
  }
}
