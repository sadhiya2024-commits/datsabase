<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">

    <title>Simple Chatting System</title>

    <style>

        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: linear-gradient(135deg, #667eea, #764ba2);
            height: 100vh;

            display: flex;
            justify-content: center;
            align-items: center;
        }

        .login-box {
            width: 350px;
            padding: 35px;

            background: white;

            border-radius: 15px;

            box-shadow:
                0 10px 30px rgba(0,0,0,0.2);
        }

        h2 {
            text-align: center;
            color: #333;
        }

        input {
            width: 100%;
            padding: 12px;

            margin: 10px 0;

            box-sizing: border-box;

            border: 1px solid #ccc;

            border-radius: 8px;
        }

        button {
            width: 100%;

            padding: 12px;

            margin-top: 15px;

            border: none;

            border-radius: 8px;

            background: #667eea;

            color: white;

            font-size: 16px;

            cursor: pointer;
        }

        button:hover {
            background: #5568d9;
        }

        .error {
            text-align: center;
            color: red;
            margin-bottom: 10px;
        }

    </style>

</head>

<body>

<div class="login-box">

    <h2>Simple Chatting System</h2>

    <%
        String error = request.getParameter("error");

        if (error != null) {
    %>

        <div class="error">
            <%= error %>
        </div>

    <%
        }
    %>

    <form action="login" method="post">

        <select
            name="username"
            required
            style="width:100%; padding:12px; margin:10px 0; box-sizing:border-box; border:1px solid #ccc; border-radius:8px; background:white;">
            <option value="" disabled selected>Select Username</option>
            <option value="Sandhya">Sandhya</option>
            <option value="shivani">shivani</option>
            <option value="rahu">rahu</option>
            <option value="motvani">motvani</option>
            <option value="shariya">shariya</option>
        </select>

        <input
            type="password"
            name="password"
            placeholder="Password (same as username)"
            required>

        <button type="submit">
            Login
        </button>

    </form>

</div>

</body>

</html>