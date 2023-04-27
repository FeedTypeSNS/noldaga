fetch("/chat")
        .then((res) =>res.json())
        .then(()=>{
            localStorage.setItem("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6IuyYgeyImCIsImlhdCI6MTY4MjQ5NTI1MSwiZXhwIjoxNjg1MDg3MjUxfQ.3gEiYXQKKSoT8FWBTDQIPJ7aVqJK4ZczpwW4wfBMGLg");
            alert("채팅 테스트");
            this.props.history.push("/Chat");
        })
