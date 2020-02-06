TP=tp3

SRC=$(wildcard src/*.java)
CLASSES=$(SRC:src/%.java=bin/%.class)

TESTS=$(wildcard tests/*.java)
TESTS_CLASSES=$(TESTS:tests/%.java=bin/%.class)

ALL_RTL=$(shell find -L rtl -name '*.java')
# Exclude these files
NOSRC_RTL_PATTERN=DebugInfo.java ConstProp* ReachableDef* Liveness* RegAlloc* Copy* Avail* CSE* SSA* graph/Color* graph/Interfer* graph/RtlCFG* interpreter DataFlowDebug* AllIdents* FreshIdent* Heap* Int.java Interpreter.java Ptr.java Value.java Parser.java parser/* syntaxtree/* visitor/* DefKind.java DefKindVisitor.java
NOSRC_RTL_PREPEND=$(patsubst %, rtl/%, $(NOSRC_RTL_PATTERN))
NOSRC_RTL=$(wildcard $(NOSRC_RTL_PREPEND))
RTL=$(filter-out $(NOSRC_RTL), $(ALL_RTL))

SCRIPTS=minijavac.sh rtl.sh minijava.sh

MINIJAVA_EXAMPLES=$(shell find -L examples/mj -name '*.java')
RTL_EXAMPLES=$(shell find -L examples/rtl -name '*.rtl')
RTL_PRIVATE_EXAMPLES=$(wildcard examples/rtl/prives/*.rtl)
RTL_EXAMPLES_STUDENTS=$(filter-out $(RTL_PRIVATE_EXAMPLES), $(RTL_EXAMPLES))

FILES=README.md Makefile\
	$(RTL) src tests $(MINIJAVA_EXAMPLES) $(RTL_EXAMPLES_STUDENTS) minijava.jar\
	.classpath .project\
	test $(SCRIPTS)

JAVAC=javac
	
all: classes tests
	
classes: $(CLASSES)

$(CLASSES): $(SRC)
	mkdir -p bin
	$(JAVAC) -cp minijava.jar -d bin $(SRC)

tests: $(TESTS_CLASSES)

$(TESTS_CLASSES): $(TESTS) $(CLASSES)
	$(JAVAC) -cp minijava.jar:src -d bin $(TESTS)

archive: $(TP).tar.gz

$(TP).tar.gz:
	tar -czhf $@ $(FILES)

mrproper: clean
	rm -f $(TP).tar.gz

clean:
	rm -rf bin

.PHONNY: all archive classes tests clean mrproper bin

.SUFFIXES:
