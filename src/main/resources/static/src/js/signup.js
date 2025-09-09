const submitBtn = document.querySelector(".login100-form-btn");

submitBtn.addEventListener("click" , function() {
    const url = "http://localhost:8080/signup";
    let username = document.querySelector(".username-input").value;
    let password = document.querySelector(".password-input").value;
    let confirmPassword = document.querySelector(".confirm-password-input").value;
    let budget = document.querySelector(".budget-input").value;
    
    let wallet_name = username + "_wallet";

    const headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('username',username);
    headers.append('password',password);
    headers.append('budget',budget);
    headers.append('wallet_name', wallet_name);

    fetch(url, {
        method: 'POST',
        headers: headers,
        // body: JSON.stringify(requestBody)
    })
    .then(response => {
        console.log('Response Status:', response.status);
    })
    .catch(error => {
        console.error('Error occurred:', error);
    });

})