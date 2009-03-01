package formatter;

/**
 * This class represents all {@link Event}s that are not messages.&nbsp;This includes
 * the event that the {@link Session} has started or ended, or either SNs have been
 * disconnected.
 * <p>
 *
 * @author Andrew Correa
 */
public class Notification implements Event
{
  /**
   * This <code>enum</code> lists the types of <code>Event</code>s that are
   * allowed.  All <code>Event</code>s must be made with one of these.
   */
  public enum Type
  {
    /** The <code>Session</code> has started. */
    START_SESSION,

    /** The <code>Session</code> has ended. */
    END_SESSION,

    /** Your buddy has logged on. */
    BUDDY_LOG_ON,

    /** Your buddy has logged off. */
    BUDDY_LOG_OFF,

    /** You were disconnected without wanting to be signed off. */
    DISCONNECT;
  }
  
  

  ////////////////////////////////////////////////////////////////////////////
  // ----------------------------- Actual Class --------------------------- //
  ////////////////////////////////////////////////////////////////////////////

  private Type type;        // The Type this Event is.
  private Date date;        // Date of occurance.
  private Timestamp time;   // The time this Event occurred.
  
  /**
   * Creates a new <code>Notification</code> object with the passed parameters.
   */
  public Notification(Type type, Date date, Timestamp time)
  {
    this.type = type;
    this.date = date;
    this.time = time;
  }
  
  /**
   * Returns the <code>Type</code> of this object.
   *
   * @return The <code>Type</code> of this object.
   */
  public Type getType()
  {
    return type;
  }
  
  /**
   * Returns the {@link Date} object corresponding to when this
   * <code>Event</code> occurred.
   * 
   * @return The {@link Date} object corresponding to when this
   *         <code>Event</code> occurred.
   */
  public Date getDate()
  {
    return date;
  }

  /**
   * Returns the {@link Timestamp} object corresponding to when this
   * <code>Event</code> occurred.
   *
   * @return The {@link Timestamp} object corresponding to when this
   *         <code>Event</code> occurred.
   */
  public Timestamp getTimestamp()
  {
    return time;
  }
}
