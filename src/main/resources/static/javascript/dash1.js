    // Button functionality
    const filterBtn = document.getElementById('filterBtn');
    const filterDropdown = document.getElementById('filterDropdown');
    const showMoreBtn = document.getElementById('showMoreBtn');
    const exportBtn = document.getElementById('exportBtn');
    const rows = document.querySelectorAll('tbody tr');
    let visibleRows = 5;

    // Filter toggle
    filterBtn.addEventListener('click', () => {
        filterDropdown.style.display = filterDropdown.style.display === 'block' ? 'none' : 'block';
    });

    // Show More functionality
    showMoreBtn.addEventListener('click', () => {
        visibleRows += 5;
        rows.forEach((row, index) => {
            if (index < visibleRows) {
                row.style.display = '';
                row.classList.add('slide-up');
            }
        });
        if (visibleRows >= rows.length) {
            showMoreBtn.style.display = 'none';
        }
    });

    // Export to CSV
    exportBtn.addEventListener('click', () => {
        const visibleRowsArray = Array.from(rows).filter(row => row.style.display !== 'none');
        const csv = [];

        // Header
        const headers = Array.from(document.querySelectorAll('th')).map(th => th.textContent.trim());
        csv.push(headers.join(','));

        // Visible rows
        visibleRowsArray.forEach(row => {
            const cols = Array.from(row.querySelectorAll('td')).map(td =>
                `"${td.textContent.trim().replace(/"/g, '""')}"`);
            csv.push(cols.join(','));
        });

        const csvBlob = new Blob([csv.join('\n')], { type: 'text/csv;charset=utf-8;' });
        const url = URL.createObjectURL(csvBlob);
        const link = document.createElement('a');
        link.href = url;
        link.download = 'crop_recommendations_' + new Date().toISOString().slice(0,10) + '.csv';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        URL.revokeObjectURL(url);
    });

    // Initial setup
    if (visibleRows >= rows.length) {
        showMoreBtn.style.display = 'none';
    }

    // Close filter dropdown when clicking outside
    document.addEventListener('click', (e) => {
        if (!filterBtn.contains(e.target) && !filterDropdown.contains(e.target)) {
            filterDropdown.style.display = 'none';
        }
    });