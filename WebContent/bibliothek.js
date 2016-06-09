function selected(s) {
    var bibliotheken = document.getElementById("menubar").children;
    for (var i = 0; i < bibliotheken.length; i++) {
        bibliotheken[i].style.backgroundColor = "dodgerblue";
    }
    bibliotheken[s].style.backgroundColor = "red";

};