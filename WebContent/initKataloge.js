var request;

function initKataloge(){
    request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if (request.readyState == 4 && request.status == 200) {

            var katalogliste = request.responseText.split(/, /);
            var katalogname = " ";

            for (var i = 0; i < katalogliste.length; i++) {
                katalogname = katalogname + '<div id="katalog">' + katalogliste[i] + '</div>';
            }

            document.getElementById("menubar").innerHTML = katalogname;

            for (var i = 0; i < katalogliste; i++) {
                document.getElementById("menubar").getElementsByTagName("div")[i].addEventListener('click', katalogLaden, false);
            }
        }
    };

    request.open("POST", "KatalogServlet", true);
    request.send();
}