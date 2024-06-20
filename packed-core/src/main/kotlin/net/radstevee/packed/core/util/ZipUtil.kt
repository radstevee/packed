package net.radstevee.packed.core.util

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Utilities regarding zip files.
 */
object ZipUtil {
    /**
     * Recursively zips a directory.
     * @param directory The directory.
     * @param zipFile The output file.
     */
    fun zipDirectory(directory: File, zipFile: File) {
        ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use {
            zipFiles(it, directory, "", zipFile)
        }
    }

    private fun zipFiles(zipOut: ZipOutputStream, sourceFile: File, parentDirPath: String, zipFile: File) {
        val data = ByteArray(2048)
        sourceFile.listFiles()?.forEach { file ->
            if (file.name == zipFile.name) return
            if (file.isDirectory) {
                val path = if (parentDirPath == "") {
                    file.name
                } else {
                    parentDirPath + File.separator + file.name
                }
                val entry = ZipEntry(path + File.separator)
                entry.time = file.lastModified()
                entry.isDirectory
                entry.size = file.length()
                zipOut.putNextEntry(entry)
                // Call recursively to add files within this directory
                zipFiles(zipOut, file, path, zipFile)
            } else {
                FileInputStream(file).use { fi ->
                    BufferedInputStream(fi).use { origin ->
                        val path = parentDirPath + File.separator + file.name
                        val entry = ZipEntry(path)
                        entry.time = file.lastModified()
                        entry.isDirectory
                        entry.size = file.length()
                        zipOut.putNextEntry(entry)
                        while (true) {
                            val readBytes = origin.read(data)
                            if (readBytes == -1) {
                                break
                            }
                            zipOut.write(data, 0, readBytes)
                        }
                    }
                }
            }
        }
    }
}