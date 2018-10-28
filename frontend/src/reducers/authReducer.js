import { FETCH_USER } from '../actions/types';

export default function(state = null, action) {
	switch (action.type) {
		case FETCH_USER:
			return action.payload || state;
		default:
			return state;
	}
}
