class CreateDomainUmlClassGrailsPlugin {
    def version = "0.1"
    def grailsVersion = "1.3.5 > *"
    def dependsOn = [:]
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def author = "Juan Carlos Clemente"
    def authorEmail = "zetaweb@gmail.com"
    def title = "Create Domain Uml Class"
    def description = '''\\
    Create UML Class diagrams for Grails Domain Classes
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/create-domain-uml-class"

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before 
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
