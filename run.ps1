$basepath = "C:\Program Files\Gabriel"
$calpath = "$basepath\Calendar"
$jrepath = "$basepath\jre-10.0.1\bin"

cd $calpath

$javaexec = "$jrepath\java.exe"
$jararg = "-jar"
$jarexec = "$calpath\calendar.jar"

& $javaexec $jararg $jarexec