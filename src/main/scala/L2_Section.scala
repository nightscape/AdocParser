import org.parboiled2._

/**
  * Created by eperson on 2016-09-20.
  */
trait L2_Section {  this: Parser
  with L0_Basics
  with L1_Paragraph
  =>
  def section = rule { sectionHeader ~ NewLine.* ~ zeroOrMore(paragraph).separatedBy(NewLine)}
  def sectionHeader = rule {  (s5Header | s4Header | s3Header | s2Header)}
  def s5Header = rule { "===== " ~ WhiteSpace ~ Phrase ~ LineEnd   }
  def s4Header = rule { "==== " ~ WhiteSpace ~ Phrase  ~ LineEnd  }
  def s3Header = rule { "=== " ~ WhiteSpace ~ Phrase ~ LineEnd  }
  def s2Header = rule { "== " ~ WhiteSpace ~ Phrase ~ LineEnd  }

}
