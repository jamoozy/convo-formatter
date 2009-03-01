all :
	javac formatter/*.java reader/*.java writer/*.java ui/*.java reader/lexer/html/*.java

doc : FORCE
	if [ ! -d doc ]; then mkdir doc; fi
	javadoc ui formatter reader writer reader.lexer.html -d doc

t-d :
	if [ ! -d out ]; then mkdir out; fi
	java formatter.Main --trillian-deadaim in/Trillian\ example.txt out/

r-d :
	java formatter.Main --read-deadaim in/2005-08-12\ \[Friday\].htm

clean :
	rm formatter/*.class reader/*.class writer/*.class ui/*.class reader/lexer/html/*.class

wc :
	wc -l formatter/*.java reader/*.java reader/lexer/html/*.java ui/*.java writer/*.java

FORCE :
