package me.xx2bab.ruler.battery.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class BatteryAspectWeavingPlugin: Plugin<Project> {

    override fun apply(project: Project) {
        project.logger.info("\"me.2bab.ruler.battery\" plugin applied.")

    }

}