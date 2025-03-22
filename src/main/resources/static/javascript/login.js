// Show error message if error parameter exists in URL
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('error')) {
        document.getElementById('error-message').style.display = 'block';
    }

    // Password visibility toggle
    const passwordToggle = document.getElementById('password-toggle');
    const passwordField = document.getElementById('password');

    passwordToggle.addEventListener('click', function() {
        const type = passwordField.getAttribute('type') === 'password' ? 'text' : 'password';
        passwordField.setAttribute('type', type);

        // Toggle icon
        if (type === 'text') {
            passwordToggle.classList.remove('fa-eye');
            passwordToggle.classList.add('fa-eye-slash');
            passwordToggle.classList.add('visible');
        } else {
            passwordToggle.classList.remove('fa-eye-slash');
            passwordToggle.classList.add('fa-eye');
            passwordToggle.classList.remove('visible');
        }
    });

    // Add ripple effect to buttons
    const buttons = document.querySelectorAll('.btn');
    buttons.forEach(btn => {
        btn.addEventListener('click', function(e) {
            let x = e.clientX - e.target.offsetLeft;
            let y = e.clientY - e.target.offsetTop;

            let ripple = document.createElement('span');
            ripple.style.left = `${x}px`;
            ripple.style.top = `${y}px`;

            this.appendChild(ripple);

            setTimeout(function() {
                ripple.remove();
            }, 600);
        });
    });