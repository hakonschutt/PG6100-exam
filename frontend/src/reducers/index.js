import { combineReducers } from 'redux';
import { reducer as formReducer } from 'redux-form';
import authReducer from './authReducer';
import moviesReducer from './moviesReducer';

const rootReducer = combineReducers({
	form: formReducer,
	auth: authReducer,
	movies: moviesReducer,
});

export default rootReducer;
