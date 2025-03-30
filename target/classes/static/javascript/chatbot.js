const form = document.getElementById("chat-form");
const messagesDiv = document.getElementById("messages");
const typingIndicator = document.querySelector(".typing-indicator");
const userInput = document.getElementById("user-message");
const languageSelect = document.getElementById("language-select");
const featuresBtn = document.getElementById("features-btn");
const howToUseBtn = document.getElementById("how-to-use-btn");
const howItHelpsBtn = document.getElementById("how-it-helps-btn");

// FAQ Database - Agriculture Content Only
const faqDatabase = {
    "features": {
        question: "Features of AgriConnect / AgriConnect की विशेषताएं",
        answer: `
            <strong>AgriConnect is your ultimate agricultural companion, designed to revolutionize farming with cutting-edge technology! 🚜</strong><br><br>
            <strong>🌱 Crop Recommendations:</strong> Our advanced AI analyzes multiple critical parameters to suggest the <strong>PERFECT</strong> crop for your land:
            <ul>
                <li>Soil Nutrients: Nitrogen (N), Phosphorus (P), Potassium (K)</li>
                <li>Environmental Factors: Temperature, Humidity, Rainfall</li>
                <li>Precise Location-Based Insights</li>
                <li>Soil pH Analysis</li>
            </ul>
            <strong>💰 Crop Price Prediction:</strong> Maximize Your Earnings! Instantly discover the most profitable selling strategies:
            <ul>
                <li>Real-time market price tracking</li>
                <li>State and district-specific pricing</li>
                <li>Minimum and maximum price suggestions</li>
                <li>Live market updates to optimize your sales</li>
            </ul>
            <strong>📊 Historical Data Tracking:</strong> Smart Farming, Smarter Decisions
            <ul>
                <li>Store and analyze past predictions</li>
                <li>Comprehensive agricultural performance insights</li>
                <li>Data-driven planning for future seasons</li>
            </ul>
            <strong>Transform your farming experience with AgriConnect - Where Technology Meets Agriculture! 🌍</strong>
        `
    },
    "how-to-use": {
        question: "How to Use AgriConnect / AgriConnect का उपयोग कैसे करें",
        answer: `
            Getting started is as easy as 1-2-3!
            <ol>
                <li>Quick Registration
                    <ul>
                        <li>Create your FREE account in minutes</li>
                        <li>Secure, user-friendly interface</li>
                        <li>No technical skills required!</li>
                    </ul>
                </li>
                <li>Explore Powerful Features
                    <ul>
                        <li>Navigate intuitive dashboard</li>
                        <li>Select 'Crop Recommendation' or 'Price Prediction'</li>
                        <li>Input your farm's unique details</li>
                    </ul>
                </li>
                <li>Receive Instant Insights
                    <ul>
                        <li>Get personalized agricultural recommendations</li>
                        <li>Review data-driven suggestions</li>
                        <li>Make informed farming decisions</li>
                    </ul>
                </li>
            </ol>
            <em>Pro Tip: Use historical tracking to continuously improve your strategy! 💡</em>
        `
    },
    "how-it-helps": {
        question: "How AgriConnect Helps / AgriConnect कैसे मदद करता है",
        answer: `
            We're not just an app, we're your agricultural partner!
            <strong>💸 Boost Your Profitability</strong>
            <ul>
                <li>Increase crop yields by 30%</li>
                <li>Optimize resource allocation</li>
                <li>Reduce farming uncertainties</li>
            </ul>
            <strong>🔍 Precision Agriculture</strong>
            <ul>
                <li>Data-driven crop selection</li>
                <li>Real-time market insights</li>
                <li>Personalized farming strategies</li>
            </ul>
            <strong>🌍 Empowering Rural Innovation</strong>
            <ul>
                <li>Supporting small and marginal farmers</li>
                <li>Bridging technology and traditional farming</li>
                <li>Creating sustainable agricultural ecosystems</li>
            </ul>
        `
    }
};

// Language translations
const languageResponses = {
    "en": "You have selected English. Now you can ask me anything about agriculture in English.",
    "hi": "आपने हिंदी चुनी है। अब आप मुझसे कृषि के बारे में हिंदी में कुछ भी पूछ सकते हैं।",
    "bn": "আপনি বাংলা নির্বাচন করেছেন। এখন আপনি আমাকে কৃষি সম্পর্কে বাংলায় যেকোনো কিছু জিজ্ঞাসা করতে পারেন।",
    "te": "మీరు తెలుగు ఎంచుకున్నారు. ఇప్పుడు మీరు నాతో వ్యవసాయం గురించి తెలుగులో ఏదైనా అడగవచ్చు.",
    "mr": "तुम्ही मराठी निवडले आहे. आता तुम्ही माझ्याकडे शेतीविषयी मराठीत काहीही विचारू शकता.",
    "ta": "நீங்கள் தமிழ் தேர்ந்தெடுத்துள்ளீர்கள். இப்போது நீங்கள் என்னிடம் விவசாயம் பற்றி தமிழில் எதையும் கேட்கலாம்.",
    "gu": "તમે ગુજરાતી પસંદ કર્યું છે. હવે તમે મને ખેતી વિશે ગુજરાતી માં કંઈપણ પૂછી શકો છો.",
    "kn": "ನೀವು ಕನ್ನಡವನ್ನು ಆಯ್ಕೆ ಮಾಡಿದ್ದೀರಿ. ಈಗ ನೀವು ನನಗೆ ಕೃಷಿ ಬಗ್ಗೆ ಕನ್ನಡದಲ್ಲಿ ಏನು ಬೇಕಾದರೂ ಕೇಳಬಹುದು.",
    "ml": "നിങ്ങൾ മലയാളം തിരഞ്ഞെടുത്തു. ഇപ്പോൾ നിങ്ങൾക്ക് കൃഷിയെക്കുറിച്ച് മലയാളത്തിൽ എന്നോട് എന്തും ചോദിക്കാം.",
    "pa": "ਤੁਸੀਂ ਪੰਜਾਬੀ ਚੁਣਿਆ ਹੈ। ਹੁਣ ਤੁਸੀਂ ਮੈਨੂੰ ਖੇਤੀਬਾੜੀ ਬਾਰੇ ਪੰਜਾਬੀ ਵਿੱਚ ਕੁਝ ਵੀ ਪੁੱਛ ਸਕਦੇ ਹੋ।",
    "or": "ଆପଣ ଓଡ଼ିଆ ଚୟନ କରିଛନ୍ତି। ଏବେ ଆପଣ ମତେ କୃଷି ବିଷୟରେ ଓଡ଼ିଆରେ କିଛିପି ପଚାରି ପାରିବେ।",
    "as": "আপুনি অসমীয়া বাচনি কৰিছে। এতিয়া আপুনি মোক কৃষি সম্পৰ্কে অসমীয়াত যিকোনো কথা সুধিব পাৰে।"
};

// Agricultural responses database (fallback for when API fails)
const agricultureResponses = {
    "fertilizer": "For optimal crop growth, consider using organic fertilizers rich in nitrogen, phosphorus, and potassium. The recommended NPK ratio varies by crop type - cereals need more nitrogen, while fruiting plants benefit from phosphorus.",
    "irrigation": "Efficient irrigation methods include drip irrigation, sprinkler systems, and flood irrigation. Drip irrigation can save up to 60% water compared to conventional methods while improving yield by 90%.",
    "seeds": "Always choose certified high-yielding variety (HYV) seeds from reliable sources. Consider disease-resistant varieties appropriate for your local climate and soil conditions.",
    "pest": "Implement Integrated Pest Management (IPM) by combining biological controls, crop rotation, and judicious use of pesticides. Monitor pest populations regularly to catch infestations early.",
    "crop rotation": "Rotate crops from different families to break pest cycles, improve soil health, and maximize nutrient usage. A typical rotation might include legumes to fix nitrogen followed by nitrogen-hungry crops.",
    "soil health": "Maintain soil health through regular testing, appropriate amendments, cover cropping, and minimizing tillage. Healthy soil should have 5% organic matter and a pH between 6.0-7.0 for most crops.",
    "sustainable": "Sustainable farming practices include organic farming, permaculture, agroforestry, and conservation agriculture. These approaches reduce environmental impact while maintaining productivity.",
    "market prices": "Check the AgriConnect app for real-time market prices. Current national trends show increasing demand for organic produce with price premiums of 20-30% over conventional crops.",
    "weather": "Our app provides 7-day agricultural weather forecasts to help with planning farm operations. Always prepare contingency plans for extreme weather events."
};

// Agriculture-related keywords for response matching
const agricultureKeywords = [
    "crop", "farm", "soil", "seed", "harvest", "fertilizer", "irrigation",
    "pest", "organic", "weather", "yield", "rotation", "sustainable",
    "market", "price", "rain", "season", "plant", "grow", "agriculture",
    "फसल", "खेती", "मिट्टी", "बीज", "उपज", "खाद", "सिंचाई", "कीट", "जैविक",
    "मौसम", "पैदावार", "चक्र", "टिकाऊ", "बाजार", "कीमत", "बारिश", "मौसम",
    "पौधा", "बढ़ना", "कृषि"
];

// Initialize the chat interface
function initializeChatInterface() {
    // Check if DOM elements exist
    if (!form || !messagesDiv || !typingIndicator || !userInput || !languageSelect) {
        console.error("Required DOM elements not found. Check your HTML structure.");
        return false;
    }

    // Add event listeners
    form.addEventListener("submit", handleUserSubmit);
    languageSelect.addEventListener("change", handleLanguageChange);

    if (featuresBtn) featuresBtn.addEventListener("click", () => handleFAQClick("features"));
    if (howToUseBtn) howToUseBtn.addEventListener("click", () => handleFAQClick("how-to-use"));
    if (howItHelpsBtn) howItHelpsBtn.addEventListener("click", () => handleFAQClick("how-it-helps"));

    // Display welcome message
    const welcomeMessage = "Welcome to AgriConnect! I'm your agricultural assistant. How can I help with your farming needs today?";
    displayMessage(welcomeMessage, "bot");

    return true;
}

// Form submission handler
async function handleUserSubmit(event) {
    event.preventDefault();
    const userMessage = userInput.value.trim();

    if (!userMessage) return;

    displayMessage(userMessage, "user");
    userInput.value = "";

    showTypingIndicator();

    try {
        // Call Gemini API
        const response = await fetchGeminiResponse(userMessage);
        hideTypingIndicator();
        displayMessage(response, "bot");
    } catch (error) {
        console.error("API Error:", error);
        // Fallback to local response generation if API fails
        const botResponse = generateLocalAgricultureResponse(userMessage);
        hideTypingIndicator();
        displayMessage(botResponse, "bot");

        // Optionally show error notification to user
        displayErrorMessage("I'm having trouble connecting to our knowledge base. I've provided a basic response, but for more detailed information, please try again later.");
    }
}

// Display error message
function displayErrorMessage(message) {
    const errorDiv = document.createElement("div");
    errorDiv.classList.add("message", "error");
    errorDiv.textContent = message;
    messagesDiv.appendChild(errorDiv);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;

    // Auto-remove error message after 5 seconds
    setTimeout(() => {
        errorDiv.classList.add("fade-out");
        setTimeout(() => {
            messagesDiv.removeChild(errorDiv);
        }, 500);
    }, 5000);
}

// Fetch response from Gemini API
async function fetchGeminiResponse(userMessage) {
    // Get selected language
    const selectedLang = languageSelect.value;

    // Create a context-aware prompt for agriculture focus
    const contextPrompt = `You are an agricultural assistant for the AgriConnect app.
    Respond in ${selectedLang === 'en' ? 'English' : languageSelect.options[languageSelect.selectedIndex].text}.
    Focus only on agricultural topics like farming, crops, soil, irrigation, weather, and market prices.
    Keep responses concise, practical, and farmer-friendly.
    User query: ${userMessage}`;

    try {
        const apiKeyResponse = await fetch("/api/get-api-key");
        const apiKey = await apiKeyResponse.text();

        const response = await fetch(`https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=${apiKey}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                contents: [{
                    parts: [{ text: contextPrompt }]
                }]
            })
        });

        if (!response.ok) {
            throw new Error(`API responded with status: ${response.status}`);
        }

        const data = await response.json();

        if (data.candidates && data.candidates[0]?.content?.parts?.[0]?.text) {
            return data.candidates[0].content.parts[0].text;
        } else {
            throw new Error("Invalid API response structure");
        }
    } catch (error) {
        console.error("API Error details:", error);
        throw error; // Re-throw to trigger fallback
    }
}

// Generate local agriculture-focused responses (fallback when API fails)
function generateLocalAgricultureResponse(userMessage) {
    const lowerMessage = userMessage.toLowerCase();

    // Check for exact matches in agriculture responses
    for (const [key, response] of Object.entries(agricultureResponses)) {
        if (lowerMessage.includes(key)) {
            return response;
        }
    }

    // More flexible keyword matching with scoring
    let bestMatch = null;
    let highestScore = 0;

    for (const keyword of agricultureKeywords) {
        const keywordLower = keyword.toLowerCase();
        if (lowerMessage.includes(keywordLower)) {
            // Calculate a simple relevance score based on keyword frequency and position
            const count = (lowerMessage.match(new RegExp(keywordLower, 'g')) || []).length;
            const position = lowerMessage.indexOf(keywordLower);
            const score = count * 10 + (100 - position); // Higher score for earlier positions

            if (score > highestScore) {
                highestScore = score;
                bestMatch = keyword;
            }
        }
    }

    if (bestMatch) {
        return "I understand you're asking about " + bestMatch + ". As an agricultural assistant, I can help with crop recommendations, soil health, irrigation techniques, and market prices for your farming needs. Could you provide more specific details about your query?";
    }

    // Default agricultural response
    return "I'm your agricultural assistant and can help with crop recommendations, soil analysis, weather forecasts, market prices, and farming techniques. Please ask me any specific questions about agriculture or farming.";
}

// Handle FAQ button clicks
function handleFAQClick(faqKey) {
    if (!faqDatabase[faqKey]) {
        console.error(`FAQ key '${faqKey}' not found in database`);
        return;
    }

    const faq = faqDatabase[faqKey];
    displayMessage(faq.question, "user");
    showTypingIndicator();

    setTimeout(() => {
        hideTypingIndicator();
        displayMessage(faq.answer, "bot", true);
    }, 1000);
}

// Handle language selection
function handleLanguageChange() {
    const selectedLang = languageSelect.value;
    if (!languageResponses[selectedLang]) {
        console.error(`Language '${selectedLang}' not found in responses`);
        return;
    }

    const langResponse = languageResponses[selectedLang];
    displayMessage("Changed language to: " + languageSelect.options[languageSelect.selectedIndex].text, "user");
    showTypingIndicator();

    setTimeout(() => {
        hideTypingIndicator();
        displayMessage(langResponse, "bot");
    }, 1000);
}

// Display message in the chat
function displayMessage(message, sender, isHTML = false) {
    const messageDiv = document.createElement("div");
    messageDiv.classList.add("message", sender);

    if (isHTML) {
        // Sanitize HTML before inserting
        const sanitizedHTML = sanitizeHTML(message);
        messageDiv.innerHTML = sanitizedHTML;
    } else {
        messageDiv.textContent = message;
    }

    messagesDiv.appendChild(messageDiv);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}

// Basic HTML sanitization function
function sanitizeHTML(html) {
    // This is a simple sanitization. In production, use a library like DOMPurify
    const temp = document.createElement('div');
    temp.innerHTML = html;

    // Remove potentially dangerous elements and attributes
    const scripts = temp.querySelectorAll('script');
    scripts.forEach(script => script.remove());

    const elements = temp.querySelectorAll('*');
    elements.forEach(el => {
        // Remove event handlers and javascript: URLs
        for (const attr of el.attributes) {
            if (attr.name.startsWith('on') ||
                (attr.name === 'href' && attr.value.toLowerCase().startsWith('javascript:'))) {
                el.removeAttribute(attr.name);
            }
        }
    });

    return temp.innerHTML;
}

// Show typing indicator
function showTypingIndicator() {
    typingIndicator.style.display = "flex";
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}

// Hide typing indicator
function hideTypingIndicator() {
    typingIndicator.style.display = "none";
}

// Initialize the chat interface when the DOM is fully loaded
document.addEventListener('DOMContentLoaded', initializeChatInterface);