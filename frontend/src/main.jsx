import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";
import WelcomePage from "./pages/welcome/WelcomePage.jsx";
import JournalPage from "./pages/journal/JournalPage.jsx";
import { createBrowserRouter, RouterProvider } from "react-router-dom";

const router = createBrowserRouter([
  { path: "/", element: <App /> },
  { path: "/welcome", element: <WelcomePage /> },
  { path: "/journal", element: <JournalPage /> },
]);

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);
