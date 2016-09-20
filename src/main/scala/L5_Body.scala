import org.parboiled2._

/**
  * Created by eperson on 2016-09-20.
  */
trait L5_Body {  this: Parser with L0_Basics
  with L1_Paragraph
  with L2_Section
  =>

  def body = rule { preamble  ~
            zeroOrMore(NewLine)~
            zeroOrMore(section)
  }

  def preamble = rule {zeroOrMore(paragraph).separatedBy(oneOrMore(NewLine))  }


}
