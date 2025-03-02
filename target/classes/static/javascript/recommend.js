// JavaScript file (recommend.js)
document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');

    // Create loading overlay
    const createLoadingOverlay = () => {
        // Create overlay elements
        const overlay = document.createElement('div');
        overlay.className = 'fixed inset-0 bg-black bg-opacity-70 flex items-center justify-center z-50';
        overlay.id = 'loading-overlay';

        const loadingCard = document.createElement('div');
        loadingCard.className = 'bg-white rounded-lg p-8 max-w-lg w-full mx-4 shadow-2xl transform transition-all animate__animated animate__fadeInUp';

        const loadingContent = document.createElement('div');
        loadingContent.className = 'text-center';

        // Create animated farm icon
        const farmIcon = document.createElement('div');
        farmIcon.className = 'mx-auto mb-6';
        farmIcon.innerHTML = `
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100" width="100" height="100">
                <!-- Sun with rotating animation -->
                <circle cx="75" cy="25" r="12" fill="#f1c40f" class="rotating-sun">
                    <animateTransform
                        attributeName="transform"
                        type="rotate"
                        from="0 75 25"
                        to="360 75 25"
                        dur="10s"
                        repeatCount="indefinite"
                    />
                </circle>

                <!-- Growing plant animation -->
                <g class="growing-plant">
                    <!-- Soil -->
                    <path d="M20,80 L80,80 L80,85 L20,85 Z" fill="#8B4513"/>

                    <!-- Plant stem -->
                    <path d="M50,80 L50,60" stroke="#27ae60" stroke-width="3" fill="none">
                        <animate
                            attributeName="d"
                            values="M50,80 L50,80; M50,80 L50,60; M50,80 L50,40"
                            dur="3s"
                            repeatCount="indefinite"
                        />
                    </path>

                    <!-- Leaves -->
                    <path d="M50,70 C60,65 65,70 60,75" stroke="#2ecc71" stroke-width="2" fill="#2ecc71" opacity="0">
                        <animate
                            attributeName="opacity"
                            values="0;0;1"
                            dur="3s"
                            repeatCount="indefinite"
                        />
                    </path>

                    <path d="M50,60 C40,55 35,60 40,65" stroke="#2ecc71" stroke-width="2" fill="#2ecc71" opacity="0">
                        <animate
                            attributeName="opacity"
                            values="0;0;0;1"
                            dur="3s"
                            repeatCount="indefinite"
                        />
                    </path>

                    <!-- Flower -->
                    <circle cx="50" cy="40" r="6" fill="#e74c3c" opacity="0">
                        <animate
                            attributeName="opacity"
                            values="0;0;0;0;1"
                            dur="3s"
                            repeatCount="indefinite"
                        />
                        <animate
                            attributeName="r"
                            values="0;6"
                            dur="1s"
                            begin="2s"
                            fill="freeze"
                        />
                    </circle>
                </g>

                <!-- Rain drops -->
                <g class="rain-drops">
                    <circle cx="35" cy="30" r="2" fill="#3498db">
                        <animate
                            attributeName="cy"
                            values="30;80"
                            dur="1.5s"
                            repeatCount="indefinite"
                        />
                        <animate
                            attributeName="opacity"
                            values="1;0"
                            dur="1.5s"
                            repeatCount="indefinite"
                        />
                    </circle>

                    <circle cx="50" cy="20" r="2" fill="#3498db">
                        <animate
                            attributeName="cy"
                            values="20;80"
                            dur="1.2s"
                            repeatCount="indefinite"
                        />
                        <animate
                            attributeName="opacity"
                            values="1;0"
                            dur="1.2s"
                            repeatCount="indefinite"
                        />
                    </circle>

                    <circle cx="65" cy="40" r="2" fill="#3498db">
                        <animate
                            attributeName="cy"
                            values="40;80"
                            dur="1.8s"
                            repeatCount="indefinite"
                        />
                        <animate
                            attributeName="opacity"
                            values="1;0"
                            dur="1.8s"
                            repeatCount="indefinite"
                        />
                    </circle>
                </g>
            </svg>
        `;

        // Create loading heading
        const loadingHeading = document.createElement('h2');
        loadingHeading.className = 'text-2xl font-bold text-gray-800 mb-6';
        loadingHeading.textContent = 'Analyzing Your Soil Data';

        // Create message container
        const messageContainer = document.createElement('div');
        messageContainer.className = 'message-container relative h-20 overflow-hidden';

        // The rotating messages
        const messages = [
            "Analyzing soil nutrient composition...",
            "Calculating optimal crop matches...",
            "Checking environmental compatibility...",
            "Evaluating growth potential based on your inputs...",
            "Finalizing crop recommendations for your unique conditions..."
        ];

        // Create message elements and add to container
        messages.forEach((message, index) => {
            const messageElement = document.createElement('div');
            messageElement.className = 'message-item';
            messageElement.textContent = message;
            messageContainer.appendChild(messageElement);
        });

        // Create progress bar
        const progressContainer = document.createElement('div');
        progressContainer.className = 'w-full bg-gray-200 rounded-full h-2 my-8';

        const progressBar = document.createElement('div');
        progressBar.className = 'bg-green-500 h-2 rounded-full width-animate';
        progressContainer.appendChild(progressBar);

        // Add all elements to loading card
        loadingContent.appendChild(farmIcon);
        loadingContent.appendChild(loadingHeading);
        loadingContent.appendChild(messageContainer);
        loadingContent.appendChild(progressContainer);

        loadingCard.appendChild(loadingContent);
        overlay.appendChild(loadingCard);

        return overlay;
    };

    // Function to animate the messages
    const animateMessages = () => {
        const messages = document.querySelectorAll('.message-item');
        let currentIndex = 0;

        // Hide all messages initially
        messages.forEach(msg => {
            msg.style.opacity = '0';
            msg.style.position = 'absolute';
            msg.style.top = '0';
            msg.style.left = '0';
            msg.style.right = '0';
        });

        // Show the first message
        if (messages.length > 0) {
            messages[0].style.opacity = '1';
        }

        // Set up interval to rotate messages
        setInterval(() => {
            // Hide current message
            if (messages[currentIndex]) {
                messages[currentIndex].style.opacity = '0';
            }

            // Move to next message
            currentIndex = (currentIndex + 1) % messages.length;

            // Show next message
            if (messages[currentIndex]) {
                messages[currentIndex].style.opacity = '1';
            }
        }, 3000); // Change message every 3 seconds
    };

    // Handle form submission
    form.addEventListener('submit', function(e) {
        // Add loading overlay
        const overlay = createLoadingOverlay();
        document.body.appendChild(overlay);

        // Start message animation
        setTimeout(animateMessages, 100);

        // Allow the form to submit normally
    });
});