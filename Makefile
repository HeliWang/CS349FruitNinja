.phony: all run clean

# super simple makefile
# call it using 'make NAME=name_of_code_file_without_extension'
# (assumes a .java extension)
NAME = "Main"
SRC = "src"

all:
	@echo "Compiling..."
	javac src/*.java

run: all
	@echo "Running..."
	java -classpath $(SRC) $(NAME)

clean:
	rm *.class
