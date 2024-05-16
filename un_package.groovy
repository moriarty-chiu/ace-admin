@Grab(group='org.apache.commons', module='commons-compress', version='1.21')

import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.BufferedOutputStream

def unpackageTarFile(String tarFilePath, String outputDirPath) {
    def outputDir = new File(outputDirPath)
    if (!outputDir.exists()) {
        outputDir.mkdirs()
    }

    new File(tarFilePath).withInputStream { fileInputStream ->
        def tarInput = new TarArchiveInputStream(fileInputStream)
        TarArchiveEntry entry
        while ((entry = tarInput.getNextTarEntry()) != null) {
            def outputFile = new File(outputDir, entry.name)
            if (entry.isDirectory()) {
                outputFile.mkdirs()
            } else {
                outputFile.parentFile.mkdirs()
                outputFile.withOutputStream { fos ->
                    tarInput.copyTo(fos)
                }
            }
        }
    }
}

// Example usage
String tarFilePath = '/path/to/your/file.tar' // Path to your TAR file
String outputDir = '/path/to/output/directory' // Path to the output directory

unpackageTarFile(tarFilePath, outputDir)
