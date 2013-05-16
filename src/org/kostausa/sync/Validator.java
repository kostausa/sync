package org.kostausa.sync;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Validator 
{
  public static boolean isValidEmail(String email)
  {
    boolean result = true;
    try
    {
      InternetAddress emailAddr = new InternetAddress(email);
      emailAddr.validate();
    }
    catch (AddressException e)
    {
      result = false;
    }
    return result;
  }
}
