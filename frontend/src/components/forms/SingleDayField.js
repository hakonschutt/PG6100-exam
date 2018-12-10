import 'react-dates/initialize';
import 'react-dates/lib/css/_datepicker.css';

import React, { Component } from 'react';
import moment from 'moment';

import { SingleDatePicker } from 'react-dates';

class SingleDayField extends Component {
	constructor(props) {
		super(props);

		this.state = {
			focused: false,
		};
	}

	onDateChange(date) {
		const {
			input: { onChange },
		} = this.props;

		onChange(date ? date.toDate() : null);
	}

	render() {
		const {
			input: { value },
			extraProps,
		} = this.props;

		let time = value ? moment(value) : null;

		return (
			<div className="data-selector">
				<label>{this.props.label}</label>
				<SingleDatePicker
					id="date_input"
					date={time}
					numberOfMonths={1}
					focused={this.state.focused}
					onDateChange={this.onDateChange.bind(this)}
					onFocusChange={({ focused }) => this.setState({ focused })}
					{...extraProps}
				/>
			</div>
		);
	}
}

export default SingleDayField;
