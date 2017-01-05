
default:
	@sbt compile


.PHONY: run ruin clean package feature log doc opendoc

run:
	@sbt run

ruin:
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

doc:
	@sbt doc

opendoc:
	xdg-open target/scala-2.10/api/index.html
