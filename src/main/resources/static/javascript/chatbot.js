    class AgriConnectChatbot {
        constructor() {
            this.form = document.getElementById("chat-form");
            this.messagesDiv = document.getElementById("messages");
            this.typingIndicator = document.querySelector(".typing-indicator");
            this.userInput = document.getElementById("user-message");
            this.languageSelect = document.getElementById("language-select");
            this.sendBtn = document.querySelector(".send-btn");
            this.statusIndicator = document.getElementById("status-indicator");

            this.isProcessing = false;
            this.currentLanguage = 'en';

            this.initializeEventListeners();
            this.initializeFAQButtons();
        }

        initializeEventListeners() {
            if (this.form) {
                this.form.addEventListener("submit", (e) => this.handleUserSubmit(e));
            }

            if (this.languageSelect) {
                this.languageSelect.addEventListener("change", () => this.handleLanguageChange());
            }

            if (this.userInput) {
                this.userInput.addEventListener("keydown", (e) => {
                    if (e.key === "Enter" && !e.shiftKey) {
                        e.preventDefault();
                        if (!this.isProcessing) {
                            this.handleUserSubmit(e);
                        }
                    }
                });

                this.userInput.addEventListener("input", () => {
                    this.sendBtn.disabled = this.userInput.value.trim().length === 0;
                });
            }
        }

        initializeFAQButtons() {
            const faqButtons = [
                { id: "features-btn", key: "features" },
                { id: "how-to-use-btn", key: "how-to-use" },
                { id: "how-it-helps-btn", key: "how-it-helps" }
            ];

            faqButtons.forEach(({ id, key }) => {
                const btn = document.getElementById(id);
                if (btn) {
                    btn.addEventListener("click", () => this.handleFAQClick(key));
                }
            });
        }

        async handleUserSubmit(event) {
            event.preventDefault();

            const userMessage = this.userInput.value.trim();
            if (!userMessage || this.isProcessing) return;

            this.isProcessing = true;
            this.sendBtn.disabled = true;

            this.displayMessage(userMessage, "user");
            this.userInput.value = "";
            this.showTypingIndicator();

            try {
                const response = await this.fetchBotResponse(userMessage);
                this.hideTypingIndicator();
                this.displayMessage(response, "bot", true);
            } catch (error) {
                console.error("Chat Error:", error);
                this.hideTypingIndicator();

                const fallbackResponse = this.generateFallbackResponse(userMessage);
                this.displayMessage(fallbackResponse, "bot");

                this.showStatusMessage("Connection issue - using offline mode", "error");
            } finally {
                this.isProcessing = false;
                this.sendBtn.disabled = false;
            }
        }

        async fetchBotResponse(userMessage) {
            const contextPrompt = this.buildContextPrompt(userMessage);

            try {
                const apiKeyResponse = await fetch("/api/get-api-key", {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json"
                    }
                });

                if (!apiKeyResponse.ok) {
                    throw new Error("Failed to retrieve API key");
                }

                const apiKey = await apiKeyResponse.text();

                const response = await fetch(`https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=${apiKey}`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        contents: [{
                            parts: [{ text: contextPrompt }]
                        }],
                        generationConfig: {
                            temperature: 0.7,
                            topK: 40,
                            topP: 0.95,
                            maxOutputTokens: 1000
                        },
                        safetySettings: [
                            {
                                category: "HARM_CATEGORY_HARASSMENT",
                                threshold: "BLOCK_MEDIUM_AND_ABOVE"
                            }
                        ]
                    })
                });

                if (!response.ok) {
                    throw new Error(`Gemini API error: ${response.status} - ${response.statusText}`);
                }

                const data = await response.json();

                if (data.candidates && data.candidates[0]?.content?.parts?.[0]?.text) {
                    return this.formatBotResponse(data.candidates[0].content.parts[0].text);
                } else {
                    throw new Error("Invalid response structure from Gemini API");
                }
            } catch (error) {
                console.error("API call failed:", error);
                throw error;
            }
        }

        buildContextPrompt(userMessage) {
            const languageName = this.getLanguageName(this.currentLanguage);

            return `You are AgriConnect, an expert agricultural AI assistant focused exclusively on farming and agriculture.

            IMPORTANT GUIDELINES:
            - Respond only to agriculture-related queries (farming, crops, soil, irrigation, weather, livestock, market prices, etc.)
            - If asked non-agricultural questions, politely redirect to agricultural topics
            - Provide practical, actionable advice for farmers
            - Respond in ${languageName}
            - Keep responses concise but informative (max 200 words)
            - Use simple, farmer-friendly language
            - Include relevant emojis where appropriate

            User Question: ${userMessage}

            Please provide a helpful agricultural response in ${languageName}.`;
        }

        getLanguageName(code) {
            const languages = {
                'en': 'English',
                'hi': 'Hindi'
            };
            return languages[code] || 'English';
        }

        formatBotResponse(text) {
            return text
                .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
                .replace(/\*(.*?)\*/g, '<em>$1</em>')
                .replace(/\n\n/g, '<br><br>')
                .replace(/\n/g, '<br>')
                .replace(/(\d+\.\s)/g, '<br>$1')
                .replace(/•/g, '<br>• ');
        }

        generateFallbackResponse(userMessage) {
            const lowerMessage = userMessage.toLowerCase();

            const agricultureKeywords = {
                'crop': this.currentLanguage === 'hi' ?
                    'फसल संबंधी प्रश्नों के लिए, मैं मिट्टी की जांच, उपयुक्त किस्मों का चयन, और उचित रोपण कार्यक्रम की सलाह देता हूं। आप किस विशिष्ट फसल में रुचि रखते हैं? 🌾' :
                    'For crop-related queries, I recommend checking soil health, selecting appropriate varieties, and following proper planting schedules. Which specific crop are you interested in? 🌾',
                'soil': this.currentLanguage === 'hi' ?
                    'मिट्टी का स्वास्थ्य खेती की सफलता के लिए महत्वपूर्ण है। pH स्तर, पोषक तत्व सामग्री (NPK), और जैविक पदार्थ की जांच करवाएं। क्या आपको विशिष्ट मिट्टी प्रबंधन सुझाव चाहिए? 🌱' :
                    'Soil health is crucial for farming success. Consider testing pH levels, nutrient content (NPK), and organic matter. Would you like specific soil management tips? 🌱',
                'water': this.currentLanguage === 'hi' ?
                    'जल प्रबंधन कृषि के लिए आवश्यक है। ड्रिप सिंचाई, वर्षा जल संचयन, और उचित जल निकासी मुख्य तकनीकें हैं। आपकी वर्तमान सिंचाई विधि क्या है? 💧' :
                    'Water management is essential for agriculture. Drip irrigation, rainwater harvesting, and proper drainage are key techniques. What\'s your current irrigation method? 💧',
                'fertilizer': this.currentLanguage === 'hi' ?
                    'मिट्टी परीक्षण और फसल की आवश्यकताओं के आधार पर उर्वरक चुनें। कंपोस्ट और वर्मीकंपोस्ट जैसे जैविक विकल्प टिकाऊ हैं। विशिष्ट उर्वरक सुझाव चाहिए? 🌿' :
                    'Choose fertilizers based on soil tests and crop requirements. Organic options like compost and vermicompost are sustainable choices. Need specific fertilizer recommendations? 🌿',
                'pest': this.currentLanguage === 'hi' ?
                    'एकीकृत कीट प्रबंधन (IPM) जैविक, सांस्कृतिक और रासायनिक विधियों को मिलाता है। रोकथाम हमेशा इलाज से बेहतर है। आप किन कीट समस्याओं का सामना कर रहे हैं? 🐛' :
                    'Integrated Pest Management (IPM) combines biological, cultural, and chemical methods. Prevention is always better than cure. What pest issues are you facing? 🐛',
                'weather': this.currentLanguage === 'hi' ?
                    'मौसम निगरानी रोपण निर्णयों और फसल सुरक्षा में मदद करती है। स्थानीय मौसम पूर्वानुमान और कृषि सलाह का उपयोग करें। मौसम आधारित खेती के सुझाव चाहिए? 🌤️' :
                    'Weather monitoring helps with planting decisions and crop protection. Use local weather forecasts and agricultural advisories. Need weather-based farming tips? 🌤️'
            };

            for (const [keyword, response] of Object.entries(agricultureKeywords)) {
                if (lowerMessage.includes(keyword)) {
                    return response;
                }
            }

            return this.currentLanguage === 'hi' ?
                'नमस्ते! मैं आपका AgriConnect सहायक हूं, कृषि मार्गदर्शन में विशेषज्ञ। मैं फसलों, मिट्टी, सिंचाई, कीट प्रबंधन और बाजार की जानकारी में मदद कर सकता हूं। कृपया मुझसे खेती से संबंधित कोई भी प्रश्न पूछें! 🚜' :
                'Hello! I\'m your AgriConnect assistant, specialized in agricultural guidance. I can help with crops, soil, irrigation, pest management, and market information. Please ask me any farming-related question! 🚜';
        }

        handleFAQClick(faqKey) {
            const faqResponses = {
                'en': {
                    "features": {
                        question: "What are the key features of AgriConnect?",
                        answer: `<strong>🌟 AgriConnect Features</strong><br><br>
                        <strong>🌱 Smart Crop Recommendations:</strong><br>
                        • AI-powered crop selection based on soil data<br>
                        • Climate and location-specific suggestions<br>
                        • NPK and pH analysis integration<br><br>

                        <strong>💰 Market Price Intelligence:</strong><br>
                        • Real-time crop prices by state/district<br>
                        • Price trend analysis and predictions<br>
                        • Best selling time recommendations<br><br>

                        <strong>📊 Agricultural Insights:</strong><br>
                        • Historical farming data tracking<br>
                        • Weather-based farming advisories<br>
                        • Personalized farming strategies`
                    },
                    "how-to-use": {
                        question: "How do I use AgriConnect effectively?",
                        answer: `<strong>📱 Getting Started with AgriConnect</strong><br><br>
                        <strong>Step 1: Registration</strong><br>
                        • Create your free farmer profile<br>
                        • Add your farm location and details<br><br>

                        <strong>Step 2: Input Farm Data</strong><br>
                        • Enter soil test results (NPK, pH)<br>
                        • Specify your land area and type<br><br>

                        <strong>Step 3: Get Recommendations</strong><br>
                        • Receive crop suggestions<br>
                        • Check market prices<br>
                        • Follow farming calendar<br><br>

                        <em>💡 Pro Tip: Regular data updates improve recommendation accuracy!</em>`
                    },
                    "how-it-helps": {
                        question: "How does AgriConnect help farmers?",
                        answer: `<strong>🎯 How AgriConnect Transforms Farming</strong><br><br>
                        <strong>💸 Increase Profitability</strong><br>
                        • 20-30% higher crop yields<br>
                        • Better market price timing<br>
                        • Reduced input costs<br><br>

                        <strong>🔬 Scientific Farming</strong><br>
                        • Data-driven decisions<br>
                        • Precision agriculture techniques<br>
                        • Risk reduction strategies<br><br>

                        <strong>🌍 Sustainable Agriculture</strong><br>
                        • Eco-friendly farming practices<br>
                        • Resource optimization<br>
                        • Long-term soil health improvement<br><br>

                        <em>Join thousands of successful farmers using AgriConnect! 🚜</em>`
                    }
                },
                'hi': {
                    "features": {
                        question: "AgriConnect की मुख्य विशेषताएं क्या हैं?",
                        answer: `<strong>🌟 AgriConnect विशेषताएं</strong><br><br>
                        <strong>🌱 स्मार्ट फसल सिफारिशें:</strong><br>
                        • मिट्टी डेटा के आधार पर AI-संचालित फसल चयन<br>
                        • जलवायु और स्थान-विशिष्ट सुझाव<br>
                        • NPK और pH विश्लेषण एकीकरण<br><br>

                        <strong>💰 बाजार मूल्य बुद्धिमत्ता:</strong><br>
                        • राज्य/जिले के अनुसार वास्तविक समय फसल कीमतें<br>
                        • मूल्य प्रवृत्ति विश्लेषण और भविष्यवाणियां<br>
                        • सर्वोत्तम बिक्री समय सिफारिशें<br><br>

                        <strong>📊 कृषि अंतर्दृष्टि:</strong><br>
                        • ऐतिहासिक खेती डेटा ट्रैकिंग<br>
                        • मौसम आधारित कृषि सलाह<br>
                        • व्यक्तिगत कृषि रणनीतियां`
                    },
                    "how-to-use": {
                        question: "मैं AgriConnect का प्रभावी रूप से उपयोग कैसे करूं?",
                        answer: `<strong>📱 AgriConnect के साथ शुरुआत</strong><br><br>
                        <strong>चरण 1: पंजीकरण</strong><br>
                        • अपना मुफ्त किसान प्रोफाइल बनाएं<br>
                        • अपने खेत की स्थान और विवरण जोड़ें<br><br>

                        <strong>चरण 2: खेत डेटा इनपुट करें</strong><br>
                        • मिट्टी परीक्षण परिणाम दर्ज करें (NPK, pH)<br>
                        • अपने भूमि क्षेत्र और प्रकार निर्दिष्ट करें<br><br>

                        <strong>चरण 3: सिफारिशें प्राप्त करें</strong><br>
                        • फसल सुझाव प्राप्त करें<br>
                        • बाजार कीमतें जांचें<br>
                        • कृषि कैलेंडर का पालन करें<br><br>

                        <em>💡 सुझाव: नियमित डेटा अपडेट सिफारिश की सटीकता में सुधार करता है!</em>`
                    },
                    "how-it-helps": {
                        question: "AgriConnect किसानों की कैसे मदद करता है?",
                        answer: `<strong>🎯 AgriConnect कैसे खेती को बदलता है</strong><br><br>
                        <strong>💸 लाभप्रदता बढ़ाएं</strong><br>
                        • 20-30% अधिक फसल उत्पादन<br>
                        • बेहतर बाजार मूल्य समय<br>
                        • कम इनपुट लागत<br><br>

                        <strong>🔬 वैज्ञानिक खेती</strong><br>
                        • डेटा-संचालित निर्णय<br>
                        • सटीक कृषि तकनीकें<br>
                        • जोखिम कम करने की रणनीतियां<br><br>

                        <strong>🌍 टिकाऊ कृषि</strong><br>
                        • पर्यावरण-अनुकूल कृषि प्रथाएं<br>
                        • संसाधन अनुकूलन<br>
                        • दीर्घकालिक मिट्टी स्वास्थ्य सुधार<br><br>

                        <em>AgriConnect का उपयोग करने वाले हजारों सफल किसानों से जुड़ें! 🚜</em>`
                    }
                }
            };

            const currentLangData = faqResponses[this.currentLanguage];
            const faq = currentLangData[faqKey];

            if (faq) {
                this.displayMessage(faq.question, "user");
                this.showTypingIndicator();

                setTimeout(() => {
                    this.hideTypingIndicator();
                    this.displayMessage(faq.answer, "bot", true);
                }, 1500);
            }
        }

        handleLanguageChange() {
            this.currentLanguage = this.languageSelect.value;

            const responses = {
                'en': `Language changed to English. How can I assist with your farming needs today?`,
                'hi': `भाषा हिंदी में बदल गई है। आज मैं आपकी कृषि आवश्यकताओं में कैसे सहायता कर सकता हूं?`
            };

            const languageNames = {
                'en': 'English',
                'hi': 'हिंदी'
            };

            const message = responses[this.currentLanguage] || responses['en'];
            const langDisplay = languageNames[this.currentLanguage] || 'English';

            // Update placeholder text
            const placeholderTexts = {
                'en': 'Ask me anything about agriculture...',
                'hi': 'कृषि के बारे में मुझसे कुछ भी पूछें...'
            };
            this.userInput.placeholder = placeholderTexts[this.currentLanguage];

            // Update FAQ button texts
            const faqTexts = {
                'en': {
                    'features-btn': 'Features',
                    'how-to-use-btn': 'How to Use',
                    'how-it-helps-btn': 'How It Helps'
                },
                'hi': {
                    'features-btn': 'विशेषताएं',
                    'how-to-use-btn': 'उपयोग कैसे करें',
                    'how-it-helps-btn': 'कैसे मदद करता है'
                }
            };

            Object.entries(faqTexts[this.currentLanguage]).forEach(([btnId, text]) => {
                const btn = document.getElementById(btnId);
                if (btn) {
                    const icon = btn.querySelector('i');
                    btn.innerHTML = `${icon ? icon.outerHTML + ' ' : ''}${text}`;
                }
            });

            // Update welcome message
            const welcomeMessages = {
                'en': `<strong>🌱 Welcome to AgriConnect!</strong><br>
                       I'm your intelligent agricultural assistant, ready to help with crop recommendations, soil analysis, weather insights, market prices, and farming techniques. How can I assist you today?`,
                'hi': `<strong>🌱 AgriConnect में आपका स्वागत है!</strong><br>
                       मैं आपका बुद्धिमान कृषि सहायक हूं, फसल सिफारिशों, मिट्टी विश्लेषण, मौसम अंतर्दृष्टि, बाजार कीमतों और कृषि तकनीकों में मदद के लिए तैयार हूं। आज मैं आपकी कैसे सहायता कर सकता हूं?`
            };

            // Update the first bot message
            const firstBotMessage = this.messagesDiv.querySelector('.message.bot');
            if (firstBotMessage) {
                firstBotMessage.innerHTML = welcomeMessages[this.currentLanguage];
            }

            this.displayMessage(`Language: ${langDisplay}`, "user");
            this.showTypingIndicator();

            setTimeout(() => {
                this.hideTypingIndicator();
                this.displayMessage(message, "bot");
            }, 800);
        }

        displayMessage(message, sender, isHTML = false) {
            const messageDiv = document.createElement("div");
            messageDiv.classList.add("message", sender);

            if (isHTML) {
                messageDiv.innerHTML = this.sanitizeHTML(message);
            } else {
                messageDiv.textContent = message;
            }

            const typingIndicator = this.messagesDiv.querySelector('.typing-indicator');
            this.messagesDiv.insertBefore(messageDiv, typingIndicator);

            this.scrollToBottom();
        }

        sanitizeHTML(html) {
            const temp = document.createElement('div');
            temp.innerHTML = html;

            const dangerousElements = temp.querySelectorAll('script, object, embed, link, meta, style');
            dangerousElements.forEach(el => el.remove());

            const allElements = temp.querySelectorAll('*');
            allElements.forEach(el => {
                Array.from(el.attributes).forEach(attr => {
                    if (attr.name.startsWith('on') ||
                        (attr.name === 'href' && attr.value.toLowerCase().includes('javascript:'))) {
                        el.removeAttribute(attr.name);
                    }
                });
            });

            return temp.innerHTML;
        }

        showTypingIndicator() {
            this.typingIndicator.style.display = "flex";
            this.scrollToBottom();
        }

        hideTypingIndicator() {
            this.typingIndicator.style.display = "none";
        }

        showStatusMessage(message, type = "info") {
            this.statusIndicator.textContent = message;
            this.statusIndicator.style.display = "block";
            this.statusIndicator.style.background = type === "error" ?
                "rgba(244, 67, 54, 0.9)" : "rgba(76, 175, 80, 0.9)";

            setTimeout(() => {
                this.statusIndicator.style.display = "none";
            }, 3000);
        }

        scrollToBottom() {
            setTimeout(() => {
                this.messagesDiv.scrollTop = this.messagesDiv.scrollHeight;
            }, 100);
        }
    }

    // Initialize the chatbot when DOM is loaded
    document.addEventListener('DOMContentLoaded', () => {
        new AgriConnectChatbot();
    });

    // Handle page visibility for better performance
    document.addEventListener('visibilitychange', () => {
        if (document.hidden) {
            console.log('Page hidden - optimizing performance');
        } else {
            console.log('Page visible - resuming normal operation');
        }
    });
