import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { signup } from "../api/authApi";

const Signup = () => {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    name: "",
    email: "",
    password: ""
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async e => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      await signup(form);
      navigate("/states");
    } catch (err) {
      setError("Signup failed. Email may already exist.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-5">
      <div className="card shadow-sm mx-auto p-4" style={{ maxWidth: 400 }}>
        <h4 className="text-center mb-3">Signup</h4>

        {error && (
          <div className="alert alert-danger">{error}</div>
        )}

        <form onSubmit={handleSubmit}>
          <input
            type="text"
            name="name"
            className="form-control mb-3"
            placeholder="Name"
            onChange={handleChange}
            required
          />

          <input
            type="email"
            name="email"
            className="form-control mb-3"
            placeholder="Email"
            onChange={handleChange}
            required
          />

          <input
            type="password"
            name="password"
            className="form-control mb-3"
            placeholder="Password"
            onChange={handleChange}
            required
          />

          <button
            className="btn btn-success w-100"
            disabled={loading}
          >
            {loading ? "Creating account..." : "Signup"}
          </button>
        </form>
      </div>
    </div>
  );
};

export default Signup;
