def parten = ~/^#{2,3}\s{1}.{1,}/
def parten2 = ~/[a-zA-Z]{1}[a-zA-Z -\\’]{1,}/


def name=$/c:\WJW_D\讲义\Geode\Geode_7_Tools_and_Modules.md/$
String newLine;
String[] twoHead
new File(name).eachLine{line->
  if (parten.matcher(line.trim()).matches() ) {
    twoHead = line.split(" \\{\\#")
    if(twoHead.length==2) {
      println "".padLeft((twoHead[0].count("#")-1)*2)+"* ["+twoHead[0].replaceAll("#","")+"](Geode_7_Tools_and_Modules.md#"+twoHead[1].replaceAll("\\{#","").replaceAll("}",")")
    }
  }
}
