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


includeTargets << grailsScript("Init")
includeTargets << new File ( "${grailsHome}/scripts/Bootstrap.groovy" )

fileName = 'classes.uml'
//url = 'http://yuml.me'

target(main: "The description of the script goes here!") {
    depends( configureProxy, packageApp, classpath, loadApp, configureApp )
    generate(grailsApp.domainClasses)
}

setDefaultTarget(main)

private void generate(def domainClasses){
    def classes = ''
    def relationships = ''
    domainClasses.each { domainClass ->
        def relations = classDef = ''
	    //def classDef = ''
	    domainClass.properties.each{ prop ->
            if(prop.name != 'id' && prop.name != 'version'){
                if (prop.isAssociation()){
                    // if its association only show the do the owning side
                    if(!prop.isBidirectional() || prop.isOwningSide())
                        relations += getRelationship(domainClass.name, prop) + "\n"
                } else {
                    classDef += resolveName(prop.getType().getName()) + ' ' + prop.name + ";\n"
                }
            }
        }
        classDef = (classDef == "") ? '' : '|' + classDef
        classDef += "[${domainClass.name}${classDef}],"
        classes += classDef
        relationships += relations
    }
    createFile(classes + relationships)
}

private String getRelationship(name, prop){

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
    "[${name}]${association}[${resolveName(prop.getReferencedPropertyType().getName())}],"
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

