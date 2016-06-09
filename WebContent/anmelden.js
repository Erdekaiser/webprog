var user = new Array();
var gameongoing = false;

function anmelden() {
    updateusers(prompt("Bitte Username eingeben"));
}

function updateusers(name) {
    if (toString(name).trim="") {
        
        alert("Bitte Username angeben");
        return;
    }
    if (user.length>6) {

        alert("maximal 6 spieler erlaubt");
        return;
    }
    for (var i = 0; i < user.length; i++) {
        if (user[i] == name) {
            alert("User bereits vorhanden");
            return;
        }
    }
    user[user.length] = name;

    printusers();
    if (user.length >= 2 & gameongoing == false) {
        document.getElementById("starten").style.display = "inline-block";
    } 
}

function printusers() {
    var spieler;
    document.getElementById("User").innerHTML = "";
    for (var i = 0; i < user.length; i++) {
        spieler = document.createElement("div");
        spieler.className = "spieler"
        spieler.innerHTML = "   <div class=\"name\">" + user[i] + "</div><div class=\"punkte\">0</div>"
        spieler.addEventListener("click", swap ,false);
        document.getElementById("User").appendChild(spieler)
        spieler=null;

    }
    
}
function starten() {
    gameongoing = true;
    document.getElementById("anmelden").style.display = "none";
    document.getElementById("quiz").style.backgroundColor = "white";
    document.getElementById("starten").style.display = "none";
    document.getElementById("Frage").style.display = "block";
    var a = document.getElementsByClassName("Antwort");
    for (var i = 0; i < a.length; i++) {
        a[i].style.display = "inline-block";
    }
}

function swap(event) {
    var e =event.target;
    var name = e.innerHTML;
    var next ;
    d = 0;
    for (var i = 0; i < user.length; i++) {
        if (user[i] == name) {
            next = user[0];
            user[0] = user[i];
            user[i] = next;

            printusers();
            return;
            //d = i;
            //break;
        }
        
    }/*
    if (d==0){
        return;
    }
    next=user[0]
    user[0]=user[d];
    for (var i = 0; i < d; i++) {
        next=user[i+1]

        user[0] = user[i];
        user[i] = ;
        printusers();
        return;
    }*/

}