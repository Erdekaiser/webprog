var request;

function initKataloge(){
    request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.readyState == 4 && request.status == 200) {

            var katalogliste = request.responseText.split(/, /);
            var kataloge =document.getElementById("menubar");
            var katalog;
            for (var i = 0; i < katalogliste.length; i++) {
            	katalog = document.createElement("div");
            	if(i==0){
            		katalog.className= "Kataloge active"
            	}else{
            		katalog.className ="Kataloge";
            	}
            	katalog.Id =  katalogliste[i]
            	katalog.innerHTML=katalogliste[i];
            	
            	kataloge.appendChild(katalog)
//                katalogname = katalogname + '<div class="Kataloge" id="'+katalogliste[i]+'">' + katalogliste[i] + '</div>';
            	
            }

        	kataloge.addEventListener('click', katalogLaden, false)
//            document.getElementById("menubar").innerHTML = katalogname;

//            for (var i = 0; i < katalogliste; i++) {
//                document.getElementById("menubar").getElementsByTagName("div")[i].addEventListener('click', katalogLaden, false);
//            }
        }
    };

    request.open("POST", "KatalogServlet", true);
    request.send();
}
