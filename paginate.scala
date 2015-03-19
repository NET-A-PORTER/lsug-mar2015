val X_OFFSET = 1500

val lines = scala.io.Source.fromFile("index.html").getLines.toSeq
val numOfSlides = lines.count(_ contains "data-x=")

var xValue = -X_OFFSET
var yValue = 0

val dataY = "data-y=\"([-0-9]+)\"".r

val newLines = lines map { line => 



	if(line.contains("data-x=")) {
		if(!line.contains("snap-vertical")) {
			xValue += X_OFFSET
		}
		line.replaceAll("data-x=\"[-0-9]+\"", s"data-x=\"${xValue}\"")
		line.replaceAll("data-y=\"[-0-9]+\"", s"data-y=\"${yValue}\"")

    yValue = line match {
    	case dataY(y) => y.toInt
    	case _ => yValue
    }

	} else {
		line
	}
}

val p = new java.io.PrintWriter(new java.io.File("index.html"))
newLines.foreach(p.println)
p.close()
