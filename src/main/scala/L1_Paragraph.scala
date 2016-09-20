import org.parboiled2._

/**
  * Created by eperson on 2016-09-20.
  */
trait L1_Paragraph { this: Parser
    with L0_Basics
  with L2_Section
  =>
  def paragraph = rule {  (literalparagraph | admonitonparagraph | leadparagraph |normalparagraph)

  }

  def normalparagraph = rule { !sectionHeader ~ oneOrMore(NotNewline ~ ANY)

  }
  def literalparagraph = rule { " " ~ normalparagraph

  }
  def leadparagraph = rule { "[.lead]" ~ NewLine ~ normalparagraph

  }
  def admonitonparagraph = rule { "NOTE: " | "TIP: " | "IMPORTANT: " | "WARNING: " ~ normalparagraph

  }
}
