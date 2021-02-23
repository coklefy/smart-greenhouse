/*
 * Queste funzioni ricevono i dati in formato json dai servlet, per poi andare ad aggiornare i dati nell'html. 
 * 
 */


//function for read umidity 
function readFromHerdd(){
        var xhr = new XMLHttpRequest();
        console.log("entry js function");
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                var data = xhr.responseText;
                var obj=JSON.parse(data);
                $("#umidityTable").html("");
                var tr="";
                for(var i =0 ; i <obj.length; i++){
                    tr+="<tr>";
                    tr+="<td>" + "Read in"+"</td>";
                    tr+="<td>" + obj[i].data + "</td>";
                    tr+="<td>" + obj[i].name+ "</td>";
                    tr+="<td>" + obj[i].valore+ "</td>";
                    tr+="</tr>";

                }
                $("#umidityTable").append(tr);
                console.log(data);
            }
        };
        xhr.open('GET', 'greenhouse', true);
        xhr.send(null);
}


//funtion for read pump status, for example when pump is open, is close and when server send message for start irrigation
function readPumpStatus(){
        var xhr = new XMLHttpRequest();
        console.log("entry js pump function");
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                var data = xhr.responseText;
                var obj=JSON.parse(data);
                $("#umidityTable").html("");
                var tr="";
                for(var i =0 ; i <obj.length; i++){
                    tr+="<tr>";
                    tr+="<td>" + obj[i].data + "</td>";
                    tr+="<td>" + obj[i].info+ "</td>";
                    tr+="</tr>";

                }
                $("#pumpTable").append(tr);
                console.log(data);
            }
        };
        xhr.open('GET', 'ViewPumpLog', true);
        xhr.send(null);
}

// when server start, server configuration is saved in configuration table.
function readServerConfig(){
            var xhr = new XMLHttpRequest();
        console.log("entry js pump function");
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                var data = xhr.responseText;
                var obj=JSON.parse(data);
                $("#serviceconftable").html("");
                var tr="";
                for(var i =0 ; i <obj.length; i++){
                    tr+="<tr>";
                    tr+="<td>" + obj[i].conf + "</td>";
                    tr+="<td>" + obj[i].port+ "</td>";
                    tr+="</tr>";

                }
                $("#serviceconftable").append(tr);
                console.log(data);
            }
        };
        xhr.open('GET', 'readConf', true);
        xhr.send(null);
}


//irrigation have 2 status: MANUAL MODE and AUTOMATIC MODE.
function readmode(){
                var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                var data = xhr.responseText;
                var obj=JSON.parse(data);
                var tr=obj[i].info;
                document.getElementById('modality').innerHTML = tr;
                console.log(data);
            }
        };
        xhr.open('GET', 'Readmode', true);
        xhr.send(null);
}




























