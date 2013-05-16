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
  
  @Before
  public void setUp() throws Exception {
    _name  = "코스타";
    _gender = "M";
    _email = "web@kostausa.org";
  }

  @Test
  public void testGender() throws IncompleteRecordException
  {
    Kostan person1 = new Kostan(Conference.INDIANAPOLIS, _name, "여", _email);
    assertEquals(person1.getGender(), "F");
    Kostan person2 = new Kostan(Conference.INDIANAPOLIS, _name, "남", _email);        
    assertEquals(person2.getGender(), "M");
  }
  
  @Test
  public void testNullName() throws IncompleteRecordException
  {
    exception.expect(IncompleteRecordException.class);
    Kostan person = new Kostan(Conference.INDIANAPOLIS, null, _gender, _email);
  }

  @Test
  public void testEmptyName() throws IncompleteRecordException
  {
    exception.expect(IncompleteRecordException.class);
    Kostan person1 = new Kostan(Conference.INDIANAPOLIS, "", _gender, _email);

    exception.expect(IncompleteRecordException.class);
    Kostan person2 = new Kostan(Conference.INDIANAPOLIS, "   ", _gender, _email);
  }

  @Test
  public void testNullGender() throws IncompleteRecordException
  {
    exception.expect(IncompleteRecordException.class);
    Kostan person = new Kostan(Conference.INDIANAPOLIS, _name, null, _email);
  }  

  @Test
  public void testNullEmail() throws IncompleteRecordException
  {
    exception.expect(IncompleteRecordException.class);
    Kostan person = new Kostan(Conference.INDIANAPOLIS, _name, _gender, null);
  } 
  
  @Test
  public void testInvalidEmails() throws IncompleteRecordException
  {
    exception.expect(IncompleteRecordException.class);
    Kostan person1 = new Kostan(Conference.INDIANAPOLIS, _name, _gender, "1234");    

    exception.expect(IncompleteRecordException.class);
    Kostan person2 = new Kostan(Conference.INDIANAPOLIS, _name, _gender, "1@1");    

    exception.expect(IncompleteRecordException.class);
    Kostan person3 = new Kostan(Conference.INDIANAPOLIS, _name, _gender, "asdf@asdf");        

    exception.expect(IncompleteRecordException.class);
    Kostan person4 = new Kostan(Conference.INDIANAPOLIS, _name, _gender, "asdf@한글");          

    exception.expect(IncompleteRecordException.class);
    Kostan person5 = new Kostan(Conference.INDIANAPOLIS, _name, _gender, "asdf.hello@hello.e");            
  }

  @Test
  public void testValidEmails() throws IncompleteRecordException
  {
    Kostan person1 = new Kostan(Conference.INDIANAPOLIS, _name, _gender, "web.team@kostausa.org");    
    assertEquals(person1.getEmail(), "web.team@kostausa.org");
    
    Kostan person2 = new Kostan(Conference.INDIANAPOLIS, _name, _gender, "web@kosta.us");    
    assertEquals(person2.getEmail(), "web@kosta.us");
  }
  
  @Test
  public void testTrim() throws IncompleteRecordException
  {
    Kostan person1 = new Kostan(Conference.INDIANAPOLIS, _name, _gender, "  web@kostausa.org  \t ");        
    assertEquals(person1.getEmail(), _email);

    Kostan person2 = new Kostan(Conference.INDIANAPOLIS, _name, "  여 \n", _email);        
    assertEquals(person2.getGender(), "F");
    
    Kostan person3 = new Kostan(Conference.INDIANAPOLIS, " 김 은   규 ", _gender, _email);
    assertEquals(person3.getName(), "김은규");
    
  }
}
