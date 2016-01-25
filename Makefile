
default:
	@sbt compile

.PHONY: run clean package

run:
	@sbt run

clean:
	@sbt clean

package:
	@sbt clean package
