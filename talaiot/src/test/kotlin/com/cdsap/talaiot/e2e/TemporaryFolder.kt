package com.cdsap.talaiot.e2e

import java.io.File
import java.io.IOException

class TemporaryFolder {
    private var folder: File? = null


    @Throws(IOException::class)
    fun create() {
        folder = createTemporaryFolderIn(null)
    }

    @Throws(IOException::class)
    fun newFile(fileName: String): File {
        val file = File(getRoot(), fileName)
        if (!file.createNewFile()) {
            throw IOException(
                "a file with the name \'$fileName\' already exists in the test folder"
            )
        }
        return file
    }

    fun newFileInPath(fileName: String): File {
        val file = File(getRoot(), fileName).also { file ->
            file.parentFile.mkdirs()
        }
        return file
    }

    @Throws(IOException::class)
    fun newFile(): File {
        return File.createTempFile("junit", null, getRoot())
    }

    @Throws(IOException::class)
    fun newFolder(folder: String): File {
        var file = getRoot()
        val folderName = folder
        validateFolderName(folderName)
        file = File(file, folderName)
        if (!file.mkdir()) {
            throw IOException(
                "a folder with the name \'$folderName\' already exists"
            )
        }
        return file
    }


    @Throws(IOException::class)
    private fun validateFolderName(folderName: String) {
        val tempFile = File(folderName)
        if (tempFile.parent != null) {
            val errorMsg =
                "Folder name cannot consist of multiple path components separated by a file separator." + " Please use newFolder('MyParentFolder','MyFolder') to create hierarchies of folders"
            throw IOException(errorMsg)
        }
    }

    private fun isLastElementInArray(index: Int, array: Array<String>): Boolean {
        return index == array.size - 1
    }

    @Throws(IOException::class)
    fun newFolder(): File {
        return createTemporaryFolderIn(getRoot())
    }

    @Throws(IOException::class)
    private fun createTemporaryFolderIn(parentFolder: File?): File {
        val createdFolder = File.createTempFile("junit", "", parentFolder)
        createdFolder.delete()
        createdFolder.mkdir()
        return createdFolder
    }

    fun getRoot(): File {
        if (folder == null) {
            throw IllegalStateException(
                "the temporary folder has not yet been created"
            )
        }
        return folder!!
    }

    fun delete() {
        if (folder != null) {
            recursiveDelete(folder!!)
        }
    }

    private fun recursiveDelete(file: File) {
        val files = file.listFiles()
        if (files != null) {
            for (each in files) {
                recursiveDelete(each)
            }
        }
        file.delete()
    }
}
