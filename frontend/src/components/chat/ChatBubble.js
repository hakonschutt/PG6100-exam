import React from 'react';
import PropTypes from 'prop-types';

import ChatIcon from '../../assets/imgs/icons/ChatIcon';

const ChatBubble = ({ isOpen, onClick }) => {
	return (
		<a onClick={onClick} href="#0" id="chat-bubble">
			<ChatIcon />
		</a>
	);
};

ChatBubble.propTypes = {
	onClick: PropTypes.func.isRequired,
	isOpen: PropTypes.bool.isRequired,
};

export default ChatBubble;
