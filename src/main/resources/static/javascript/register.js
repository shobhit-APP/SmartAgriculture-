document.addEventListener("DOMContentLoaded", function () {
    // Show success alert function
    function showSuccessAlert(message) {
        const alert = document.getElementById('successAlert');
        if (!alert) return; // Prevent errors if the element is missing

        alert.innerText = message;
        alert.style.display = 'block';
        alert.classList.add('show');

        // Auto-hide after 5 seconds
        setTimeout(() => closeAlert(), 5000);
    }

    // Close alert function
    function closeAlert() {
        const alert = document.getElementById('successAlert');
        if (!alert) return;
        alert.classList.remove('show');
        alert.style.display = 'none';
    }

    // Show error messages function
    function showErrorMessages(errors) {
        const errorContainer = document.getElementById('errorMessages');
        if (!errorContainer) return;

        errorContainer.innerHTML = ''; // Clear previous errors

        Object.entries(errors).forEach(([field, message]) => {
            const errorItem = document.createElement('div');
            errorItem.classList.add('error-message');
            errorItem.innerText = `${field}: ${message}`;
            errorContainer.appendChild(errorItem);
        });

        errorContainer.style.display = 'block'; // Show error container
    }

    // Form submission handler
    document.getElementById('registrationForm')?.addEventListener('submit', function (e) {
        e.preventDefault();

        const formData = new FormData(this);
        const urlEncodedData = new URLSearchParams(formData).toString();

        fetch('/api/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: urlEncodedData
        })
        .then(response => {
            // If response is NOT ok (400 or 500), throw error
            if (!response.ok) {
                return response.json().then(errData => {
                    throw { status: response.status, ...errData };
                });
            }
            return response.json();
        })
        .then(data => {
            if (data.status === 'success') {
                showSuccessAlert(data.message);
                e.target.reset(); // Reset form
            }
        })
        .catch(error => {
            console.error('Error submitting form:', error);

            if (error.status === 400) {
                // Handle Validation Errors
                showErrorMessages(error.errors);
            } else if (error.status === 500) {
                // Handle Server Error
                alert("Internal Server Error: " + (error.message || "Something went wrong."));
            } else {
                alert("Unexpected error: " + error.message);
            }
        });
    });
});
