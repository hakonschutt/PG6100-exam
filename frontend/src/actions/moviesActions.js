import { FETCH_MOVIES } from './types';
import { movieList } from '../json';

export const fetchMovies = () => dispatch => {
	dispatch({
		type: FETCH_MOVIES,
		payload: {
			list: movieList,
			count: movieList.length,
		},
	});
};
