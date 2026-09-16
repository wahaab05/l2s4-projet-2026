# load JAVA_HOME directory
-include .env
export

PROJECT=projet-fromage
JAVA=$(JAVA_HOME)java
JAVAC=$(JAVA_HOME)javac
JAVADOC=$(JAVA_HOME)javadoc
JAR=$(JAVA_HOME)jar

SRC_DIR=src
TEST_DIR=tests
CLASSES_DIR=classes
LIBS_DIR=lib
BUILD_DIR=build
JAR_DIR=jar

LIVRABLE=6

# board dimensions
DEFAULT_W=20
DEFAULT_H=8
DEFAULT_NB_ROADS=5

all: clean classes tests docs jar
.PHONY: all

clean:
	- rm -rf $(CLASSES_DIR)/*
	- rm -f livrable*.jar
	- rm -f $(JAR_DIR)/*.jar
	- rm -rf design/docs/*
	- rm -rf $$(find ./$(TEST_DIR) -iname "*.class")
	- rm -rf $(BUILD_DIR)/*
# draw current diagram of the project
diagram:
	python3 javaParser.py ; plantuml -tsvg ./design/diagrams/parsed.plantuml


classes:
	mkdir -p $(CLASSES_DIR)
	mkdir -p $(BUILD_DIR)
	# find all java files
	find $(SRC_DIR) -name "*.java" > $(BUILD_DIR)/sources.txt
	# compile project
	$(JAVAC) -classpath $(LIBS_DIR)/* -d $(CLASSES_DIR) @$(BUILD_DIR)/sources.txt
	rm -f $(BUILD_DIR)/sources.txt
.PHONY: classes diagram

tests: classes
	mkdir -p $(CLASSES_DIR)
	mkdir -p $(BUILD_DIR)
	find $(TEST_DIR) -name "*.java" > $(BUILD_DIR)/sources_tests.txt
	$(JAVAC) -classpath "junit-console.jar:$(LIBS_DIR)/*:$(CLASSES_DIR)" -d $(CLASSES_DIR) @$(BUILD_DIR)/sources_tests.txt
	rm -f $(BUILD_DIR)/sources_tests.txt
.PHONY: tests

runtests: tests
	$(JAVA) -jar junit-console.jar -classpath $(CLASSES_DIR) --scan-classpath
.PHONY: runtests

jar: classes
	mkdir -p $(JAR_DIR)
	cp config.properties $(CLASSES_DIR)/
	$(JAR) cvfe $(JAR_DIR)/towerdefense-a-interactive.jar game.Livrable6 -C $(CLASSES_DIR) .
	$(JAR) cvfe $(JAR_DIR)/towerdefense-b-interactive.jar game.Livrable6 -C $(CLASSES_DIR) .
	$(JAR) cvfe $(JAR_DIR)/towerdefense-a-random.jar game.Livrable6 -C $(CLASSES_DIR) .
	$(JAR) cvfe $(JAR_DIR)/towerdefense-b-random.jar game.Livrable6 -C $(CLASSES_DIR) .
.PHONY: jar

run6: run6auto
.PHONY: run6

run6manual: jar
	@echo ""
	@echo "== Livrable 6 | plateau A | interactif =="
	@$(JAVA) -jar $(JAR_DIR)/towerdefense-a-interactive.jar $(DEFAULT_W) $(DEFAULT_H)
.PHONY: run6manual

run6auto: jar
	@echo ""
	@echo "== Livrable 6 | plateau A | aleatoire =="
	@$(JAVA) -jar $(JAR_DIR)/towerdefense-a-random.jar $(DEFAULT_W) $(DEFAULT_H)
.PHONY: run6auto

run6manualmultipath: jar
	@echo ""
	@echo "== Livrable 6 | plateau B | interactif =="
	@$(JAVA) -jar $(JAR_DIR)/towerdefense-b-interactive.jar $(DEFAULT_W) $(DEFAULT_H) $(DEFAULT_NB_ROADS)
.PHONY: run6manualmultipath

run6automultipath: jar
	@echo ""
	@echo "== Livrable 6 | plateau B | aleatoire =="
	@$(JAVA) -jar $(JAR_DIR)/towerdefense-b-random.jar $(DEFAULT_W) $(DEFAULT_H) $(DEFAULT_NB_ROADS)
.PHONY: run6automultipath

run6multipathmanual: run6manualmultipath
.PHONY: run6multipathmanual

run6multipathauto: run6automultipath
.PHONY: run6multipathauto

docs:
	mkdir -p design/docs
	$(JAVADOC) -sourcepath $(SRC_DIR) -classpath "$(LIBS_DIR)/*" -d design/docs $$(find $(SRC_DIR) -iname "*.java")
.PHONY: docs
