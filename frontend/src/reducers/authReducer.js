import {
	FETCH_USER,
	SIGNUP_USER,
	LOGIN_USER,
	LOGOUT_USER,
} from '../actions/types';

const defaultState = {
	isEnabled: false,
	username: null,
};

export default function(state = defaultState, action) {
	switch (action.type) {
	case FETCH_USER:
		return action.payload || state;
	case SIGNUP_USER:
		return { isEnabled: true, username: action.payload };
	case LOGIN_USER:
		return { isEnabled: true, username: action.payload };
	case LOGOUT_USER:
		return defaultState;
	default:
		return state;
	}
}
