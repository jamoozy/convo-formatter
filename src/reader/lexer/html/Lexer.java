package reader.lexer.html;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import reader.FileFormatException;
import reader.lexer.html.Lexeme.Type;


// TODO: Test this.
public class Lexer
{
  private FileReader reader;
  private boolean html;

  private char in;

  /**
   *
   * @param name
   *
   * @throws FileNotFoundException
   * @throws IOException
   */
  public Lexer(String name) throws IOException
  {
    reader = new FileReader(name);
    in = (char)reader.read();
    html = false;
  }

  public Lexeme next()
  {
    try
    {
      switch (in)
      {
        case (char) -1:
          // EOF
          return new Lexeme(Type.eof);

        case '<':
          if (html) throw new FileFormatException("Got a '<' out of place.");
          html = true;
          nextIn();
          return next();

        case '>':
          if (!html) throw new FileFormatException("Got a '>' out of place.");
          html = false;
          nextIn();
          return next();

        case ' ':
          nextIn();
          return next();

        default:
          if (html)
          {
            StringBuffer name = new StringBuffer("");
            while (!isBlank(in) && in != '=' && in != '>')
            {
              name.append(in);
              nextIn();
            }

            while (isBlank(in)) nextIn();

            if (in == '=')
            {
              while (isBlank(in) || in == '"') nextIn();

              StringBuffer value = new StringBuffer("");
              while (!isBlank(in) && in != '>')
              {
                value.append(in);
                nextIn();
              }

              while (isBlank(in) || in == '"');

              return new Lexeme(Type.toType(name.toString()), value.toString());
            }

            return new Lexeme(Type.toType(name.toString()));
          }
          else
          {
            StringBuffer buffer = new StringBuffer(in);
            nextIn();

            while (in != '<')
            {
              if (in == '&')
              {
                StringBuffer symbol = new StringBuffer("");

                do
                {
                  nextIn();
                  symbol.append(in);
                }
                while (in != ';');

                if (symbol.equals("nbsp")) buffer.append(' ');
                if (symbol.equals("gt")) buffer.append('>');
                if (symbol.equals("lt")) buffer.append('<');
              }
              else
              {
                buffer.append(in);
              }

              nextIn();
            }

            return new Lexeme(Type.text, buffer.toString());
          }
      }

    }
    catch (IOException e)
    {
      System.err.println("The powers that be have decided not to allow me to make the next token.  Sorry.");
      return null;
    }
  }

  private boolean isBlank(char c)
  {
    return (c == ' ');// || c == '\n' || c == '\r');
  }

  private void nextIn() throws IOException
  {
    in = (char)reader.read();
  }
}
