# Java Makefile
# Dr Alun Moon
# alun.moon@northumbria.ac.uk

%.class:%.java
	javac $<

main:=Lander
jarfile:=LunarLander.jar
sources:=$(wildcard *.java */*.java)
assets=

classes=$(sources:%.java=%.class)
innerclasses=$(classes:%.class=%$\*.class)

all: tags $(jarfile)

jarfile: $(jarfile)

tags: $(sources)
	ctags --extra=fq $(sources)

$(jarfile): $(classes) $(assets)
	jar cfe $@ $(main) $(classes) $(innerclasses) $(assets)

manifest:
	echo "Main-class: $(main)" > manifest

.PHONY: clean
clean:
	rm *.class $(jarfile) tags

.PHONY: mostlyclean
mostlyclean:
	rm  *.class $(jarfile)

.PHONY: run
run: $(jarfile)
	java -jar $(jarfile) 

.PHONY: pretty
pretty: $(sources)
	astyle -q $(sources)
