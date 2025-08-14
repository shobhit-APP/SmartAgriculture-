
document.addEventListener('DOMContentLoaded', function() {
    // Create crop icon SVG
    const resultDiv = document.querySelector('.result');
    const cropName = document.querySelector('.result p').textContent.trim();

    // Create SVG element for plant icon
    const createPlantIcon = () => {
        const svgNS = "http://www.w3.org/2000/svg";
        const svg = document.createElementNS(svgNS, "svg");
        svg.setAttribute("viewBox", "0 0 100 100");
        svg.setAttribute("class", "crop-icon plant-animation");

        // Create plant stem
        const stem = document.createElementNS(svgNS, "path");
        stem.setAttribute("d", "M50,90 L50,40");
        stem.setAttribute("stroke", "#27ae60");
        stem.setAttribute("stroke-width", "4");
        stem.setAttribute("fill", "none");

        // Create leaves
        const leaf1 = document.createElementNS(svgNS, "path");
        leaf1.setAttribute("d", "M50,70 C60,60 70,65 65,75 C75,65 60,55 50,65");
        leaf1.setAttribute("fill", "#2ecc71");

        const leaf2 = document.createElementNS(svgNS, "path");
        leaf2.setAttribute("d", "M50,55 C40,45 30,50 35,60 C25,50 40,40 50,50");
        leaf2.setAttribute("fill", "#2ecc71");

        // Create flower/fruit
        const flower = document.createElementNS(svgNS, "circle");
        flower.setAttribute("cx", "50");
        flower.setAttribute("cy", "30");
        flower.setAttribute("r", "12");
        flower.setAttribute("fill", "#f1c40f");

        // Add all elements to SVG
        svg.appendChild(stem);
        svg.appendChild(leaf1);
        svg.appendChild(leaf2);
        svg.appendChild(flower);

        return svg;
    };

    // Insert plant icon before the heading
    const plantIcon = createPlantIcon();
    resultDiv.insertBefore(plantIcon, resultDiv.firstChild);

    // Add confetti effect
    function createConfetti() {
        const colors = ['#2ecc71', '#3498db', '#f1c40f', '#e74c3c', '#9b59b6'];
        const confettiCount = 100;
        const container = document.body;

        for (let i = 0; i < confettiCount; i++) {
            const confetti = document.createElement('div');

            // Style the confetti pieces
            confetti.style.position = 'fixed';
            confetti.style.width = Math.random() * 10 + 5 + 'px';
            confetti.style.height = Math.random() * 5 + 3 + 'px';
            confetti.style.backgroundColor = colors[Math.floor(Math.random() * colors.length)];
            confetti.style.opacity = Math.random() + 0.5;
            confetti.style.pointerEvents = 'none';
            confetti.style.zIndex = '1000';

            // Position confetti
            confetti.style.left = Math.random() * 100 + 'vw';
            confetti.style.top = -20 + 'px';

            // Add animation
            confetti.style.animation = `fall ${Math.random() * 3 + 2}s linear forwards`;

            // Add confetti to body
            container.appendChild(confetti);

            // Remove after animation completes
            setTimeout(() => {
                confetti.remove();
            }, 5000);
        }
    }

    // Add keyframe animation for confetti
    const styleSheet = document.createElement('style');
    styleSheet.textContent = `
        @keyframes fall {
            0% {
                transform: translateY(0) rotate(0deg);
            }
            100% {
                transform: translateY(100vh) rotate(360deg);
            }
        }
    `;
    document.head.appendChild(styleSheet);

    // Trigger confetti after a short delay
    setTimeout(createConfetti, 500);

    // Add click interaction to the result box
    resultDiv.addEventListener('click', function() {
        this.classList.add('result-pulse');
        setTimeout(() => {
            this.classList.remove('result-pulse');
        }, 500);
    });

    // Add back button hover sound
    const backBtn = document.querySelector('.btn');
    backBtn.addEventListener('mouseenter', function() {
        const hoverSound = new Audio();
        hoverSound.src = 'data:audio/mp3;base64,SUQzBAAAAAAAI1RTU0UAAAAPAAADTGF2ZjU4Ljc2LjEwMAAAAAAAAAAAAAAA//tAwAAAAAAAAAAAAAAAAAAAAAAASW5mbwAAAA8AAAASAAAeMwAUFBQUFCIiIiIiIjAwMDAwMD4+Pj4+PklJSUlJSVdXV1dXV2VlZWVlZXNzc3Nzc4GBgYGBgY+Pj4+Pj52dnZ2dnaurq6urq7i4uLi4uMbGxsbGxtTU1NTU1OHh4eHh4e/v7+/v7/////////////8AAAAATGF2YzU4LjEzAAAAAAAAAAAAAAAAJAYBKgAAPAAAAB4zjpxR';
        hoverSound.volume = 0.2;
        hoverSound.play().catch(e => {
            // Handle autoplay restrictions silently
        });
    });

    // Add success check mark animation for recommendation
    const addCheckMark = () => {
        const checkContainer = document.createElement('div');
        checkContainer.style.position = 'absolute';
        checkContainer.style.top = '10px';
        checkContainer.style.right = '10px';
        checkContainer.style.width = '30px';
        checkContainer.style.height = '30px';
        checkContainer.style.borderRadius = '50%';
        checkContainer.style.background = '#2ecc71';
        checkContainer.style.display = 'flex';
        checkContainer.style.justifyContent = 'center';
        checkContainer.style.alignItems = 'center';
        checkContainer.style.opacity = '0';
        checkContainer.style.transform = 'scale(0)';
        checkContainer.style.animation = 'popIn 0.5s 1s forwards';

        const check = document.createElement('div');
        check.style.width = '15px';
        check.style.height = '8px';
        check.style.borderLeft = '3px solid white';
        check.style.borderBottom = '3px solid white';
        check.style.transform = 'rotate(-45deg) translate(1px, -2px)';

        checkContainer.appendChild(check);
        resultDiv.appendChild(checkContainer);

        // Add animation keyframes
        const styleSheet = document.createElement('style');
        styleSheet.textContent = `
            @keyframes popIn {
                0% { opacity: 0; transform: scale(0); }
                70% { opacity: 1; transform: scale(1.2); }
                100% { opacity: 1; transform: scale(1); }
            }

            @keyframes resultPulse {
                0% { transform: scale(1); }
                50% { transform: scale(1.03); }
                100% { transform: scale(1); }
            }

            .result-pulse {
                animation: resultPulse 0.5s;
            }
        `;
        document.head.appendChild(styleSheet);
    };

    // Add the check mark
    addCheckMark();

    // Type effect for the crop result
    const typeEffect = () => {
        const cropElement = document.querySelector('.result p');
        const cropText = cropElement.textContent;
        cropElement.textContent = '';
        cropElement.style.opacity = '1';
        cropElement.style.transform = 'translateY(0)';

        let i = 0;
        const typing = setInterval(() => {
            if (i < cropText.length) {
                cropElement.textContent += cropText.charAt(i);
                i++;
            } else {
                clearInterval(typing);

                // Add a subtle highlight effect after typing completes
                cropElement.style.textShadow = '0 0 10px rgba(46, 204, 113, 0.5)';
            }
        }, 100);
    };

    // Start typing effect after a delay
    setTimeout(typeEffect, 1000);
});