package me.xx2bab.ruler.battery.gradle

import com.android.build.api.transform.*
import com.android.utils.FileUtils
import org.apache.commons.io.IOUtils
import org.gradle.api.Project
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

class BatteryConsumingTaskMonitorTransform(private val project: Project) : Transform() {

    override fun getName(): String {
        return "BatteryConsumingTaskMonitorTransform"
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return mutableSetOf(QualifiedContent.DefaultContentType.CLASSES)
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return mutableSetOf(QualifiedContent.Scope.PROJECT,
                QualifiedContent.Scope.SUB_PROJECTS,
                QualifiedContent.Scope.EXTERNAL_LIBRARIES)
    }

    override fun isIncremental(): Boolean {
        return true
    }

    override fun isCacheable(): Boolean {
        return true
    }

    override fun transform(transformInvocation: TransformInvocation) {
        super.transform(transformInvocation)
        val isIncrementalBuild = transformInvocation.isIncremental
        project.logger.info("BatteryConsumingTaskMonitorTransform: isIncremental $isIncrementalBuild")
        project.logger.info("BatteryConsumingTaskMonitorTransform: isCacheable $isCacheable")

        if (!isIncrementalBuild) {
            transformInvocation.outputProvider.deleteAll()
        }

        transformInvocation.inputs.forEach { inputs ->
            inputs.jarInputs.forEach { jarInput ->
                val dest = transformInvocation.outputProvider.getContentLocation(jarInput.name,
                        jarInput.contentTypes, jarInput.scopes, Format.JAR)
                when (jarInput.status) {
                    Status.NOTCHANGED -> {
                    }
                    Status.ADDED -> {
                        project.logger.info("BatteryConsumingTaskMonitorTransform: jarInput added ${jarInput.name}")
                        extractJarWhenAdded(jarInput.file, dest)
                    }
                    Status.CHANGED -> {
                        project.logger.info("BatteryConsumingTaskMonitorTransform: jarInput changed ${jarInput.name}")
                        extractJarWhenAdded(jarInput.file, dest)
                    }
                    Status.REMOVED -> {
                        project.logger.info("BatteryConsumingTaskMonitorTransform: jarInput removed ${jarInput.name}")
                        extractJarWhenRemoved(jarInput.file, dest)
                    }
                    else -> {
                        project.logger.info("BatteryConsumingTaskMonitorTransform: jarInput status is null")
                    }
                }
                if (!isIncrementalBuild) {
                    extractJarWhenAdded(jarInput.file, dest)
                }
            }

            inputs.directoryInputs.forEach { directoryInput ->
                val destDir = transformInvocation.outputProvider.getContentLocation(directoryInput.name,
                        directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                if (!destDir.exists()) {
                    destDir.mkdirs()
                }
                directoryInput.changedFiles.forEach { changedFileEntry -> // Incremental build
                    val changedFile = changedFileEntry.key
                    val destFile = File(changedFile.absolutePath.replace(directoryInput.file.absolutePath,
                            destDir.absolutePath))
                    when (changedFileEntry.value) {
                        Status.NOTCHANGED -> { // It's not getting triggered actually, can ignore
                            project.logger.info("BatteryConsumingTaskMonitorTransform: directoryInput nochanged ${changedFileEntry.key.name}")
                        }
                        Status.ADDED -> {
                            project.logger.info("BatteryConsumingTaskMonitorTransform: directoryInput added ${changedFileEntry.key.name}")
                            val processedByteArray = onClassAdded(directoryInput.file.inputStream())
                            byteArrayToFile(processedByteArray, destFile)
                        }
                        Status.CHANGED -> {
                            project.logger.info("BatteryConsumingTaskMonitorTransform: directoryInput changed ${changedFileEntry.key.name}")
                            val processedByteArray = onClassChanged(directoryInput.file.inputStream())
                            byteArrayToFile(processedByteArray, destFile)
                        }
                        Status.REMOVED -> {
                            project.logger.info("BatteryConsumingTaskMonitorTransform: directoryInput removed ${changedFileEntry.key.name}")
                            project.logger.info("BatteryConsumingTaskMonitorTransform: directoryInput removed ${changedFile.absolutePath}")
                            project.logger.info("BatteryConsumingTaskMonitorTransform: directoryInput removed ${destFile.absolutePath}")
                            project.logger.info("BatteryConsumingTaskMonitorTransform: directoryInput removed ${destFile.exists()}")
                            onClassDeleted(directoryInput.file.inputStream())
                            if (destFile.exists()) {
                                destFile.delete()
                            }
                        }
                        else -> {
                            project.logger.info("BatteryConsumingTaskMonitorTransform: directoryInput status is null")
                        }
                    }
                }
                if (!isIncrementalBuild) { // Clean build
                    directoryInput.file.walk()
                            .filter { it.isFile && it.extension == "class" }
                            .forEach { file ->
                                val destFile = File(file.absolutePath.replace(directoryInput.file.absolutePath,
                                        destDir.absolutePath))
                                val processedByteArray = onClassAdded(file.inputStream())
                                byteArrayToFile(processedByteArray, destFile)
                            }
                }
            }
        }
    }

    private fun byteArrayToFile(processedByteArray: ByteArray, destFile: File) {
        if (!destFile.exists()) {
            destFile.parentFile.mkdirs()
            destFile.createNewFile()
        }
        destFile.writeBytes(processedByteArray)
    }

    private fun extractJarWhenAdded(jarInput: File, destFile: File) {
        val jar = JarFile(jarInput)
        val enumeration = jar.entries()
        val tmpFile = File(jarInput.parent + File.separator
                + "classes_temp_${jarInput.nameWithoutExtension}.jar")
        if (tmpFile.exists()) {
            tmpFile.delete()
        }
        val jarOutputStream = JarOutputStream(FileOutputStream(tmpFile))
        while (enumeration.hasMoreElements()) {
            // Extracting input class
            val element = enumeration.nextElement()
            val className = element.name
            val zipEntry = ZipEntry(className)
            val inputStream = jar.getInputStream(zipEntry)
            // Processing input class
            val processedByteArray = onClassAdded(inputStream)
            // Output
            // to a new jar's entry
            jarOutputStream.putNextEntry(zipEntry)
            jarOutputStream.write(processedByteArray)
            jarOutputStream.closeEntry()
        }
        jarOutputStream.close()
        jar.close()
        FileUtils.copyFile(tmpFile, destFile)
        tmpFile.delete()
    }

    private fun extractJarWhenRemoved(jarInput: File,  destFile: File) {
        val jar = JarFile(jarInput)
        val enumeration = jar.entries()
        while (enumeration.hasMoreElements()) {
            val element = enumeration.nextElement()
            val inputStream = jar.getInputStream(ZipEntry(element.name))
            onClassDeleted(inputStream)
        }
        if (destFile.exists()) {
            destFile.delete()
        }
    }

    fun onClassAdded(input: InputStream): ByteArray {
        return IOUtils.toByteArray(input)
    }

    fun onClassChanged(input: InputStream): ByteArray {
        return IOUtils.toByteArray(input)
    }

    fun onClassDeleted(input: InputStream) {
    }

}