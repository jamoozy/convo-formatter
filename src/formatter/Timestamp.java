package formatter;

/**
 * A time stamp is an hour, minute and second group representing the time.
 * 
 * @author Andrew Correa
 */
public class Timestamp
{
  // The time data.
  private int hour;
  private int minute;
  private int second;

  
  
  ////////////////////////////////////////////////////////////////////////////
  // -------------------------- Initialization ---------------------------- //
  ////////////////////////////////////////////////////////////////////////////  

  /**
   * Makes a new <code>Timestamp</code> representing the time:<br />
   * <code>h : m : s </code>
   * @param h Hour of the time.
   * @param m Minute of the time.
   * @param s Second of the time.
   */
  public Timestamp(int h, int m, int s)
  {    
    
    if (h > 23 || h < 0)
      throw new IllegalArgumentException("Hour must be in [0,23]  got:" + h);
    
    if (m > 59 || m < 0)
      throw new IllegalArgumentException("Minute must be in [0,59]  got:" + m);
    
    if (s > 59 || s < 0)
      throw new IllegalArgumentException("Second must be in [0,59]  got:" + s);
    
    hour = h;
    minute = m;
    second = s;
  }
  
  
  
  ////////////////////////////////////////////////////////////////////////////
  // ---------------------------- Accessors ------------------------------- //
  ////////////////////////////////////////////////////////////////////////////  
  
  /**
   * Returns the hour in the 12-hour time system.
   * @return A number in [1,12]
   */
  public int getHour12() { return hour + ((hour > 12)? -12 : ((hour == 0)? 12 : 0)); }
  
  /**
   * Returns the hour in the 24-hour time system.
   * @return A number in [0,23]
   */
  public int getHour24() { return hour; }
  
  /**
   * Returns the minute of time.
   * @return A number in [0,59]
   */
  public int getMinute() { return minute; }
  
  /**
   * Returns the second in time.
   * @return A number in [0,59]
   */
  public int getSecond() { return second; }
  
  /**
   * Returns <code>"AM"</code> or <code>"PM"</code> depending on whether it's am or pm.
   * @return <code>"AM"</code> or <code>"PM"</code>
   */
  public String getAMPM() { return ((hour > 11)? "PM" : "AM"); }
}
