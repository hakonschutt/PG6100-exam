import React from "react";
import PropTypes from "prop-types";

const AppWrapper = ({ children }) => {
  // ADD GLOBALE COMPONENTS

  console.log(children);

  return (
    <div id="app-wrapper">
      <main>
        {children}
      </main>
    </div>
  );
};

AppWrapper.propTypes = {
  children: PropTypes.object.isRequired
};

export default AppWrapper;
