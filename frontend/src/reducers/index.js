import { combineReducers } from 'redux';
import { reducer as formReducer } from 'redux-form';
import userReducer from './userReducer';
import moviesReducer from './moviesReducer';

const rootReducer = combineReducers({
	form: formReducer,
	user: userReducer,
	movies: moviesReducer,
});

export default rootReducer;
