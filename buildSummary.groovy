//构建SUMMARY.md里的章节

def parten = ~/^#{2,3}\s{1}.{1,}/

def filename=$/Geode_6_Developing_with_Apache_Geode.md/$
def fullName=$/c:\WJW_E\白石-Markdown\Geode\${filename}/$

String newLine;
String[] twoHead
new File(fullName).eachLine{line->
  if (parten.matcher(line.trim()).matches() ) {
    twoHead = line.split(" \\{\\#")
    if(twoHead.length==2) {
      println "".padLeft((twoHead[0].count("#")-1)*2)+"* ["+twoHead[0].replaceAll("#","").trim()+"](${filename}#"+twoHead[1].replaceAll("\\{#","").replaceAll("}",")")
    } else {
      println "".padLeft((twoHead[0].count("#")-1)*2)+"* ["+twoHead[0].replaceAll("#","").trim()+"](${filename}#"+twoHead[0].replaceAll("#","").trim()+")"
    }
  }
}
