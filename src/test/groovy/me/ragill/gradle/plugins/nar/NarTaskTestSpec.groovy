package me.ragill.gradle.plugins.nar;

import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.bundling.Jar
import spock.lang.Specification
import me.ragill.gradle.plugins.nar.NarTask

/**
 * @author Richard A. Gill
 * @date Sep 12, 2019
 * Run Spock Tests on NarTask
 */
public class NarTaskTestSpec  extends Specification {
	def Project project
	def Task task

	def setup() {
		project = ProjectBuilder.builder().build()
		task = project.task('nar', type: NarTask)
	}
	def "Task Exists"(){
		expect:
		task instanceof NarTask
	}
	def "Task is Jar Type" (){
		task instanceof Jar
	}
	def "Task enabled"(){
		expect:
		task.enabled
	}
	def "Task output has nar extension" () {
		given:
		def NarTask nar = task
		expect:
		nar.getArchiveExtension().get() == 'nar'
	}

	def "Manifest exists"(){
		expect:
		task.manifest != null
	}
	def "Manifest has attributes"(){
		expect:
		task.manifest.attributes != null
	}
}
