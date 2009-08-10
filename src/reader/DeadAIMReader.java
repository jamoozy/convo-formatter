package reader;

import java.util.Iterator;
import java.util.List;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;

import org.htmlcleaner.BaseToken;
import org.htmlcleaner.ContentToken;
import org.htmlcleaner.EndTagToken;
import org.htmlcleaner.TagNode;

import formatter.Message;
import formatter.Session;
import formatter.Timestamp;

// TODO: actually write this.
public class DeadAIMReader extends HTMLReader
{
  public DeadAIMReader()
  {

  }

  /**
   * Loads the {@link Session}s of this file into this class.
   * 
   * @param filename
   *          The name of the file to laod.
   * @return <code>true</code> if the file was loaded correctly,
   *         <code>false</code> otherwise.
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  // protected boolean loadDocument(TagNode root)
  // {
  // List list = root.getChildren();
  // return false;
  // }
  // public boolean loadFile(String filename) throws IOException
  // {
  // super.loadFile(filename);
  //
  // // Determine the date from the name of the file.
  // int start1 = filename.lastIndexOf('/');
  // int start2 = filename.lastIndexOf('\\');
  // int start = (start1 > start2 ? start1+1 : start2+1);
  // Date date = new Date(Integer.parseInt(filename.substring(start+0,start+4)),
  // Integer.parseInt(filename.substring(start+5,start+7)),
  // Integer.parseInt(filename.substring(start+8,start+10)),
  // filename.substring(start+12,filename.indexOf(']', start+13)));
  // System.out.println("Determined Date:"+date.toString());
  //
  // // Eat up some of the header stuff.
  // eat("<html>", "No <html> tag found.");
  // eat("<head>", "No <head> tag found.");
  // eat("<title>", "No <head> tag found.");
  //
  // // Find out who I'm speaking with -- it's the 3rd word in the sequence:
  // // Talk with [sn] on [date]
  // eat("Talk", "\"Talk\" expected.");
  // eat("with", "\"with\" expected.");
  // String yourSN = line.substring(0, line.indexOf(' '));
  // System.out.println("Determined yourSN:"+yourSN);
  // eat(yourSN, "Could not read SN correctly on line "+lineNumber);
  // eat("on", "\"on\" expectd.");
  // eat(date.toString(), "Could not read date correctly:"+lineNumber);
  // eat("</title>", "</title> tag expected");
  // eat("</head>", "</head> tag expected");
  //
  // // Get the background color
  // eatBodyTag();
  // System.out.println("Got bgcolor=" + fs.getBGColor());
  //
  // //Session.makeSession(mySN, yourSN, date);
  //
  // // for (int i = 1; next != null; i++)
  // // {
  // // _parseLine(i, next);
  // // next = reader.readLine();
  // // }
  //
  // eat("</body>", "</body> expected.");
  // eat("</html>", "</html> expected.");
  //
  // return false;
  // }

  /**
   * Parse a single line of text.
   * 
   * @param lineNumber
   * @param text
   */
  // private void _parseLine(int lineNumber, String text)
  // {
  //
  // }

  private enum State
  {
    NAME, // Screenname
    TS, // Timestamp
    COLON, // colon after name/ts
    EMPTY, // Empty block that sometimes comes after
    MSG; // The actual message.
  }

  private State state;

  @Override
  protected boolean loadDocument(TagNode root)
  {
    System.out.println("Starting traversing ...\n");
    Iterator<TagNode> iter = root.getChildren().iterator();
    TagNode next = null;
    while (iter.hasNext())
      if ((next = iter.next()).getName().equals("body"))
        loadBody(next);
    System.out.println("\nDone!");
    return false;
  }

  private void loadBody(TagNode body)
  {
    TagNode[] tags = body.getAllElements(false);
    for (int i = 0; i < tags.length; i++)
    {
      TagNode next = tags[i];
      if (next.getName().equals("span"))
      {
        addMessage(next); // span with a few <b>'s in it
      }
      else if (next.getName().equals("b"))
      {
        endSession(next);
      }
      else if (!next.getName().equals("hr"))
        ;
      System.out.println(next.getName() + "  er??");
    }
  }

  private void addMessage(TagNode span)
  {
    System.out.printf("%s with %d elems, %d kids\n", span.getName(), span
        .getAllElements(false).length, span.getChildren().size());

    TagNode[] nodes = span.getAllElements(false);
    for (int i = 0; i < nodes.length; i++)
    {
      TagNode next = nodes[i];
      printTagNode(next, "  ");
      if (next.toString().equals("font"))
      {

      }
    }

    // Get the name and timestamp info.
    String name = null;
    Timestamp ts = null;
    int i = 0;
    for (; i < nodes.length; i++)
    {
      if (nodes[i].getName().equals("b"))
      {
        name = nameFont(nodes[i].getAllElements(false)[0]);
        ts = tsFont(nodes[i].getAllElements(false)[1]);
        break;
      }
    }

    Message message = new Message(name, ts, null, false);

    // Get the text of the message.
    for (; i < nodes.length; i++)
    {
      if (nodes[i].getName().equals("font"))
      {
        TagNode[] kids = nodes[i].getAllElements(false);
        if (kids.length > 0)
        {
          List children = kids[0].getChildren();
          if (children.size() > 0)
          {
            ContentToken content = ((ContentToken) children.get(0));
            String part = content.getContent().trim();
            if (part.length() > 0 && !part.equals(":"))
            {
              System.out.println("Adding \"" + part + "\"");
              message.append(part, nodes[i].getAttributeByName("font"), null);
            }
          }
        }
        List children = nodes[i].getChildren();
        if (children.size() > 0)
        {
          ContentToken content = ((ContentToken) children.get(0));
          String part = content.getContent().trim();
          if (part.length() > 0 && !part.equals(":"))
          {
            System.out.println("Adding \"" + part + "\"");
            message.append(part, nodes[i].getAttributeByName("font"), null);
          }
        }
      }
    }

    if (!Session.sessionIsActive())
      Session.makeSession(name, null, null);
    Session.getSession().add(message);

    System.out.println("Added message: " + message.toString());
  }

  // private void checkHR(TagNode hr)
  // {
  // System.out.printf("hr with %d attribs & %d kids\n",
  // hr.getAttributes().size(), hr.getChildren().size());
  // }

  private void endSession(TagNode b)
  {
    System.out.printf("b with %d attribs & %d kids\n",
        b.getAttributes().size(), b.getChildren().size());
    System.out.println("  \""
        + ((ContentToken) b.getChildren().get(0)).getContent() + "\"");
  }

  private void printTagNode(TagNode node, String prefix)
  {
    System.out.printf("%s<%s:%d elems,%d kids>\n", prefix, node.getName(), node
        .getAllElements(true).length, node.getChildren().size());
    // System.out.println(prefix + "<" + node.getName() + ":"
    // + node.getAllElements(true).length + " elems,>");
    Iterator<BaseToken> kids = node.getChildren().iterator();
    while (kids.hasNext())
    {
      BaseToken next = kids.next();
      if (next.getClass() == TagNode.class)
        printTagNode((TagNode) next, prefix + "  ");
      else if (next.getClass() == ContentToken.class)
        System.out
            .println(prefix + "  $ " + ((ContentToken) next).getContent());
      else if (next.getClass() == EndTagToken.class)
        System.out.println(prefix + "  </" + ((EndTagToken) next).getName()
            + ">");
      else
        System.out.println(prefix + " -> " + next.getClass().getSimpleName());
    }
  }

  private void pushNameTS(TagNode b)
  {
    System.out.println("Using " + b.getName());
    nameFont(b.getAllElements(false)[0]);
    tsFont(b.getAllElements(false)[1]);
  }

  private String nameFont(TagNode font)
  {
    return ((ContentToken) font.getChildren().get(0)).getContent().trim();
  }

  private Timestamp tsFont(TagNode font)
  {
    return Timestamp.parseString12(((ContentToken) font.getChildren().get(0))
        .getContent().trim());
  }

  @Override
  protected void flush()
  {
    System.out.println("In flush()");
  }

  @Override
  protected void handleComment(char[] data, int pos)
  {
    System.out.printf("In handleComment(%s,%d)\n", data, pos);
  }

  @Override
  protected void handleEndOfLineString(String eol)
  {
    System.out.printf("In handleEndOfLineString(%s)\n", eol);
  }

  @Override
  protected void handleEndTag(HTML.Tag t, int pos)
  {
    System.out.printf("In handleEndTag(%s,%d)\n", t.toString(), pos);
  }

  @Override
  protected void handleError(String errorMsg, int pos)
  {
    System.out.printf("In handleError(%s, %d)\n", errorMsg, pos);
  }

  @Override
  protected void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos)
  {
    System.out.printf("In handleSimepleTag(%s,%s,%d)\n", t.toString(), a
        .toString(), pos);
  }

  @Override
  protected void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos)
  {
    System.out.printf("In handleStartTag(%s, %s, %d)\n", t, a, pos);
  }

  @Override
  protected void handleText(char[] data, int pos)
  {
    System.out.printf("In handleText(%s, %d)\n", data, pos);
  }

  /**
   * Returns an <code>Iterator</code> of the <code>Session</code>s in the
   * last-read file.
   * 
   * @return The <code>Iterator</code>.
   */
  // public Iterator<Session> iterator()
  // {
  // // TODO Auto-generated method stub
  // return null;
  // }
}
