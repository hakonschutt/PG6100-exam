import React, { Component } from 'react';
import { connect } from 'react-redux';
import ChatBubble from './ChatBubble';
import ChatView from './ChatView';

class Chat extends Component {
	constructor(props) {
		super(props);

		this.toggleOpen = this.toggleOpen.bind(this);

		this.state = {
			isOpen: false,
		};
	}

	toggleOpen() {
		this.setState((state, props) => {
			return { isOpen: !state.isOpen };
		});
	}

	render() {
		const { isOpen } = this.state;

		return [
			<ChatView key="view" isOpen={isOpen} />,
			<ChatBubble key="bubble" isOpen={isOpen} onClick={this.toggleOpen} />,
		];
	}
}

function mapStateToProps({ chat }) {
	return { chat };
}

export default connect(mapStateToProps)(Chat);
