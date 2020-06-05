package me.ragill.gradle.plugins.nar

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention


public class NarPlugin implements Plugin<Project> {
	def static String NAR_TASK = "nar"

	/**
	 * Called when task is executed
	 */
	@Override
	public void apply(Project project) {
		this.ensureJavaPlugin(project)
		def Configuration conf = this.createConfiguration(project)
		this.createTask(project, conf)
	}

	/**
	 * Task requires Java Plugin
	 * @param project
	 */
	def ensureJavaPlugin(Project project) {
		if (!project.plugins.hasPlugin(JavaPlugin)) {
			project.plugins.apply(JavaPlugin)
		}
	}

	/**
	 * Set up configuration object for plugin
	 * @param project
	 * @return
	 */
	def createConfiguration(project) {
		Configuration conf = project.configurations.create(NAR_TASK)
		project.configurations.implementation.extendsFrom(project.configurations.nar)
		project.configurations.implementation.canBeResolved = true
		conf.transitive = false
		conf
	}

	/**
	 * Create the nar task
	 * @param project
	 * @param conf
	 * @return
	 */
	def createTask(Project project, conf) {
		def NarTask nar = project.tasks.create(NAR_TASK, NarTask)
		nar.setGroup(BasePlugin.BUILD_GROUP)
		nar.inputs.files(conf)
		def JavaPluginConvention convention = project.getConvention().getPlugin(JavaPluginConvention.class)
		nar.from(convention.sourceSets.main.output)
		//add dependencies

		nar.bundledDependencies = [
			project.configurations.implementation,
			project.tasks[JavaPlugin.JAR_TASK_NAME]
		]
		//set parent configuration
		nar.parentNarConfiguration = conf
		//add task as dependency on assemble task
		project.tasks[BasePlugin.ASSEMBLE_TASK_NAME].dependsOn(nar)
		//disable jar task so that only the nar gets built
		project.tasks.getByName('jar').enabled = false
	}
}
