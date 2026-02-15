import { useNavigate } from "react-router-dom";

const IndexPage = () => {
  const navigate = useNavigate();

  return (
    <div className="container-fluid vh-100 d-flex align-items-center justify-content-center bg-light">
      <div className="text-center p-5 shadow rounded bg-white col-md-6">

        <h1 className="fw-bold text-primary mb-3">
          Smart Tourism Platform
        </h1>

        <p className="text-muted mb-4">
          Discover destinations and plan your trip smartly.
        </p>

        <button
          className="btn btn-primary btn-lg"
          onClick={() => navigate("/states")}
        >
          Explore Destinations
        </button>

      </div>
    </div>
  );
};

export default IndexPage;
