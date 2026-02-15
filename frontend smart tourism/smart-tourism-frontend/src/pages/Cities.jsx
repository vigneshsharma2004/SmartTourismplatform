import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getCities } from "../api/cityApi";

const Cities = () => {
  const { stateId } = useParams();
  const [cities, setCities] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    getCities(stateId).then(res => setCities(res.data));
  }, [stateId]);

  return (
    <div className="container mt-4">
      <h3 className="text-center mb-4">Select City</h3>

      <div className="row">
        {cities.map(city => (
          <div key={city.cityId} className="col-md-4 mb-3">
            <div
              className="card shadow-sm text-center"
              style={{ cursor: "pointer" }}
              onClick={() => navigate(`/places/${city.cityId}`)}
            >
              <div className="card-body">
                <h5 className="card-title">
                  {city.cityName}
                </h5>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Cities;
