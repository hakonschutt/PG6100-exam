import React from 'react';
import PropTypes from 'prop-types';

import ChatIcon from '../../assets/imgs/icons/ChatIcon';

const ChatBubble = ({ onClick }) => {
	return (
		<a onClick={onClick} href="#0" id="chat-bubble">
			<ChatIcon />
		</a>
	);
};

ChatBubble.propTypes = {
	onClick: PropTypes.func.isRequired,
};

export default ChatBubble;
