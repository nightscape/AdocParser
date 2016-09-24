/**
  * Created by eperson on 2016-09-19.
  */
import org.parboiled2._
import org.parboiled2.examples.Calculator2.APhrase
trait L0_Basics { this: Parser =>

  val WhiteSpaceChar = CharPredicate(" \t\f")

  def WhiteSpace = rule { zeroOrMore(WhiteSpaceChar) }
  def NewLine = rule( quiet( ('\r'.? ~ '\n' ) | '\r') )
  def WL = rule( quiet( (WSCHAR | NewLine).* ) )
  val WSCHAR = CharPredicate(" \t")
  def LineEnd = rule( quiet ( WSCHAR.* ~ NewLine ) )
  def NewLineChars = rule { anyOf("\n\r") }
  def WS = rule( quiet (WSCHAR ).* )
  def NotNewline: Rule0 = rule( &( WS ~ !NewLine  ) )
  val NWCHAR = CharPredicate(" \t\n\r") ++ EOI
  val RNCHAR = CharPredicate("\n\r") ++ EOI
  def NotEndChar: Rule0 = rule( &(  !RNCHAR))
  def NotWordChar: Rule0 = rule( &(  !NWCHAR))
  def OneNL: Rule0 = rule( quiet ( WS ~ NewLine.?  ) )
  def OneNLMax: Rule0 = rule( quiet ( WS ~ NewLine.?  ~ NotNewline ) )
  def textline = rule { oneOrMore(NotNewline ~ ANY) ~ NewLine }
  def Phrase = rule { oneOrMore(CWord) ~ WS }
  def Word = rule { oneOrMore(NotWordChar ~ ANY)  }
  def CWord = rule {oneOrMore(NotWordChar ~ ANY)  }


}
