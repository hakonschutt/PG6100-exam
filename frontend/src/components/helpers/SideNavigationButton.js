import React from 'react';
import PropTypes from 'prop-types';

import Arrow from '../../assets/imgs/icons/Arrow';

const SideNavigationButton = ({ isActive, side, onClick }) => {
	return (
		<button
			onClick={() => onClick(side)}
			className={`side-navigation-button ${side} ${isActive ? '' : 'inactive'}`}
		>
			<Arrow />
		</button>
	);
};

SideNavigationButton.propTypes = {
	isActive: PropTypes.bool.isRequired,
	side: PropTypes.string.isRequired,
	onClick: PropTypes.func.isRequired,
};

export default SideNavigationButton;
