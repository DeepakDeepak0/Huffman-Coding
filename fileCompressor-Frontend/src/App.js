import React, { useState } from "react";
import { motion } from "framer-motion";

function App() {
  const [file, setFile] = useState(null);
  const [darkMode, setDarkMode] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const handleCompress = async () => {
    if (!file) return alert("Please select a file to compress.");

    setIsLoading(true);
    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await fetch("http://localhost:8080/api/files/compress", {
        method: "POST",
        body: formData,
      });

      if (response.ok) {
        const blob = await response.blob();
        const downloadUrl = window.URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = downloadUrl;
        link.download = "compressed.txt"; // human-readable format
        link.click();
        alert("File compressed and downloaded successfully.");
      } else {
        alert("Compression failed.");
      }
    } catch (err) {
      alert("Error compressing file!");
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className={`${darkMode ? "dark" : ""}`}>
      <div className="min-h-screen bg-gray-300 dark:bg-gray-900 transition duration-300 text-gray-900 dark:text-white flex flex-col items-center justify-center p-6">
        <div className="h-64 w-64">
          <img src="/Boy.png" alt="Illustration" />
        </div>

        <motion.h1
          style={{ textShadow: "1px 1px 24px white" }}
          className="text-3xl md:text-4xl font-bold mb-6 text-center "
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 1 }}
        >
          Huffman File Compression
        </motion.h1>

        <motion.div
          className="bg-gray-400 border shadow-md  dark:shadow-blue-200 shadow-blue-300 dark:bg-gray-800  p-8 rounded-xl w-full max-w-md"
          initial={{ opacity: 0, scale: 0.95 }}
          animate={{ opacity: 1, scale: 1 }}
          transition={{ duration: 1.5 }}
        >
          <input
            type="file"
            className="mb-4 block w-full text-sm text-gray-700 dark:text-white file:mr-4 file:py-2 file:px-4 file:rounded-md file:border-0 file:text-sm file:font-semibold file:bg-blue-500 file:text-white hover:file:bg-blue-600"
            onChange={(e) => setFile(e.target.files[0])}
          />

          <div className="flex flex-col gap-4">
            <button
              onClick={handleCompress}
              disabled={isLoading}
              className="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 transition disabled:opacity-60"
            >
              {isLoading ? "Compressing..." : "Compress & Download"}
            </button>
          </div>

          <button
            onClick={() => setDarkMode(!darkMode)}
            className="mt-4 inline-flex items-center px-4 py-2 text-sm font-medium transition-all duration-300 rounded-xl bg-gray-200 dark:bg-gray-700 hover:bg-gray-300 dark:hover:bg-gray-600 text-gray-900 dark:text-gray-100 shadow-md"
          >
            {darkMode ? "Light Mode" : "Dark Mode"}
          </button>

          <footer className="w-full mt-8 border-t pt-6 border-gray-300 dark:border-gray-700 text-center text-sm text-gray-600 dark:text-gray-400">
            <p className="mb-2">
              Made with 🖤{" "}
              <span className="font-semibold text-blue-600 dark:text-blue-400">
                Deepak Maurya
              </span>
            </p>
            <p className="text-xs">
              &copy; {new Date().getFullYear()} Huffman Coding. All rights
              reserved.
            </p>
          </footer>
        </motion.div>
      </div>
    </div>
  );
}

export default App;
