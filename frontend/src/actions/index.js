import axios from 'axios';
import { FETCH_USER, SIGNUP_USER, LOGIN_USER, LOGOUT_USER } from './types';

export const fetchUser = () => async dispatch => {
	try {
		// TODO: MAKE REQUST!
		// const res = await axios.get('/auth-service/user', fields);

		dispatch({ type: FETCH_USER, payload: null });
	} catch (err) {}
};

export const signupUser = (fields, cb) => async dispatch => {
	try {
		// delete fields.repeat_password;
		// await axios.post('/auth-service/signUp', fields);

		dispatch({ type: SIGNUP_USER, payload: fields.username });
		cb(false, null);
	} catch (err) {
		cb(true, err.data.message);
	}
};

export const loginUser = (fields, cb) => async dispatch => {
	try {
		// await axios.post('/auth-service/login', fields);

		dispatch({ type: LOGIN_USER, payload: fields.username });
		cb(false, null);
	} catch (err) {
		cb(true, err.data.message);
	}
};

export const signoutUser = cb => async dispatch => {
	try {
		// await axios.post('/auth-service/logout');

		dispatch({ type: LOGOUT_USER, payload: null });
		cb(false, null);
	} catch (err) {
		cb(true, err.data.message);
	}
};
