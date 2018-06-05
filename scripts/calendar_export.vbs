rem Set defaults
Dim path 
path = "C:\EXPORT"
Dim fromdate
fromdate = "20180101"
Dim todate
todate = "30000101"

rem Get commandline arguments
Dim args
Set args = WScript.Arguments
If args.Count="3" Then
    path = args.Item(0)
    fromdate = args.Item(1)
    todate = args.Item(2)
End If

rem Print values
Wscript.Echo "Path is set to: " + path
Wscript.Echo "Fromdate is set to: " + fromdate
Wscript.Echo "To date is set to: " + todate

rem export to do given path with filename 20180101000000_30000101000000.xml 
Dim hMClient
Set hMClient=CreateObject("MISOLE.MISCommand.1")
Dim stav
stav=hMClient.Do("dataKal^ZakKALEN",path,fromdate,todate)
Wscript.Echo stav