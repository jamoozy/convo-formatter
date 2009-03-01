package formatter;

/**
 * Stores the year, month, date (day of the month), and day (name of the day of
 * the week).  The distinction between the "date" and the "day" are that the 
 * date is a number from 1 to 31 and is relative to the month, whereas the day
 * is a {@link String} that correlates to the day within the week, e.g.
 * <code>"Tuesday"</code> and <code>"Friday"</code>.
 * 
 * @author Andrew Correa
 */
public class Date
{
  ////////////////////////////////////////////////////////////////////////////
  // ---------------------------- Conversions ----------------------------- //
  ////////////////////////////////////////////////////////////////////////////
  
  /**
   * Converts the smaller section of a day-of-the-week's name to the full section.
   *
   * @param day The beginning of the day of the week.  Must be at least 2 characters.
   * @return The full name of the day of the week.  E.g. "Wednesday"
   *
   * @throws IllegalArgumentException If the parameter is not a day of the week.
   */
  public static String dayToFullName(String day)
  {
    switch (day.charAt(0))
    {
      case 'M':
      case 'm':
        return "Monday";
      case 'T':
      case 't':
        return ((day.charAt(1) == 'u' || day.charAt(1) == 'U')? "Tuesday" : "Thursday");
      case 'W':
      case 'w':
        return "Wednesday";
      case 'F':
      case 'f':
        return "Friday";
      case 'S':
      case 's':
        return ((day.charAt(1) == 'u' || day.charAt(1) == 'U')? "Sunday" : "Saturday");
      default:
        System.err.println("Got day:" + day + " in TrillianReader.dayToFullName()");
        return null;
    }
  }
  
  /**
   * Returns the number corresponding to the month in the year.  E.g. March is 3, December is 12.
   *
   * @param month The name of the month.  Must be at least 3 characters long.
   * @return The number corresponding to the month in the year.
   *
   * @throws IllegalArgumentException If the string is not a valid month.
   */
  public static int monthToInt(String month)
  {
    if (month.substring(0,3).toLowerCase().equals("jan"))
      return 1;
    if (month.substring(0,3).toLowerCase().equals("feb"))
      return 2;
    if (month.substring(0,3).toLowerCase().equals("mar"))
      return 3;
    if (month.substring(0,3).toLowerCase().equals("apr"))
      return 4;
    if (month.substring(0,3).toLowerCase().equals("may"))
      return 5;
    if (month.substring(0,3).toLowerCase().equals("jun"))
      return 6;
    if (month.substring(0,3).toLowerCase().equals("jul"))
      return 7;
    if (month.substring(0,3).toLowerCase().equals("aug"))
      return 8;
    if (month.substring(0,3).toLowerCase().equals("sep"))
      return 9;
    if (month.substring(0,3).toLowerCase().equals("oct"))
      return 10;
    if (month.substring(0,3).toLowerCase().equals("nov"))
      return 11;
    if (month.substring(0,3).toLowerCase().equals("dec"))
      return 12;

    throw new IllegalArgumentException("Unrecognized month:" + month);
  }
  
  
  
  ////////////////////////////////////////////////////////////////////////////
  // ---------------------------- Actual Class ---------------------------- //
  ////////////////////////////////////////////////////////////////////////////

  // Date
  private int year;
  private int month;
  private int date;
  
  // Name of the day of the week.
  private String day;
  
  
  
  ////////////////////////////////////////////////////////////////////////////
  // --------------------------- Initialization --------------------------- //
  ////////////////////////////////////////////////////////////////////////////

  /**
   * Stores the date represented by the year, month, and date parameters.
   * Computes which date of the week this date represents.
   * 
   * @param y Year of the date.
   * @param m Month of the date.
   * @param dt Date of the date -- the day-portion.
   */
  public Date(int y, int m, int dt)
  {
    // First this, for error checking.
    this(y,m,dt,null);
    
    // Use this to compute the date for us (yay).
    java.util.Calendar c = java.util.Calendar.getInstance();
    c.set(java.util.Calendar.YEAR, y);
    c.set(java.util.Calendar.MONTH, m);
    c.set(java.util.Calendar.DAY_OF_MONTH, dt);
    day = intToString(c.get(java.util.Calendar.DAY_OF_WEEK));
  }
  
  /**
   * Stores the date represented by the year, month, and date parameters.
   * 
   * @param y Year of the date.
   * @param m Month of the date.
   * @param dt Date of the date -- the day-portion.
   * @param dy The day of the week.
   */
  public Date(int y, int m, int dt, String dy)
  {
    switch (m)
    {
      case 1:    // January
      case 3:    // March
      case 5:    // May
      case 7:    // July
      case 8:    // August
      case 10:   // October
      case 12:   // December
        if (dt > 31)
          throw new IllegalArgumentException("Date must be in bounds of month. Month:" + m + " Date:" + dt);
        else
          break;
        
      case 2:    // February
        if (dt > 28)
          throw new IllegalArgumentException("Date must be in bounds of month. Month:" + m + " Date:" + dt);
        else
          break;
        
      case 4:    // April
      case 6:    // June
      case 9:    // September
      case 11:   // November
        if (dt > 30)
          throw new IllegalArgumentException("Date must be in bounds of month. Month:" + m + " Date:" + dt);
        else
          break;
        
      default:
        throw new IllegalArgumentException("Month must be in [1,12].  got:" + m);
    }
    
    if (dt < 1)
      throw new IllegalArgumentException("Date must be greater than 0.  got:" + dt);

    year = y;
    month = m;
    date = dt;
    day = dy;
  }
  
  /*
   * Converts from a {@link Calendar} int representation of a day of the
   * week to a string representation.
   *  
   * @param i The int representation.
   * @return The String representation.
   */
  private String intToString(int i)
  {
    switch(i)
    {
      case java.util.Calendar.MONDAY:
        return "Monday";
      case java.util.Calendar.TUESDAY:
        return "Tuesday";
      case java.util.Calendar.WEDNESDAY:
        return "Wednesday";
      case java.util.Calendar.THURSDAY:
        return "Thursday";
      case java.util.Calendar.FRIDAY:
        return "Friday";
      case java.util.Calendar.SATURDAY:
        return "Saturday";
      case java.util.Calendar.SUNDAY:
        return "Sunday";
      default:
        throw new IllegalArgumentException("Illegal day:" + i);
    }
  }
  
  
  
  ////////////////////////////////////////////////////////////////////////////
  // ---------------------------- Accessors ------------------------------- //
  ////////////////////////////////////////////////////////////////////////////

  /**
   * Returns the year.  E.g. "2005"
   * @return The year.
   */
  public int getYear() { return year; }
  
  /**
   * Returns the month of th year, e.g. "3" for March.
   * @return The month.
   */
  public int getMonth() { return month; }
  
  /**
   * Returns the day of the month.  E.g. the 4th.
   * @return The date.
   */
  public int getDate() { return date; }
  
  /**
   * Returns the day of the week.  E.g. "Wednesday"
   * @return The day.
   */
  public String getDay() { return day; }

  /**
   * Returns a <code>String</code> representation of this <code>Date</code>.
   * @return <code>String</code> representation.
   */
  public String toString()
  {
    return year + "/" + month + "/" + date + " -- (" + day + ")";
  }
  
  /**
   * Determines if the passed date object is equivalent to this one.
   * 
   * @param that The other date to compare <code>this</code> with.
   * 
   * @return Returns <code>true</code> if the dates are the same
   *         and <code>false</code> otherwise.
   */
  public boolean equals(Object that)
  {
    return that instanceof Date && this.equals((Date)that);
  }

  /**
   * Determines if the passed date object is equivalent to this one.
   * 
   * @param that The other date to compare <code>this</code> with.
   * 
   * @return Returns <code>true</code> if the dates are the same
   *         and <code>false</code> otherwise.
   */
  public boolean equals(Date that)
  {
    return this.year == that.year && this.month == that.month && this.date == that.date;
  }
  
  /**
   * Determines if this date is after the passed date.
   * 
   * @param that The Date to compare against.
   * 
   * @return true if this date is after that date.
   */
  public boolean after(Date that)
  {
    return this.year > that.year || this.month > that.month || that.date > that.date;
  }

  /**
   * Determines if this date is before the passed date.
   * 
   * @param that The Date to compare against.
   * 
   * @return true if this date is before that date.
   */
  public boolean before(Date that)
  {
    return this.year < that.year || this.month < that.month || this.date < that.date;
  }
}
