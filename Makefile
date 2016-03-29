
default:
	@sbt compile


.PHONY: run clean package feature log

run:
	@sbt run

feature:
	@sbt -feature

clean:
	@sbt clean

package:
	@sbt clean package

log:
	@rm log
	@make run &> log
