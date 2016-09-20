import org.parboiled2.Parser

/**
  * Created by eperson on 2016-09-20.
  */
trait L6_Document {  this: Parser with L0_Basics
  with L5_Body
with L1_DocHeader
with L5_Body
  =>
  def InputLine = rule { WhiteSpace ~ adocument ~ EOI  }

  //  def Document: Rule1[Expr] = rule { Header}
  def adocument = rule { aheader ~ body }

}
