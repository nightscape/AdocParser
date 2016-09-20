/**
  * Created by eperson on 2016-09-19.
  */
import org.parboiled2._
class ADocParser(val input: ParserInput) extends Parser
  with WhitespaceStringsAndChars
  with L0_Basics
  with L1_Paragraph
  with L1_Attributes
  with L1_DocHeader
  with L2_Section
  with L3_Block
  with L4_List
  with L5_Body
  with L6_Document
{

}
