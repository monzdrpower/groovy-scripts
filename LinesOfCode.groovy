println "----- java ------"
int tt_java = 0
[
    'backend/src/main',
    'business/src/main',
    'common/src/main',
    'data/src/main',
    'test/src/test'
]. each { d ->
    int t = 0
    def dir = new File('C:/projects/branches/singleDB/' + d)
    dir.eachFileRecurse{file->
        
        if(file.name.endsWith('.java') ){
            int l = 0
            boolean insideComment = false
            file.eachLine {
                def g = it.trim()
                
                if(g.startsWith('//') || g.startsWith('@')){
                     return
                 }
 
                if(g.endsWith('*/')){
                    insideComment = false
                    return
                }

                if(insideComment)
                    return
                if(g.startsWith('/*')){
                    insideComment = true
                    return
                }
                if(g.length() > 1 && !g.startsWith('import')){
                    //println g
                    l++
                }
            }
            t += l
        }
    }
    println d +' ' + t
    tt_java += t
}
println tt_java

println "----- groovy ------"

tt_groovy = 0
[
    'backend/src/main',
    'business/src/main',
    'common/src/main',
    'data/src/main',
    'test/src/test'
]. each { d ->
    int t = 0
    def dir = new File('C:/projects/branches/singleDB/' + d)
    dir.eachFileRecurse{file->
        
        if(file.name.endsWith('.groovy')){
            int l = 0
            boolean insideComment = false
            file.eachLine {
                def g = it.trim()
                
                if(g.startsWith('//') || g.startsWith('@')){
                     return
                 }
 
                if(g.endsWith('*/')){
                    insideComment = false
                    return
                }

                if(insideComment)
                    return
                if(g.startsWith('/*')){
                    insideComment = true
                    return
                }
                if(g.length() > 1 && !g.startsWith('import')){
                    //println g
                    l++
                }
            }
            t += l
        }
    }
    println d +' ' + t
    tt_groovy += t
}
println tt_groovy

println "\n---------\n${tt_java + tt_groovy}"
