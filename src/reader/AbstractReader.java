package reader;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import java.io.BufferedReader;
import java.io.FileReader;

import formatter.Session;

abstract class AbstractReader implements Reader
{
  // The sessions in the last-read file.
  protected Vector<Session> sessions;

  protected AbstractReader() {}

  public Iterator<Session> iterator()
  {
    return sessions.iterator();
  }

  protected BufferedReader getBufferedReader(String filename)
  {
    try
    {
      return new BufferedReader(new FileReader(filename));
    }
    catch (IOException ioe)
    {
      throw new NoFileException("File " + filename + " does not exist!");
    }
  }

  /**
   * Loads the file with the given URL.
   * 
   * @param name The name of the file to open.
   * @return <code>true</code> if successful, <code>false</code> otherwise.
   * 
   * @throws IOException if any other IO error occurs during reading.
   * @throws FileNotFoundException if the file can't be found.
   * @throws FileFormatException if the file's format is bad.
   * 
   * @see Reader#loadFile(String)
   */
  public boolean loadFile(String filename) throws IOException
  {
    BufferedReader reader = getBufferedReader(filename);
    sessions = new Vector<Session>();

    String next = null;
    for (int i = 1; (next = reader.readLine()) != null; i++)
    {
      _parseLine(i, next);
      next = reader.readLine();
    }

    return true;
  }

  /**
   * Parse this line.
   *
   * @param lineNumber line number we're on -- used to report which line an error ocurred on.
   * @param line The text of the line to parse.
   *
   * @throws FileFormatException When the file is not formattec correctly.
   */
  protected abstract void _parseLine(int lineNumber, String line) throws FileFormatException;
}
