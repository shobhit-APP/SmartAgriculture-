// File handling functions
function handleFileSelect() {
  const fileInput = document.getElementById("fileInput");
  const previewSection = document.getElementById("previewSection");
  const previewImg = document.getElementById("previewImage");
  const fileName = document.getElementById("fileName");
  const fileSize = document.getElementById("fileSize");
  const fileType = document.getElementById("fileType");
  const analyzeBtn = document.getElementById("analyzeBtn");

  if (fileInput.files && fileInput.files[0]) {
    const file = fileInput.files[0];

    // Validate file type
    const validTypes = ["image/jpeg", "image/png", "image/jpg", "image/jfif", "image/heic"];
    if (!validTypes.includes(file.type)) {
      showError("Invalid file type. Only JPG, PNG, JPEG, JFIF, HEIC files are supported.");
      fileInput.value = "";
      return;
    }

    // Validate file size (10MB limit)
    if (file.size > 10 * 1024 * 1024) {
      showError("File size too large. Please select a file smaller than 10MB.");
      fileInput.value = "";
      return;
    }

    hideError();

    // Display file info
    fileName.textContent = file.name;
    fileSize.textContent = formatFileSize(file.size);
    fileType.textContent = file.type.split("/")[1].toUpperCase();

    // Preview image
    const reader = new FileReader();
    reader.onload = function (e) {
      previewImg.src = e.target.result;
      previewSection.style.display = "block";
      analyzeBtn.disabled = false;
    };
    reader.readAsDataURL(file);
  }
}

function formatFileSize(bytes) {
  if (bytes === 0) return "0 Bytes";
  const k = 1024;
  const sizes = ["Bytes", "KB", "MB", "GB"];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + " " + sizes[i];
}

function showError(message) {
  const errorAlert = document.getElementById("errorAlert");
  errorAlert.querySelector("span").textContent = message;
  errorAlert.style.display = "flex";
  setTimeout(hideError, 5000);
}

function hideError() {
  const errorAlert = document.getElementById("errorAlert");
  if (errorAlert) {
    errorAlert.style.display = "none";
  }
}

function closeResultPopup() {
  const popup = document.getElementById("resultPopup");
  if (popup) {
    popup.classList.remove("show");
    document.body.style.overflow = "auto";
  }
}

function startNewAnalysis() {
  closeResultPopup();
  document.getElementById("fileInput").value = "";
  document.getElementById("previewSection").style.display = "none";
  document.getElementById("analyzeBtn").disabled = true;
  document.getElementById("loadingSection").style.display = "none";
}

// Drag and drop functionality
const uploadArea = document.querySelector(".upload-area");

["dragenter", "dragover", "dragleave", "drop"].forEach((eventName) => {
  uploadArea.addEventListener(eventName, preventDefaults, false);
  document.body.addEventListener(eventName, preventDefaults, false);
});

function preventDefaults(e) {
  e.preventDefault();
  e.stopPropagation();
}

["dragenter", "dragover"].forEach((eventName) => {
  uploadArea.addEventListener(eventName, highlight, false);
});

["dragleave", "drop"].forEach((eventName) => {
  uploadArea.addEventListener(eventName, unhighlight, false);
});

function highlight(e) {
  uploadArea.classList.add("dragover");
}

function unhighlight(e) {
  uploadArea.classList.remove("dragover");
}

uploadArea.addEventListener("drop", handleDrop, false);

function handleDrop(e) {
  const dt = e.dataTransfer;
  const files = dt.files;
  const fileInput = document.getElementById("fileInput");
  fileInput.files = files;
  handleFileSelect();
}

// Form submission
document.getElementById("analysisForm").addEventListener("submit", function () {
  document.getElementById("loadingSection").style.display = "block";
  document.getElementById("analyzeBtn").disabled = true;
  hideError();
});

// Show popup on page load if results exist
document.addEventListener("DOMContentLoaded", function () {
  const popup = document.getElementById("resultPopup");
  if (popup) {
    popup.classList.add("show");
    document.body.style.overflow = "hidden";
    console.log("Popup shown on page load");
  }
});

// Close popup on overlay click
document.getElementById("resultPopup").addEventListener("click", function (e) {
  if (e.target === this) {
    closeResultPopup();
  }
});

// Keyboard navigation
document.addEventListener("keydown", function (e) {
  if (e.key === "Escape") {
    closeResultPopup();
  }
});