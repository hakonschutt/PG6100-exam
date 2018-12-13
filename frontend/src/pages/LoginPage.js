import React, { Component } from 'react';
import axios from 'axios';
import { connect } from 'react-redux';
import { reduxForm } from 'redux-form';
import { withRouter } from 'react-router-dom';

import FormBuilder from '../components/forms/FormBuilder';
import requireUnauth from '../hocs/requireUnauth';
import { formValidation } from '../utils/forms';

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

	onSubmit() {
		// TODO: Make action call to make auth.
	}

	// login = user => {
	// 	axios
	// 		.post('http://localhost:8080/auth-service/login', {
	// 			username: user.username,
	// 			password: user.password,
	// 		})
	// 		.then(function(response) {
	// 			console.log(response);
	// 		})
	// 		.catch(function(error) {
	// 			console.log(error);
	// 		});
	// };
	//
	// signUp = user => {
	// 	axios
	// 		.post('http://localhost:8080/auth-service/signUp', {
	// 			username: user.username,
	// 			password: user.password,
	// 		})
	// 		.then(function(response) {
	// 			console.log(response);
	// 		})
	// 		.catch(function(error) {
	// 			console.log(error);
	// 		});
	// };
	render() {
		const { handleSubmit } = this.props;

		return (
			<section className="container py-5">
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
		null
	)(withRouter(requireUnauth(LoginPage)))
);
