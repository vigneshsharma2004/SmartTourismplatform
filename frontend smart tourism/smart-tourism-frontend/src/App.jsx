import { Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import IndexPage from "./pages/IndexPage";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import States from "./pages/States";
import Cities from "./pages/Cities";
import Places from "./pages/Places";
import Preferences from "./pages/Preferences";
import Itinerary from "./pages/Itinerary";
import MyTrips from "./pages/MyTrips";
import SavedTrip from "./pages/SavedTrip";
import BookTrip from "./pages/BookTrip";
import PrivateRoute from "./routes/PrivateRoute";

function App() {
  return (
    <>
      <Navbar />

      <Routes>
        {/* PUBLIC ROUTES */}
        <Route path="/" element={<IndexPage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/states" element={<States />} />
        <Route path="/cities/:stateId" element={<Cities />} />
        <Route path="/places/:cityId" element={<Places />} />
        <Route path="/preferences" element={<Preferences />} /> {/* ✅ PUBLIC */}

        {/* PROTECTED ROUTES */}
        <Route
          path="/itinerary"
          element={
            <PrivateRoute>
              <Itinerary />
            </PrivateRoute>
          }
        />

        <Route
          path="/my-trips"
          element={
            <PrivateRoute>
              <MyTrips />
            </PrivateRoute>
          }
        />

        <Route
          path="/trips/:tripId"
          element={
            <PrivateRoute>
              <SavedTrip />
            </PrivateRoute>
          }
        />

        <Route
          path="/trips/:tripId/book"
          element={
            <PrivateRoute>
              <BookTrip />
            </PrivateRoute>
          }
        />
      </Routes>
    </>
  );
}

export default App;
