 document.addEventListener('DOMContentLoaded', function() {
        const navButtons = document.querySelectorAll('.nav-button');

        navButtons.forEach(button => {
            button.addEventListener('mouseover', function() {
                this.style.transform = 'translateY(-8px) scale(1.03)';
            });

            button.addEventListener('mouseout', function() {
                this.style.transform = '';
            });

            button.addEventListener('click', function() {
                const ripple = document.createElement('span');
                ripple.style.position = 'absolute';
                ripple.style.top = '0';
                ripple.style.left = '0';
                ripple.style.width = '100%';
                ripple.style.height = '100%';
                ripple.style.background = 'rgba(255, 255, 255, 0.3)';
                ripple.style.borderRadius = '1rem';
                ripple.style.transform = 'scale(0)';
                ripple.style.transition = 'all 0.5s';

                this.appendChild(ripple);

                setTimeout(() => {
                    ripple.style.transform = 'scale(1)';
                    ripple.style.opacity = '0';
                }, 0);

                setTimeout(() => {
                    ripple.remove();
                }, 500);
            });
        });
    });