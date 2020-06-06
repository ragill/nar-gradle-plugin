/**
 * 
 */
package me.ragill.gradle.plugins;

import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.java.archives.Attributes
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.bundling.Jar


/**
 * @author Richard A. Gill
 * @date Aug 26, 2019
 *
 */
class NarTask extends Jar {
	@Internal
	List<Object> bundledDependencies = []

	@Internal
	Configuration parentNarConfiguration

	def static final NAR_EXTENSION = 'nar'

	def NarTask() {
		super()
		this.setDescription('Bundles the project as a nar archive for NiFi')
		this.getArchiveExtension().set(NAR_EXTENSION)
		metadataCharset('UTF-8')
		this.configureBundledDependencies()
		this.configureManifest()
		this.configureParentNarManifestEntry()

	}

	/**
	 * Add bundled dependencies
	 */
	def configureBundledDependencies() {
		configure {
			into('META-INF/bundled-dependencies') {
				from({ -> bundledDependencies })
			}
		}
	}
	/**
	 * Add NAR attributes to manifest
	 */
	def configureManifest() {
		project.afterEvaluate {
			configure {
				Attributes attr = manifest.attributes
				attr.putIfAbsent(NarManifestEntry.NAR_GROUP.manifestKey, project.group)
				attr.putIfAbsent(NarManifestEntry.NAR_ID.manifestKey, project.name)
				attr.putIfAbsent(NarManifestEntry.NAR_VERSION.manifestKey, project.version)
			}
		}
	}
	/**
	 * Configure Manifest attributes for Parent (if exists)
	 */
	def Task configureParentNarManifestEntry() {
		project.afterEvaluate {
			configure {
				if (parentNarConfiguration == null) return

					if (parentNarConfiguration.size() > 1) {
						throw new RuntimeException("Only one parent nar dependency allowed in nar configuration but found ${parentNarConfiguration.size()} configurations")
					}

				if (parentNarConfiguration.size() == 1) {
					Dependency parentNarDependency = parentNarConfiguration.allDependencies.first()
					Attributes attr = manifest.attributes
					attr.putIfAbsent(NarManifestEntry.NAR_DEPENDENCY_GROUP.manifestKey, parentNarDependency.group)
					attr.putIfAbsent(NarManifestEntry.NAR_DEPENDENCY_ID.manifestKey, parentNarDependency.name)
					attr.putIfAbsent(NarManifestEntry.NAR_DEPENDENCY_VERSION.manifestKey, parentNarDependency.version)
				}
			}
		}
	}
}
