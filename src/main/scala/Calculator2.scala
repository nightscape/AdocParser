/*
 * Copyright (C) 2009-2013 Mathias Doenitz, Alexander Myltsev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.parboiled2.examples

import java.io.InputStream

import scala.annotation.tailrec
import scala.util.{Failure, Success}
import scala.io.{Source, StdIn}
import org.parboiled2._
import org.parboiled2.examples.Calculator2.AAuthorVersion


object Calculator2 extends App {
  val WhiteSpaceChar = CharPredicate(" \t\f")

  repl()

  // @tailrec
  def repl(): Unit = {

    println(getClass)
    val stream : InputStream = getClass.getResourceAsStream("/1.txt")
    val url = Calculator2.getClass.getResource("/1.txt")
    println(url)
    val lines = Source.fromURL( url ).mkString

    // once BUG https://issues.scala-lang.org/browse/SI-8167 is fixed
    //    print("---\nEnter calculator expression > ")
    //  Console.out.flush()
    //    StdIn.readLine()
    val oldLines = "= Abcd efg \n" +
      "Daniel Persson <fafa@fafa.se>\n" +
      "v10 \n" +
      "\n" +
      "Normal Paragraph\n" +
      "Hoho fafa\n" +
      "\n" +
      " Ad paragraph\n" +
      "Hoho fafa\n" +
      "\n" +
    "[.lead]\n" +
      "Lead Paragraph\n" +
    "Hoho fafa\n"

    lines match {

      case "" =>
      case line =>
        println(">" + line + "<")
        val parser = new Calculator2(line)
        parser.InputLine.run() match {
          case Success(exprAst) => println("Result: " + eval("", exprAst) + exprAst)
          case Failure(e: ParseError) => println("Expression is not valid: " + parser.formatError(e))
          case Failure(e) => println("Unexpected error during parsing run: " + e)
        }
      //repl()
    }
  }


  def eval(i: String, expr: Expr): Boolean =
    expr match {
      case AFile( a) => { println;println(i + "AFile");  eval(i + " ", a) ; println; true }
      case ADocument(a, b) => println(i + "ADocument"); eval(i + " ",a) && eval(i + " ",b)
      case AHeader(a, b) => println(i + "AHeader"); eval(i + " ",a) && eval(i + " AuthorAndVersion ",b)
      case APhrase(a) => println(i + "APhrase"); println(i + " " + a); true
      case Word(a) => println(i + "Word"); println(i + " " + a); true
      case ATextLine(a) => println(i + "ATextLine"); println(i + " " + a); true

      case ADocTitle(a) => println(i + "ADocTitle");eval(i + " ",a)
      case AAuthorVersion(a: AAuthorVersion2) => {
        eval(i + " ", a)
      }
      case AAuthorVersion2(a: ATextLine, b: Option[String]) => {
        println(i + "AAuthorVersion");
        eval(i + " Author: ",a); eval(i + " Version: ",b)
      }
      case AAuthor(a: ATextLine) => println(i + "AAuthor"); eval(i + " ",a)
      case AVersion(a: String) => println(i + "AVersion"); eval(i + " ",a)
      case AParagraph(a: String) => println(i + "AParagraph");eval(i + " ",a)
      case ALitParagraph(a: Expr) => println(i + "ALitParagraph");eval(i + " ",a)
      case ALeadParagraph(a: Expr) => println(i + "ALeadParagraph");eval(i + " ",a)
      case AAdParagraph(a: String, b: Expr) => println(i + "AAdParagraph");eval(i + " ",a)
      case AGenParagraph(a: Expr) => println(i + "AGenParagraph");eval(i + " ",a)
      case APreamble(a: Seq[Expr]) => println(i + "APreamble");eval(i + " ",a)
      case ABody(a: APreamble, b: Seq[Expr]) => println(i + "ABody");eval(i + " ",a); eval(i + " ", b)
      case ASection(a: ASecH, b: Seq[AGenParagraph]) => println(i + "ASection");eval(i + " ",a);eval(i + " ",b)
      case ASecH(a: Expr) => println(i + "ASecH");eval(i + " ",a)
      case S5H(a: Expr) => println(i + "S5H");eval(i + " ",a)
      case S4H(a: Expr) => println(i + "S4H");eval(i + " ",a)
      case S3H(a: Expr) => println(i + "S3H");eval(i + " ",a)
      case S2H(a: Expr) => println(i + "S2H");eval(i + " ",a)
    }

  def eval(i: String, expr: String): Boolean = {
    println(i + expr)
    return true
  }

  def eval(i: String, expr: Seq[Expr]): Boolean = {
    expr.foreach {
      eval(i, _)
    }
    true
  }

  def eval(i: String, expr: Option[Any]): Boolean =
    expr match {
      case Some(n: Expr) => eval(i + " ", n)
      case Some(n: String) => println(i + n); true
      case None => println( i + "Empty Optional")
        true
    }
//  def eval(i: String, expr: Option[String]): Boolean =
//    expr match {
//      case Some(n: String) => print(i + n); true
//      case None => println( i + "Empty Optional")
//        true
//    }

  def eval(i: String, expr: Unit): Boolean = {
    print("Fafa")
  true
}


  // our abstract syntax tree model
  sealed trait Expr
  case class AFile(doc: ADocument) extends Expr
  case class ADocument(header: AHeader, body:ABody) extends Expr
  case class ATextLine(line:String) extends Expr
  case class AHeader(s:ADocTitle, elefant: Option[AAuthorVersion]) extends Expr
  case class APhrase(value: String) extends Expr
  case class Word(value: String) extends Expr
  case class ADocTitle(value: ATextLine) extends Expr
  case class AAuthorVersion(value1: AAuthorVersion2) extends Expr
  case class AAuthorVersion2(value1: ATextLine, version: Option[String]) extends Expr
  case class AAuthor(a: ATextLine) extends Expr
  case class AEMail(value: String) extends Expr
  case class AVersion(value: String) extends Expr
  case class AParagraph(value: String) extends Expr
  case class ALitParagraph(value: Expr) extends Expr
  case class ALeadParagraph(value: Expr) extends Expr
  case class AAdParagraph(s:String, value: Expr) extends Expr
  case class AGenParagraph(value: Expr) extends Expr
  case class APreamble(value: Seq[Expr]) extends Expr
  case class ABody(preamble:APreamble, sections: Seq[Expr]) extends Expr
  case class ASection(header:ASecH, paras: Seq[AGenParagraph]) extends Expr
  case class ASecH(value: Expr) extends Expr
  case class S5H(value: Expr) extends Expr
  case class S4H(value: Expr) extends Expr
  case class S3H(value: Expr) extends Expr
  case class S2H(value: Expr) extends Expr
  case class AAttributes(value: Seq[AAttribute]) extends Expr
  case class AAttribute(name: String, value: APhrase) extends Expr
}

/**
  * This parser reads simple calculator expressions and builds an AST
  * for them, to be evaluated in a separate phase, after parsing is completed.
  */
class Calculator2(val input: ParserInput) extends Parser {
  import Calculator2._

  def InputLine = rule { WhiteSpace ~ adocument ~ EOI  ~> AFile}
  def WhiteSpace = rule { zeroOrMore(WhiteSpaceChar) }

  //  def Document: Rule1[Expr] = rule { Header}
  def adocument = rule { aheader ~ body ~> ADocument}

  def aheader = rule { adoctitle ~ optional( authorAndVersion)  ~ zeroOrMore(NewLine) ~> AHeader}

  def authorAndVersion:Rule1[AAuthorVersion] = rule { authorAndVersion2  ~> AAuthorVersion
  }

  def body = rule { preamble  ~ zeroOrMore(NewLine)~ zeroOrMore(section)  ~> ABody

  }

  def preamble:Rule1[APreamble] = rule {zeroOrMore(paragraph).separatedBy(oneOrMore(NewLine))  ~> APreamble}


  def section:Rule1[ASection] = rule { sectionHeader ~ NewLine.* ~ zeroOrMore(paragraph).separatedBy(NewLine)  ~> ASection

  }

  def sectionHeader:Rule1[ASecH] = rule {  (s5Header | s4Header | s3Header | s2Header) ~> ASecH

  }

  def s5Header = rule { "===== " ~ WhiteSpace ~ Phrase ~ LineEnd  ~> S5H }
  def s4Header = rule { "==== " ~ WhiteSpace ~ Phrase  ~ LineEnd ~> S4H }
  def s3Header = rule { "=== " ~ WhiteSpace ~ Phrase ~ LineEnd  ~> S3H }
  def s2Header = rule { "== " ~ WhiteSpace ~ Phrase ~ LineEnd  ~> S2H }


  def paragraph:Rule1[AGenParagraph] = rule {  (literalparagraph | admonitonparagraph | leadparagraph |normalparagraph) ~> AGenParagraph

  }

  def normalparagraph:Rule1[AParagraph] = rule { !sectionHeader ~ capture(oneOrMore(NotNewline ~ ANY))  ~> AParagraph

  }
  def literalparagraph:Rule1[ALitParagraph] = rule { " " ~ normalparagraph  ~> ALitParagraph

  }
  def leadparagraph:Rule1[ALeadParagraph] = rule { "[.lead]" ~ NewLine ~ normalparagraph ~> ALeadParagraph

  }
  def admonitonparagraph:Rule1[AAdParagraph] = rule { capture("NOTE: " | "TIP: " | "IMPORTANT: " | "WARNING: ") ~ normalparagraph  ~> AAdParagraph

  }

  def authorAndVersion2:Rule1[AAuthorVersion2] = rule {
    textline ~ optional(version) ~> AAuthorVersion2
  }

 // def author:Rule1[ATextLine] = rule {
//    (textline:Rule1[ATextLine])
//  }
//  def author2:Rule1[AAuthor] = rule {
//    (Word:Rule1[String]) ~ WS ~ ((Word: Rule1[String]) ~ WS ~ '<' ~ WS ~ ((email:Rule1[String]) ~ WS ~ '>' ~ LineEnd ~> ((a:String,b:String,c:String) => AAuthor(a,b,c))))
//  }
  def version:Rule1[String] = rule {
    capture('v' ~  oneOrMore( CharPredicate.Digit)) ~ LineEnd
  }

  def email:Rule1[String] = rule {
    capture(oneOrMore(NotELChar ~ ANY) ~ ch('@') ~ oneOrMore(NotEDChar ~ ANY))
  }
//    def email2:Rule1[String] = rule {
//    (emaillocalpart: Rule1[String]) ~  ch('@') ~ (emaildomain: Rule1[String] ~> ((a,b) => a + b))
//  }

//  def emaillocalpart:Rule1[String] = rule {
//    capture(oneOrMore(NotELChar ~ ANY))
//  }
  val NELCHAR = CharPredicate("@ \t\n\r") ++ EOI
  def NotELChar: Rule0 = rule( &(  !NELCHAR))

  def emaildomain:Rule1[String] = rule {
    capture(oneOrMore(NotEDChar ~ ANY))
  }
  val NEDCHAR = CharPredicate(" \t\n\r") ++ EOI
  def NotEDChar: Rule0 = rule( &(  !NEDCHAR))


  def attributes: Rule1[AAttributes] = rule {
    zeroOrMore(zattribute) ~> AAttributes
  }
    def zattribute:Rule1[AAttribute] = rule {
    ':' ~ capture(oneOrMore(!NewLine ~ ANY)) ~ ':' ~ Phrase ~ NewLine ~> AAttribute
  }
  def adoctitle = rule { '=' ~ WS ~ textline  ~> (s => ADocTitle(s))}
  def textline = rule { capture(oneOrMore(NotNewline ~ ANY)) ~ NewLine ~>ATextLine}
  def NewLine = rule( quiet( ('\r'.? ~ '\n' ) | '\r') )
  def WL = rule( quiet( (WSCHAR | NewLine).* ) )
  val WSCHAR = CharPredicate(" \t")
  	  def LineEnd = rule( quiet ( WSCHAR.* ~ NewLine ) )
  def NewLineChars = rule { anyOf("\n\r") }
  def WS = rule( quiet (WSCHAR ).* )
  def Phrase = rule { capture(oneOrMore(CWord)) ~ WS ~> ( s => APhrase(s))}
  def Word:Rule1[String] = rule { capture(oneOrMore(NotWordChar ~ ANY))  }
  def CWord = rule {oneOrMore(NotWordChar ~ ANY)  }
  def NotNewline: Rule0 = rule( &( WS ~ !NewLine ) )
  val NWCHAR = CharPredicate(" \t\n\r") ++ EOI
  def NotWordChar: Rule0 = rule( &(  !NWCHAR))
  def OneNL: Rule0 = rule( quiet ( WS ~ NewLine.?  ) )
  def OneNLMax: Rule0 = rule( quiet ( WS ~ NewLine.?  ~ NotNewline ) )
  def EmailWord = rule {
    oneOrMore(CharPredicate.Alpha ~CharPredicate.Digit)
  }
}
