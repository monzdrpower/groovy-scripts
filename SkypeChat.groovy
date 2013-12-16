import java.text.SimpleDateFormat

def file = new File('d:/chat.txt')

def formatter = new SimpleDateFormat('dd.MM.yyyy')

class Stat {
  def simple = 0, url = 0
}

def from = formatter.parse('13.10.2010') 
def to = formatter.parse('13.02.2015')

def names = [] as Set
def result = [:]
def currentName, currentDate, currentDateDate, phrase
file.eachLine {
	if(!it)
		return
	
	if(it ==~ /\[\d\d\.\d\d\.\d\d\d\d.*/ && it[1..10] != currentDate){
		currentDate = it[1..10]
		currentDateDate = formatter.parse(currentDate)
	}
	if(currentDateDate < from || currentDateDate > to){
		return
	}
		
	def m = it =~ /^\[\d\d\..+?\](.*?):/
	if(m) {
		def name = m[0][1].trim()
		if(name[0] != '*'){
			names << name
			currentName = name
			phrase = it.substring(it.lastIndexOf("$name: ")+ name.size() + 2)
			//println name
			//println "\t"+phrase
		}
	} else {
		phrase = it
		//println "\t"+phrase
	}

	def person = result[(currentName)] ?: new Stat()
	
	if(phrase.contains('http')){
		person.url += 1
	} else {
		person.simple += 1
	}

	result[(currentName)] = person
	
}

def sortedResult = result.sort{
	- (it.value.url / (it.value.simple + it.value.url))  
}

//println sortedResult
//println names.sort()

sortedResult.each {
	println "$it.key: $it.value.url/$it.value.simple = ${it.value.url/ (it.value.url + it.value.simple) * 100}"
}
