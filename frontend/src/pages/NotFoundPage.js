import React from "react";
import { Link } from "react-router-dom";

const NotFoundPage = () => {
  return (
    <div className="not-found">
      <div className="not-found-inner">
        <h1>404</h1>
        <div className="text-info">
          <span>Page not found.</span>
          <Link to="/">
            <span className="text">Back to frontpage</span>
          </Link>
        </div>
      </div>
    </div>
  );
};

export default NotFoundPage;
