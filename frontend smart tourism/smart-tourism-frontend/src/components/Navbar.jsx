import { Link, useNavigate } from "react-router-dom";
import { isAuthenticated, logout } from "../api/authApi";

const Navbar = () => {
  const navigate = useNavigate();
  const loggedIn = isAuthenticated();

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark px-4">
      {/* Brand */}
      <Link className="navbar-brand" to="/">
        Smart Tourism
      </Link>

      {/* Right side actions */}
      <div className="ms-auto d-flex gap-2 align-items-center">
        {!loggedIn ? (
          <>
            <Link className="btn btn-outline-light" to="/login">
              Login
            </Link>

            <Link className="btn btn-warning" to="/signup">
              Signup
            </Link>
          </>
        ) : (
          <>
            <Link className="btn btn-outline-light" to="/my-trips">
              My Trips
            </Link>

            <button
              className="btn btn-danger"
              onClick={() => {
                logout();
                navigate("/login");
              }}
            >
              Logout
            </button>
          </>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
