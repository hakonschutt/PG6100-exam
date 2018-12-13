import { FETCH_USER } from '../actions/types';

const defaultState = {
	isEnabled: false,
	role: 'unauth',
};

export default function(state = defaultState, action) {
	switch (action.type) {
	case FETCH_USER:
		return action.payload || state;
	default:
		return state;
	}
}
