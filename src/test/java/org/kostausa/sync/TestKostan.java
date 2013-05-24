package org.kostausa.sync;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
@SuppressWarnings("unused")
public class TestKostan 
{

  @Rule 
  public ExpectedException exception = ExpectedException.none();
  
  private String _name;
  private String _gender;
  private String _email;
  private String _status;
  
  @Before
  public void setUp() throws Exception {
    _name  = "코스타";
    _gender = "M";
    _email = "web@kostausa.org";
    _status = "Completed";
  }

  @Test
  public void testGender() throws IncompleteRecordException
  {
    Kostan person1 = new Kostan(Conference.INDIANAPOLIS, _name, "여", _email, _status, null, null);
    assertEquals(person1.getGender(), "F");
    Kostan person2 = new Kostan(Conference.INDIANAPOLIS, _name, "남", _email, _status, null, null);        
    assertEquals(person2.getGender(), "M");
  }
  
  @Test
  public void testNullName() throws IncompleteRecordException
  {
    exception.expect(IncompleteRecordException.class);
    Kostan person = new Kostan(Conference.INDIANAPOLIS, null, _gender, _email, _status, null, null);
  }

  @Test
  public void testEmptyName1() throws IncompleteRecordException
  {
    exception.expect(IncompleteRecordException.class);
    Kostan person1 = new Kostan(Conference.INDIANAPOLIS, "", _gender, _email, _status, null, null);
  }
  
  @Test
  public void testEmptyName2() throws IncompleteRecordException
  {
    exception.expect(IncompleteRecordException.class);
    Kostan person2 = new Kostan(Conference.INDIANAPOLIS, "   ", _gender, _email, _status, null, null);
  }

  @Test
  public void testNullGender() throws IncompleteRecordException
  {
    exception.expect(IncompleteRecordException.class);
    Kostan person = new Kostan(Conference.INDIANAPOLIS, _name, null, _email, _status, null, null);
  }  

  @Test
  public void testNullEmail() throws IncompleteRecordException
  {
    exception.expect(IncompleteRecordException.class);
    Kostan person = new Kostan(Conference.INDIANAPOLIS, _name, _gender, null, _status, null, null);
  } 
  
  @Test
  public void testNullStatus() throws IncompleteRecordException
  {
    exception.expect(IncompleteRecordException.class);
    Kostan person = new Kostan(Conference.INDIANAPOLIS, _name, _gender, _email, null, null, null);    
  }
  
  @Test
  public void testStatusComplete() throws IncompleteRecordException
  {
    Kostan person1 = new Kostan(Conference.INDIANAPOLIS, _name, _gender, _email, "Completed", null, null);
    assertEquals(person1.getStatus(), Kostan.Status.COMPLETED);
    
    Kostan person2 = new Kostan(Conference.INDIANAPOLIS, _name, _gender, _email, "CompleteD", null, null);
    assertEquals(person2.getStatus(), Kostan.Status.COMPLETED);  
  }
  
  @Test
  public void testStatusCancelled() throws IncompleteRecordException
  {
    Kostan person1 = new Kostan(Conference.INDIANAPOLIS, _name, _gender, _email, "Canceled", null, null);
    assertEquals(person1.getStatus(), Kostan.Status.CANCELED);
    
    Kostan person2 = new Kostan(Conference.INDIANAPOLIS, _name, _gender, _email, "CaNceLeD", null, null);
    assertEquals(person2.getStatus(), Kostan.Status.CANCELED);      
  }
  
  @Test 
  public void testStatusRefunded() throws IncompleteRecordException
  {
    Kostan person1 = new Kostan(Conference.INDIANAPOLIS, _name, _gender, _email, "refunded", null, null);
    assertEquals(person1.getStatus(), Kostan.Status.REFUNDED);
    
    Kostan person2 = new Kostan(Conference.INDIANAPOLIS, _name, _gender, _email, "REFUNDED", null, null);
    assertEquals(person2.getStatus(), Kostan.Status.REFUNDED);      
  }
  
  @Test 
  public void testStatusUnpaid() throws IncompleteRecordException
  {
    Kostan person1 = new Kostan(Conference.INDIANAPOLIS, _name, _gender, _email, "Unpaid", null, null);
    assertEquals(person1.getStatus(), Kostan.Status.UNPAID);
    
    Kostan person2 = new Kostan(Conference.INDIANAPOLIS, _name, _gender, _email, "UNPAID", null, null);
    assertEquals(person2.getStatus(), Kostan.Status.UNPAID);      
  }  

  @Test 
  public void testStatusPending() throws IncompleteRecordException
  {
    Kostan person1 = new Kostan(Conference.INDIANAPOLIS, _name, _gender, _email, "Pending", null, null);
    assertEquals(person1.getStatus(), Kostan.Status.PENDING);
    
    Kostan person2 = new Kostan(Conference.INDIANAPOLIS, _name, _gender, _email, "PEnding", null, null);
    assertEquals(person2.getStatus(), Kostan.Status.PENDING);      
  }  

  @Test
  public void testInvalidEmail1() throws IncompleteRecordException
  {
    exception.expect(IncompleteRecordException.class);
    Kostan person1 = new Kostan(Conference.INDIANAPOLIS, _name, _gender, "1234", _status, null, null);    
  }

  @Test
  public void testInvalidEmail2() throws IncompleteRecordException
  {
    exception.expect(IncompleteRecordException.class);
    Kostan person2 = new Kostan(Conference.INDIANAPOLIS, _name, _gender, "asdf.hello@", _status, null, null);            
  }

  @Test
  public void testValidEmail1() throws IncompleteRecordException
  {
    Kostan person1 = new Kostan(Conference.INDIANAPOLIS, _name, _gender, "web.team@kostausa.org", _status, null, null);    
    assertEquals(person1.getEmail(), "web.team@kostausa.org");
  }

  @Test
  public void testValidEmail2() throws IncompleteRecordException
  {  
    Kostan person2 = new Kostan(Conference.INDIANAPOLIS, _name, _gender, "web@kosta.us", _status, null, null);    
    assertEquals(person2.getEmail(), "web@kosta.us");
  }
  
  @Test
  public void testTrim() throws IncompleteRecordException
  {
    Kostan person1 = new Kostan(Conference.INDIANAPOLIS, _name, _gender, "  web@kostausa.org  \t ", _status, null, null);        
    assertEquals(person1.getEmail(), _email);

    Kostan person2 = new Kostan(Conference.INDIANAPOLIS, _name, "  여 \n", _email, _status, null, null);        
    assertEquals(person2.getGender(), "F");
    
    Kostan person3 = new Kostan(Conference.INDIANAPOLIS, " 김 은   규 ", _gender, _email, _status, null, null);
    assertEquals(person3.getName(), "김은규");
    
  }
}
