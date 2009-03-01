package formatter;

import java.util.Iterator;
import java.util.Vector;


/**
 * This is one chat.  This is the representation of chat {@link Notification}s and {@link Message}s
 * Which make up the time between when a chat window was opened and when one was closed.
 * For some formats this is one file, for others it is not.
 * <p>
 * This class doubles as its own factory.  It allows only one <code>Session</code> at a time.
 * To create a new <code>Session</code>, call {@link Session#makeSession(String,String,Date)}.
 * Once you're done, call {@link Session#closeSession()} before trying to start another session.
 * At any time, you can call {@link Session#getSession()} to access the active <code>Session</code>.
 * <p>
 * I have decided to call the person that created the log, i.e. the person that saved this log
 * "me" and the person that is talking with "me", "you".  Therefore, {@link #getMySN()} returns
 * the name of the creator of this log, and {@link #getYourSN()} returns the name of the person
 * talking with "me" -- the other guy.
 *
 * @author Andrew Correa
 */
public class Session
{
  ////////////////////////////////////////////////////////////////////////////
  // --------------------------- "factory" part --------------------------- //
  ////////////////////////////////////////////////////////////////////////////

  private static Session session;       // Currently active session.

  /**
   * Creates and sets active a new Session. This method is the only way
   * to create a new <code>Session</code> object.
   * 
   * @param mySN The name of the SN that is "me".
   * @param yourSN The name of the SN that I'm speaking with.
   * @param date The date this conversation took place.
   * @return The newly created <code>Session</code>.
   */
  public static Session makeSession(String mySN, String yourSN, Date date)
  {
    if (session != null)
      throw new AlreadyActiveException("Must close current Session before starting a new one.");

    session = new Session(mySN, yourSN, date);
    return session;
  }

  /**
   * Gets the most recently created <code>Session</code>.
   * 
   * @return The most recently created <code>Session</code>. 
   */
  public static Session getSession()
  {
    return session;
  }

  /**
   * Closes the current <code>Session</code>, allowing a new one to be made.
   */
  public static void closeSession()
  {
    session = null;
  }

  /**
   * Determines if there is a <code>Session</code> already active.
   * 
   * @return <code>true</code> if there is an already active <code>Session</code>
   *         <code>false</code> otherwise.
   */
  public static boolean sessionIsActive()
  {
    return session != null;
  }



  ////////////////////////////////////////////////////////////////////////////
  // ---------------------------- actual class ---------------------------- //
  ////////////////////////////////////////////////////////////////////////////

  private String mySN;             // The SN of the person that saved this log.
  private String yourSN;           // The SN of the person talking with "me"
  private Date date;               // When this Session took place.
  private Vector<Event> events;    // The Events this Session consists of.

  /**
   * Constructs a new <code>Session</code> object with the given parameters.
   *
   * @param mySN The SN of the person that saved this log.
   * @param yourSN The SN of the person that is talking with "me".
   * @param date The date this log was created.
   */
  private Session(String mySN, String yourSN, Date date)
  {
    this.mySN = mySN;
    this.yourSN = yourSN;
    this.date = date;

    events = new Vector<Event>();
  }

  /**
   * Appends the contents of the {@link String} object to the end of the last
   * {@link Message}.  Assumes the last {@link Event} passed to {@link #add(Event)}
   * was of type {@link Message}.  If the last <code>Event</code> was not of
   * type <code>Message</code> then an exception is thrown.
   *
   * @throws IllegalArgumentException when the last {@link Event} object was
   *         not of type {@link Message}.
   */
  public void append(String msg)
  {
    if (msg == null || msg.trim().equals("")) return;

    if (events.lastElement() instanceof Message)
      ((Message)events.lastElement()).append(msg);
    else
      throw new IllegalArgumentException();
  }

  /**
   * Appends the passed <code>Event</code> to the list containted in this
   * <code>Session</code>.  It is assumed that these <code>Event</code>s are
   * passed chronologically.
   *
   * @param e The <code>Event</code> to add.
   */
  public void add(Event e)
  {
    events.add(e);
  }

  /**
   * Returns the SN of the person that saved this log.
   *
   * @return The SN of the person that saved this log.
   */
  public String getMySN()
  {
    return mySN;
  }

  /**
   * Returns the name of the SN talking with "me".
   *
   * @return The name of the SN talking with "me".
   */
  public String getYourSN()
  {
    return yourSN;
  }

  /**
   * Returns the <code>Date</code> this <code>Session</code> was made.
   *
   * @return The <code>Date</code> this <code>Session</code> was made.
   */
  public Date getDate()
  {
    return date;
  }

  /**
   * Sets the date to a new value.  This is needed when a conversation
   * starts one day, but ends another.  In this case it is desirable to
   * list the conversation as having occured on the day the conversation
   * ended, as opposed to the day it was begun.
   * 
   * @param date The new <code>Date</code> to change to.
   * 
   * @throws IllegalArgumentException if the passed date occurs before
   *         the date already registered to the session.
   */
  public void setDate(Date date)
  {
    if (date.before(this.date))
      throw new IllegalArgumentException("The passed date occured before the start date.");

    this.date = date;
  }

  /**
   * Returns an <code>Iterator</code> of all the <code>Events</code> stored in
   * this <code>Session</code>.
   *
   * @return The <code>Event</code>s comprising this Session.
   */
  public Iterator<Event> iterator()
  {
    return events.iterator();
  }
}
