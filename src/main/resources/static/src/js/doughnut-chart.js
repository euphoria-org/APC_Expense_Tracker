const doughnutChart = document.querySelector("#bottom-doughnut-chart");


fetchData().then(data => {

  console.log(data);
  let categories = {};
    // Loop through transactions to calculate total amount spent on each category
    data.forEach(transaction => {
        const category = transaction.transaction_category;
        const amount = parseFloat(transaction.transaction_amount);

        // If category doesn't exist in categories object, create it with initial amount
        if (!categories[category]) {
            categories[category] = amount;
        } else {
            // If category already exists, add amount to its total
            categories[category] += amount;
        }
    });
    // Extract category names and corresponding amounts into separate arrays
    const categoryNamesAlltime = Object.keys(categories);
    const categoryAmountsAlltime = categoryNamesAlltime.map(category => categories[category]);


const dnChartvar = new Chart(doughnutChart, {
  type: "doughnut",
  data: {
    labels: categoryNamesAlltime,
    datasets: [{
      data: categoryAmountsAlltime,
      borderWidth:3,
      borderColor: ['rgba(232,241,103,1)','rgba(103,241,181,1)','rgba(103,241,112,1)','rgba(241,99,163,1)','rgba(139,241,103,1)','rgba(29,175,166,1)'],
      backgroundColor : ['rgba(232,241,103,0.2)','rgba(103,241,181,0.2)','rgba(103,241,112,0.2)','rgba(241,99,163,0.2)','rgba(139,241,103,0.2)','rgba(29,175,166,0.2)'],  
    }]
  },
  options: {
    plugins: {
      legend: {
        display: true,
        position: 'bottom',
        labels: {
          boxWidth: 20,
          padding: 20
        }
      }
    },
    cutout:65,
    title: {
      display: true,
    }
  }
});

  const dnChartSelection = document.querySelector(".m-b-r-bottom-chart-selection");
  dnChartSelection.addEventListener("change" , function() {

    if(dnChartSelection.value == "all time" ) {
      dnChartvar.data.datasets[0].data =categoryAmountsAlltime;
      dnChartvar.data.labels = categoryNamesAlltime;
      dnChartvar.update();
    }

    if(dnChartSelection.value == "this month" ) {
      const currentDate = new Date();
      const currentYear = currentDate.getFullYear();
      const currentMonth = currentDate.getMonth() + 1; // Months are zero-indexed, so we add 1
      
      // Calculate the start and end dates of the current month
      const startDate = `${currentYear}-${currentMonth.toString().padStart(2, '0')}-01`;
      const nextMonth = currentMonth === 12 ? 1 : currentMonth + 1;
      const nextMonthYear = currentMonth === 12 ? currentYear + 1 : currentYear;
      const endDate = `${nextMonthYear}-${nextMonth.toString().padStart(2, '0')}-01`;
      
      // Filter transactions that occurred within the current month
      const currentMonthTransactions = data.filter(transaction => {
          const transactionDate = new Date(transaction.transaction_date);
          return transactionDate >= new Date(startDate) && transactionDate < new Date(endDate);
      });
      
      // Categorize transactions within the current month
      const categories = {};
      currentMonthTransactions.forEach(transaction => {
          const category = transaction.transaction_category;
          const amount = parseFloat(transaction.transaction_amount);
      
          // If category doesn't exist in categories object, create it with initial amount
          if (!categories[category]) {
              categories[category] = amount;
          } else {
              // If category already exists, add amount to its total
              categories[category] += amount;
          }
      });
      
      // Extract category names and corresponding amounts into separate arrays
      const categoryNamesMonthly = Object.keys(categories);
      const categoryAmountsMonthly = categoryNamesMonthly.map(category => categories[category]);
      // console.log(categoryNames);
      // console.log(categoryAmounts);
      
      dnChartvar.data.datasets[0].data =categoryAmountsMonthly;
      dnChartvar.data.labels = categoryNamesMonthly;
      dnChartvar.update();
      
    }
    
    if(dnChartSelection.value == "this year") {
                  // Assuming currentDate is the current date in the format 'YYYY-MM-DD'
const currentDate = new Date();
const currentYear = currentDate.getFullYear();

// Filter transactions that occurred within the current year
const currentYearTransactions = data.filter(transaction => {
    const transactionDate = new Date(transaction.transaction_date);
    return transactionDate.getFullYear() === currentYear;
});

// Categorize transactions within the current year
const categories = {};
currentYearTransactions.forEach(transaction => {
    const category = transaction.transaction_category;
    const amount = parseFloat(transaction.transaction_amount);

    // If category doesn't exist in categories object, create it with initial amount
    if (!categories[category]) {
        categories[category] = amount;
    } else {
        // If category already exists, add amount to its total
        categories[category] += amount;
    }
});

// Extract category names and corresponding amounts into separate arrays
const categoryNames = Object.keys(categories);
const categoryAmounts = categoryNames.map(category => categories[category]);
console.log(categoryNames);
console.log(categoryAmounts);


dnChartvar.data.datasets[0].data = categoryAmounts;
dnChartvar.data.labels = categoryNames;
dnChartvar.update();
}
      

})
})



