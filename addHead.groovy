def parten = ~/^#{2,}\s{1}.{1,}[^}]$/
def parten2 = ~/[a-zA-Z]{1}[a-zA-Z -\\’]{1,}/

File file = new File($/c:\WJW_E\hello1.md/$)
file.write("") 

def name=$/c:\WJW_D\讲义\Geode\Geode_7_Tools_and_Modules.md/$
String newLine;
new File(name).eachLine{line->
  if (parten.matcher(line.trim()).matches() ) {
    java.util.regex.Matcher matcher2 = parten2.matcher(line.trim())
    if (matcher2.find() ) {
      String newStr = line.trim().substring(matcher2.start(), matcher2.end()).trim().replaceAll("( )|(\\?)|(\')|(-)|(/)|(\\()|(\\))|(\\.)","_")
    
      println "${line} {#${newStr}}"
      file.append("${line} {#${newStr}}","UTF-8")
    } else {
      file.append("${line}\r\n","UTF-8")
    }
  } else {
    file.append("${line}\r\n","UTF-8")
  }
}
