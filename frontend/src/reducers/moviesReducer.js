import { FETCH_MOVIES } from '../actions/types';
import { defaultMoviesState } from '../json';

export default function(state = defaultMoviesState, action) {
	switch (action.type) {
		case FETCH_MOVIES:
			return {
				...state,
				list: [...state.list, ...action.payload.list],
				count: state.count + action.payload.count,
				loading: false,
			};
		default:
			return state;
	}
}
