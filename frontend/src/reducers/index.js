import { combineReducers } from 'redux';
import { reducer as formReducer } from 'redux-form';
import userReducer from './userReducer';

const rootReducer = combineReducers({
	form: formReducer,
	user: userReducer,
});

export default rootReducer;
