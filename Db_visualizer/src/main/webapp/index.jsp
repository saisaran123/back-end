<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Form</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
        integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg=="
        crossorigin="anonymous" referrerpolicy="no-referrer">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Ubuntu', sans-serif;
            display: flex;
            height: 100vh;
            background: url("https://4kwallpapers.com/images/wallpapers/dark-background-abstract-background-network-3d-background-7680x4320-8324.png") no-repeat center center/cover;
        }

        .left-half {
            flex: 1;
            background: rgba(255, 255, 255, 0.9);
        }

        .right-half {
            flex: 1;
            display: flex;
            justify-content: center;
            align-items: center;
            background: rgba(0, 0, 0, 0.1);
        }

        .login-container {
            background-color: rgba(0, 0, 0, 0.3);
            padding: 4em;
            border-radius: 20px;
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
            width: 95%;
            max-width: 700px;
            animation: fadeIn 0.8s ease-in-out;
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateX(50%);
            }

            to {
                opacity: 1;
                transform: translateX(0);
            }
        }

        h1 {
            text-align: center;
            margin-bottom: 2em;
            color: #fff;
            font-size: 2.5em;
        }

        .form-group {
            position: relative;
            margin-bottom: 2em;
        }

        .form-group i {
            position: absolute;
            top: 50%;
            left: 1em;
            transform: translateY(-50%);
            color: rgba(255, 255, 255, 0.7);
            font-size: 1.2em;
        }

        .form-group input {
            width: 100%;
            padding: 1em 1.5em 1em 3em;
            background: rgba(255, 255, 255, 0.1);
            border: 1px solid rgba(255, 255, 255, 0.2);
            border-radius: 30px;
            color: #fff;
            font-size: 1.2em;
            outline: none;
            transition: border-color 0.3s;
        }

        .form-group input:focus {
            border-color: #6a11cb;
        }

        .options {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin: 2em;
            color: rgba(255, 255, 255, 0.8);
            font-size: 1em;
        }

        .btn {
            width: 100%;
            padding: 1em;
            background-color: #6a11cb;
            color: #fff;
            font-size: 1.2em;
            font-weight: bold;
            border: none;
            border-radius: 30px;
            cursor: pointer;
            transition: background-color 0.3s, transform 0.2s;
        }

        .btn:hover {
            background-color: #2575fc;
            transform: translateY(-4px);
        }

        .register-link {
            text-align: center;
            margin-top: 2em;
            color: rgba(255, 255, 255, 0.8);
            font-size: 1.1em;
        }

        .register-link a {
            color: #6a11cb;
            font-weight: bold;
            text-decoration: none;
        }

        .register-link a:hover {
            color: #fff;
        }
    </style>
</head>

<body>
    <div class="left-half">
        <!-- You can add additional content or leave it blank -->
    </div>Forgot
    <div class="right-half">
        <div class="login-container">
            <h1>Login</h1>
            <form method="post">
                <div class="form-group">
                    <i class="fa-solid fa-user"></i>
                    <input type="text" name="username" placeholder="Username" autocomplete="off" required>
                </div>
                <div class="form-group">
                    <i class="fa-solid fa-lock"></i>
                    <input type="password" name="password" placeholder="Password" autocomplete="off" required>
                </div>
                <div class="options">
                    <label>
                        <input type="checkbox"> Remember me
                    </label>
                    <a href="#">Forgot Password?</a>
                </div>
                <button type="submit" class="btn" id="log">Login</button>
            </form>
            <div class="register-link">
                <p>Don't have an account? <a href="/register">Register here</a></p>
            </div>
        </div>
    </div>
    
</body>
<script>
    let lbtn = document.querySelector("#log");
    lbtn.addEventListener("click",()=>{
        fetch("login").then(res =>{
            console.log(res);
        })
       })
</script>
</html>
