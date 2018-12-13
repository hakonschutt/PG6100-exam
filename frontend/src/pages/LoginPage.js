import React, { Component } from 'react';
import { connect } from 'react-redux';
import { reduxForm } from 'redux-form';
import { withRouter } from 'react-router-dom';

import FormBuilder from '../components/forms/FormBuilder';
import requireUnauth from '../hocs/requireUnauth';
import { formValidation } from '../utils/forms';
import { loginUser } from '../actions';

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
];

class LoginPage extends Component {
	constructor(props) {
		super(props);

		this.state = {
			error: '',
		};

		this.onSubmit = this.onSubmit.bind(this);
	}

	onSubmit(fields) {
		const { loginUser, history } = this.props;

		loginUser(fields, (gotError, msg) => {
			if (gotError) {
				this.setState({ error: msg });
			} else {
				history.push('/profile');
			}
		});
	}

	render() {
		const { handleSubmit } = this.props;

		return (
			<section className="container py-5">
				<h1>Login</h1>
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
	return formValidation(values, formFields);
}

export default reduxForm({
	validate,
	form: 'loginForm',
})(
	connect(
		null,
		{ loginUser }
	)(withRouter(requireUnauth(LoginPage)))
);
