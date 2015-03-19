val X_OFFSET = 1500

val lines = scala.io.Source.fromFile("index.html").getLines.toSeq

var xValue = -X_OFFSET
var yValue = 0
var yDelta = 0

val dataY = ".*data-y=\"([-0-9]+)\".*".r

def getY(line: String) = line match {
	case dataY(y) => y.toInt
	case _ => 
	println(line)
	sys.error("snap-vertical slide should have data-y")
}

val newLines = lines map { line => 

	if(line.contains("data-x=")) {

		if(!line.contains("snap-vertical")) {
			xValue += X_OFFSET
		}

		var fixedLine = line.replaceAll("data-x=\"[-0-9]+\"", s"""data-x="${xValue}"""")
		

    // If this is a snap-vertical slide, then bump y-value it by the y delta
    // If this is not a snap-vertical slide, then bump y-value to the last snap-vertical slide
		if(line.contains("snap-vertical")) {
	    yValue = getY(line) + yDelta

    } else {

      val oldYVal = getY(line)
      yDelta = yValue - oldYVal

    	val oldY = line match {
	  	  case dataY(y) => y.toInt
	   	  case _ => 
	   	  println(line)
	   	  sys.error("snap-vertical slide should have data-y")
	    }
    }

    fixedLine.replaceAll("data-y=\"[-0-9]+\"", s"""data-y="${yValue}"""")
	} else {
		line
	}

}

val p = new java.io.PrintWriter(new java.io.File("index.html"))
newLines.foreach(p.println)
p.close()
