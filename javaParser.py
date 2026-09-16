from pathlib import Path
import re

public = "+"
protected = "#"
private = "-"

if __name__=="__main__":
    p = Path('.')
    all_java_files = list(p.glob("**/*.java"))
    all_classes = "|".join([file.name[:len(file.name)-5] for file in all_java_files])
    classes_dict= dict()
    dependencies= dict()
    for file in all_java_files:
        with open(file, "r") as f:
            file_content = f.read()
            if re.search("(?:public|protected)[\s\S]+?class", file_content):
                clss = file.name[:len(file.name)-5]
                attr_expression = re.compile(f"class[\\s]*?{clss}[\\s\\S]*?(?={clss}[\\s]*\\([^;]*?\\)[\\s]*[throws]*[\\s]*[\\S]*[\\s]*?{{)")
                #extracting dependencies between classes
                deps = re.search(attr_expression, file_content)
                #cleaning up the file content so that it wouldnt contain class declaration
                file_content = re.search("public[\s\S]*?class(.*)", file_content, flags=re.DOTALL).group(1)
                #extracting all pof the methods and attributes of the class
                content = re.findall("(?:public|private|protected)[^*]*?(?:\([\s\S]*?\)|;)", file_content)
                #sorting attributes and methods into separate lists(aka separating them)
                methods = []
                attributes = []
                for i in content:
                    elem = []
                    #extracting privacy
                    if i[2] == "b":
                        elem.append("public")
                    elif i[2] == "i":
                        elem.append("private")
                    elif i[2] == "o":
                        elem.append("protected")
                    i = i[len(elem[0]):]
                    if ";" in i:
                       #Extracting the name
                       if "=" in i:
                            elem.append(re.search("([A-Za-z]*?)[\\s]*?=",i).group(0))
                            i = i[:i.index("=")+1]
                       else:
                            elem.append(re.search("([A-Za-z]*?);",i).group(1))
                       #Extracting the type of attribute
                       i=i[:-(len(elem[1])+1)]
                       elem.append(i)
                       attributes.append(elem)
                    else:
                        #Extracting parameters 
                        params = re.search(f"\\([\\s\\S]*?\\)", i).group(0)
                        i = i[:-(len(params))+1]
                        params = params[1:-1]
                        #Extracting the name
                        elem.append(re.search("([A-Za-z]*?)\\(", i).group(0)[:-1])
                        i = i[:-(len(elem[1])+1)]
                        elem.append(i)
                        elem.append (params)
                        methods.append(elem)
                classes_dict[clss] = {"methods" : methods, "attributes" : attributes}
                
                try:
                    deps_text = deps.group(0)
                    classes_expression = re.compile(f"(?:(?:private|public|protected|final|static)[\\s]+)+[\\s\\S]*?;")
                    attributes = re.findall(classes_expression, deps_text)
                    attributes = [re.search(f"\\b(?:{all_classes})\\b", att).group(0) for att in attributes if "void" not in att and "(" not in att and re.search(f"\\b(?:{all_classes})\\b", att)]
                    dependencies[clss]=attributes
                except Exception as e:
                    print(f"didnt find in file : {file.name}")


    with open("./design/diagrams/parsed.plantuml", "w") as f:
        f.write("@startuml \"parsed\"\n top to bottom direction \n skinparam linetype ortho \n skinparam ranksep 80 \n skinparam nodesep 100\n ")

        #creating the classes boxes here
        for d in dependencies:
            f.write("\n")
            f.write(f"class {d}{{\n")
            d = classes_dict[d]
            f.write("...attributes...\n")
            for i in d["attributes"]:
                if i[0]=="private":
                    f.write(f"{private}")
                elif i[0]=="public":
                    f.write(f"{public}")
                else:
                    f.write(f"{protected}")
                f.write(f"{i[1]} : {i[2]}")
                f.write("\n")
            f.write("\n")

            f.write("...methods...\n")
            for i in d["methods"]:
                if i[0]=="private":
                    f.write(f"{private}")
                elif i[0]=="public":
                    f.write(f"{public}")
                else:
                    f.write(f"{protected}")
                f.write(f"{i[1]} ({i[3]}) : {i[2]}")
                f.write("\n")
            f.write("\n}")
        
        #creating the arrows here
        f.write("\n")
        already_drawn = []
        for d in dependencies:
            for i in dependencies[d]:
                if i not in already_drawn:
                    already_drawn.append(i)
                    f.write(f"{d} -down-> {i}\n")
                else:
                    f.write(f"{d} -up-> {i}\n")
            f.write("\n")
        f.write("\n@enduml")
