import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.report.ReportMergeTask

buildscript {
    dependencies {
        classpath(libs.dagger.hilt.android.gradle.plugin)
        classpath(libs.twitter.compose.rules.detekt)
    }
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.detekt)
}
// see https://detekt.dev/docs/introduction/reporting/#merging-reports
val reportMerge by tasks.registering(ReportMergeTask::class) {
    output.set(rootProject.layout.buildDirectory.file("reports/detekt/merge.xml")) // or "reports/detekt/merge.sarif"
}
subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    dependencies {
        detektPlugins(rootProject.project.libs.detekt.formatting)
        detektPlugins(rootProject.project.libs.twitter.compose.rules.detekt)
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
