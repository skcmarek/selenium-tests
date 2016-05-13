<html>
<head>
  <title>TEST REPORT</title>
<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
<script   src="https://code.jquery.com/jquery-2.2.3.min.js"   integrity="sha256-a23g1Nt4dtEYOj7bR+vTu7+T8VP13humZFBJNIYoEJo="   crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
</head>
<body>
<button id="hideLowLevel">hide low level actions</button>
<button id="showLowLevel">show low level actions</button>
  <ol>
    <#list tests as test>
      <li>${test.name}</li>
    </#list>
  </ol>

<#list tests as test>
 <table class="table table-bordered">
    <thead>
         <tr><th>${test.name}<th></tr>
    </thead>
    <tbody>
        <#list test.steps as log>
          <tr class='${log.result}'><td>${log.command}<td>${log.description}
        </#list>
    </tbody>
  </table>
</#list>
</body>
</html> 