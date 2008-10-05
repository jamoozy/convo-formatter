package formatter;

/**
 * An <code>Event</code> is anything that can happen in a conversation.  All
 * implementing classes must define the {@link #getTimestamp()} method to return
 * the {@link Timestamp} object that represents the time of day this <code>Event</code>
 * occured.
 * 
 * @author Andrew Correa
 */
public interface Event
{
	/**
	 * Returns the time this <code>Event</code> took place.
	 * 
	 * @return The time this <code>Event</code> took place.
	 */
	public Timestamp getTimestamp();
}
