/*
 * Copyright 2009 Al Phillips.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



Ant.property(environment:"env")
grailsHome = Ant.antProject.properties."env.GRAILS_HOME"

names = ['A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S']

includeTargets << grailsScript("Init")
includeTargets << new File ( "${grailsHome}/scripts/Bootstrap.groovy" )

fileName = 'classes.uml'

target(main: "The description of the script goes here!") {
    depends( configureProxy, packageApp, classpath, loadApp, configureApp )
    generate(grailsApp.domainClasses)
}

setDefaultTarget( main )

private void generate(def domainClasses){
    def classes = ''
    def relationships = "\n\n"
    def renders = 'drawObjects('
    def positions = 'leftToRight.top(70)('
    def assoc = ''
    def i = 0
    domainClasses.each { domainClass ->
        def relations = classDef = ''
	    def classDef = "Class.${domainClass.name}(\"${domainClass.name}\")\n\t("
	    domainClass.properties.each{ prop ->
            if(prop.name != 'id' && prop.name != 'version')
            {
                assoc = ''
                if (prop.isAssociation())
                {
                    if(!prop.isBidirectional() || prop.isOwningSide())
                    {
                        assoc = "(" + getRelationship(prop) + ")"
                        relations += getRelationship(domainClass.name, prop) + "\n"
                    }   
                }
                classDef +=  '"-' + prop.name + ": " + resolveName(prop.getType().getName()) + " ${assoc} \",\n\t"
                
            }
        }
        classDef += "\t) ();\n"
        classes += classDef
        relationships += relations
        i++
    }

    
    domainClasses.each { domainClass ->
        renders += domainClass.name + ','
        positions += domainClass.name + ','
    }
    renders = renders[0..(renders.length()-2)]
    positions = positions[0..(positions.length()-2)]

    renders += ");\n"
    positions += ");\n"

    createFile(classes + positions + renders + relationships)
}

private String getRelationship(prop)
{

    def association = ''
    if (prop.isOneToMany()){
        association = prop.isOptional() ? '1-0..*>':'1-1..*>'
    } else if (prop.isOneToOne()){
        association = prop.isOptional() ? '1-0..1>':'1-1>'
    } else if (prop.isManyToMany()){
        association = prop.isOptional() ? '*-*>':'1..*-1..*>'
    }
    if(prop.isBidirectional()){
        association = '<' + association
    }
    association
}

private String getRelationship(name, prop)
{
    "clink(association)( ${name}, ${resolveName(prop.getReferencedPropertyType().getName())} );\n"
}



private resolveName(def name){
    // remove bracket if an array
    if(name.lastIndexOf('[') > -1){
        name = name.replace('[','');
    }
    // remove package name
    if (name.lastIndexOf('.') > -1){
        return name.substring(name.lastIndexOf('.')+1)
    }
    return name
}

private void createFile(umlStuff){
    File f = new File(fileName)
    f.write(umlStuff)
    println "Created file ${f.name}"
}

