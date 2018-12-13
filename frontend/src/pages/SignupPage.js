import React, { Component } from 'react';
import { connect } from 'react-redux';
import { reduxForm } from 'redux-form';
import { withRouter } from 'react-router-dom';

import FormBuilder from '../components/forms/FormBuilder';
import requireUnauth from '../hocs/requireUnauth';
import { formValidation } from '../utils/forms';
import { signupUser } from '../actions';

const formFields = [
	{
		label: 'Username',
		type: 'text',
		input: 'text',
		name: 'username',
		error: 'Need write a username',
	},
	{
		label: 'Password',
		type: 'password',
		input: 'text',
		name: 'password',
		error: 'Need to include a password',
	},
	{
		label: 'Repeat password',
		type: 'password',
		input: 'text',
		name: 'repeat_password',
		error: 'You need to write password twice',
	},
];

class SignupPage extends Component {
	constructor(props) {
		super(props);

		this.state = {
			error: '',
		};

		this.onSubmit = this.onSubmit.bind(this);
	}

	onSubmit(fields) {
		const { signupUser, history } = this.props;

		signupUser(fields, (gotError, msg) => {
			if (gotError) {
				this.setState({ error: msg });
			} else {
				history.push('/profile?new=true');
			}
		});
	}

	render() {
		const { handleSubmit } = this.props;

		return (
			<section className="container py-5">
				<h1>Sign up</h1>
				<hr />
				<FormBuilder
					onSubmit={handleSubmit(this.onSubmit)}
					error={this.state.error}
					formFields={formFields}
				/>
			</section>
		);
	}
}

function validate(values) {
	const errors = formValidation(values, formFields);

	if (values.password !== values.repeat_password) {
		errors.password = 'You need to write the same password';
	}

	return errors;
}

export default reduxForm({
	validate,
	form: 'signupForm',
})(
	connect(
		null,
		{ signupUser }
	)(withRouter(requireUnauth(SignupPage)))
);
