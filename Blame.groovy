import static groovyx.gpars.GParsPool.withPool
import groovyx.gpars.GParsPool

def start = Calendar.instance.timeInMillis

def files = []

[
    'backend/src/main',
    'business/src/main',
    'common/src/main',
    'data/src/main',
    'test/src/test'
]. each { d ->
    def dir = new File('C:/projects/branches/singleDB/' + d)
    dir.eachFileRecurse{ File file->

        if(file.isFile() && (file.name.endsWith("java") || file.name.endsWith("groovy")))
            files << file
    }
}

println "Файлов:${files.size()}"

def pattern = ~/\d+\s+(\d+)/

def blame(file, pattern) {
    def result = [:]
    "svn blame $file.path".execute().text.eachLine { line ->
        // версия, автор, текст
        def matcher = line.trim() =~ pattern
        def user = matcher[0][1]

        result[(user)] = (result[(user)] ?: 0) + 1
    }
    result
}

def result = [:]

//files.collect{ blame(it, pattern) } // 165 sec
GParsPool.withPool { files.collectParallel { blame(it, pattern) } } // 42 sec
.each { map ->
    map.each { k, v ->
        def rows = result[k] ?: 0
        result[k] = rows + v
    }
}

println result

println "Finished: ${Calendar.instance.timeInMillis - start}"
