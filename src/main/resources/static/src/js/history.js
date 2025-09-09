const historyBtn = document.querySelector(".history");
historyBtn.classList.remove("font-semibold");
historyBtn.classList.add("sidebar-hover","text-gray-800","font-bold");




// updating history date
const currentDate = new Date();
let curDay = currentDate.getDate();
let curMonth = currentDate.getMonth() + 1; // Months are zero-based
let curYear = currentDate.getFullYear();

if(curDay<10) {
  curDay = `0${curDay}`;
}
if(curMonth<10) {
  curMonth = `0${curMonth}`
}
const mainBodyDateContainer = document.querySelector(".main-date-text");
mainBodyDateContainer.innerHTML=`${curDay}-${curMonth}-${curYear}`;







const url = "http://localhost:8080/transaction";  

let transaction_data;
  
function fetchData() {
  return fetch(`${url}`)
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

// Call fetchData to initiate the fetch operation and handle the result
const userGreet = document.querySelector(".user-greet");
const historyListContainer = document.querySelector(".history-list-entries");
fetchData().then(data => {
    userGreet.innerHTML = `Hello , ${data[0].username}`;
  transaction_data = data;
  for(let key in transaction_data) {
   let listEntry = document.createElement("div");
   listEntry.classList.add("history-list-entry","flex","justify-between","px-4","py-3","text-gray-300","text-medium","font-medium","rounded-xl","items-center","hover:font-bold","ease-in-out","duration-300","hover:text-gray-800");
   listEntry.innerHTML = `<div class=history-entry-options-icon"><i class="fa-solid fa-ellipsis-vertical fa-lg"></i></div>
                          <div class="history-entry-date w-28">${transaction_data[key].transaction_date}</div>
                          <div class="history-entry-name w-28">${transaction_data[key].transaction_title}</div>
                          <div class="history-entry-wallet w-28">${transaction_data[key].wallet_name}</div>
                          <div class="history-entry-amount w-28">${transaction_data[key].transaction_amount}</div>
                          <div class="history-entry-check text-gray-800 px-2 py-1 font-semibold rounded-lg w-28 text-center">${transaction_data[key].transaction_category}</div>`;
   historyListContainer.appendChild(listEntry);
}
  
});

const sortSearchButton = document.querySelector(".sort-search-button");

sortSearchButton.addEventListener("click" , function () {
  let fromDate = document.querySelector(".header-from-date").value;
  let toDate = document.querySelector(".header-to-date").value;

const date1 = new Date(fromDate);
const date2 = new Date(toDate);

  fetchData().then(data => {

    const filteredTransactions = data.filter(transaction => {
      const transactionDate = new Date(transaction.transaction_date.replace(/-/g, '/'));
      return transactionDate >= date1 && transactionDate <= date2;
  });

  console.log(filteredTransactions);
  
  // Sort filtered transactions by transaction date
  filteredTransactions.sort((a, b) => {
      const dateA = new Date(a.transaction_date.replace(/-/g, '/'));
      const dateB = new Date(b.transaction_date.replace(/-/g, '/'));
      return dateA - dateB;
  });

  historyListContainer.innerHTML = "";

  for(let key in filteredTransactions) {
    let listEntry = document.createElement("div");
    listEntry.classList.add("history-list-entry","flex","justify-between","px-4","py-3","text-gray-300","text-medium","font-medium","rounded-xl","items-center","hover:font-bold","ease-in-out","duration-300","hover:text-gray-800");
    listEntry.innerHTML = `<div class=history-entry-options-icon"><i class="fa-solid fa-ellipsis-vertical fa-lg"></i></div>
                           <div class="history-entry-date w-28">${transaction_data[key].transaction_date}</div>
                           <div class="history-entry-name w-28">${transaction_data[key].transaction_title}</div>
                           <div class="history-entry-wallet w-28">${transaction_data[key].wallet_name}</div>
                           <div class="history-entry-amount w-28">${transaction_data[key].transaction_amount}</div>
                           <div class="history-entry-check text-gray-800 px-2 py-1 font-semibold rounded-lg w-28 text-center">${transaction_data[key].transaction_category}</div>`;
    historyListContainer.appendChild(listEntry);
 }
  })
})



// console.log(transaction_data[0].transaction_amount);





