package org.kostausa.sync;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * Validator utility
 * 
 * @author eungyu
 *
 */
public class Validator 
{
  /**
   * Checks validity of email string
   * with the 'official' java email validator
   * 
   * @param email email in string format
   * @return true if it's known to be a valid email format
   *   false otherwise
   */
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
