// getting data 

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
      // console.log(data);
      return data; // Return the data fetched from the server
    })
    .catch(error => {
      console.error('There was a problem with the fetch operation:', error);
    });
}

  

const ctx = document.getElementById('row2-chart');
const ctxContext = ctx.getContext("2d");

fetchData().then(data => {

const uniqueDates = [...new Set(data.map(trans=> trans.transaction_date))];
// Sort dates in descending order
uniqueDates.sort((a, b) => new Date(b) - new Date(a));
// Get the last 7 recent dates
const recentDates = uniqueDates.slice(0, 7);
// Filter transactions corresponding to recent dates
// const recentTransactions = data.filter(trans => recentDates.includes(trans.transaction_date));

const recentDatesAmount = recentDates.map(date => {
  // Filter transactions for the current date
const transactionsForDate = data.filter(trans => trans.transaction_date === date);
  
  // Sum up transaction amounts for the current date
const totalAmountForDate = transactionsForDate.reduce((total, trans) => total + trans.transaction_amount, 0);
  
return totalAmountForDate;
});
recentDates.reverse();
recentDatesAmount.reverse();
// console.log("Recent Dates Amount:", recentDatesAmount);
// console.log("Recent Dates:", recentDates);

var gradient = ctxContext.createLinearGradient(0, 0, 0, ctx.height*1.3);
gradient.addColorStop(0, 'rgba(232,241,103,0.7)');   
gradient.addColorStop(0.3,'rgba(232,241,103,0.5)');
gradient.addColorStop(0.7,'rgba(232,241,103,0.3)');
gradient.addColorStop(1, 'rgba(232,241,103,0.1)');

let mainChart = new Chart(ctx, {
    type: 'line',
    data: {
      labels: recentDates,
      datasets: [{
        label: 'Amount Spent',
        fill:true,
        // fillColor:'#E8F167',
        backgroundColor: gradient,
        data: recentDatesAmount,
        borderWidth: 3,
        borderColor : '#E8F167'
      }]
    },
    options: {
      responsive:true,
      scales: {
        y: {
          beginAtZero: true,
          grid:{
            color:'rgb(75 85 99)'
          },
          ticks:{
            color:'rgb(209 213 219)'
          }
        },
        x:{
          grid:{
            display:false
          },
          ticks:{color:'rgb(209 213 219)'}
        }
      },
      plugins:{legend:{labels:{color:'rgb(209 213 219)'}}},
    }
  });

  const monthBtn = document.querySelector(".row2-h-r-month");
  const weekBtn = document.querySelector(".row2-h-r-day");
  const yearBtn = document.querySelector(".row2-h-r-year");

  // updating data day wise  

  weekBtn.addEventListener("click",function() {
    mainChart.data.datasets[0].data = recentDatesAmount;
    mainChart.data.labels = recentDates;
    mainChart.update();
    monthBtn.classList.remove("text-gray-300");
    monthBtn.classList.add("text-zinc-500");
    weekBtn.classList.add("text-gray-300");
    weekBtn.classList.remove("text-zinc-500");
    yearBtn.classList.remove("text-gray-300");
    yearBtn.classList.add("text-zinc-500");
  })

  // updating graph month wise

  monthBtn.addEventListener("click", function() {
    const uniqueMonths = [...new Set(data.map(trans => {
      const date = new Date(trans.transaction_date);
      return `${date.getFullYear()}-${date.getMonth() + 1}`;
    }))];
    
    // Sort months in descending order
    uniqueMonths.sort((a, b) => {
      const [yearA, monthA] = a.split('-');
      const [yearB, monthB] = b.split('-');
      return new Date(yearB, monthB - 1) - new Date(yearA, monthA - 1);
    });
    
    // Get the last 7 recent months
    const recentMonths = uniqueMonths.slice(0, 7);
    
    // Calculate total amount for each recent month
    const totalAmountForMonth = recentMonths.map(month => {
      const [year, monthNumber] = month.split('-');
      const totalAmountForMonth = data.reduce((total, trans) => {
        const transactionDate = new Date(trans.transaction_date);
        if (transactionDate.getFullYear() == year && transactionDate.getMonth() == monthNumber - 1) {
          return total + trans.transaction_amount;
        } else {
          return total;
        }
      }, 0);
      return totalAmountForMonth;
    });
    recentMonths.reverse();
    totalAmountForMonth.reverse();
    console.log(recentMonths);
    console.log(totalAmountForMonth);

    mainChart.data.datasets[0].data = totalAmountForMonth;
    mainChart.data.labels = recentMonths;
    mainChart.update();

    monthBtn.classList.add("text-gray-300");
    monthBtn.classList.remove("text-zinc-500");
    weekBtn.classList.remove("text-gray-300");
    weekBtn.classList.add("text-zinc-500");
    yearBtn.classList.remove("text-gray-300");
    yearBtn.classList.add("text-zinc-500");
});

// updating data year wise
  yearBtn.addEventListener("click",function() {
  const uniqueYears = [...new Set(data.map(trans => new Date(trans.transaction_date).getFullYear()))];

// Sort years in descending order
uniqueYears.sort((a, b) => b - a);

// Get the last 7 recent years
const recentYears = uniqueYears.slice(0, 7);

// Calculate total amount for each recent year
const totalAmountForYear = recentYears.map(year => {
  const totalAmountForYear = data.reduce((total, trans) => {
    if (new Date(trans.transaction_date).getFullYear() == year) {
      return total + trans.transaction_amount;
    } else {
      return total;
    }
  }, 0);
  return totalAmountForYear;
});

  
  console.log(recentYears);
  console.log(totalAmountForYear);

  recentYears.reverse();
  totalAmountForYear.reverse();

  monthBtn.classList.remove("text-gray-300");
  monthBtn.classList.add("text-zinc-500");
  weekBtn.classList.remove("text-gray-300");
  weekBtn.classList.add("text-zinc-500");
  yearBtn.classList.add("text-gray-300");
  yearBtn.classList.remove("text-zinc-500");

  mainChart.data.datasets[0].data = totalAmountForYear;
  mainChart.data.labels = recentYears;
  mainChart.update();

  })

}
)

