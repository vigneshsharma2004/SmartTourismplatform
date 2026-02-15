import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import App from "./App";
import 'bootstrap/dist/css/bootstrap.min.css';
import { TripProvider } from "./context/TripContext";
import "./api/axiosConfig";


ReactDOM.createRoot(document.getElementById("root")).render(
  <BrowserRouter>
    <TripProvider>
      <App/>
    </TripProvider>
    
  </BrowserRouter>
);
