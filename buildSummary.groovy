//构建SUMMARY.md里的章节

def parten = ~/^#{2,3}\s{1}.{1,}/
def parten2 = ~/[a-zA-Z]{1}[a-zA-Z -\\’]{1,}/

def filename=$/Geode_8_Apache_Geode_Reference.md/$
def name=$/c:\WJW_E\白石-Markdown\Geode\${filename}/$

String newLine;
String[] twoHead
new File(name).eachLine{line->
  if (parten.matcher(line.trim()).matches() ) {
    twoHead = line.split(" \\{\\#")
    if(twoHead.length==2) {
      println "".padLeft((twoHead[0].count("#")-1)*2)+"* ["+twoHead[0].replaceAll("#","")+"](${filename}#"+twoHead[1].replaceAll("\\{#","").replaceAll("}",")")
    }
  }
}
