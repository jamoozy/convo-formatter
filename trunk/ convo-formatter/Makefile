all :
	javac formatter/*.java reader/*.java exception/*.java writer/*.java ui/*.java reader/lexer/html/*.java

doc :
	if [ ! -d docs ]; then mkdir docs; fi
	javadoc exception formatter reader writer reader.lexer.html -d docs

t-d :
	if [ ! -d out ]; then mkdir out; fi
	java formatter.Main --trillian-deadaim

r-d :
	java formatter.Main --read-deadaim

clean :
	rm formatter/*.class reader/*.class exception/*.class writer/*.class ui/*.class reader/lexer/html/*.class

wc :
	wc -l exception/*.java formatter/*.java reader/*.java reader/lexer/html/*.java ui/*.java writer/*.java
