const settingsBtn = document.querySelector(".settings");
settingsBtn.classList.remove("font-semibold");
settingsBtn.classList.add("sidebar-hover","text-gray-800","font-bold");



async function getWallets() {
    return fetch("http://localhost:8080/getWallets")
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response.json();
    })
    .then(data => {
      // Do whatever you want with the JSON data
      console.log(data);
      return data; // Return the data fetched from the server
    })
    .catch(error => {
      console.error('There was a problem with the fetch operation:', error);
    });
}

const changeBudgetBtn = document.querySelector(".m-b-c-heading-button");




changeBudgetBtn.addEventListener("click",function() {
    getWallets().then(data => {

    const headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('username',data[0].username);
    headers.append('newBudget',document.querySelector(".change-budget-input").value)
        fetch("http://localhost:8080/changeBudget",{
        method: 'POST',
        headers: headers,
        // body: JSON.stringify(transactionData)
        })
    })
})
