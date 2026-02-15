const Footer = () => {
  return (
    <footer className="bg-dark text-light mt-5 pt-4">
      <div className="container">
        <div className="row">

          {/* ABOUT */}
          <div className="col-md-4 mb-3">
            <h5 className="fw-bold">Smart Tourism</h5>
            <p className="text-muted">
              Smart Tourism is a travel planning platform that helps users
              explore destinations, customize trips, and build optimized
              itineraries with ease.
            </p>
          </div>

          {/* QUICK LINKS */}
          <div className="col-md-4 mb-3">
            <h6 className="fw-bold">Quick Links</h6>
            <ul className="list-unstyled">
              <li><a href="/" className="text-light text-decoration-none">Home</a></li>
              <li><a href="/states" className="text-light text-decoration-none">Explore</a></li>
              <li><a href="/preferences" className="text-light text-decoration-none">Trip Preferences</a></li>
              <li><a href="#" className="text-light text-decoration-none">About Us</a></li>
            </ul>
          </div>

          {/* CONTACT */}
          <div className="col-md-4 mb-3">
            <h6 className="fw-bold">Contact</h6>
            <p className="mb-1">📍 India</p>
            <p className="mb-1">📧 support@smarttourism.com</p>
            <p className="mb-1">📞 +91 98765 43210</p>
          </div>

        </div>

        <hr className="border-secondary" />

        {/* COPYRIGHT */}
        <div className="text-center pb-3 text-muted">
          © {new Date().getFullYear()} Smart Tourism. All rights reserved.
        </div>
      </div>
    </footer>
  );
};

export default Footer;
