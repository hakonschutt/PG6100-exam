import { combineReducers } from 'redux';
import { reducer as formReducer } from 'redux-form';
import authReducer from './authReducer';
import moviesReducer from './moviesReducer';
import chatReducer from './chatReducer';

const rootReducer = combineReducers({
	form: formReducer,
	auth: authReducer,
	movies: moviesReducer,
	chat: chatReducer,
});

export default rootReducer;
