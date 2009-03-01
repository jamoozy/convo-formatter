package reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.Iterator;

import formatter.Date;
import formatter.Session;

// TODO: actually write this.
public class DeadAIMReader extends HTMLReader
{
  /**
   * Loads the {@link Session}s of this file into this class.
   * 
   * @param filename The name of the file to laod.
   * @return <code>true</code> if the file was loaded correctly, <code>false</code> otherwise.
   */
  public boolean loadFile(String filename) throws IOException
  {
    super.loadFile(filename);

    // Determine the date from the name of the file.
    int start1 = filename.lastIndexOf('/');
    int start2 = filename.lastIndexOf('\\');
    int start = (start1 > start2 ? start1+1 : start2+1);
    Date date = new Date(Integer.parseInt(filename.substring(start+0,start+4)),
              Integer.parseInt(filename.substring(start+5,start+7)),
              Integer.parseInt(filename.substring(start+8,start+10)),
              filename.substring(start+12,filename.indexOf(']', start+13)));
    System.out.println("Determined Date:"+date.toString());

    // Eat up some of the header stuff.
    eat("<html>", "No <html> tag found.");
    eat("<head>", "No <head> tag found.");
    eat("<title>", "No <head> tag found.");
    
    // Find out who I'm speaking with -- it's the 3rd word in the sequence:
    // Talk with [sn] on [date]
    eat("Talk", "\"Talk\" expected.");
    eat("with", "\"with\" expected.");
    String yourSN = line.substring(0, line.indexOf(' '));
    System.out.println("Determined yourSN:"+yourSN);
    eat(yourSN, "Could not read SN correctly on line "+lineNumber);
    eat("on", "\"on\" expectd.");
    eat(date.toString(), "Could not read date correctly:"+lineNumber);
    eat("</title>", "</title> tag expected");
    eat("</head>", "</head> tag expected");

    // Get the background color
    eatBodyTag();
    System.out.println("Got bgcolor=" + fs.getBGColor());

    //Session.makeSession(mySN, yourSN, date);

//    for (int i = 1; next != null; i++)
//    {
//      _parseLine(i, next);
//      next = reader.readLine();
//    }

    eat("</body>", "</body> expected.");
    eat("</html>", "</html> expected.");

    return false;
  }

  /**
   * Parse a single line of text.
   * @param lineNumber
   * @param text
   */
  private void _parseLine(int lineNumber, String text)
  {
    
  }

  /**
   * Returns an <code>Iterator</code> of the <code>Session</code>s
   * in the last-read file.
   * 
   * @return The <code>Iterator</code>.
   */
  public Iterator<Session> iterator()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
