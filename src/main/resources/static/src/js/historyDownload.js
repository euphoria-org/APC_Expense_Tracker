const pdfBtn = document.querySelector(".pdf-btn");
// const excelBtn = document.querySelector(".excel-btn");

// Define function to generate PDF using pdf-lib
async function generatePdf(data) {
  const { PDFDocument, rgb } = PDFLib;
  const pdfDoc = await PDFDocument.create();
  const page = pdfDoc.addPage();

  let y = page.getHeight() - 40;
  const fontSize = 12;

  data.forEach((transaction, index) => {
    page.drawText(`Transaction ${index + 1}:`, { x: 50, y, size: fontSize });
    y -= fontSize + 5;
    Object.entries(transaction).forEach(([key, value]) => {
      page.drawText(`${key}: ${value}`, { x: 50, y, size: fontSize });
      y -= fontSize + 5;
    });
    y -= 10; // Space between transactions
  });

  const pdfBytes = await pdfDoc.save();
  return pdfBytes;
}

// Event listener for PDF button click
pdfBtn.addEventListener("click", function() {
  // Fetch data and then generate PDF
  fetchData().then(data => {
    generatePdf(data).then(pdfBytes => {
      const blob = new Blob([pdfBytes], { type: 'application/pdf' });
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'transactions.pdf';
      a.click();
      URL.revokeObjectURL(url);
    });
  });
});

// The rest of your code remains the same






















const excelBtn = document.querySelector(".excel-btn");

      function exportToExcel(data) {
        const worksheet = XLSX.utils.json_to_sheet(data);
        const workbook = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(workbook, worksheet, "Transactions");
        const excelBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
        saveExcelFile(excelBuffer, 'transactions.xlsx');
      }
  
      // Function to save Excel file
      function saveExcelFile(buffer, fileName) {
        const blob = new Blob([buffer], { type: 'application/octet-stream' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = fileName;
        a.click();
        URL.revokeObjectURL(url);
      }

    

    excelBtn.addEventListener("click" , function() {
        fetchData().then(data => {
            exportToExcel(data);
        })
    })
