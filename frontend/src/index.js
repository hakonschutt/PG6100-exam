import 'bootstrap/dist/css/bootstrap.css';

import React from 'react';
import ReactDOM from 'react-dom';

import Root from './global/Root';
import Routes from './global/Routes';

ReactDOM.render(
	<Root>
		<Routes />
	</Root>,
	document.getElementById('root')
);
