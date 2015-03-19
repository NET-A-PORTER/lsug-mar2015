val X_OFFSET = 1500

val lines = scala.io.Source.fromFile("index.html").getLines.toSeq

var xValue = -X_OFFSET
var yValue = 0

val dataY = "data-y=\"([-0-9]+)\"".r

val newLines = lines map { line => 

	if(line.contains("data-x=")) {
		if(!line.contains("snap-vertical")) {
			xValue += X_OFFSET
		}

		var fixedLine = line.replaceAll("data-x=\"[-0-9]+\"", s"""data-x="${xValue}"""")
		

    // If this is a snap-vertical slide, then listen to it's y-value
    // If this is not a snap-vertical slide, then set the y-value to the last snap-vertical slide
		if(line.contains("snap-vertical")) {
	    yValue = line match {
	    	case dataY(y) => y.toInt
	    	case _ => yValue
	    }
    } else {
    	fixedLine = fixedLine.replaceAll("data-y=\"[-0-9]+\"", s"""data-y="${yValue}"""")
    }

    fixedLine
	} else {
		line
	}

}

val p = new java.io.PrintWriter(new java.io.File("index.html"))
newLines.foreach(p.println)
p.close()
