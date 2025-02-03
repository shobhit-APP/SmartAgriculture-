const form = document.getElementById("chat-form");
const messagesDiv = document.getElementById("message");
const typingIndicator = document.querySelector(".typing-indicator");

// FAQ database

const faqDatabase = {
"Features of AgriConnect / AgriConnect की विशेषताएं": `
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
     `,

     "How to Use AgriConnect / AgriConnect का उपयोग कैसे करें": `
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
     `,

     "How AgriConnect Helps / AgriConnect कैसे मदद करता है": `
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
         </ul>`,
     "English/अंग्रेज़ी": "You have selected English. Now you can ask me anything in English.",
     "Hindi/हिंदी": "आपने हिंदी चुनी है। अब आप मुझसे हिंदी में कुछ भी पूछ सकते हैं।",
     "Bengali/বাংলা": "আপনি বাংলা নির্বাচন করেছেন। এখন আপনি আমাকে বাংলায় যেকোনো কিছু জিজ্ঞাসা করতে পারেন।",
     "Telugu/తెలుగు": "మీరు తెలుగు ఎంచుకున్నారు. ఇప్పుడు మీరు నాతో తెలుగులో ఏదైనా అడగవచ్చు.",
     "Marathi/मराठी": "तुम्ही मराठी निवडले आहे. आता तुम्ही माझ्याकडे मराठीत काहीही विचारू शकता.",
     "Tamil/தமிழ்": "நீங்கள் தமிழ் தேர்ந்தெடுத்துள்ளீர்கள். இப்போது நீங்கள் என்னிடம் தமிழில் எதையும் கேட்கலாம்.",
     "Gujarati/ગુજરાતી": "તમે ગુજરાતી પસંદ કર્યું છે. હવે તમે મને ગુજરાતી માં કંઈપણ પુછો શકો છો.",
     "Kannada/ಕನ್ನಡ": "ನೀವು ಕನ್ನಡವನ್ನು ಆಯ್ಕೆ ಮಾಡಿದ್ದೀರಿ. ಈಗ ನೀವು ನನಗೆ ಕನ್ನಡದಲ್ಲಿ ಏನು ಬೇಕಾದರೂ ಕೇಳಬಹುದು.",
     "Malayalam/മലയാളം": "നീ താനും മലയാളം തിരഞ്ഞെടുക്കും. ഇപ്പോൾ നീ എന്നെ മലയാളത്തിൽ എന്തും ചോദിക്കാം.",
     "Punjabi/ਪੰਜਾਬੀ": "ਤੁਸੀਂ ਪੰਜਾਬੀ ਚੁਣਿਆ ਹੈ। ਹੁਣ ਤੁਸੀਂ ਮੈਨੂੰ ਪੰਜਾਬੀ ਵਿੱਚ ਕੁਝ ਵੀ ਪੁੱਛ ਸਕਦੇ ਹੋ।",
     "Odia/ଓଡ଼ିଆ": "ଆପଣ ଓଡ଼ିଆ ଚୟନ କରିଛନ୍ତି। ଏବେ ଆପଣ ମତେ ଓଡ଼ିଆରେ କିଛିପି ପଚାରି ପାରିବେ।",
     "Assamese/অসমীয়া": "আপুনি অসমীয়া বাচনি কৰিছে। এতিয়া আপুনি মোক অসমীয়াত যিকোনো কথা সুধিব পাৰে।"
  };


// Handle FAQ chip clicks
document.querySelectorAll(".faq-chip").forEach((chip) => {
    chip.addEventListener("click", () => {
        const topic = chip.textContent;
        if (faqDatabase[topic]) {
            displayMessage(topic, "user");
            showTypingIndicator();
            setTimeout(() => {
                displayMessage1(faqDatabase[topic], "bot");
                hideTypingIndicator();
            }, 1000);
        }
    });
});

// Language selection handler remains the same
document.getElementById("Language-select").addEventListener("change", (event) => {
    const selectedLanguage = event.target.options[event.target.selectedIndex].text.trim();
    if (faqDatabase[selectedLanguage]) {
        displayMessage(selectedLanguage, "user");
        showTypingIndicator();
        setTimeout(() => {
            hideTypingIndicator();
            displayMessage(faqDatabase[selectedLanguage], "bot");
        }, 1000);
    }
});

function showTypingIndicator() {
    typingIndicator.style.display = "flex";
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}

function hideTypingIndicator() {
    typingIndicator.style.display = "none";
}

// Updated form submission with API key fetching
form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const userInput = document.getElementById("user-message").value;
    displayMessage(userInput, "user");
    document.getElementById("user-message").value = "";

    showTypingIndicator();

    try {
        // Get API key from your backend
        const apiKeyResponse = await fetch("/api/get-api-key");
        const apiKey = await apiKeyResponse.text();

        // Make request to OpenRouter with required headers
        const response = await fetch("https://openrouter.ai/api/v1/chat/completions", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${apiKey}`,
                "HTTP-Referer": window.location.origin,
                "X-Title": "AgriConnect"
            },
            body: JSON.stringify({
                model: "openai/gpt-3.5-turbo",
                messages: [
                    {
                        role: "system",
                        content: "You are an agricultural expert assistant. Provide helpful advice about farming, crops, and agricultural practices.",
                    },
                    { role: "user", content: userInput }
                ]
            })
        });

        const data = await response.json();
        hideTypingIndicator();

        if (data.choices && data.choices[0].message) {
            displayMessage(data.choices[0].message.content, "bot");
        } else {
            displayMessage("Error: No response from the bot.", "bot");
        }
    } catch (error) {
        console.error("Error:", error);
        hideTypingIndicator();
        displayMessage("Error: Unable to connect to the API.", "bot");
    }
});

function displayMessage1(message, sender) {
    const messageDiv = document.createElement("div");
    messageDiv.classList.add("message", sender);
    messageDiv.innerHTML = message; // For formatted content
    messagesDiv.appendChild(messageDiv);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}

function displayMessage(message, sender) {
    const messageDiv = document.createElement("div");
    messageDiv.classList.add("message", sender);
    messageDiv.textContent = message; // For plain text
    messagesDiv.appendChild(messageDiv);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}
