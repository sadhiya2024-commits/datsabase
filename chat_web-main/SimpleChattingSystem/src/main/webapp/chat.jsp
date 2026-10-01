<%@ page import="javax.servlet.http.HttpSession" %>

<%
    HttpSession chatSession =
            request.getSession(false);

    if (chatSession == null ||
        chatSession.getAttribute("username") == null) {

        response.sendRedirect("login.jsp");
        return;
    }

    String username =
            (String) chatSession.getAttribute("username");
%>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <title>Chat</title>

    <style>

        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f2f2f2;
        }

        .container {
            width: 700px;
            max-width: 95%;

            margin: 40px auto;

            background: white;

            border-radius: 15px;

            overflow: hidden;

            box-shadow:
                0 5px 25px rgba(0,0,0,0.15);
        }

        .header {
            background: #667eea;
            color: white;
            padding: 20px;
        }

        .header h2 {
            margin: 0;
        }

        .receiver {
            padding: 15px;
        }

        .receiver input,
        .receiver select {
            width: 100%;
            padding: 12px;
            box-sizing: border-box;
        }

        #chatBox {
            height: 400px;
            overflow-y: auto;
            padding: 20px;
            background: #fafafa;
        }

        .message {
            max-width: 70%;
            padding: 10px;
            margin-bottom: 12px;
            border-radius: 10px;
        }

        .sent {
            margin-left: auto;
            background: #667eea;
            color: white;
        }

        .received {
            background: #e5e5e5;
            color: black;
        }

        .sender {
            font-weight: bold;
        }

        .time {
            font-size: 11px;
            margin-top: 5px;
        }

        .input-area {
            display: flex;
            gap: 10px;
            padding: 15px;
        }

        #messageInput {
            flex: 1;
            padding: 12px;
        }

        #sendButton {
            padding: 12px 25px;
            background: #667eea;
            color: white;
            border: none;
            border-radius: 8px;
            cursor: pointer;
        }

    </style>

</head>

<body>

<div class="container">

    <div class="header">

        <h2>Simple Chatting System</h2>

        <p>
            Logged in as:
            <strong><%= username %></strong>
        </p>

    </div>


    <div class="receiver">

        <select
            id="receiver"
            style="width:100%; padding:12px; border:1px solid #ccc; border-radius:8px; background:white;">
            <option value="">Select receiver</option>
            <option value="Sandhya">Sandhya</option>
            <option value="shivani">shivani</option>
            <option value="rahu">rahu</option>
            <option value="motvani">motvani</option>
            <option value="shariya">shariya</option>
        </select>

    </div>


    <div id="chatBox">

        <p style="text-align:center;">
            Enter a username to start chatting.
        </p>

    </div>


    <div class="input-area">

        <input
            type="text"
            id="messageInput"
            placeholder="Type your message">

        <button id="sendButton">
            Send
        </button>

    </div>

</div>


<script>

const receiverInput =
    document.getElementById("receiver");

const messageInput =
    document.getElementById("messageInput");

const chatBox =
    document.getElementById("chatBox");

const sendButton =
    document.getElementById("sendButton");


function loadMessages() {

    const receiver =
        receiverInput.value.trim();

    if (receiver === "") {
        return;
    }

    fetch(
        "chat?receiver="
        + encodeURIComponent(receiver)
    )

    .then(response => response.json())

    .then(messages => {

        chatBox.innerHTML = "";

        if (messages.length === 0) {

            chatBox.innerHTML =
                "<p style='text-align:center;'>"
                + "No messages yet."
                + "</p>";

            return;
        }

        messages.forEach(msg => {

            const div =
                document.createElement("div");

            const type =
                msg.sender === "<%= username %>"
                ? "sent"
                : "received";

            div.className =
                "message " + type;

            div.innerHTML =
                "<div class='sender'>"
                + escapeHTML(msg.sender)
                + "</div>"

                + "<div>"
                + escapeHTML(msg.message)
                + "</div>"

                + "<div class='time'>"
                + escapeHTML(msg.time)
                + "</div>";

            chatBox.appendChild(div);

        });

        chatBox.scrollTop =
            chatBox.scrollHeight;

    })

    .catch(error => {

        console.error(error);

    });
}


function sendMessage() {

    const receiver =
        receiverInput.value.trim();

    const message =
        messageInput.value.trim();

    if (receiver === "") {

        alert("Select a receiver username");

        return;
    }

    if (message === "") {

        alert("Enter a message");

        return;
    }

    const data =
        "receiver="
        + encodeURIComponent(receiver)
        + "&message="
        + encodeURIComponent(message);


    fetch("chat", {

        method: "POST",

        headers: {
            "Content-Type":
                "application/x-www-form-urlencoded"
        },

        body: data

    })

    .then(response => response.text())

    .then(result => {

        messageInput.value = "";

        loadMessages();

    })

    .catch(error => {

        console.error(error);

        alert("Message sending failed");

    });
}


sendButton.addEventListener(
    "click",
    sendMessage
);


messageInput.addEventListener(
    "keypress",
    function(event) {

        if (event.key === "Enter") {

            sendMessage();

        }

    }
);


receiverInput.addEventListener(
    "input",
    loadMessages
);


setInterval(
    loadMessages,
    2000
);


function escapeHTML(text) {

    const div =
        document.createElement("div");

    div.textContent = text;

    return div.innerHTML;
}

</script>

</body>

</html>