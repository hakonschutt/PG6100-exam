import { FETCH_MOVIES } from '../actions/types';

const defaultState = {
	list: [],
	count: 0,
	filter: {
		query: null,
		offset: 0,
		limit: 20,
		sortProperty: 'release',
		direction: -1,
	},
	loading: true,
	error: null,
};

export default function(state = defaultState, action) {
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
