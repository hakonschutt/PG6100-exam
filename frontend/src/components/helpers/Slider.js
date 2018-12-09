import React, { Component } from 'react';
import PropTypes from 'prop-types';

import SideNavigationButton from './SideNavigationButton';

class Slider extends Component {
	constructor(props) {
		super(props);

		this.sliderInner = React.createRef();
		this.sliderInnerElements = React.createRef();

		this.state = {
			moveLeft: false,
			moveRight: true,
			offset: 0,
			itemSize: 0,
		};
	}

	componentDidMount() {
		this.setItemSize();
	}

	setItemSize() {
		console.log('GOT HERE');
	}

	slide(direction) {}

	render() {
		const { moveLeft, moveRight, itemSize } = this.state;

		const children = React.Children.map(this.props.children, (child, index) => {
			return React.cloneElement(child, { itemSize });
		});

		return (
			<section className="global-slider">
				<SideNavigationButton
					isActive={moveLeft}
					onClick={this.slide.bind(this)}
					side="left"
				/>
				<div
					className="slider-inner clearfix"
					ref={this.sliderInner}
					style={{ left: `${0}px` }}
				>
					{children}
				</div>
				<SideNavigationButton
					isActive={moveRight}
					onClick={this.slide.bind(this)}
					side="right"
				/>
			</section>
		);
	}
}

Slider.propTypes = {
	children: PropTypes.any.isRequired,
};

export default Slider;
