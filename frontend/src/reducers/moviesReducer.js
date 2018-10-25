import { FETCH_MOVIES } from '../actions/types';

export default function(state = null, action) {
	switch (action.type) {
		case FETCH_MOVIES:
			return action.payload;
		default:
			return state;
	}
}
