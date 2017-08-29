function showdbs(){
    //make a rest API call
    var xhttp = new XMLHttpRequest();
    xhttp.open("GET", "/dataservice/database", false);
    xhttp.send();

    //parse the string from "Buffer(admin, db1, local, myblog)"
    var start_pos = xhttp.responseText.indexOf('(') + 1;
    var end_pos = xhttp.responseText.indexOf(')',start_pos);
    var dbArray = xhttp.responseText.substring(start_pos,end_pos)

    var database = dbArray.split(",")

    //create a drop down bar
    var select = document.getElementById('dbtable');
    var option;
    for (var i = 0; i < database.length; i++) {
        option = document.createElement('option');
        option.text = database[i];
        select.add(option);
    }
}

function getSelectedDB(){
    var doc = document.getElementById("dbtable");
    var selectedDB = doc.options[doc.selectedIndex].text;
    getCollections(selectedDB)
}

function getCollections(selectedDB){
    $( "#result" ).load(window.location.href + " #result" ); //hide before update
    var url = "/dataservice/database/"+selectedDB
    $.getJSON(url,function(collection) {
        getMetadata(selectedDB, collection.toString())
    });
}

 function getMetadata(selectedDB, collection){
     var partsOfStr = collection.split(',');
     for (var i = 0; i < partsOfStr.length; i++) {
         url = "/dataservice/database/metadata/" + selectedDB + "/" + partsOfStr[i]
         $.getJSON(url, function (metadata) {
             parseMetadata(selectedDB, metadata)
         });
     }
 }

function parseMetadata(selectedDB, metadata) {
    $('#result').show();
        for (var i=0; i < 5; i++) {
            var row = $('<tr/>');
            var url = "/dataservice/database/getDoc/" + selectedDB + "/" + metadata.collection + "/" + "0/500";
            row.append($('<td><a href=' + url + '>' + metadata.collection + '</td>'));
            row.append($('<td>' + metadata.documentCount + '</td>'));
            row.append($('<td>' + formatBytes(metadata.totalDocSize) + '</td>'));
            row.append($('<td>' + formatBytes(metadata.avgDocSize) + '</td>'));
            row.append($('<td>' + metadata.numIndex + '</td>'));
        }
    $('#result').append(row);

}

function formatBytes(bytes,decimals) {
    if(bytes == 0) return '0 Bytes';
    var k = 1000,
        dm = decimals || 2,
        sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
        i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
}



//call getDocument REST API and display in HTML
function getMongoDocuments(){
    $('#table').show();
    var url = "/dataservice/database/getDoc/db1/students/0/500"
    $.getJSON(url,function(collectionDocumentList) {
        parseDocuments(collectionDocumentList)
    });
}

//parse JSON Documents to be displayed in HTML
function parseDocuments(documentArr){

    var numDocs = documentArr.length;
    for (var i = 0; i < numDocs ; i++) {
        if(documentArr[i].name && documentArr[i].age && documentArr[i].gender){
            var row = $('<tr/>');
            row.append($('<td><input type="checkbox" name="Check" value='+documentArr[i]._id.$oid+' ></input></td>'));
            row.append($('<td>' + documentArr[i].name + '</td>'));
            row.append($('<td>' + documentArr[i].gender + '</td>'));
            row.append($('<td>' + documentArr[i].age + '</td>'));
            //txt += "<tr>"+'<td><input type="checkbox" name="Check" value='+documentArr[i]._id.$oid+' ></input></td>'+ "<td>"+documentArr[i].name+"</td><td>"+documentArr[i].gender+"</td><td>"+documentArr[i].age+"</td></tr>";
            $("#table").append(row);

        }
        //'+documentArr[i]._id.$oid+'

    }
}

function deleteDocument(databaseName, collectionName, docID){
    $( "#table" ).load(window.location.href + " #table" ); //hide
    console.log(docID);
    url = "/dataservice/database/delete/" + databaseName +"/"+ collectionName +"/" +docID
    var xhttp = new XMLHttpRequest();
    xhttp.open("GET", url, false);
    xhttp.send();
    console.log(xhttp.responseText);
    getMongoDocuments();
}


