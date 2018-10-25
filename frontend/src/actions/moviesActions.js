import { FETCH_MOVIES } from './types';
import movies from '../data/movies';

export const fetchMovies = () => dispatch => {
	dispatch({ type: FETCH_MOVIES, payload: movies });
};
