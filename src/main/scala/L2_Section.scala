import org.parboiled2._

/**
  * Created by eperson on 2016-09-20.
  */
trait L2_Section {  this: Parser
  with L0_Basics
  with L1_Paragraph
  =>
  def section:Rule0 = rule { sectionHeader ~ NewLine.* ~ zeroOrMore(paragraph).separatedBy(oneOrMore(NewLine))}
  def sectionHeader:Rule0 = rule {  (s5Header | s4Header | s3Header | s2Header)}
  def s5Header = rule { twolines5Header | onelines5Header   }
  def onelines5Header = rule { "===== " ~ WhiteSpace ~ Phrase ~ LineEnd   }
  def twolines5Header = rule { !anyOf("=-~^+") ~ textline ~ "++++" ~ ch('+').* ~ LineEnd  }
  def s4Header = rule { twolines4Header | onelines4Header   }
  def onelines4Header = rule { "==== " ~ WhiteSpace ~ Phrase  ~ LineEnd  }
  def twolines4Header = rule { !anyOf("=-~^+") ~ textline ~ "^^^^" ~ ch('^').* ~ LineEnd  }
  def s3Header = rule { twolines3Header | onelines3Header   }
  def onelines3Header = rule { "=== " ~ WhiteSpace ~ Phrase ~ LineEnd  }
  def twolines3Header = rule { !anyOf("=-~^+") ~ textline ~ "~~~~" ~ ch('~').* ~ LineEnd  }
  def s2Header = rule { twolines2Header | onelines2Header  }
  def onelines2Header = rule { "== " ~ WhiteSpace ~ Phrase ~ LineEnd  }
  def twolines2Header = rule { !anyOf("=-~^+") ~ textline ~ "----" ~ ch('-').* ~ LineEnd  }

}
