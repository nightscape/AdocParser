/**
  * Created by eperson on 2016-09-20.
  */


  import java.io.{File, FileInputStream}
  import java.nio.ByteBuffer
  import java.nio.charset.Charset

  import org.specs2.execute.FailureException
  import org.specs2.mutable.Specification
  import org.parboiled2._

  class ADocParserSpec  extends Specification {
    sequential

    "The ADocParser should successfully parse the following project sources" >> {

      "testadoc" in checkDir("C:\\Users\\eperson\\IdeaProjects\\NihParser\\src\\main\\resources")

    }

    val utf8 = Charset.forName("UTF-8")
    val formatter = new ErrorFormatter(showTraces = true)

    def checkDir(path: String, blackList: String*): String => Boolean = { exampleName =>
      def checkFile(path: String): Int = {
        println("Checking " + path)
        val inputStream = new FileInputStream(path)
        val utf8Bytes = Array.ofDim[Byte](inputStream.available)
        inputStream.read(utf8Bytes)
        inputStream.close()
        val charBuffer = utf8.decode(ByteBuffer.wrap(utf8Bytes))
        val parser = new ADocParser(ParserInput(charBuffer.array(), charBuffer.remaining()))
        def fail(msg: String) = throw new FailureException(org.specs2.execute.Failure(msg))
        parser.InputLine.run().failed foreach {
          case error: ParseError => fail(s"Error in file `$path`:\n" + error.format(parser, formatter))
          case error => fail(s"Exception in file `$path`:\n$error")

        }
        parser.input.length
      }
      def listFiles(file: File): Iterator[String] = {
        val (dirs, files) = file.listFiles().toIterator.partition(_.isDirectory)
        files.map(_.getPath) ++ dirs.flatMap(listFiles)
      }

      val startTime = System.nanoTime()
      val fileChars =
        for {
          fileName <- listFiles(new File(if (path startsWith "~") System.getProperty("user.home") + path.tail else path))
          if fileName endsWith ".txt"
          if !blackList.exists(fileName.contains)
        } yield checkFile(fileName)
      val totalChars = fileChars.sum / 1000
      val millis = (System.nanoTime() - startTime)/1000000
      println(s"$exampleName:\n  ${totalChars}K chars in $millis ms (${totalChars*1000/millis}K chars/sec})")
      true
    }
  }

