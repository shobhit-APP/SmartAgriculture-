      // Clear the history state to prevent navigating back to protected pages
      window.history.pushState(null, null, window.location.href);
      window.addEventListener("popstate", function (event) {
        window.history.pushState(null, null, window.location.href);
      });

      // Redirect countdown timer
      let secondsLeft = 10; // Default value if not provided by server
      const messageElement = document.getElementById("message");
      const originalMessage = messageElement.textContent;

      function updateTimer() {
        if (secondsLeft <= 0) {
          window.location.href = "/api/login";
        } else {
          messageElement.textContent = `[[#{logout.message.base}]] Redirecting to login page in ${secondsLeft} seconds...`;
          secondsLeft--;
          setTimeout(updateTimer, 1000);
        }
      }

      // Start countdown after 3 seconds to allow user to read logout message
      setTimeout(updateTimer, 3000);