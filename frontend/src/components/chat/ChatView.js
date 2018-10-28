import React from 'react';
import AnimateHeight from 'react-animate-height';
import PropTypes from 'prop-types';
import ChatViewHeader from './ChatViewHeader';
import ChatViewInner from './ChatViewInner';

const ChatView = ({ isOpen }) => {
	return (
		<div id="chat-view">
			<AnimateHeight duration={500} height={isOpen ? 'auto' : 0}>
				<ChatViewHeader />
				<ChatViewInner />
			</AnimateHeight>
		</div>
	);
};

ChatView.propTypes = {
	isOpen: PropTypes.bool.isRequired,
};

export default ChatView;
