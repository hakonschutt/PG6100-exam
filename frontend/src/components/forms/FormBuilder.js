import React, { Component } from 'react';
import { Field } from 'redux-form';

import FormField from './FormField';
import FormTextarea from './FormTextarea';
import Alert from '../helpers/Alert';

class FormBuilder extends Component {
	renderFields() {
		const { formFields } = this.props;

		return formFields.map(({ name, label, type, input, extraProps }) => {
			let Comp = null;

			switch (input) {
			case 'textarea':
				Comp = FormTextarea;
				break;
			default:
				Comp = FormField;
			}
			return (
				<Field
					key={name}
					label={label}
					type={type}
					name={name}
					component={Comp}
					extraProps={extraProps}
				/>
			);
		});
	}

	render() {
		const { onSubmit, error } = this.props;

		return (
			<div className="form-wrap">
				<Alert msg={error} />
				<form onSubmit={onSubmit}>
					{this.renderFields()}
					<div className="form-group my-3">
						<button type="submit" className="btn btn-primary">
							Submit
						</button>
					</div>
				</form>
			</div>
		);
	}
}

export default FormBuilder;
