import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getStates } from "../api/stateApi";

const States = () => {
  const [states, setStates] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    getStates().then(res => setStates(res.data));
  }, []);

  return (
    <div className="container mt-4">
      <h3 className="text-center mb-4">Select State</h3>

      <div className="row">
        {states.map(state => (
          <div key={state.stateId} className="col-md-4 mb-3">
            <div
              className="card shadow-sm text-center"
              style={{ cursor: "pointer" }}
              onClick={() => navigate(`/cities/${state.stateId}`)}
            >
              <div className="card-body">
                <h5 className="card-title">
                  {state.stateName}
                </h5>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default States;
