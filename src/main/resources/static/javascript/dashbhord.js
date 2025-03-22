document.addEventListener("DOMContentLoaded", function () {
    // Get elements
    const filterBtn = document.getElementById("filterBtn");
    const filterDropdown = document.getElementById("filterDropdown");
    const applyFilter = document.getElementById("applyFilter");
    const stateFilter = document.getElementById("stateFilter");
    const exportBtn = document.getElementById("exportBtn");
    const showMoreBtn = document.getElementById("showMoreBtn");

    // Toggle Filter Dropdown
    filterBtn.addEventListener("click", function () {
        filterDropdown.classList.toggle("hidden");
    });

    // Apply Filter
    applyFilter.addEventListener("click", function () {
        const selectedState = stateFilter.value.toLowerCase();
        const tableRows = document.querySelectorAll("#marketTableBody tr");

        tableRows.forEach(row => {
            const stateColumn = row.children[0].textContent.toLowerCase();
            if (selectedState === "" || stateColumn === selectedState) {
                row.style.display = "";
            } else {
                row.style.display = "none";
            }
        });

        filterDropdown.classList.add("hidden");
    });

    // Export Table Data to CSV
    exportBtn.addEventListener("click", function () {
        let table = document.getElementById("marketTable");
        let rows = Array.from(table.querySelectorAll("tr"));
        let csvContent = rows.map(row => {
            let columns = Array.from(row.querySelectorAll("td, th"));
            return columns.map(col => col.textContent.trim()).join(",");
        }).join("\n");

        let blob = new Blob([csvContent], { type: "text/csv" });
        let a = document.createElement("a");
        a.href = URL.createObjectURL(blob);
        a.download = "market_prices.csv";
        a.click();
    });

    // Load More Data (Dummy Functionality)
    showMoreBtn.addEventListener("click", function () {
        alert("Loading more data... (You can implement pagination here)");
    });

    // Close dropdown when clicking outside
    document.addEventListener("click", function (event) {
        if (!filterDropdown.contains(event.target) && event.target !== filterBtn) {
            filterDropdown.classList.add("hidden");
        }
    });
});
