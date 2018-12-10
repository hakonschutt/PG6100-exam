import React, { Component } from 'react';
import Select from 'react-select';

class SelectField extends Component {
	handleChange(value) {
		const {
			input: { onChange },
		} = this.props;

		onChange(value);
	}

	render() {
		const {
			input: { value },
		} = this.props;

		return (
			<div className="form-selector">
				<label>{this.props.label}</label>
				<Select
					placeholder={this.props.placeholder}
					value={value}
					onChange={this.handleChange.bind(this)}
					isSearchable={true}
					isClearable={true}
					options={this.props.options}
				/>
			</div>
		);
	}
}

export default SelectField;
