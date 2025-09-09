const row3chart = document.getElementById('row3-chart');
const chartSelection = document.querySelector(".row-3-chart-options");

fetchData().then( data => {
const sortedData = data.sort((a, b) => new Date(b.transaction_date) - new Date(a.transaction_date));
const uniqueDates = new Set();
const dates = [];
const frequencies = [];
for (const transaction of sortedData) {
    if (uniqueDates.size === 5) break;
    if (!uniqueDates.has(transaction.transaction_date)) {
        uniqueDates.add(transaction.transaction_date);
        dates.push(transaction.transaction_date);
        frequencies.push(1);
    } else {
        const index = dates.indexOf(transaction.transaction_date);
        frequencies[index]++;
    }
}
dates.reverse();
frequencies.reverse();
// Output the results
console.log("Dates array:", dates);
console.log("Frequencies array:", frequencies);



const row3chartvar = new Chart(row3chart, {
    type: 'bar',
    data: {
      labels: dates,
      datasets: [{
        label: '# of transactions',
        data: frequencies,
        borderWidth: 3,
        borderColor: ['#8BF167','#1DAFA6'],
        fill:true,
        borderRadius:25,
        backgroundColor : ['rgba(139,241,103,0.2)','rgba(29,175,166,0.2)'],
        barPercentage:0.7,
        // barThickness:25
      }]
    },
    options: {
        responsive:true,
      scales: {
        y: {
          beginAtZero: true,
          grid:{color:'rgb(75 85 99)'},
          ticks:{color:'rgb(209 213 219)'}
        },
        x: {
          grid:{display:false},
          ticks:{color:'rgb(209 213 219)'}
        }
      }, animations:{
        tension: {
          duration: 1000,
          easing: 'linear',
          from: 1,
          to: 0,
          loop: true
        }
      }   }
  });


  chartSelection.addEventListener("change",function () {
    if(chartSelection.value == "monthly") {
      const sortedData = data.sort((a, b) => new Date(b.transaction_date) - new Date(a.transaction_date));
      const monthlyTransactions = {};
      for (const transaction of sortedData) {
          const month = new Date(transaction.transaction_date).toLocaleString('default', { month: 'long' });
          if (monthlyTransactions.hasOwnProperty(month)) {
              monthlyTransactions[month]++;
          } else {
              monthlyTransactions[month] = 1;
          }
      }
      const months = Object.keys(monthlyTransactions).sort((a, b) => new Date(b + ' 1, 2024') - new Date(a + ' 1, 2024'));
      const frequenciesMonthly = months.map(month => monthlyTransactions[month]);
      months.reverse();
      frequenciesMonthly.reverse();
      console.log(months);
      console.log(frequencies);
      row3chartvar.data.datasets[0].data=frequenciesMonthly;
      row3chartvar.data.labels=months;
      row3chartvar.update();
    }

    if(chartSelection.value=="weekly") {
      row3chartvar.data.datasets[0].data=frequencies;
      row3chartvar.data.labels=dates;
      row3chartvar.update();
    }

    if(chartSelection.value=="Yearly") {
      const sortedData = data.sort((a, b) => new Date(b.transaction_date) - new Date(a.transaction_date));
      const uniqueDates = new Set();
      const years = [];
      const frequenciesYearly = [];
      for (const transaction of sortedData) {
          const year = new Date(transaction.transaction_date).getFullYear();
          if (!uniqueDates.has(year)) {
              uniqueDates.add(year);
              years.push(year);
              frequenciesYearly.push(1);
          } else {
              const index = years.indexOf(year);
              frequenciesYearly[index]++;
          }
      }
      years.reverse();
      frequenciesYearly.reverse();

      row3chartvar.data.datasets[0].data=frequenciesYearly;
      row3chartvar.data.labels=years;
      row3chartvar.update();
    }
  })
});
  
  
  


  