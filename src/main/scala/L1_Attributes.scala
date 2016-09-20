import org.parboiled2._

/**
  * Created by eperson on 2016-09-20.
  */
trait L1_Attributes {this: Parser with L0_Basics =>
  def attributes = rule {
    zeroOrMore(zattribute)
  }
  def zattribute = rule {
    ':' ~ oneOrMore(!NewLine ~ ANY) ~ ':' ~ Phrase ~ NewLine
  }
}
