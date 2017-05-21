var Game = {};
Game.connect = (function () {
    console.log("connect");
    Game.socket = new WebSocket("ws://" + window.location.hostname + ":" + window.location.port + "/game");

    Game.socket.onopen = function () {
        console.log('Info: WebSocket connection opened.');
    };

    Game.socket.onclose = function () {
        console.log('Info: WebSocket closed.');
    };

    Game.socket.onmessage = function (event) {
        var message = JSON.parse(event.data);

        console.log("Info: onmessage");

        if (message.type === "java.lang.String") {
            console.log(JSON.parse(message.content));
        }
    };

    var resp = {};
    resp.type = "java.lang.String";
    resp.content = JSON.stringify("Hello!");
    console.log(Game.socket);
    Game.socket.send(JSON.stringify(resp));
});

Game.connect();