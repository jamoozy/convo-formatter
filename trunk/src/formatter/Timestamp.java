package formatter;

/**
 * A time stamp is an hour, minute and second group representing the time.
 * 
 * @author Andrew Correa
 */
public class Timestamp
{
  // The time data.
  private final int hour;
  private final int minute;
  private final int second;

  // //////////////////////////////////////////////////////////////////////////
  // -------------------------- Initialization ---------------------------- //
  // //////////////////////////////////////////////////////////////////////////

  // FIXME This does not work on input "(3:31:55 PM)"
  public static Timestamp parseTimestamp(String time)
  {
    String[] parts = time.replace("[()]", "").split(":| ");

    int h, m, s;

    try
    {
      h = Integer.parseInt(parts[0]);
    }
    catch (NumberFormatException nfe)
    {
      throw new IllegalArgumentException("Hour is not a number: " + parts[0]);
    }

    try
    {
      m = Integer.parseInt(parts[0]);
    }
    catch (NumberFormatException nfe)
    {
      throw new IllegalArgumentException("Minute is not a number: " + parts[1]);
    }

    try
    {
      s = Integer.parseInt(parts[0]);
    }
    catch (NumberFormatException nfe)
    {
      throw new IllegalArgumentException("Second is not a number: " + parts[2]);
    }

    if (parts.length == 4 && parts[3].equalsIgnoreCase("pm"))
      h += 12;

    System.out.printf("Got (%d:%d:%d)\n", h, m, s);
    return new Timestamp(h, m, s);
  }

  // //////////////////////////////////////////////////////////////////////////
  // -------------------------- Initialization ---------------------------- //
  // //////////////////////////////////////////////////////////////////////////

  /**
   * Makes a new <code>Timestamp</code> representing the time:<br />
   * <code>h : m : s </code>
   * 
   * @param h
   *          Hour of the time.
   * @param m
   *          Minute of the time.
   * @param s
   *          Second of the time.
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

  // //////////////////////////////////////////////////////////////////////////
  // ---------------------------- Accessors ------------------------------- //
  // //////////////////////////////////////////////////////////////////////////

  /**
   * Returns the hour in the 12-hour time system.
   * 
   * @return A number in [1,12]
   */
  public int getHour12()
  {
    return hour + ((hour > 12) ? -12 : ((hour == 0) ? 12 : 0));
  }

  /**
   * Returns the hour in the 24-hour time system.
   * 
   * @return A number in [0,23]
   */
  public int getHour24()
  {
    return hour;
  }

  /**
   * Returns the minute of time.
   * 
   * @return A number in [0,59]
   */
  public int getMinute()
  {
    return minute;
  }

  /**
   * Returns the second in time.
   * 
   * @return A number in [0,59]
   */
  public int getSecond()
  {
    return second;
  }

  /**
   * Returns <code>"AM"</code> or <code>"PM"</code> depending on whether it's am
   * or pm.
   * 
   * @return <code>"AM"</code> or <code>"PM"</code>
   */
  public String getAMPM()
  {
    return ((hour > 11) ? "PM" : "AM");
  }

  @Override
  public String toString()
  {
    return String.format("(%02d:%02d:%02d)", hour, minute, second);
  }

  public static Timestamp parseString12(String fmt)
  {
    int h, m, s, pos;

    System.out.println("Got string: " + fmt);

    // Get the hour.
    if (fmt.charAt(0) == '(')
    {
      try
      {
        h = Integer.parseInt(fmt.substring(1, 3));
      }
      catch (NumberFormatException nfe)
      {
        h = Integer.parseInt(fmt.substring(1, 2));
      }
      pos = 3;
    }
    else
    {
      try
      {
        h = Integer.parseInt(fmt.substring(0, 2));
      }
      catch (NumberFormatException nfe)
      {
        h = Integer.parseInt(fmt.substring(0, 1));
      }
      pos = 2;
    }

    // Get the minute.
    if (Character.isDigit(fmt.charAt(pos)))
    {
      try
      {
        m = Integer.parseInt(fmt.substring(pos, pos + 2));
      }
      catch (NumberFormatException nfe)
      {
        m = Integer.parseInt(fmt.substring(pos, pos + 1));
      }
      pos += 2;
    }
    else
    {
      try
      {
        m = Integer.parseInt(fmt.substring(pos + 1, pos + 3));
      }
      catch (NumberFormatException nfe)
      {
        m = Integer.parseInt(fmt.substring(pos + 1, pos + 2));
      }
      pos += 3;
    }

    // Get the second.
    if (Character.isDigit(fmt.charAt(pos)))
    {
      try
      {
        s = Integer.parseInt(fmt.substring(pos, pos + 2));
      }
      catch (NumberFormatException nfe)
      {
        s = Integer.parseInt(fmt.substring(pos, pos + 1));
      }
      pos += 2;
    }
    else
    {
      try
      {
        s = Integer.parseInt(fmt.substring(pos + 1, pos + 3));
      }
      catch (NumberFormatException nfe)
      {
        s = Integer.parseInt(fmt.substring(pos + 1, pos + 2));
      }
      pos += 3;
    }

    // Determine AM/PM.
    while (!Character.isLetter(fmt.charAt(pos)) && pos < fmt.length())
      pos++;

    if (fmt.substring(pos, pos + 2).equalsIgnoreCase("pm"))
      h += 12;

    System.out.printf("Got (%2d,%2d,%2d)\n", h, m, s);
    return new Timestamp(h, m, s);
  }
}
