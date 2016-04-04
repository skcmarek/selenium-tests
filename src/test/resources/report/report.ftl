<html>
<head>
  <title>TEST REPORT</title>
<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">
<style>
table {margin:0 auto;}td:first-child {width:200px;}td:nth-child(2) {width:660px;}td:nth-child(3)
{width:100px;}tr.success{color:black;background-color:#CCFFCC;}
tr.warning{color:black;background-color:#FEE01E;}
tr.error{color:black;background-color:#FFCCCC;}
tr.info{color:white;background-color:#78a1c0}
tr.step{color:white;background:grey}
td { border-top: 1px solid grey; }
</style>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.8.2.min.js"></script>
</head>
<body>
<button id="hideLowLevel">hide low level actions</button>
<button id="showLowLevel">show low level actions</button>
  <ul>
    <#list logs as log>
      <li>STEP: ${log_index + 1}. ${log.command} from ${log.description}</li>
    </#list>
  </ul>

 <table border=1>
    <#list logs as log>
      <tr calss='${log.result}'><td>${log.command}<td>${log.description}
    </#list>
  </table>
</body>
</html> 