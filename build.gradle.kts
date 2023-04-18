import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.report.ReportMergeTask

buildscript {
    dependencies {
        classpath(DependencyNames.hiltAndroidGradlePlugin)
        classpath(DependencyNames.twitterComposeRulesDetekt)
    }
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.0.0" apply false
    id("com.android.library") version "8.0.0" apply false
    kotlin("android") version DependencyVersions.kotlinVersion apply false
    kotlin("jvm") version DependencyVersions.kotlinVersion
    kotlin("plugin.serialization") version DependencyVersions.kotlinVersion
    id("io.gitlab.arturbosch.detekt") version DependencyVersions.detektVersion
}
// see https://detekt.dev/docs/introduction/reporting/#merging-reports
val reportMerge by tasks.registering(ReportMergeTask::class) {
    output.set(rootProject.layout.buildDirectory.file("reports/detekt/merge.xml")) // or "reports/detekt/merge.sarif"
}
subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    dependencies {
        detektPlugins(DependencyNames.detektFormatting)
        detektPlugins(DependencyNames.twitterComposeRulesDetekt)
    }

    detekt {
        buildUponDefaultConfig = true
        parallel = true
        config = files("${project.rootDir}/config/detekt/detekt.yml")
        baseline = file("${project.rootDir}/config/detekt/baseline.yml")
        ignoreFailures = false
        autoCorrect = true
    }

    plugins.withType<DetektPlugin> {
        tasks.withType<Detekt> detekt@{
            finalizedBy(reportMerge)
            reports.xml.required.set(true)

            reportMerge.configure {
                input.from(this@detekt.xmlReportFile)
            }
        }
    }
}
tasks.withType<Detekt>().configureEach {
    jvmTarget = "17"
}
tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = "17"
}
val detektProjectBaseline by tasks.registering(DetektCreateBaselineTask::class) {
    description = "Overrides current baseline."
    ignoreFailures.set(true)
    parallel.set(true)
    buildUponDefaultConfig.set(true)
    setSource(files(rootDir))
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
    baseline.set(file("$rootDir/config/detekt/baseline.xml"))
    include("**/*.kt")
    include("**/*.kts")
    exclude("**/resources/**")
    exclude("**/build/**")
}
