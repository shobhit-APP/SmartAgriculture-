//document.addEventListener("DOMContentLoaded", function () {
//    // Get elements
//    const filterBtn = document.getElementById("filterBtn");
//    const filterDropdown = document.getElementById("filterDropdown");
//    const applyFilter = document.getElementById("applyFilter");
//    const stateFilter = document.getElementById("stateFilter");
//    const exportBtn = document.getElementById("exportBtn");
//    const showMoreBtn = document.getElementById("showMoreBtn");
//
//    // Toggle Filter Dropdown
//    filterBtn.addEventListener("click", function () {
//        filterDropdown.classList.toggle("hidden");
//    });
//
//    // Apply Filter
//    applyFilter.addEventListener("click", function () {
//        const selectedState = stateFilter.value.toLowerCase();
//        const tableRows = document.querySelectorAll("#marketTableBody tr");
//
//        tableRows.forEach(row => {
//            const stateColumn = row.children[0].textContent.toLowerCase();
//            if (selectedState === "" || stateColumn === selectedState) {
//                row.style.display = "";
//            } else {
//                row.style.display = "none";
//            }
//        });
//
//        filterDropdown.classList.add("hidden");
//    });
//
//    // Export Table Data to CSV
//    exportBtn.addEventListener("click", function () {
//        let table = document.getElementById("marketTable");
//        let rows = Array.from(table.querySelectorAll("tr"));
//        let csvContent = rows.map(row => {
//            let columns = Array.from(row.querySelectorAll("td, th"));
//            return columns.map(col => col.textContent.trim()).join(",");
//        }).join("\n");
//
//        let blob = new Blob([csvContent], { type: "text/csv" });
//        let a = document.createElement("a");
//        a.href = URL.createObjectURL(blob);
//        a.download = "market_prices.csv";
//        a.click();
//    });
//
//    // Load More Data (Dummy Functionality)
//    showMoreBtn.addEventListener("click", function () {
//        alert("Loading more data... (You can implement pagination here)");
//    });
//
//    // Close dropdown when clicking outside
//    document.addEventListener("click", function (event) {
//        if (!filterDropdown.contains(event.target) && event.target !== filterBtn) {
//            filterDropdown.classList.add("hidden");
//        }
//    });
//});
document.addEventListener("DOMContentLoaded", function () {
    // Get elements
    const filterBtn = document.getElementById("filterBtn");
    const filterDropdown = document.getElementById("filterDropdown");
    const applyFilter = document.getElementById("applyFilter");
    const stateFilter = document.getElementById("stateFilter");
    const exportBtn = document.getElementById("exportBtn");
    const showMoreBtn = document.getElementById("showMoreBtn");
    const marketTableBody = document.getElementById("marketTableBody");

    // Toggle Filter Dropdown
    filterBtn.addEventListener("click", function (event) {
        event.stopPropagation();
        filterDropdown.classList.toggle("hidden");
    });

    // Apply Filter
    applyFilter.addEventListener("click", function () {
        const selectedState = stateFilter.value.toLowerCase();
        const tableRows = document.querySelectorAll("#marketTableBody tr");
        let visibleRows = 0;

        tableRows.forEach(row => {
            const stateColumn = row.children[0].textContent.toLowerCase();
            if (selectedState === "" || stateColumn === selectedState) {
                row.style.display = "";
                visibleRows++;
            } else {
                row.style.display = "none";
            }
        });

        // Update UI to reflect filter results
        showMoreBtn.textContent = visibleRows > 0 ? "Show More" : "No Results Found";
        showMoreBtn.disabled = visibleRows === 0;

        filterDropdown.classList.add("hidden");
    });

    // Export Table Data to CSV
    exportBtn.addEventListener("click", function () {
        // Get visible rows only
        let table = document.getElementById("marketTable");
        let headers = Array.from(table.querySelectorAll("thead th")).map(th => th.textContent.trim());
        let visibleRows = Array.from(table.querySelectorAll("tbody tr")).filter(row => row.style.display !== "none");

        // Create CSV content
        let csvContent = [headers.join(",")];

        visibleRows.forEach(row => {
            let columns = Array.from(row.querySelectorAll("td"));
            csvContent.push(columns.map(col => {
                // Handle commas in text by quoting
                let text = col.textContent.trim();
                return text.includes(",") ? `"${text}"` : text;
            }).join(","));
        });

        // Download CSV
        const csvString = csvContent.join("\n");
        const blob = new Blob([csvString], { type: "text/csv;charset=utf-8;" });
        const link = document.createElement("a");
        const url = URL.createObjectURL(blob);

        // Create filename with date
        const date = new Date().toISOString().split('T')[0];
        link.setAttribute("href", url);
        link.setAttribute("download", `agriconnect_market_prices_${date}.csv`);
        link.style.visibility = 'hidden';

        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    });

    // Show More functionality
    let visibleRows = 5; // Initially showing 5 rows
    const rowIncrement = 5; // Show 5 more rows each time

    showMoreBtn.addEventListener("click", function () {
        const tableRows = Array.from(document.querySelectorAll("#marketTableBody tr"));
        const filteredRows = tableRows.filter(row => {
            // Consider both the current filter and the pagination
            const stateColumn = row.children[0].textContent.toLowerCase();
            const selectedState = stateFilter.value.toLowerCase();
            return selectedState === "" || stateColumn === selectedState;
        });

        // Show next batch of rows
        for (let i = visibleRows; i < visibleRows + rowIncrement && i < filteredRows.length; i++) {
            filteredRows[i].style.display = "";
            filteredRows[i].classList.add("slide-up"); // Add animation
        }

        visibleRows += rowIncrement;

        // Update button text/state if we've shown all rows
        if (visibleRows >= filteredRows.length) {
            showMoreBtn.textContent = "All Data Loaded";
            showMoreBtn.disabled = true;
        }
    });

    // Close dropdown when clicking outside
    document.addEventListener("click", function (event) {
        if (!filterDropdown.contains(event.target) && event.target !== filterBtn) {
            filterDropdown.classList.add("hidden");
        }
    });

    // Add responsive table handling for mobile
    const handleResponsiveTable = () => {
        const table = document.getElementById("marketTable");
        if (window.innerWidth < 768) {
            table.classList.add("responsive-table");
        } else {
            table.classList.remove("responsive-table");
        }
    };

    // Initialize responsive table
    handleResponsiveTable();
    window.addEventListener("resize", handleResponsiveTable);
});
