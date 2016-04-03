
default:
	@sbt compile


.PHONY: run ruin clean package feature log

run:
	@sbt run

ruin:
	sbt run

feature:
	@sbt -feature

clean:
	@sbt clean

package:
	@sbt clean package

log:
	@rm log
	@make run &> log
