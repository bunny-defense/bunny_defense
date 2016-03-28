
default:
	@sbt compile


.PHONY: run clean package feature

run:
	@sbt run

feature:
	@sbt -feature

clean:
	@sbt clean

package:
	@sbt clean package
