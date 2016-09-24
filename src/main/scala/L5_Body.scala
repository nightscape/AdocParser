import org.parboiled2._

/**
  * Created by eperson on 2016-09-20.
  */
trait L5_Body {  this: Parser with L0_Basics
  with L1_Paragraph
  with L2_Section
  =>

  def body:Rule0 = rule { zeroOrMore(sectionHeader  | paragraph | NewLine   ) ~EOI
  }

  def preamble:Rule0 = rule {zeroOrMore(paragraph).separatedBy(oneOrMore(NewLine))  }


}
