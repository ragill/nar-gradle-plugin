package me.ragill.gradle.plugins


import org.gradle.testfixtures.ProjectBuilder

import me.ragill.gradle.plugins.NarPlugin
import me.ragill.gradle.plugins.NarTask

import static me.ragill.gradle.plugins.NarManifestEntry.*

import org.gradle.api.Project
import spock.lang.Specification
import org.gradle.api.plugins.JavaPlugin

/**
 * Unit tests for the 'nar-plugin'
 */
public class NarPluginTestSpec extends Specification {

	def static TASK = "nar"
	def Project project

	private Project project

	def setup() {
		project = ProjectBuilder.builder().build()
		project.pluginManager.apply NarPlugin
	}

	def "plugin registers task"() {
		expect:
		project.tasks.findByName(TASK) != null
	}

	def "plugin creates empty project"(){
		expect:
		project.plugins.hasPlugin(JavaPlugin)
		and:
		project.plugins.hasPlugin(NarPlugin)
	}

	def "plugin is configured"(){
		given:
		def conf = project.configurations.findByName(TASK)
		expect:
		conf != null
		and:
		conf.transitive == false
	}
	def "has nar file extension"() {
		given:
		NarTask nar = project.tasks[TASK]
		expect:
		nar.archiveExtension.get() == TASK
	}
	def "nar has no NarTask-Dependency-Id when no nar dependency is set"() {
		given:
		NarTask nar = project.tasks[TASK]
		expect:
		!nar.manifest.attributes.containsKey(NAR_DEPENDENCY_ID.getManifestKey())
	}
	def "plugin disables Java jar task" (){
		expect:
		project.tasks['jar'].enabled == false
	}
}