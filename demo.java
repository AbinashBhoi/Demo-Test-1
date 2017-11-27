// Prints a Document Designer Document. Returns "error" or "base64doc".
response = dict("string");
/* ************************************** */
// PRINT TEMPLATE
url = siteUrl+"/rest/v1/documentGenerator";
headers = dict("string");
put(headers, "Authorization", userAuth );
put(headers, "Accept", "application/json");
put(headers, "Content-Type", "application/json");
// add json data
jsonObj = json();
jsonput(jsonObj, "processVarname", processVarName);
jsonput(jsonObj, "templateName", templateName);
jsonput(jsonObj, "transactionId", transactionId);
jsonput(jsonObj, "languageCode", templateLang);
jsonput(jsonObj, "outputFormat", outputFormat);
// call rest method
xlsxResponse = urldata( url, "POST", headers, jsontostr(jsonObj) );
/* ************************************** */
// CHECK OUTPUT, UPDATE STATUS
statusCode = get(xlsxResponse, "Status-Code");
printStatus = "";
if( statusCode <> "204" ){
printStatus = "Print FAILED: " + templateName + "\n" +
"Error: " + get(xlsxResponse, "Error-Message");
put(response, "error", printStatus);
return response;
/* ************************************** */
}else{
printStatus = "Print Completed: " + templateName + "\n";
}
/* ************************************** */
// GET PRINT OUTPUT
url = get(xlsxResponse, "Location");
headers = dict("string");
put(headers, "Authorization", userAuth);
put(headers, "Accept", "application/json");
// get print output call
xlsxResponse = urldata( url, "GET", headers );
mb = get(xlsxResponse, "Message-Body");
if(len(mb) > 0){
jmb = json(mb);
printOutput = jsonget(jmb , "document" );
put(response, "base64doc", printOutput);
}else{
put(response, "error", "Document Output was empty.");
}
print("PRINT COMPLETE");
print("");
return response;