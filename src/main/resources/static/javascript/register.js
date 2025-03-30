// Show success alert function
function showSuccessAlert() {
    const alert = document.getElementById('successAlert');
    alert.classList.add('show');

    // Auto hide after 5 seconds
    setTimeout(() => {
        closeAlert();
    }, 5000);
}

// Close alert function
function closeAlert() {
    const alert = document.getElementById('successAlert');
    alert.classList.remove('show');
}

// Form submission handler
document.getElementById('registrationForm').addEventListener('submit', function(e) {
    e.preventDefault();

    // Create FormData object
    const formData = new FormData(this);

    // Create URL-encoded string manually to ensure proper encoding
    const urlEncodedData = new URLSearchParams(formData).toString();

    // Send the data to the server using fetch API
    fetch('/api/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: urlEncodedData
    })
    .then(response => {
        if (response.ok) {
            // Show success message
            showSuccessAlert();

            // Reset form
            this.reset();

            // Optional: Redirect after a delay
            // setTimeout(() => {
            //     window.location.href = '/api/welcome';
            // }, 2000);
        } else {
            // Handle server errors
            console.error('Server returned an error');
            // You could show an error message to the user here
        }
    })
    .catch(error => {
        console.error('Error submitting form:', error);
        // You could show an error message to the user here
    });
});