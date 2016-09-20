import org.parboiled2._
import org.parboiled2.examples.Calculator2.ADocTitle

/**
  * Created by eperson on 2016-09-20.
  */
trait L1_DocHeader { this: Parser with L0_Basics =>


  def aheader = rule { adoctitle ~ optional( authorAndVersion)  ~ zeroOrMore(NewLine)}

  def adoctitle = rule { '=' ~ WS ~ textline  }

  def authorAndVersion = rule { authorAndVersion2  }

    def authorAndVersion2 = rule {
      textline ~ optional(version)
    }
    def version = rule {
      'v' ~  oneOrMore( CharPredicate.Digit) ~ LineEnd
    }

    def email = rule {
      oneOrMore(NotELChar ~ ANY) ~ ch('@') ~ oneOrMore(NotEDChar ~ ANY)
    }
    val NELCHAR = CharPredicate("@ \t\n\r") ++ EOI
    def NotELChar: Rule0 = rule( &(  !NELCHAR))

    def emaildomain = rule {
      oneOrMore(NotEDChar ~ ANY)
    }
    val NEDCHAR = CharPredicate(" \t\n\r") ++ EOI
    def NotEDChar: Rule0 = rule( &(  !NEDCHAR))

}
